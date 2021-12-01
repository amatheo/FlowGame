package io.github.amatheo.listener;

import io.github.amatheo.model.CaseModel;
import io.github.amatheo.model.CaseType;
import io.github.amatheo.model.Chemin;
import io.github.amatheo.model.Jeu;
import io.github.amatheo.vc.CaseVue;
import io.github.amatheo.vc.GrilleVue;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


public class GridMouseListener implements MouseListener, MouseWheelListener, MouseMotionListener {
    private Jeu jeu;
    ArrayList<CaseType> validCaseType;
    public GridMouseListener(Jeu jeu) {
        this.jeu = jeu;
        validCaseType = new ArrayList<>();
        validCaseType.add(CaseType.S1);
        validCaseType.add(CaseType.S2);
        validCaseType.add(CaseType.S3);
        validCaseType.add(CaseType.S4);
        validCaseType.add(CaseType.S5);
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
        }else if (
                validCaseType.contains(model.getType())
        ){
            System.out.println("New path ("+model.getType()+") started");
            caseVue.setSelected();
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
            Point preLastPoint = null;
            if (jeu.tempPath.getPoints().size() > 2){
                preLastPoint = jeu.tempPath.getPoints().get(jeu.tempPath.getPoints().size()-2);
            }
            System.out.println("Entered point "+ enteredPoint);
            if (enteredPoint == preLastPoint) {
                System.out.println("Delete Point");
                GrilleVue.caseVueHashmap.get(lastPoint).deselect();
                jeu.tempPath.getPoints().remove(lastPoint);

            } else if (
                    (type == CaseType.empty || type == jeu.tempPath.startingTile.getType())
                            && Jeu.arePointConnected(enteredPoint, lastPoint)
                            && Jeu.arePointConnected(lastPoint, enteredPoint)
            ) {
                System.out.println("Added point "+ caseVue.getModel().getPoint());
                caseVue.setSelected();
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
