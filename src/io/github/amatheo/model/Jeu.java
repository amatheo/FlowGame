package io.github.amatheo.model;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import java.awt.*;

public class Jeu extends Observable {
    private CaseModel[][] grid;
    private ArrayList<Chemin> paths;

    public Chemin tempPath;
    public boolean isDrawing;



    public Jeu(int size) {
        grid = new CaseModel[size][size];
        initializeGrid();

        isDrawing = false;
        paths = new ArrayList<Chemin>();
    }

    //TODO This will be more like void validatePath(Chemin chem)
    public void addPath(Chemin chemin){
        paths.add(chemin);
        //grid[x][y] = type.empty


        for (Chemin path: paths) {
            ArrayList<Point> points = path.getPoints();
            //grid[points.get(0).x][points.get(0).y].setType(drawCase(null, points.get(0), points.get(1)));//set le premier
            for(int i = 1; i < points.size()-1; i++){//tout sauf le premier et le dernier
                Point before = points.get(i-1);
                Point current = points.get(i);
                Point after = points.get(i+1);
                grid[current.x][current.y].setType(drawCase(before, current, after));
            }
            //Point lastPoint = points.get(points.size()-1);
            //grid[lastPoint.x][lastPoint.y].setType(drawCase(points.get(points.size()-2)), lastPoint, null);//set le dernier

        }
        setChanged();
        notifyObservers();
    }

    private CaseType drawCase(Point before, Point current, Point after){
        if(grid[current.x][current.y].getType() != null){
            return CaseType.cross;
        }else{
            if(before.x == current.x){
                if(after.x == current.x){
                    return CaseType.LR;
                }else{
                    if(after.y > current.y){
                        if(after.x < current.x){
                            return CaseType.BR;
                        }else{
                            return CaseType.BL;
                        }
                    }else{
                        if(after.x < current.x){
                            return CaseType.TR;
                        }else{
                            return CaseType.TL;
                        }
                    }
                }
            }
            if(before.y == current.y){
                if(after.y == current.y){
                    return CaseType.TB;
                }else{
                    if(after.x > current.x){
                        if(after.y < current.y){
                            return CaseType.TL;
                        }else{
                            return CaseType.BR;
                        }
                    }else{
                        if(after.y < current.y){
                            return CaseType.TR;
                        }else{
                            return CaseType.BL;
                        }
                    }
                }

            }
        }

        return CaseType.empty;

    }

    /*
    public boolean isTheCaseOccupied(int x, int y){
        //Pour tout les chemins enregistré dans le jeu
        for(int i = 0; i < paths.size(); i++){
            //Pour toute les case d'un chemin particulier
            for (int j = 0; j < paths.get(i).getPoints().length ; j++) {
                Point p = paths.get(i).getPoints()[j];
                if (p.x == x && p.y == y){
                    return true;
                }
            }
        }
        return false;
    }
    */

    public void setCaseTypeAtCoordinate(int x, int y, CaseType type){
        grid[x][y].setType(type);
        setChanged();
        notifyObservers();
    }

    private void initializeGrid(){
        for (int j = 0; j < grid.length; j++) {
            for (int i = 0; i < grid.length; i++) {
                grid[i][j] = new CaseModel(CaseType.empty, new Point(i,j));
            }
        }
        setChanged();
        notifyObservers();
    }

    public CaseModel getCaseModelAtCoordinate(int x, int y){
        return grid[x][y];
    }

    //Based on the fact that the grid is a square
    public int getSize(){
        return grid.length;
    }
}
