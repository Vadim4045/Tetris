package com.gmail.focusdigit;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.Random;

public class App extends JFrame
{
    public App() {
        super("Tetris Demo");

        int width = 400;
        int height = 600;
        int brickWidth = 20;

        setSize(width, height);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10,10));

        GamePanel panel = new GamePanel(width, height, brickWidth);
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
}
