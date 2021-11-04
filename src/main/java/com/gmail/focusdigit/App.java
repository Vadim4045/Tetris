package com.gmail.focusdigit;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.Random;

public class App extends JFrame
{
    private final int width=400;
    private final int height=600;
    private boolean gameFlag,mooveFlag;
    private Figure currentFigure;

    public App() {
        super("Tetris Demo");

        getContentPane().setBackground(Color.GRAY);
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        gameFlag=true;
        mooveFlag=true;
    }

    private void start(final Graphics g) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Graphics2D g2d = (Graphics2D) g;
                    Stroke stroke1 = new BasicStroke(1f);
                    g2d.setColor(Color.BLACK);
                    g2d.setStroke(stroke1);
                    while (gameFlag) {
                        currentFigure = getRandomFigure(20);
                        if(currentFigure==null)continue;

                        while (currentFigure.getPoint().getY()<height){
                            while (!mooveFlag) Thread.sleep(1);
                            currentFigure.mooveRelative(0,1);
                            refresh(g2d);
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

    private void refresh(Graphics2D g2d) {
        this.repaint();
        currentFigure.draw(g2d);
    }

    public void paint(Graphics g) {
        super.paint(g);
        start(g);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new App().setVisible(true);

            }
        });
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
}
