package io.github.amatheo.listener;

import io.github.amatheo.model.CaseModel;
import io.github.amatheo.model.CaseType;
import io.github.amatheo.model.Chemin;
import io.github.amatheo.model.Jeu;
import io.github.amatheo.vc.CaseVue;
import io.github.amatheo.vc.GrilleVue;

import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


public class GridMouseListener implements MouseListener, MouseWheelListener, MouseMotionListener {
    private final Jeu jeu;
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
        Point clickedPoint = model.getPoint();
        CaseType clickedType = model.getType();


        System.out.println("Clicked on (" + clickedType + "): " + clickedPoint);

        if (jeu.isDrawing) {
            //Clicked on the same type of the starting type AND the case is not the same position as the startTile position
            if (jeu.tempPath.getPathType() == clickedType) {
               if (clickedPoint == jeu.tempPath.getFirstPoint()){
                   //Reset all selection
                   for (int i = 0; i < jeu.tempPath.getSize(); i++) {
                       Point p = jeu.tempPath.getPoints().get(i);
                       GrilleVue.caseVueHashmap.get(p).deselect();
                   }
                   jeu.tempPath = null;
                   jeu.isDrawing = false;

               }else if (clickedPoint == jeu.tempPath.getLastPoint()){
                   System.out.println("Path ended");
                   jeu.addPath(jeu.tempPath);
                   jeu.isDrawing = false;
               }
            }
        } else {
            if (jeu.isPointOnAnExistingPath(clickedPoint)) {
                //TODO for amelioration : it can loop trough all path of the point then kill all of them
                Chemin parent = jeu.getParentPathOfPoint(clickedPoint);
                jeu.deletePath(parent);
            } else if (
                    validCaseType.contains(clickedType)
                            && !jeu.linkedCaseType.get(clickedType)
            ) {
                System.out.println("New path (" + clickedType + ") started");
                caseVue.setSelected();
                jeu.tempPath = new Chemin(clickedPoint, clickedType);
                jeu.isDrawing = true;
            }
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
        Dimension d = caseVue.getSize();
        if (jeu.isDrawing) {
            Point enteredPoint = caseVue.getModel().getPoint();
            Point lastPoint = jeu.tempPath.getLastPoint();
            Point preLastPoint = null;
            if (jeu.tempPath.getPoints().size() > 2) {
                preLastPoint = jeu.tempPath.getPoints().get(jeu.tempPath.getPoints().size() - 2);
            }
            //In case of loop deselect and remove all point prior to this one
            if (jeu.tempPath.getPoints().contains(enteredPoint)) {
                int indexPoint = jeu.tempPath.getPoints().indexOf(enteredPoint);
                for (int i = jeu.tempPath.getPoints().size() - 1; i > indexPoint; i--) {
                    Point p = jeu.tempPath.getPoints().get(i);
                    jeu.tempPath.getPoints().remove(i);
                    GrilleVue.caseVueHashmap.get(p).deselect();
                }
            } else if (enteredPoint == preLastPoint) {
                //Delete last case
                GrilleVue.caseVueHashmap.get(lastPoint).deselect();
                jeu.tempPath.removePoint(lastPoint);
            } else {
                CaseType lastType = jeu.getCaseTypeAtCoordinate(lastPoint.x, lastPoint.y);
                if (jeu.tempPath.getSize() > 2 && lastType == jeu.tempPath.getPathType()){

                }else if (
                        (type == CaseType.empty || type == jeu.tempPath.getPathType() || type == CaseType.cross)
                                && Jeu.arePointConnected(enteredPoint, lastPoint)
                                && Jeu.arePointConnected(lastPoint, enteredPoint)
                ) {
                    System.out.println("Added point " + enteredPoint);
                    caseVue.setSelected();
                    jeu.tempPath.addPoint(enteredPoint);
                }
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
