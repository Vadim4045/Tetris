package com.gmail.focusdigit;

import java.awt.*;
import java.util.InvalidPropertiesFormatException;

public class Figure{

    private final Brick[] bricks;

    public Figure(Figure toClone){
        this.bricks = new Brick[toClone.getBricks().length];
        for(int i=0;i<bricks.length;i++)
            bricks[i]=new Brick(toClone.getBricks()[i]);
    }

    public Figure(Point place, int width, int angle, int... param) throws InvalidPropertiesFormatException {
        if(param.length < 2 || param.length%2 != 0 || angle%90 != 0 || width <= 0)
            throw new InvalidPropertiesFormatException("Bad coordinates");

        bricks = new Brick[param.length/2+1];

        bricks[0] = new Brick(place,width);

        for(int i=0;i<param.length/2;i++)
            bricks[i+1] = new Brick(bricks[0].getX()+param[2*i]*width,
                    bricks[0].getY()+param[2*i+1]*width,
                    width);



        turnRelative(angle);
    }

    public void mooveRelative(int x, int y){
        for(Brick b: bricks){
            b.mooveRelative(x,y);
        }
    }

    public void turnRelative(int angle){
        for(Brick b:bricks){
            Point vector = getPoint().getRelativeVector(b.getPoint());
            switch (angle%360){
                case 270:
                    b.mooveAbsolute(getX() + vector.getY(),
                            getY() - vector.getX());
                    break;
                case 180:
                    b.mooveAbsolute(getX() - vector.getX(),
                            getY() - vector.getY());
                    break;
                case 90:
                    b.mooveAbsolute(getX() - vector.getY(),
                            getY() + vector.getX());
                    break;
                case 0:
                default:
                    break;
            }
        }

    }

    private Point getPoint() {
        return bricks[0].getPoint();
    }

    public int getX(){
        return bricks[0].getX();
    }

    public int getY(){
        return bricks[0].getY();
    }

    public Brick[] getBricks() {
        return bricks;
    }

    public void draw(Graphics g){
        for(Brick b:bricks)
            if(b!=null) b.draw(g);
    }
}