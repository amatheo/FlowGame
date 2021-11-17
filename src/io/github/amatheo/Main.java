package io.github.amatheo;

import io.github.amatheo.model.Jeu;
import io.github.amatheo.vc.GrilleVue;
import java.util.Observable;
import java.util.Observer;

public class Main {

    public static void main(String[] args) {
        Jeu j = new Jeu(5);
        GrilleVue vue = new GrilleVue(j);
        j.addObserver(vue);
        vue.setVisible(true);

    }
}
