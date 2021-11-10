package com.gmail.focusdigit;

import javax.swing.*;
import java.awt.*;
import java.util.InvalidPropertiesFormatException;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


public class GamePanel extends JPanel
{
    App parent;
    private final int width;
    private final int height;
    private final int brickWidth;
    private volatile boolean gameFlag;
    private Figure currentFigure;
    private Brick[][] list;
    private int pause;
    private int timeOut;
    private static ThreadPoolExecutor fixedThreadPoolWithQueueSize;


    public GamePanel(App parent, int width, int height, int brickWidth){
        this.parent = parent;
        this.width = width*brickWidth;
        this.height = height*brickWidth;
        this.brickWidth=brickWidth;
        this.setSize(this.width,this.height);
        gameFlag=false;
        timeOut=500;
        pause=timeOut;
        list = new Brick[height][width];

        fixedThreadPoolWithQueueSize = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        fixedThreadPoolWithQueueSize.setCorePoolSize(1);

        startGame();
    }

    private void startGame(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int counter=0;
                    while (true) {
                        parent.addFiguresCount(++counter);

                        currentFigure = getRandomFigure();
                        if(currentFigure==null)continue;

                        while (downCheck()){
                            while (!gameFlag){
                                Thread.sleep(1000);
                            }
                            mooveFigure(-1);

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

    private void figureStop(Figure figure){
        fixedThreadPoolWithQueueSize.getQueue().clear();
        for(Brick b:figure.getBricks()) {
            if (b.getY()<=0) endOfGame();
            list[b.getY() / brickWidth][b.getX() / brickWidth] = b;
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
                    list[i+1][j].setY(list[i+1][j].getY()+brickWidth);
                }
            }
        }
        parent.addLevel();
        setTimeOut(timeOut-10);
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
        fixedThreadPoolWithQueueSize.execute(new MooveTask(code));
    }

    private boolean downCheck(){
        for(Brick brick:currentFigure.getBricks()){
            if(brick.getY()/brickWidth+1 >= list.length) return false;

            for(Brick[] row:list)
                for(Brick b:row) {
                    if (b!=null
                            && brick.getX()==b.getX()
                            && brick.getY() + brickWidth == b.getY())
                        return false;
                }
        }
        return true;
    }

    private boolean turnCheck() {
        Figure f = new Figure((currentFigure));
        f.turnRelative(90);
        if(f.getX()<width/2){
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
            int curX = brick.getX() - brickWidth;
            if (curX < 0) return false;

            for (Brick[] row : list)
                for (Brick b : row)
                    if (b != null && b.getX() == curX
                            && b.getY()==currentFigure.getY()) return false;
        }
        return true;
    }

    private boolean rightCheck() {
        for(Brick brick:currentFigure.getBricks()){
            int curX = brick.getX()+brickWidth;
            if(curX>width-brickWidth) return false;

            for(Brick[] row:list)
                for(Brick b:row)
                    if(b!=null && b.getX()==curX
                            && b.getY()==currentFigure.getY()) return false;
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

    public void setFlag() {
        gameFlag=!gameFlag;
    }

    private void endOfGame() {
        JOptionPane.showMessageDialog(null,
                "You loose!",
                "Tetris demo",
                JOptionPane.WARNING_MESSAGE);
        System.exit(0);
    }

    private class MooveTask implements Runnable{
        private int code;

        public MooveTask(int code) {
            this.code=code;
        }

        @Override
        public void run() {
            switch (code){
                case -1:
                    if(downCheck()) currentFigure.mooveRelative(0,1);
                    break;
                case 32:
                    fastMotion();
                    break;
                case 39:
                    if(rightCheck())currentFigure.mooveRelative(1,0);
                    break;
                case 37:
                    if(leftCheck())currentFigure.mooveRelative(-1,0);
                    break;
                case 38:
                    if (turnCheck()) currentFigure.turnRelative(90);
                    break;
                case 40:
                    if (turnCheck()) currentFigure.turnRelative(270);
                    break;
                default:
                    break;
            }
            GamePanel.this.repaint();
        }
    }
}