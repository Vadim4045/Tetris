package com.gmail.focusdigit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.Random;

public class GamePanel extends JPanel {
    App parent;
    private final int width;
    private final int height;
    private final int brickWidth;
    private volatile boolean gameFlag,mooveFlag,stepFlag;
    private Figure currentFigure;
    private Brick[][] list;
    private int pause;
    private int timeOut;

    public GamePanel(App parent, int width, int height, int brickWidth){
        this.parent = parent;
        this.width = width*brickWidth;
        this.height = height*brickWidth;
        this.brickWidth=brickWidth;
        this.setSize(this.width,this.height);
        gameFlag=true;
        mooveFlag=true;
        stepFlag=false;
        timeOut=500;
        pause=timeOut;
        list = new Brick[height][width];

        start();
    }

    private void start(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int counter=0;
                    while (gameFlag) {
                        parent.addFiguresCount(++counter);

                        currentFigure = getRandomFigure();
                        if(currentFigure==null)continue;

                        while (mooveCheck(currentFigure)){
                            while (!mooveFlag) Thread.sleep(1);
                            stepFlag=true;
                            currentFigure.mooveRelative(0,1);
                            GamePanel.this.repaint();
                            stepFlag=false;

                            Thread.sleep(pause);
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

    private void endOfGame() {
        JOptionPane.showMessageDialog(null,
                "You loose!",
                "Tetris demo",
                JOptionPane.WARNING_MESSAGE);
        System.exit(0);
    }

    private boolean mooveCheck(Figure figure){
        for(Brick brick:figure.getBricks()){
            if(brick.getPoint().getY()/brickWidth+1 >= list.length) return false;

            for(Brick[] row:list)
                for(Brick b:row) {
                    if (b!=null
                            && brick.getPoint().getX()==b.getPoint().getX()
                                && brick.getPoint().getY() + brickWidth == b.getPoint().getY())
                                     return false;
                }
            }
        return true;
    }

    private void figureStop(Figure figure){
        for(Brick b:figure.getBricks()) {
            if (b.getPoint().getY()<=0) endOfGame();
            list[b.getPoint().getY() / brickWidth][b.getPoint().getX() / brickWidth] = b;
        }
        checkMap();
    }

    private void checkMap() {
        for(int i=list.length-1;i>=0;i--){
            int count=0;
            for(Brick b:list[i])
                if(b!=null) count++;

            if(count==0) return;

            if(count==list[i].length) {
                for(int j=0;j<list[i].length;j++)
                    list[i][j]=null;
                dounMap(i++);
            }
        }
    }

    private void dounMap(int line) {
        for(int i=line-1;i>=0;i--) {
            for (int j=0;j<list[i].length;j++) {
                if(list[i][j]!=null){
                    list[i+1][j]=list[i][j];
                    list[i][j]=null;
                    list[i+1][j].getPoint().setY(list[i+1][j].getPoint().getY()+brickWidth);
                }
            }
        }
        parent.addLevel();
        setTimeOut(timeOut-20);
        slowMoition();
        GamePanel.this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        for(Brick[] row:list) {
            for(Brick b:row)
                if(b!=null)b.draw(g);

        currentFigure.draw(g);
        }
    }

    public void mooveFigure(int code){
        switch (code){
            case 39:
                while (stepFlag) {
                    try {
                        Thread.currentThread().sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                    mooveFlag=false;
                    if(rightCheck())currentFigure.mooveRelative(1,0);
                    mooveFlag=true;
                break;
            case 37:
                while (stepFlag) {
                    try {
                        Thread.currentThread().sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                    mooveFlag=false;
                    if(leftCheck())currentFigure.mooveRelative(-1,0);
                    mooveFlag=true;
                break;
            case 38:
                while (stepFlag) {
                    try {
                        Thread.currentThread().sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mooveFlag=false;
                if (turnCheck()) currentFigure.turnRelative(90);
                mooveFlag=true;
                break;
            case 40:
                while (stepFlag) {
                    try {
                        Thread.currentThread().sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mooveFlag=false;
                if (turnCheck()) currentFigure.turnRelative(270);
                mooveFlag=true;
                break;
            default:
                break;
        }
    }

    private boolean turnCheck() {
        Figure f = new Figure((currentFigure));
        f.turnRelative(90);
        if(f.getPoint().getX()<width/2){
            f.mooveRelative(1,0);
            if(leftCheck())return true;
        }else{
            f.mooveRelative(-1,0);
            if(rightCheck()) return true;
        }
        return false;
    }

    private boolean leftCheck() {
        for(Brick brick:currentFigure.getBricks()) {
            int curX = brick.getPoint().getX() - brickWidth;
            if (curX < 0) return false;

            for (Brick[] row : list)
                for (Brick b : row)
                    if (b != null && b.getPoint().getX() == curX
                        && b.getPoint().getY()==currentFigure.getPoint().getY()) return false;
        }
        return true;
    }

    private boolean rightCheck() {
        for(Brick brick:currentFigure.getBricks()){
            int curX = brick.getPoint().getX()+brickWidth;
            if(curX>width-brickWidth) return false;

            for(Brick[] row:list)
                for(Brick b:row)
                    if(b!=null && b.getPoint().getX()==curX
                        && b.getPoint().getY()==currentFigure.getPoint().getY()) return false;
        }
        return true;
    }

    private Figure getRandomFigure() {
        try {
            final int[][] sets = {{1, 0}, {1, 0, -1, 0}, {1, 0, 2, 0, -1, 0},
                    {-1, 0, 0, -1, 1, -1}, {1, 0, 0, -1, -1, 0}, {1, 0, -1, 0, -1, -1},
                    {1, 0, -1, 0, 1, -1}, {1, 0, 0, -1, -1, -1},{1, 0, 0, -1, 1, -1}};

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

    public void setPause(int pause) {
        this.pause = pause;
    }

    public void fastMotion() {
        setPause(timeOut/10);
    }

    public void slowMoition(){
        setPause(timeOut);
    }
}
