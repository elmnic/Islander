package islander;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Control extends JPanel implements ActionListener, KeyListener {

	private static final long serialVersionUID = 1L;
	private int width;
    private int height;
    private int scale;
    private double ratio;
    private boolean running = false;
    private int playerDir = 4;
    private JPanel controlPanel;
    private JPanel viewPanel;
    private JLabel playerInv;
    private JLabel bridgeOnMap;
    private JButton restart;
    private JButton refine;
    private JButton gridToggle;
    private JButton Jesus;
    private JButton play;
    private Map map;

    public Control(int x, int y, double ratio, int scale) {
        width = x;
        height = y;
        this.scale = scale;
        this.ratio = ratio;

        //TODO
//        addMouseListener(new MouseAdapter() {
//        	public void mousePressed(MouseEvent me) { 
//                System.out.println(me.getX()); 
//                System.out.println(me.getY()); 
//              }
//        });
        addKeyListener(this);
        setFocusable(true);
        setLayout(new BorderLayout());

        map = new Map(width, height, this.ratio, this.scale);
        viewPanel = new JPanel(new BorderLayout());
        controlPanel = new JPanel(new FlowLayout());

        playerInv = new JLabel();
        bridgeOnMap = new JLabel();
        
        play = new JButton("Play");
        Jesus = new JButton("Jesus");
        refine = new JButton("Refine");
        restart = new JButton("New Map");
        gridToggle = new JButton("Grid");

        Jesus.setFocusable(false);
        gridToggle.setFocusable(false);
        
        play.setVisible(false);
        Jesus.setVisible(false);
        refine.setVisible(false);
        playerInv.setVisible(false);
        gridToggle.setVisible(false);
        bridgeOnMap.setVisible(false);

        //Buttons
        play.addActionListener(new RunListener());
        Jesus.addActionListener(new RunListener());
        refine.addActionListener(new RunListener());
        restart.addActionListener(new RunListener());
        gridToggle.addActionListener(new RunListener());

        // Add buttons to panel on window
        viewPanel.add(map);
        controlPanel.add(play);
        controlPanel.add(Jesus);
        controlPanel.add(refine);
        controlPanel.add(restart);
        controlPanel.add(playerInv);
        controlPanel.add(gridToggle);
        controlPanel.add(bridgeOnMap, FlowLayout.CENTER);

        add(controlPanel, BorderLayout.SOUTH);
        add(viewPanel, BorderLayout.CENTER);
    }
    

    private class RunListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            //Refines the map, if the player chooses to
            if (e.getSource() == refine) {
                map.refineMap();
                repaint();
            }
            //Generates a new map
            if (e.getSource() == restart) {
                running = false;
                play.setVisible(true);
                refine.setVisible(true);
                playerInv.setVisible(false);
                bridgeOnMap.setVisible(false);
                map.setWon(false);
                map.buildMap();
                repaint();
            }
            //Toggles the grid
            if (e.getSource() == gridToggle) {
                map.setGrid(!map.isGrid());
                repaint();
            }
            //Starts the game
            if (e.getSource() == play) {
                running = true;
                Jesus.setVisible(false);
                play.setVisible(false);
                refine.setVisible(false);
                playerInv.setVisible(true);
                bridgeOnMap.setVisible(true);
                map.addBridge();
                map.addGoal(true);
                map.addPlayer(true);
                map.setPlayerInv(0);
                playerInv.setText("Bridge: " + map.getPlayerInv());
                bridgeOnMap.setText("Total Bridge: " + map.getBridgeOnMap());
                repaint();
            }
            //Toggles between Jesus and Not Jesus
            if (e.getSource() == Jesus) {
            	map.setJesus(!map.isJesus());
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (running) {
        	//The range in which the arrow keys are
            if (e.getKeyCode() <= 40 && e.getKeyCode() >= 37) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    playerDir = 0;
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    playerDir = 1;
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    playerDir = 2;
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    playerDir = 3;
                }
                map.updateMap(playerDir, false);
                playerInv.setText("Bridge: " + map.getPlayerInv());
                repaint();
            }
            //Spacebar
            if (e.getKeyCode() == 32) {
				map.updateMap(playerDir, true);
				playerInv.setText("Bridge: " + map.getPlayerInv());
				repaint();
			}
            //Turns the player into Jesus
            if (e.getKeyCode() == KeyEvent.VK_J) {
            	map.setJesus(!map.isJesus());
            	repaint();
			}
            //Toggles the grid
            if (e.getKeyCode() == KeyEvent.VK_G) {
            	map.setGrid(!map.isGrid());
            	repaint();
			}
            //Gives the player bridge blocks
            if (e.getKeyCode() == KeyEvent.VK_B) {
            	map.increaseBridge();
            	playerInv.setText("Bridge: " + map.getPlayerInv());
            	repaint();
			}
            bridgeOnMap.setText("Total Bridge: " + map.getBridgeOnMap());
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
    }
}
