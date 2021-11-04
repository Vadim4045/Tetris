package com.gmail.focusdigit;

import java.awt.*;

public class Brick {
    private Point point;
    private int width;

    public Brick(Point point, int width){
        this.point=new Point(point);
        this.width=width;
    }

    public Brick(int x, int y, int width){
        point=new Point(x,y);
        this.width=width;
    }

    public void mooveAbsolute(int x, int y){
        this.point.mooveAbsolute(x,y);
    }

    public void mooveRelative(int x,int y){
        this.point.mooveRelative(x, y);
    }

    public Point getPoint() {
        return point;
    }

    public void draw(Graphics2D g2d){
        g2d.drawRect(point.getX(), point.getY(), width, width);
    }
}
