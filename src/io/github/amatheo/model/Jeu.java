package io.github.amatheo.model;

import java.util.Observable;
import java.util.Observer;

import java.awt.*;

public class Jeu extends Observable {
    private CaseModel[][] grid;

    public Jeu(int size) {
        grid = new CaseModel[size][size];
        initializeGrid();
    }

    public void setCaseTypeAtCoordinate(int x, int y, CaseType type){
        grid[x][y].setType(type);
        setChanged();
        notifyObservers();
    }

    private void initializeGrid(){
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                grid[i][j] = new CaseModel(CaseType.empty, new Point(i,j));
            }
        }
    }

    public CaseModel getCaseModelAtCoordinate(int x, int y){
        return grid[x][y];
    }

    //Based on the fact that the grid is a square
    public int getSize(){
        return grid.length;
    }
}
