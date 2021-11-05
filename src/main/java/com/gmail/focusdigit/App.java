package com.gmail.focusdigit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class App extends JFrame implements KeyListener
{
    private static final int cellsWidth = 16;
    private static final int cellsHeight = 30;
    GamePanel panel;

    public App() {
        super("Tetris Demo");

        int brickWidth;
        int height;
        int width;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double screeHeight = screenSize.getHeight();
        double screenWidth = screenSize.getWidth();
        if(screenWidth>screeHeight){
            brickWidth = (int)Math.round(screeHeight*0.8/cellsHeight);
            height = brickWidth*cellsHeight;
            width = brickWidth*cellsWidth;
        }else{
            brickWidth = (int)Math.round(screenWidth*0.8/cellsWidth);
            height = brickWidth*cellsHeight;
            width = brickWidth*cellsWidth;
        }

        setSize(width, height);
        this.addKeyListener(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10,10));

        panel = new GamePanel(width, height, brickWidth);
        getContentPane().add(panel, BorderLayout.CENTER);
    }



    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new App().setVisible(true);

            }
        });
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==40) panel.fastMotion();
        else panel.turnRight(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        panel.slowMoition();
    }
}
