package io.github.amatheo.vc;

import io.github.amatheo.listener.GridMouseListener;
import io.github.amatheo.model.CaseType;
import io.github.amatheo.model.Chemin;
import io.github.amatheo.model.Jeu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

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
        //Update view grid with current model grid
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
        //Mise a jour des couleur
        updatePathColor(jeu.getPaths());

        setContentPane(contentPane);
    }

    public void updatePathColor(ArrayList<Chemin> chemins){
        for (Chemin c : chemins) {
            for (Point p : c.getPoints()) {
                caseVue[p.x][p.y].couleur = getColorBasedOnCaseType(c.getPathType());
                caseVue[p.x][p.y].repaint();
            }
        }
    }
    public Color getColorBasedOnCaseType(CaseType type){
        Color c = Color.black;
        switch (type){
            case S1 :
                c = Color.BLUE;
                break;
            case S2 :
                c = Color.cyan;
                break;
            case S3 :
                c = Color.green;
                break;
            case S4 :
                c = Color.MAGENTA;
                break;
            case S5:
                c = Color.ORANGE;
                break;
        }
        return c;
    }
    public void setSelectedAtPosition(int x, int y){
        caseVue[x][y].isSelected = true;
        caseVue[x][y].repaint();
    }
    @Override
    public void update(Observable o, Object arg) {
        Jeu j = (Jeu) o;
        updateVue(j);
        updateWindowTitleCompletion(j.getCountOfLinkedType(), j.linkedCaseType.size());
        if(j.isLevelFinished){
            j.isLevelFinished = false;
            displayWin();
        }
    }
    private void updateWindowTitleCompletion(int completed, int total){
        String s = ""+completed+" / "+total+" completed";
        setTitle(s);
    }
    private void displayWin(){
        int n = JOptionPane.showConfirmDialog(
                this,
                "You won ! Now try another level.",
                "Congartulation",
                JOptionPane.CLOSED_OPTION);
    }
}
