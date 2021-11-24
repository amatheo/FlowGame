package io.github.amatheo.listener;

import io.github.amatheo.model.CaseModel;
import io.github.amatheo.model.CaseType;
import io.github.amatheo.model.Chemin;
import io.github.amatheo.model.Jeu;
import io.github.amatheo.vc.CaseVue;

import java.awt.*;
import java.awt.event.*;


public class GridMouseListener implements MouseListener, MouseWheelListener, MouseMotionListener {
    private Jeu jeu;

    public GridMouseListener(Jeu jeu) {
        this.jeu = jeu;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        CaseVue caseVue = (CaseVue) e.getSource();
        CaseModel model = caseVue.getModel();

        if(jeu.isDrawing){
            //Clicked on the same type of the starting type AND the case is not the same position as the startTile position
            if (jeu.tempPath.startingTile.getType() == model.getType() &&
                    model.getPoint() != jeu.tempPath.startingTile.getPoint()){

                //TODO jeu.validate(tempPath)
                jeu.isDrawing = false;
            }else
            {
                //Do nothing. Click while moving mouse and creating path
            }

        }else {
            //TODO Check if type is only to S1 -S5
            jeu.tempPath = new Chemin(model);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        CaseVue caseVue = (CaseVue) e.getSource();
        CaseType type = caseVue.getModel().getType();

        Point clickedPoint = caseVue.getModel().getPoint();

        /*
        before
            int size = jeu.tempPath.getPoints().size();

            Point lastPoint = jeu.tempPath.getPoints().get(size); //size-1 nan ?

            boolean isPointAlreadyInPath = jeu.tempPath.getPoints().contains(caseVue.getModel().getPoint());
            boolean isTypeAccepted = type == CaseType.empty; //TODO Add cross compatibility
         */
        //after je suis pas sur de ce que j'ai fait
        //mais ducoup de ce que j'ai cru comprendre
        //peut etre qu'au debut ya pas encre de tempPath donc ca trouve pas vu que c'est null
        int size;
        Point lastPoint;
        boolean isPointAlreadyInPath;
        if(jeu.tempPath == null){
            size = 0;
            lastPoint = null;
            isPointAlreadyInPath = false;
        }else{
            size = jeu.tempPath.getPoints().size();
            lastPoint = jeu.tempPath.getPoints().get(size);
            isPointAlreadyInPath = jeu.tempPath.getPoints().contains(caseVue.getModel().getPoint());
        }
        boolean isTypeAccepted = type == CaseType.empty; //TODO Add cross compatibility
        //---


        if (!jeu.isDrawing){
            return;
        }else if (caseVue.getModel().getPoint() == lastPoint){
                //Delete Point
        }else if(isTypeAccepted && !isPointAlreadyInPath && arePointConnected(lastPoint, clickedPoint)){

            jeu.tempPath.addPoint(caseVue.getModel().getPoint());
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }

    private boolean arePointConnected(Point p1, Point p2){
         int xOffset = p1.x - p2.x;
         int yOffset = p1.y - p2.y;
         //Only check North South East West, not the corner
         if ((Math.abs(xOffset) == 1 && Math.abs(yOffset) == 0) || (Math.abs(xOffset) == 0 && Math.abs(yOffset) ==1)){
             return true;
         }
         return false;
    }
}
