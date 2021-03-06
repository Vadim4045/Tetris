package com.gmail.focusdigit;

public class Point {
    private int X;
    private int Y;

    public Point(Point point){
        setX(point.getX());
        setY(point.getY());
    }

    public Point(int x, int y) {
        this.X = x;
        this.Y = y;
    }

    public void mooveAbsolute(int x, int y){
        setX(x);
        setY(y);
    }

    public void mooveRelative(int x, int y){
        setX(this.getX()+x);
        setY(this.getY()+y);
    }

    public int getX() {
        return X;
    }

    public void setX(int x) {
        this.X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        this.Y = y;
    }

    public Point getRelativeVector(Point point) {
        return new Point(point.getX()-getX(), point.getY()-getY());
    }
}