package io.github.amatheo.model;

import com.google.gson.*;

import javax.swing.*;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import java.awt.*;

public class Jeu extends Observable {
    public boolean isLevelFinished;
    private CaseModel[][] grid;
    private ArrayList<Chemin> paths;

    public Chemin tempPath;
    public boolean isDrawing;

    public HashMap<CaseType, Boolean> linkedCaseType;

    public Jeu(int size) {
        grid = new CaseModel[size][size];
        isDrawing = false;

        paths = new ArrayList<Chemin>();
        linkedCaseType = new HashMap<>();

        loadLevel(Level.LEVEL1);

    }

    public void addPath(Chemin chemin){
        if (validatePath(chemin)){
            fillPathType(chemin);
            paths.add(chemin);
            linkedCaseType.put(chemin.getPathType(), true);
            isLevelFinished = checkWinState();
        }
        setChanged();
        notifyObservers();
    }
    //Check if path and type is valid
    private boolean validatePath(Chemin chemin){
        Point firstPoint = chemin.getFirstPoint();
        Point lastPoint = chemin.getLastPoint();
        CaseType cheminType = chemin.getPathType();

        boolean isTypeValid = false;
        if (
                cheminType == getCaseTypeAtCoordinate(firstPoint.x, firstPoint.y)
                && cheminType == getCaseTypeAtCoordinate(lastPoint.x, lastPoint.y)
        ){
            isTypeValid = true;
        }

        boolean isPathInvalid = false;
        for (int i = 1; i<chemin.getPoints().size()-1;i++){
            Point before = chemin.getPoints().get(i-1);
            Point current = chemin.getPoints().get(i);
            Point after = chemin.getPoints().get(i+1);
            CaseType currentType = getCaseTypeAtCoordinate(current.x, current.y);
            if (!(
                    arePointConnected(before, current)
                    && arePointConnected(current, after)
                    && (currentType == CaseType.empty || currentType == CaseType.cross)
            )){
                isPathInvalid = true;
            }
        }
        //Check if point of start is not same as the path endpoint but the same one as the path one
        boolean areEndpointsValid = false;
        if (
                chemin.getFirstPoint() != lastPoint
                && !isDuplicate(chemin.getPoints()) //Check if there is duplicated point in the path ex : a loop
        ) {
            areEndpointsValid = true;
        }
        System.out.println("Duplicate : "+isDuplicate(chemin.getPoints())+"; TypeValid : "+ isTypeValid+"; PathValid : "+!isPathInvalid+"; EndpointValid : "+areEndpointsValid+";");
        return isTypeValid && !isPathInvalid && areEndpointsValid;
    }

    private void fillPathType(Chemin chemin){
        for (int i = 1; i<chemin.getPoints().size()-1;i++){
            Point before = chemin.getPoints().get(i-1);
            Point current = chemin.getPoints().get(i);
            Point after = chemin.getPoints().get(i+1);

            CaseType typeToSet = defineCaseType(before, current, after);
            setCaseTypeAtCoordinate(typeToSet, current.x, current.y);
        }
    }

    private CaseType defineCaseType(Point before, Point current, Point after){
        //If a cross is already setup
        if (getCaseTypeAtCoordinate(current.x, current.y) == CaseType.cross){
            return CaseType.cross;
        }
        if(before.x == current.x){
            if(after.x == current.x) {
                return CaseType.TB;
            }
            if(before.y < current.y){
                //descend
                if(after.x < current.x){
                    //gauche
                    return CaseType.TL;
                }else{
                    //droite
                    return CaseType.TR;
                }
            }else{
                //monte
                if(after.x < current.x){
                    //gauche
                    return CaseType.BL;
                }else{
                    //droite
                    return CaseType.BR;
                }
            }
        }
        if(before.y == current.y){
            if(after.y == current.y){
                return CaseType.LR;
            }
            if(before.x < current.x){
                //gauche
                if(after.y < current.y){
                    //descent
                    return CaseType.TL;
                }else{
                    //monte
                    return CaseType.BL;
                }
            }else{
                //droite
                if(after.y < current.y){
                    //descend
                    return CaseType.TR;
                }else{
                    //monte
                    return CaseType.BR;
                }
            }

        }
        return CaseType.empty;
    }

    public void setCaseTypeAtCoordinate(CaseType type, int x, int y){
        grid[x][y].setType(type);
    }

    public CaseType getCaseTypeAtCoordinate(int x, int y){
        return grid[x][y].getType();
    }

    private void initializeGrid(){
        System.out.println("Resetting grid");
        isDrawing = false;
        paths = new ArrayList<Chemin>();
        linkedCaseType = new HashMap<>();
        
        for (int j = 0; j < grid.length; j++) {
            for (int i = 0; i < grid[j].length; i++) {
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

    public static boolean arePointConnected(Point p1, Point p2){
        int xOffset = p1.x - p2.x;
        int yOffset = p1.y - p2.y;
        //Only check North South East West, not the corner
        if ((Math.abs(xOffset) == 1 && Math.abs(yOffset) == 0) || (Math.abs(xOffset) == 0 && Math.abs(yOffset) ==1)){
            return true;
        }
        return false;
    }

    public static <T> boolean isDuplicate(Collection<T> collection) {
        boolean asDuplicate = false;
        Set<T> set = new HashSet<>();
        for(T t : collection) {
            if (!set.add(t)){
                asDuplicate = true;
            }
        }
        return asDuplicate;
    }

    //Fill grid model with empty case then fill it with level case in json
    public void loadLevel(String path) {
            initializeGrid();
        System.out.println("Loading level located in : "+ path);
            // create Gson instance
            Gson gson = new Gson();

            // create a reader
        Reader reader = null;
        try {
            reader = Files.newBufferedReader(Paths.get(path));
        }catch (Exception ex){
            ex.printStackTrace();
        }

            JsonParser parser = new JsonParser();
            JsonObject rootObj = parser.parse(reader).getAsJsonObject();
            JsonArray pointsArray = rootObj.getAsJsonArray("points");
            for (JsonElement point : pointsArray) {
                JsonObject pointObj = point.getAsJsonObject();

                CaseType type = gson.fromJson(pointObj.get("type"), CaseType.class);
                int coordX = 0;
                int coordY = 0;

                JsonArray coordsArray = pointObj.getAsJsonArray("coords");
                for (JsonElement coord : coordsArray){
                    JsonObject coordObj = coord.getAsJsonObject();
                    coordX = coordObj.get("X").getAsInt();
                    coordY = coordObj.get("Y").getAsInt();
                    setCaseTypeAtCoordinate(type, coordX, coordY);
                }
                linkedCaseType.put(type, false);
            }
            setChanged();
            notifyObservers();
    }
    public void loadLevel (Level level){
        loadLevel(level.path);
    }

    private boolean checkWinState(){
        //Check if all type are connected
        boolean areAllTypeConnected = true;
        for (Map.Entry<CaseType, Boolean> entry: linkedCaseType.entrySet()) {
            if (entry.getValue() == Boolean.FALSE){
                areAllTypeConnected = false;
            }
        }

        //Check if all case are not empty
        boolean areCaseLeftEmpty = false;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if(getCaseTypeAtCoordinate(i,j) == CaseType.empty){
                    areCaseLeftEmpty = true;
                }
            }
        }
        return areAllTypeConnected && !areCaseLeftEmpty;
    }

    public boolean isPointOnAnExistingPath(Point p1){
        boolean isAlreadyUsed = false;
        for (Chemin c : paths){
            for (Point p2 : c.getPoints()){
                if (p1 == p2){
                    isAlreadyUsed = true;
                }
            }
        }
        return isAlreadyUsed;
    }
    //TODO for amelioration : it can return list of all path is in (in case of cross)
    public Chemin getParentPathOfPoint(Point p1){
        for (Chemin c : paths){
            for (Point p2 : c.getPoints()){
                if (p1 == p2){
                    return c;
                }
            }
        }
        return null;
    }

    public void deletePath(Chemin c){
        //Reset all tile except for first and last tile
        for (int i = 1; i < c.getPoints().size()-1 ; i++) {
            Point p = c.getPoints().get(i);
            CaseType toReset = CaseType.empty;
            if (getCaseTypeAtCoordinate(p.x, p.y) == CaseType.cross){
                toReset = CaseType.cross;
            }
            setCaseTypeAtCoordinate(toReset, p.x, p.y);
        }
        paths.remove(c);
        linkedCaseType.put(c.getPathType(), false);
        setChanged();
        notifyObservers();
    }
}
