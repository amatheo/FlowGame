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
        System.out.println("Clicked on ("+ model.getType() +"): "+ model.getPoint());
        if(jeu.isDrawing){
            //Clicked on the same type of the starting type AND the case is not the same position as the startTile position
            if (
                    jeu.tempPath.startingTile.getType() == model.getType()
                    && model.getPoint() != jeu.tempPath.startingTile.getPoint()
            ){
                System.out.println("Path ended");
                jeu.addPath(jeu.tempPath);
                jeu.isDrawing = false;
            }else
            {
                //Do nothing. Click while moving mouse and creating path
            }
        }else {
            //TODO add check to prevent creating path on empty case
            System.out.println("New path ("+model.getType()+") started");
            jeu.tempPath = new Chemin(model);
            jeu.isDrawing = true;
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

        if (jeu.isDrawing) {
            Point enteredPoint = caseVue.getModel().getPoint();
            Point lastPoint =  jeu.tempPath.getPoints().get(jeu.tempPath.getPoints().size()-1);
            System.out.println("Entered point "+ enteredPoint);
            if (enteredPoint == lastPoint) {
                //TODO Better delete
                System.out.println("Delete Point");
            } else if (
                    (type == CaseType.empty || type == jeu.tempPath.startingTile.getType())
                            && Jeu.arePointConnected(enteredPoint, lastPoint)
                            && Jeu.arePointConnected(lastPoint, enteredPoint)
            ) {
                System.out.println("Added point "+ caseVue.getModel().getPoint());
                jeu.tempPath.addPoint(caseVue.getModel().getPoint());
            }
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
}
