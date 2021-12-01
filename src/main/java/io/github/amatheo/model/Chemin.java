package io.github.amatheo.model;

import java.awt.*;
import java.util.ArrayList;

public class Chemin {
    private ArrayList<Point> pointArray;
    public CaseModel startingTile;

    public Chemin(CaseModel startingTile) {
        this.startingTile = startingTile;
        pointArray = new ArrayList<Point>();
        pointArray.add(startingTile.getPoint()); // Add first tile to point array
    }

    public void addPoint(Point p){
        pointArray.add(p);
    }

    /*
    * cherche la case correspondante a la position
    * puis change la taille accessible, les cases existent encore mais
    * ne sont plus visible et vont etre réecrit dessus.
    * */
    void removePoint(Point p){
        pointArray.remove(p);
    }

    void removePointAndAllAfter(Point p){
        for (int i = pointArray.size(); i > 0; i--) {
            if(pointArray.get(i) != p){
                pointArray.remove(i);
            }
            if(pointArray.get(i) == p){
                pointArray.remove(i);
            }
        }
    }
    public ArrayList<Point> getPoints(){
        return pointArray;
    }
}
