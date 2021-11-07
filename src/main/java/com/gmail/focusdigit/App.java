package com.gmail.focusdigit;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

public class App extends JFrame
{
    private static int level = 0;
    private static final int cellsWidth = 16;
    private static final int cellsHeight = 25;
    private boolean flag;
    private int brickWidth;
    private GamePanel panel=null;
    private JLabel[] infoLabels;
    private JButton[] buttons;

    public App() {
        super("Tetris Demo");

        final String[] forButtons = {"left", "turn", "right", "doun"};
        int height;
        int width;
        flag=false;

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

        setFocusable(true);
        setSize(width, height + 130);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topPanel = makeTopPanel();
        getContentPane().add(topPanel,BorderLayout.NORTH);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

        JPanel tmpPanel = new JPanel();

        buttons = new JButton[forButtons.length];
        for(int i=0;i<3;i++){
            buttons[i] = new JButton(forButtons[i]);
            buttons[i].setFont(new Font("Serif", Font.PLAIN,18));
            buttons[i].setActionCommand(String.valueOf(37+i));
            buttons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    panel.mooveFigure(Integer.valueOf(e.getActionCommand()));
                }
            });
            tmpPanel.add(buttons[i]);
        }
        controlPanel.add(tmpPanel);

        tmpPanel = new JPanel();
        buttons[3]=new JButton(forButtons[3]);
        buttons[3].setFont(new Font("Serif", Font.PLAIN,18));
        buttons[3].setActionCommand("3");
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

        getContentPane().add(controlPanel, BorderLayout.SOUTH);

        panel = new GamePanel(this, cellsWidth, cellsHeight, brickWidth);
        panel.setBorder(new LineBorder(Color.GRAY,1,false));
        getContentPane().add(panel, BorderLayout.CENTER);

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==27) changePauseState();
                else if(flag) panel.mooveFigure(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(flag) panel.slowMoition();
            }
        });

        this.requestFocus();
    }

    private JPanel makeTopPanel(){
        String[] forLabels = {" Figures:0 ", " Level:0 ", "ESC to start"};
        JPanel topPanel = new JPanel();

        infoLabels = new JLabel[forLabels.length];
        for(int i=0;i<infoLabels.length;i++){
            infoLabels[i] = new JLabel(forLabels[i]);
            infoLabels[i].setFont(new Font("Serif", Font.PLAIN,18));
            topPanel.add(infoLabels[i]);
        }

        topPanel.setLayout(new GridLayout());
        return topPanel;
    }

    public void addFiguresCount(int i) {
        infoLabels[0].setText(" Figures:" + i + " ");
    }

    public void addLevel() {
        infoLabels[1].setText(" Level:" + ++level + " ");
    }

    public void changePauseState(){
        panel.setFlag();
        if(infoLabels[2].getText().equals("ESC to start")){
            flag=true;
            for(JButton b:buttons) b.setEnabled(true);
            infoLabels[2].setText("ESC to stop");
        }else{
            flag=false;
            for(JButton b:buttons) b.setEnabled(false);
            infoLabels[2].setText("ESC to start");
        }
    }

    public void onKeyPressed(boolean b) {
        for(JButton btn:buttons) btn.setEnabled(b);
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
