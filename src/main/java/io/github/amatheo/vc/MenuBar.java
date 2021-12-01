package io.github.amatheo.vc;

import io.github.amatheo.model.Jeu;
import io.github.amatheo.model.Level;

import javax.swing.*;
import java.awt.event.*;

import javax.swing.JFileChooser;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

public class MenuBar extends JMenuBar {
    //https://docs.oracle.com/javase/tutorial/uiswing/components/menu.html

    private JMenu menu;
    private JFrame f;
    private Jeu j;
    JMenuItem i0, i1, i2, i3, i4, i5;
    public MenuBar(JFrame f, Jeu j){
        this.f = f;
        this.j = j;
        JMenuBar mb = new JMenuBar();
        menu=new JMenu("Level");
        i0 = new JMenuItem("Level 1");
        i1 = new JMenuItem("Level 2");
        i2 = new JMenuItem("Level 3");
        i3 = new JMenuItem("Level 4");
        i4 = new JMenuItem("Level 5");
        i5 = new JMenuItem("Load Custom Level");
        menu.add(i0);
        menu.add(i1);
        menu.add(i2);
        menu.add(i3);
        menu.add(i4);
        menu.add(i5);
        mb.add(menu);
        f.setJMenuBar(mb);

        i0.addActionListener(this::actionPerformed);
        i1.addActionListener(this::actionPerformed);
        i2.addActionListener(this::actionPerformed);
        i3.addActionListener(this::actionPerformed);
        i4.addActionListener(this::actionPerformed);
        i5.addActionListener(this::actionPerformed);

    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==i0){
            j.loadLevel(Level.LEVEL1);
        }
        if(e.getSource()==i1){
            j.loadLevel(Level.LEVEL2);
        }
        if(e.getSource()==i2){
            j.loadLevel(Level.LEVEL3);
        }
        if(e.getSource()==i3){
            j.loadLevel(Level.LEVEL4);
        }
        if(e.getSource()==i4){
            j.loadLevel(Level.LEVEL5);
        }
        if(e.getSource()==i5){
            String s = fileSelector();
            if (s != null){
                j.loadLevel(s);
            }
        }
    }

    public String fileSelector(){
        JFileChooser file = new JFileChooser();
        file.setMultiSelectionEnabled(false);
        file.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (file.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            java.io.File selectedFile = file.getSelectedFile();
            return selectedFile.getAbsolutePath();
        }
        return null;
    }
}