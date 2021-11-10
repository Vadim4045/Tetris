package com.gmail.focusdigit;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class App extends JFrame implements ActionListener
{
    private static int level = 0;
    private static final int cellsWidth = 16;
    private static final int cellsHeight = 25;
    private boolean flag;
    private int brickWidth;
    private GamePanel panel=null;
    private JLabel[] infoLabels;
    private JButton[] buttons;

    public App() throws IOException {
        super("Tetris Demo");
        final String[] toolTips = {"key-LEFT", "key-UP/DOWN", "key-RIGHT", "key-SPACE"};
        final String[] forButtons = {"src/main/resources/images/arrow-png-left.png"
                , "src/main/resources/images/arrow-png-turn.png"
                , "src/main/resources/images/arrow-png-right.png"
                , "src/main/resources/images/arrow-png-down.png"};

        int height;
        int width;
        flag=false;
        int borderWidth = 2;

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
        setSize(width+brickWidth-borderWidth, height + 130);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(borderWidth,borderWidth));

        JPanel topPanel = makeTopPanel();
        getContentPane().add(topPanel,BorderLayout.NORTH);
        getContentPane().add(new JPanel(), BorderLayout.WEST);
        getContentPane().add(new JPanel(), BorderLayout.EAST);
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

        JPanel tmpPanel = new JPanel();

        buttons = new JButton[forButtons.length];
        for(int i=0;i<3;i++){
            BufferedImage buttonIcon = ImageIO.read(new File(forButtons[i]));
            buttons[i] = new JButton(new ImageIcon(buttonIcon.getScaledInstance(40,21,0)));
            buttons[i].setActionCommand(String.valueOf(37+i));
            buttons[i].setToolTipText(toolTips[i]);
            buttons[i].setFocusable(false);
            buttons[i].addActionListener(this);
            tmpPanel.add(buttons[i]);
        }
        controlPanel.add(tmpPanel);

        tmpPanel = new JPanel();
        BufferedImage buttonIcon = ImageIO.read(new File(forButtons[3]));
        buttons[3] = new JButton(new ImageIcon(buttonIcon.getScaledInstance(120,21,0)));
        buttons[3].setActionCommand("3");
        buttons[3].setToolTipText(toolTips[3]);
        buttons[3].setFocusable(false);
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
                try {
                    new App().setVisible(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        panel.mooveFigure(Integer.valueOf(e.getActionCommand()));
    }
}