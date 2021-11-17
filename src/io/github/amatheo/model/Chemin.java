package io.github.amatheo.model;

public class Chemin {
    private CaseModel[] caseModels;
    private int pathSize = 0;

    void addCase(CaseModel caseModel){
        caseModels[pathSize] = caseModel;
        pathSize++;
    }

    /*
    * cherche la case correspondante a la position
    * puis change la taille accessible, les cases existent encore mais
    * ne sont plus visible.
    * */
    void removeCase(int x, int y){
        for (int i = 0; i < pathSize; i++){
            if(caseModels[i].getPoint().y == y && caseModels[i].getPoint().x == x){
                pathSize = i-1;
            }
        }
    }

    CaseModel[] getPath(){
        return caseModels;
    }


}
