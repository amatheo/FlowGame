package io.github.amatheo.vc;

import io.github.amatheo.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Objects;
import java.util.Random;

public class CaseVue extends JPanel {
    private CaseModel caseModel;
    public boolean isSelected = false;
    public CaseVue(CaseModel caseModel) {
        this.caseModel = caseModel;
    }
    public Color couleur;

    private void drawTop(Graphics g) {
        g.drawLine(getWidth()/2, getHeight()/2, getWidth()/2, 0);
    }//top

    private void drawLeft(Graphics g) {
        g.drawLine(0, getHeight()/2, getWidth()/2, getHeight()/2);
    }//left

    private void drawBottom(Graphics g) {
        g.drawLine(getWidth()/2, getHeight()/2, getWidth()/2, getHeight());
    }//bottom

    private void drawRight(Graphics g) {
        g.drawLine(getWidth()/2, getHeight()/2, getWidth(), getHeight()/2);
    }//right

    public void setSelected(){
        isSelected = true;
        repaint();
    }

    public void deselect(){
        isSelected = false;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());

        if (isSelected){
            g.setColor(Color.RED);
            g.fillRoundRect(getWidth()/4, getHeight()/4, getWidth()/2, getHeight()/2, 5, 5);
            g.setColor(Color.black);
        }


        Rectangle2D deltaText =  g.getFont().getStringBounds("0", g.getFontMetrics().getFontRenderContext()); // "0" utilisé pour gabarit
        g.setColor(couleur);
        switch(caseModel.getType()) {
            case empty:
                g.drawRoundRect(getWidth()/4, getHeight()/4, getWidth()/2, getHeight()/2, 5, 5);
                break;
            case S1 :
                g.drawRoundRect(getWidth()/4, getHeight()/4, getWidth()/2, getHeight()/2, 5, 5);
                g.drawString("1", getWidth()/2 - (int) deltaText.getCenterX(), getHeight()/2 - (int) deltaText.getCenterY());
                break;
            case S2 :
                g.drawRoundRect(getWidth()/4, getHeight()/4, getWidth()/2, getHeight()/2, 5, 5);
                g.drawString("2", getWidth()/2  - (int) deltaText.getCenterX(), getHeight()/2 - (int) deltaText.getCenterY());
                break;
            case S3 :
                g.drawRoundRect(getWidth()/4, getHeight()/4, getWidth()/2, getHeight()/2, 5, 5);
                g.drawString("3", getWidth()/2  - (int) deltaText.getCenterX(), getHeight()/2 - (int) deltaText.getCenterY());
                break;
            case S4 :
                g.drawRoundRect(getWidth()/4, getHeight()/4, getWidth()/2, getHeight()/2, 5, 5);
                g.drawString("4", getWidth()/2  - (int) deltaText.getCenterX(), getHeight()/2 - (int) deltaText.getCenterY());
                break;
            case S5 :
                g.drawRoundRect(getWidth()/4, getHeight()/4, getWidth()/2, getHeight()/2, 5, 5);
                g.drawString("5", getWidth()/2  - (int) deltaText.getCenterX(), getHeight()/2 - (int) deltaText.getCenterY());
                break;
            case TL:
                g.drawRoundRect(getWidth()/4, getHeight()/4, getWidth()/2, getHeight()/2, 5, 5);
                drawLeft(g);
                drawTop(g);
                break;
            case BL:
                g.drawRoundRect(getWidth()/4, getHeight()/4, getWidth()/2, getHeight()/2, 5, 5);
                drawLeft(g);
                drawBottom(g);
                break;
            case TR:
                g.drawRoundRect(getWidth()/4, getHeight()/4, getWidth()/2, getHeight()/2, 5, 5);
                drawRight(g);
                drawTop(g);
                break;
            case BR:
                g.drawRoundRect(getWidth()/4, getHeight()/4, getWidth()/2, getHeight()/2, 5, 5);
                drawRight(g);
                drawBottom(g);
                break;
            case LR:
                g.drawRoundRect(getWidth()/4, getHeight()/4, getWidth()/2, getHeight()/2, 5, 5);
                drawRight(g);
                drawLeft(g);
                break;
            case TB:
                g.drawRoundRect(getWidth()/4, getHeight()/4, getWidth()/2, getHeight()/2, 5, 5);
                drawTop(g);
                drawBottom(g);
                break;
            case cross:
                g.setColor(Color.black);
                g.drawRoundRect(getWidth()/4, getHeight()/4, getWidth()/2, getHeight()/2, 5, 5);
                drawTop(g);
                drawBottom(g);
                drawRight(g);
                drawLeft(g);
                break;
            case BLOCK:
                //g.fillRoundRect(getWidth()/4, getHeight()/4, getWidth()/2, getHeight()/2, 5, 5);
                break;
        }
    }

    public CaseModel getModel(){
        return caseModel;
    }

    public String toString() {
        return caseModel.getPoint().x + ", " + caseModel.getPoint().y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CaseVue caseVue = (CaseVue) o;
        return Objects.equals(caseModel, caseVue.caseModel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(caseModel);
    }
}