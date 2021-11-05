package com.gmail.focusdigit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.Random;

public class GamePanel extends JPanel {
    private final int width;
    private final int height;
    private final int brickWidth;
    private boolean gameFlag,mooveFlag;
    private Figure currentFigure;
    private Brick[][] list;
    private int timeOut;

    public GamePanel(int width, int height, int brickWidth) {
        this.width = width-20;
        this.height = height-20;
        this.brickWidth=brickWidth;
        gameFlag=true;
        mooveFlag=true;
        timeOut=500;
        list = new Brick[this.height/brickWidth][this.width/brickWidth];
        start();
    }

    private void start() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (gameFlag) {
                            currentFigure = getRandomFigure(20);
                        if(currentFigure==null)continue;

                        while (mooveCheck(currentFigure)){
                            while (!mooveFlag) Thread.sleep(1);

                            synchronized (currentFigure){
                                currentFigure.mooveRelative(0,1);
                                GamePanel.this.repaint();
                            }

                            Thread.sleep(timeOut);
                        }

                        figureStop(currentFigure);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private boolean mooveCheck(Figure figure){
        for(Brick brick:figure.getBricks()){
            for(Brick[] row:list)
                for(Brick b:row) {
                    if (brick.getPoint().getY() + brickWidth>=height ||
                            (b!=null &&
                                brick.getPoint().getX()==b.getPoint().getX() &&
                                brick.getPoint().getY() + brickWidth == b.getPoint().getY()))
                                     return false;
                }
            }
        return true;
    }

    private void figureStop(Figure figure){
        for(Brick b:figure.getBricks()){
            list[b.getPoint().getY()/brickWidth][b.getPoint().getX()/brickWidth] = b;
        }
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        currentFigure.draw(g);
        for(Brick[] row:list) {
            for(Brick b:row)
                if(b!=null)b.draw(g);
        }
    }

    public void turnRight(int code){
        System.out.println(code);
        switch (code){
            case 39:
                mooveFlag=false;
                    currentFigure.mooveRelative(1,0);
                mooveFlag=true;
                break;
            case 37:
                mooveFlag=false;
                currentFigure.mooveRelative(-1,0);
                mooveFlag=true;
                break;
            case 38:
                mooveFlag=false;
                try {
                    currentFigure.turnRelative(90);
                } catch (InvalidPropertiesFormatException invalidPropertiesFormatException) {
                    invalidPropertiesFormatException.printStackTrace();
                }
                mooveFlag=true;
                break;
            default:
                break;
        }
    }

    private Figure getRandomFigure(int brickWidth) {
        try {
            final int[][] sets = {{1, 0}, {1, 0, -1, 0}, {1, 0, 2, 0, -1, 0},
                    {-1, 0, 0, -1, 1, -1}, {1, 0, 0, -1, -1, 0}, {1, 0, -1, 0, -1, -1},
                    {1, 0, -1, 0, 1, -1}, {1, 0, 0, -1, -1, -1}};

            Random ran = new Random();
            int num = ran.nextInt(sets.length);
            int angle = ran.nextInt(4);
            int placeX = ran.nextInt(width/brickWidth-6)+3;

            return  new Figure(new Point(placeX * brickWidth, -2 * brickWidth),
                    brickWidth, angle * 90, sets[num]);
        }catch (InvalidPropertiesFormatException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public void fastMotion() {
        setTimeOut(20);
    }

    public void slowMoition(){
        setTimeOut(500);
    }
}
