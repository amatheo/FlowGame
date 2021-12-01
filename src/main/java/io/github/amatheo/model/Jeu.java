package io.github.amatheo.model;

import com.google.gson.*;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import java.awt.*;
import java.util.List;
import java.util.stream.Stream;

public class Jeu extends Observable {
    private CaseModel[][] grid;
    private ArrayList<Chemin> paths;

    public Chemin tempPath;
    public boolean isDrawing;

    public HashMap<CaseType, Boolean> linkedCaseType;

    public Jeu(int size) {
        grid = new CaseModel[size][size];
        initializeGrid();

        isDrawing = false;
        paths = new ArrayList<Chemin>();
    }

    public void addPath(Chemin chemin){
        if (validatePath(chemin)){
            fillPathType(chemin);
            paths.add(chemin);
            //linkedCaseType.put(chemin.startingTile.getType(), true);
        }
        setChanged();
        notifyObservers();
    }
    //Check if path and type is valid
    private boolean validatePath(Chemin chemin){
        Point firstPoint = chemin.getPoints().get(0);
        Point lastPoint = chemin.getPoints().get(chemin.getPoints().size()-1);
        CaseType cheminType = chemin.startingTile.getType();

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

            if (!(
                    arePointConnected(before, current)
                    && arePointConnected(current, after)
                    && getCaseTypeAtCoordinate(current.x, current.y) == CaseType.empty
            )){
                isPathInvalid = true;
            }
        }
        //Check if point of start is not same as the path endpoint but the same one as the path one
        boolean areEndpointsValid = false;
        if (
                chemin.startingTile.getPoint() != lastPoint
                && chemin.startingTile.getPoint() == firstPoint
                && !isDuplicate(chemin.getPoints()) //Check if there is duplicated point in the path ex : a loop
        ){
            areEndpointsValid = true;
        }
        System.out.println("Does the path loop : "+isDuplicate(chemin.getPoints()));
        System.out.println("TypeValid : "+ isTypeValid+"; PathValid : "+!isPathInvalid+"; EndpointValid : "+areEndpointsValid+";");
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
            if(before.x == current.x){
                if(after.x == current.x){
                    return CaseType.TB;
                }else{
                    if(after.y > current.y){
                        if(after.x < current.x){
                            return CaseType.BL;
                        }else{
                            return CaseType.BR;
                        }
                    }else{
                        if(after.x < current.x){
                            return CaseType.BL;
                        }else{
                            return CaseType.BR;
                        }
                    }
                }
            }
            if(before.y == current.y){
                if(after.y == current.y){
                    return CaseType.LR;
                }else{
                    if(after.x > current.x){
                        if(after.y < current.y){
                            return CaseType.BR;
                        }else{
                            return CaseType.BL;
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
        return CaseType.empty;
    }

    public void setCaseTypeAtCoordinate(CaseType type, int x, int y){
        grid[x][y].setType(type);
    }

    public CaseType getCaseTypeAtCoordinate(int x, int y){
        return grid[x][y].getType();
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

    public void loadLevel(Level level) throws IOException {
        try {
            // create Gson instance
            Gson gson = new Gson();

            // create a reader
            Reader reader = Files.newBufferedReader(Paths.get(level.path));

            JsonParser parser = new JsonParser();
            JsonObject rootObj = parser.parse(reader).getAsJsonObject();
            JsonArray pointsArray = rootObj.getAsJsonArray("points");
            for (JsonElement point : pointsArray) {
                JsonObject pointObj = point.getAsJsonObject();

                CaseType type = gson.fromJson(pointObj.get("type"), CaseType.class);
                System.out.println(type.toString());
                int coordX = 0;
                int coordY = 0;

                JsonArray coordsArray = pointObj.getAsJsonArray("coords");
                for (JsonElement coord : coordsArray){
                    JsonObject coordObj = coord.getAsJsonObject();
                    coordX = coordObj.get("X").getAsInt();
                    coordY = coordObj.get("Y").getAsInt();
                }
                setCaseTypeAtCoordinate(type, coordX, coordY);
            }
            //TODO fill linkedCasetype to false with key present in json
            setChanged();
            notifyObservers();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean checkWinState(){
        //TODO Hashmap linked with boolean to a type
        for (int i = 0; i < linkedCaseType.size(); i++) {
            
        }
        return false;
    }
}
