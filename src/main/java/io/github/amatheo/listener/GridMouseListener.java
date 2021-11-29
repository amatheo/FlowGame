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
        System.out.println("Clicked on : "+ model.getPoint());

        System.out.println("Game drawing : "+jeu.isDrawing);
        if(jeu.isDrawing){
            //Clicked on the same type of the starting type AND the case is not the same position as the startTile position
            if (
                    jeu.tempPath.startingTile.getType() == model.getType()
                    && model.getPoint() != jeu.tempPath.startingTile.getPoint()
            ){
                System.out.println("Path sent to validation");
                jeu.isDrawing = false;
            }else
            {
                //Do nothing. Click while moving mouse and creating path
            }
        }else {
            System.out.println("Created path. Start Point : "+ model.getPoint());
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

        boolean isTypeAccepted = type == CaseType.empty; //TODO Add cross compatibility


        if (jeu.isDrawing) {

            Point enteredPoint = caseVue.getModel().getPoint();
            Point lastPoint = jeu.tempPath.getPoints().get(jeu.tempPath.getPoints().size()-1);

            if (enteredPoint == lastPoint) {
                //Delete point
            } else if (
                    isTypeAccepted
                            && !Jeu.arePointConnected(enteredPoint, lastPoint)
                            && Jeu.arePointConnected(lastPoint, enteredPoint)
            ) {
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
