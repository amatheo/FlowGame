package io.github.amatheo.model;

import java.awt.*;

public class CaseModel {
    private CaseType type;

    private Point point;

    public CaseModel(CaseType type, Point point) {
        this.type = type;
        this.point = point;
    }

    public CaseType getType() {
        return type;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public void setType(CaseType type) {
        this.type = type;
    }
}
