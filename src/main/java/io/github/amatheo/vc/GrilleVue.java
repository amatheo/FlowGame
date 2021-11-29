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
    public static HashMap<CaseVue, Point> hashmap; // voir (*)
    // currentComponent : par défaut, mouseReleased est exécutée pour le composant (JLabel) sur lequel elle a été enclenchée (mousePressed) même si celui-ci a changé
    // Afin d'accéder au composant sur lequel le bouton de souris est relaché, on le conserve avec la variable currentComponent à
    // chaque entrée dans un composant - voir (**)
    private JComponent currentComponent;

    JPanel contentPane;
    public GrilleVue(Jeu jeu) {
        int size = jeu.getSize();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(size * PIXEL_PER_SQUARE, size * PIXEL_PER_SQUARE);

        caseVue = new CaseVue[size][size];
        hashmap = new HashMap<CaseVue, Point>();

        contentPane = new JPanel(new GridLayout(size, size));
        updateVue(jeu);
        setContentPane(contentPane);
    }
    //Update view grid with current model grid
    public void updateVue(Jeu jeu){
        hashmap.clear();
        contentPane.removeAll();
        int size = jeu.getSize();
        for (int j = 0; j < size; j++) {
            for (int i = 0; i < size; i++) {

                caseVue[i][j] = new CaseVue(jeu.getCaseModelAtCoordinate(i,j));
                caseVue[i][j].addMouseListener(new GridMouseListener(jeu));

                contentPane.add(caseVue[i][j]);
                hashmap.put(caseVue[i][j], new Point(i, j));

            }
        }
        setContentPane(contentPane);
    }

    @Override
    public void update(Observable o, Object arg) {
        updateVue((Jeu)o);
    }
}
