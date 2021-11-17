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
    private HashMap<CaseVue, Point> hashmap; // voir (*)
    // currentComponent : par défaut, mouseReleased est exécutée pour le composant (JLabel) sur lequel elle a été enclenchée (mousePressed) même si celui-ci a changé
    // Afin d'accéder au composant sur lequel le bouton de souris est relaché, on le conserve avec la variable currentComponent à
    // chaque entrée dans un composant - voir (**)
    private JComponent currentComponent;

    public GrilleVue(Jeu jeu) {
        int size = jeu.getSize();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(size * PIXEL_PER_SQUARE, size * PIXEL_PER_SQUARE);

        caseVue = new CaseVue[size][size];
        hashmap = new HashMap<CaseVue, Point>();

        JPanel contentPane = new JPanel(new GridLayout(size, size));

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                caseVue[i][j] = new CaseVue(jeu.getCaseModelAtCoordinate(i,j));
                contentPane.add(caseVue[i][j]);

                hashmap.put(caseVue[i][j], new Point(j, i));

                caseVue[i][j].addMouseListener(new GridMouseListener());
            }
        }
        setContentPane(contentPane);
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
