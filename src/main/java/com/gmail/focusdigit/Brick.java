package com.gmail.focusdigit;

import java.awt.*;

public class Brick {
    private Point point;
    final private int width;

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
        this.point.mooveRelative(x*width, y*width);
    }

    public Point getPoint() {
        return point;
    }

    public int getWidth() {
        return width;
    }

    public void draw(Graphics g){
        g.setColor(Color.GRAY);
        g.fillRect(point.getX(), point.getY(), width, width);
        g.setColor(Color.white);
        g.drawRect(point.getX(), point.getY(), width, width);
    }
}
