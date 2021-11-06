package com.gmail.focusdigit;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App extends JFrame
{
    private static int level = 0;
    private static final int cellsWidth = 16;
    private static final int cellsHeight = 25;
    private int brickWidth;
    private GamePanel panel=null;
    private JPanel nextPanel;
    private JLabel[] infoLabels;
    private JButton[] buttons;

    public App() {
        super("Tetris Demo");

        final String[] forButtons = {"left", "turn", "right", "doun"};
        int height;
        int width;

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
        setSize(width, height + 106);
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
            final JButton tmpBtn = new JButton(forButtons[i]);
            buttons[i] = tmpBtn;
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

        add(controlPanel, BorderLayout.SOUTH);

        panel = new GamePanel(this, cellsWidth, cellsHeight, brickWidth);
        panel.setBorder(new LineBorder(Color.GRAY,1,false));
        getContentPane().add(panel, BorderLayout.CENTER);
        this.requestFocus();
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

    public void addFiguresCount(int i) {
        infoLabels[0].setText(" Figures:" + i + " ");
    }

    public void addLevel() {
        infoLabels[1].setText(" Level:" + ++level + " ");
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
