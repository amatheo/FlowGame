package io.github.amatheo.vc;

import io.github.amatheo.listener.GridMouseListener;
import io.github.amatheo.model.Jeu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class GrilleVue extends JFrame implements Observer {
    private static final int PIXEL_PER_SQUARE = 60;
    // tableau de cases : i, j -> case
    private CaseVue[][] caseVue;
    // hashmap : case -> i, j
    public static HashMap<Point, CaseVue> caseVueHashmap;

    JPanel contentPane;
    public GrilleVue(Jeu jeu) {
        int size = jeu.getSize();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(size * PIXEL_PER_SQUARE, size * PIXEL_PER_SQUARE);

        caseVue = new CaseVue[size][size];
        caseVueHashmap = new HashMap<Point, CaseVue>();

        contentPane = new JPanel(new GridLayout(size, size));
        updateVue(jeu);
        setContentPane(contentPane);
    }
    //Update view grid with current model grid
    public void updateVue(Jeu jeu){
        caseVueHashmap.clear();
        contentPane.removeAll();
        int size = jeu.getSize();
        for (int j = 0; j < size; j++) {
            for (int i = 0; i < size; i++) {

                caseVue[i][j] = new CaseVue(jeu.getCaseModelAtCoordinate(i,j));
                caseVue[i][j].addMouseListener(new GridMouseListener(jeu));

                contentPane.add(caseVue[i][j]);
                caseVueHashmap.put(new Point(i, j), caseVue[i][j]);

            }
        }
        setContentPane(contentPane);
    }

    public void setSelectedAtPosition(int x, int y){
        caseVue[x][y].isSelected = true;
        caseVue[x][y].repaint();
    }
    @Override
    public void update(Observable o, Object arg) {
        Jeu j = (Jeu) o;
        updateVue(j);
        if(j.isLevelFinished){
            j.isLevelFinished = false;
            displayWin();
        }
    }

    private void displayWin(){
        int n = JOptionPane.showConfirmDialog(
                null,
                "You won ! Would you like to continue ?",
                "Congartulation",
                JOptionPane.YES_NO_OPTION);
    }
}
