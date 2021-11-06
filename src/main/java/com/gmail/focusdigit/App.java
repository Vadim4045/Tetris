package com.gmail.focusdigit;

import com.github.kwhat.jnativehook.*;
import com.github.kwhat.jnativehook.keyboard.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.InvalidPropertiesFormatException;

public class App extends JFrame implements ActionListener, NativeKeyListener
{
    private static int level = 0;
    private static final int cellsWidth = 16;
    private static final int cellsHeight = 25;
    int brickWidth;
    private GamePanel panel=null;
    private JPanel nextPanel;
    private JLabel[] infoLabels;
    private JButton[] buttons;

    public App() {
        super("Tetris Demo");

        final String[] forButtons = {"left", "turn", "right", "doun"};
        int height;
        int width;

        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            e.printStackTrace();
            System.exit(1);
        }
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double screeHeight = screenSize.getHeight();
        double screenWidth = screenSize.getWidth();
        if(screenWidth>screeHeight){
            brickWidth = (int)Math.round(screeHeight*0.7/cellsHeight);
            height = brickWidth*cellsHeight+brickWidth;
            width = brickWidth*cellsWidth+brickWidth;
        }else{
            brickWidth = (int)Math.round(screenWidth*0.7/cellsWidth);
            height = brickWidth*cellsHeight+brickWidth;
            width = brickWidth*cellsWidth+brickWidth;
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topPanel = makeTopPanel();
        add(topPanel,BorderLayout.NORTH);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

        JPanel tmpPanel = new JPanel();
        buttons = new JButton[forButtons.length];
        for(int i=0;i<3;i++){
            buttons[i] = new JButton(forButtons[i]);
            buttons[i].setActionCommand(String.valueOf(i));
            buttons[i].addActionListener(this);
            tmpPanel.add(buttons[i]);
        }
        controlPanel.add(tmpPanel);

        tmpPanel = new JPanel();
        buttons[3]=new JButton(forButtons[3]);
        buttons[3].setActionCommand("3");
        buttons[3].addActionListener(this);
        buttons[3].getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(buttons[3].getModel().isArmed()){
                    panel.fastMotion();
                }else{
                    panel.slowMoition();
                }
            }
        });
        tmpPanel.add(buttons[3]);
        controlPanel.add(tmpPanel);


        add(controlPanel, BorderLayout.SOUTH);

        panel = new GamePanel(this, cellsWidth, cellsHeight, brickWidth);
        panel.setBorder(new LineBorder(Color.GRAY,1,false));
        getContentPane().add(panel, BorderLayout.CENTER);

        setSize(width, height + 106);

        GlobalScreen.addNativeKeyListener(this);
    }

    private JPanel makeTopPanel(){
        String[] forLabels = {" Figures:0 ", " Level:0 "};
        JPanel topPanel = new JPanel();

        infoLabels = new JLabel[forLabels.length];
        for(int i=0;i<infoLabels.length;i++){
            infoLabels[i] = new JLabel(forLabels[i]);
            topPanel.add(infoLabels[i]);
        }

        topPanel.setLayout(new GridLayout());
        return topPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case "0":
                panel.mooveFigure(37);
                break;
            case "1":
                panel.mooveFigure(38);
                break;
            case "2":
                panel.mooveFigure(39);
                break;
            default:
                break;
        }
    }

    public void nativeKeyPressed(NativeKeyEvent event){
        for(JButton b:buttons) b.setEnabled(false);

        if(event.getRawCode()==32) panel.fastMotion();
        else panel.mooveFigure(event.getRawCode());
    }

    public void nativeKeyReleased(NativeKeyEvent event){
        for(JButton b:buttons) b.setEnabled(true);

        if(event.getRawCode()==32) panel.slowMoition();
    }

    public void addFiguresCount(int i) {
        infoLabels[0].setText(" Figures:" + i + " ");
    }

    public void addLevel() {
        infoLabels[1].setText(" Level:" + ++level + " ");
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
