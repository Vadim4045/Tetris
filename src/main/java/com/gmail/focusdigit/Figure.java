package com.gmail.focusdigit;

import java.awt.*;
import java.util.InvalidPropertiesFormatException;

public class Figure {

    private Brick[] bricks;


    public Figure(Point place, int width, int angle, int... param) throws InvalidPropertiesFormatException {
        if(param.length < 2 || param.length%2 != 0 || angle%90 != 0 || width < 0)
            throw new InvalidPropertiesFormatException("Bad coordinates");

        bricks = new Brick[param.length/2+1];

        bricks[0] = new Brick(place,width);

        for(int i=0;i<param.length/2;i++)
            bricks[i+1] = new Brick(bricks[0].getPoint().getX()+param[2*i]*width,
                    bricks[0].getPoint().getY()+param[2*i+1]*width,
                                         width);

        //turnRelative(angle);
    }

    public void mooveRelative(int x, int y){
        for(Brick b: bricks){
            b.mooveRelative(x,y);
        }
    }

    public void turnRelative(int angle) throws InvalidPropertiesFormatException {
        if(angle%90!=0) throw new InvalidPropertiesFormatException("Angle is not correct");

        for(Brick b:bricks){
            Point vector = bricks[0].getPoint().getRelativeVector(b.getPoint());
            switch (angle%360){
                case 270:
                    b.mooveRelative(vector.getY(),-vector.getX());
                    break;
                case 180:
                    b.mooveRelative(-vector.getX(),-vector.getY());
                    break;
                case 90:
                    b.mooveRelative(-vector.getY(),vector.getX());
                    break;
                case 0:
                default:
                    break;
            }
        }

    }

    public Point getPoint(){
        return bricks[0].getPoint();
    }

    public Brick[] getBricks() {
        return bricks;
    }

    public void draw(Graphics2D g2d){
        for(Brick b:bricks)
            if(b!=null) b.draw(g2d);
    }
}
