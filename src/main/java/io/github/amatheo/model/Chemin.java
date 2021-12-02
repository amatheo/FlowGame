package io.github.amatheo.model;

import java.awt.*;
import java.util.ArrayList;

public class Chemin {
    private ArrayList<Point> pointArray;
    private CaseType pathType;


    public Chemin(Point startPoint, CaseType pathType) {
        pointArray = new ArrayList<Point>();
        pointArray.add(startPoint); // Add first tile to point array
        this.pathType = pathType;
    }

    public void addPoint(Point p){
        pointArray.add(p);
    }


    public void removePoint(Point p){
        pointArray.remove(p);
    }

    /*
     * cherche la case correspondante a la position
     * puis change la taille accessible, les cases existent encore mais
     * ne sont plus visible et vont etre réecrit dessus.
     * */
    public void removePointAndAllAfter(Point p){
        int indexPoint = pointArray.indexOf(p);
        for (int i = pointArray.size()-1; i > indexPoint; i--) {
            pointArray.remove(i);
        }
    }

    public ArrayList<Point> getPoints(){
        return pointArray;
    }

    public CaseType getPathType(){
        return pathType;
    }
    public Point getFirstPoint(){
        return pointArray.get(0);
    }
    public Point getLastPoint(){
        int index = pointArray.size()-1;
        return pointArray.get(index);
    }
    public int getSize(){
        return pointArray.size();
    }
}
