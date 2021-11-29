package io.github.amatheo;

import io.github.amatheo.model.Jeu;
import io.github.amatheo.vc.GrilleVue;
import java.util.Observable;
import java.util.Observer;

public class Main {
    private static Jeu jeu;

    public static Jeu getGameInstance(){
        return jeu;
    }

    public static void main(String[] args) {
        jeu = new Jeu(5);
        GrilleVue vue = new GrilleVue(jeu);
        jeu.addObserver(vue);
        vue.setVisible(true);
    }
}
