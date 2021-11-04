package com.gmail.focusdigit;

import javax.swing.*;
import java.awt.*;
import java.util.InvalidPropertiesFormatException;
import java.util.Random;

public class GamePanel extends JPanel {
    private final int width;
    private final int height;
    private final int brickWidth;
    private boolean gameFlag,mooveFlag;
    private Figure currentFigure;

    public GamePanel(int width, int height, int brickWidth) {
        this.width = width-20;
        this.height = height-20;
        this.brickWidth=brickWidth;
        gameFlag=true;
        mooveFlag=true;
        start();
    }

    private void start() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (gameFlag) {
                        if(currentFigure==null || currentFigure.getPoint().getY()>=height)
                            currentFigure = getRandomFigure(20);
                        if(currentFigure==null)continue;

                        System.out.println(currentFigure.getPoint().getY()<height);

                        while (currentFigure.getPoint().getY()<height){
                            while (!mooveFlag) Thread.sleep(1);

                            synchronized (currentFigure){
                                currentFigure.mooveRelative(0,1);
                                GamePanel.this.repaint();
                            }

                            Thread.sleep(500);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
/*        Graphics2D g2d = (Graphics2D) g;
        Stroke stroke1 = new BasicStroke(1f);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(stroke1);*/
        currentFigure.draw(g);
    }

    private void refresh(Graphics2D g2d) {
        GamePanel.this.repaint();
        currentFigure.draw(g2d);
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
            System.out.println("Created new figure");
            return  new Figure(new Point(placeX * brickWidth, -2 * brickWidth),
                    brickWidth, angle * 90, sets[num]);
        }catch (InvalidPropertiesFormatException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean isGameFlag() {
        return gameFlag;
    }

    public void setGameFlag(boolean gameFlag) {
        this.gameFlag = gameFlag;
    }

    public boolean isMooveFlag() {
        return mooveFlag;
    }

    public void setMooveFlag(boolean mooveFlag) {
        this.mooveFlag = mooveFlag;
    }
}
