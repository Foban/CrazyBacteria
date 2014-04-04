/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import DNA.Ferm;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Scanner;
import javax.swing.*;
 
public class CrazyBacteria extends JFrame implements ActionListener {
    
    private static final Dimension DEFAULT_WINDOW_SIZE = new Dimension(800, 600);
    private static final Dimension MINIMUM_WINDOW_SIZE = new Dimension(400, 400);
    private static final int BLOCK_SIZE = 10;
 
    private JMenuBar mb_menu;
    private JMenu m_file, m_game, m_fill;
    private JMenuItem mi_file_options, mi_file_exit;
    private JMenuItem mi_game_play, mi_game_stop, mi_game_reset;
    private int i_movesPerSecond = 3;
    private GameBoard gb_gameBoard;
    private Thread game;
 
    public static void main(String[] args) throws IOException {
        JFrame game = new CrazyBacteria();
        game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.setTitle("Crazy Bacteria");
        game.setSize(DEFAULT_WINDOW_SIZE);
        game.setMinimumSize(DEFAULT_WINDOW_SIZE);
        game.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - game.getWidth())/2, 
        (Toolkit.getDefaultToolkit().getScreenSize().height - game.getHeight())/2);
        game.setVisible(true);
    }
 
    public CrazyBacteria() throws IOException {
        mb_menu = new JMenuBar();
        setJMenuBar(mb_menu);
        m_file = new JMenu("File");
        mb_menu.add(m_file);
        m_game = new JMenu("Game");
        mb_menu.add(m_game);
        mi_file_options = new JMenuItem("Options");
        mi_file_options.addActionListener(this);
        mi_file_exit = new JMenuItem("Exit");
        mi_file_exit.addActionListener(this);
        m_file.add(mi_file_options);
        m_file.add(new JSeparator());
        m_file.add(mi_file_exit);
        mi_game_play = new JMenuItem("Play");
        mi_game_play.addActionListener(this);
        mi_game_stop = new JMenuItem("Stop");
        mi_game_stop.setEnabled(false);
        mi_game_stop.addActionListener(this);
        mi_game_reset = new JMenuItem("Reset");
        mi_game_reset.addActionListener(this);
        m_game.add(mi_game_play);
        m_game.add(new JSeparator());
        m_game.add(mi_game_stop);
        m_game.add(new JSeparator());
        m_game.add(mi_game_reset);

        gb_gameBoard = new GameBoard();
        //gb_gameBoard.constructFromFile("C:\\Users\\user\\Documents\\NetBeansProjects\\GameOfLife2\\src\\gameoflife2\\INPUT");
        //gb_gameBoard.constructFromFile("C:\\Users\\user\\Documents\\NetBeansProjects\\GameOfLife2\\src\\gameoflife2\\BLUSTER");
        add(gb_gameBoard);
    }
    public void setGameBeingPlayed(boolean isBeingPlayed) {
        if (isBeingPlayed) {
            mi_game_play.setEnabled(false);
            mi_game_stop.setEnabled(true);
            game = new Thread(gb_gameBoard);
            game.start();
        } else {
            mi_game_play.setEnabled(true);
            mi_game_stop.setEnabled(false);
            game.interrupt();
        }
    }
 
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(mi_file_exit)) {
            System.exit(0);
        } else if (ae.getSource().equals(mi_file_options)) {
            final JFrame f_options = new JFrame();
            f_options.setTitle("Options");
            f_options.setSize(300,60);
            f_options.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - f_options.getWidth())/2, 
                (Toolkit.getDefaultToolkit().getScreenSize().height - f_options.getHeight())/2);
            f_options.setResizable(false);
            JPanel p_options = new JPanel();
            p_options.setOpaque(false);
            f_options.add(p_options);
            p_options.add(new JLabel("Number of moves per second:"));
            Integer[] secondOptions = {1,2,3,4,5,10,15,20,50,100,200,500,1000};
            final JComboBox cb_seconds = new JComboBox(secondOptions);
            p_options.add(cb_seconds);
            cb_seconds.setSelectedItem(i_movesPerSecond);
            cb_seconds.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent ae) {
                    i_movesPerSecond = (Integer)cb_seconds.getSelectedItem();
                    f_options.dispose();
                }
            });
            f_options.setVisible(true);
        } else if (ae.getSource().equals(mi_game_reset)) {
            gb_gameBoard.resetBoard();
            gb_gameBoard.repaint();
        } else if (ae.getSource().equals(mi_game_play)) {
            setGameBeingPlayed(true);
        } else if (ae.getSource().equals(mi_game_stop)) {
            setGameBeingPlayed(false);
        }
    }

    private class GameBoard extends JPanel implements ComponentListener, MouseListener, MouseMotionListener, Runnable {
        private Dimension d_gameBoardSize = null;
        private ArrayList<Point> point = new ArrayList<Point>(0);
        private JLabel eventLabel;
        private Ferm m;

        public GameBoard() {

            setBackground(Color.WHITE);
            addComponentListener(this);
            addMouseListener(this);
            addMouseMotionListener(this);

        }

 
        private void updateArraySize() {
            ArrayList<Point> removeList = new ArrayList<Point>(0);
            for (Point current : point) {
                if ((current.x > d_gameBoardSize.width-1) || (current.y > d_gameBoardSize.height-1)) {
                    removeList.add(current);
                }
            }
            point.removeAll(removeList);
            repaint();
        }
        
        public void constructFromFile(String filename) throws FileNotFoundException, IOException{
            Scanner reader = new Scanner(new File(filename));
            while(reader.hasNext()){
                point.add(new Point(reader.nextInt(), reader.nextInt()));
            }
        }
 
        public void addPoint(int x, int y) {
            if (!point.contains(new Point(correctXForToroidalProperty(x), correctYForToroidalProperty(y)))) {
                point.add(new Point(correctXForToroidalProperty(x), correctYForToroidalProperty(y)));
            } else {
                point.remove(new Point(correctXForToroidalProperty(x), correctYForToroidalProperty(y)));
            }
            repaint();
        }
 
        public void addPoint(MouseEvent me) {
            int x = me.getPoint().x/10-1;
            int y = me.getPoint().y/10-1;
            addPoint(correctXForToroidalProperty(x), correctYForToroidalProperty(y));
        }
        
        private final int correctXForToroidalProperty(int x){
            if(x < 0 || x > this.d_gameBoardSize.width){
                x = Math.abs((this.d_gameBoardSize.width - Math.abs(x))) % this.d_gameBoardSize.width;
                return x;
            }
            else{
                return x;
            }
        }
        
        private final int correctYForToroidalProperty(int y){
            if(y < 0 || y > this.d_gameBoardSize.height){
                y = Math.abs((this.d_gameBoardSize.height - Math.abs(y))) % this.d_gameBoardSize.height;
                return y;
            }
            else{
                return y;
            }
        }
        
        public void removePoint(int x, int y) {
            point.remove(new Point(x,y));
        }
 
        public void resetBoard() {
            point.clear();
        }
 
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            try {
                for (Point newPoint : point) {
                    switch (m.getBacterium(newPoint.x, newPoint.y).getType()){
                    case 'P':
                        g.setColor(Color.GREEN);
                        break;
                    case 'B':
                        g.setColor(Color.BLUE);
                        break;
                    case 'F':
                        g.setColor(Color.MAGENTA);
                        break;
                    case 'N':
                        g.setColor(Color.RED);
                        break;
                    }
                    /*if(m.getBacterium(newPoint.x, newPoint.y).getType() == 'P')
                        g.setColor(Color.GREEN);
                    else
                        g.setColor(Color.BLUE);*/
                    g.fillRect(BLOCK_SIZE + (BLOCK_SIZE*newPoint.x), BLOCK_SIZE + (BLOCK_SIZE*newPoint.y), BLOCK_SIZE, BLOCK_SIZE);
                }
            } catch (ConcurrentModificationException cme) {}
            g.setColor(Color.BLACK);
            for (int i=0; i<=d_gameBoardSize.width; i++) {
                g.drawLine(((i*BLOCK_SIZE)+BLOCK_SIZE), BLOCK_SIZE, (i*BLOCK_SIZE)+BLOCK_SIZE, BLOCK_SIZE + (BLOCK_SIZE*d_gameBoardSize.height));
            }
            for (int i=0; i<=d_gameBoardSize.height; i++) {
                g.drawLine(BLOCK_SIZE, ((i*BLOCK_SIZE)+BLOCK_SIZE), BLOCK_SIZE*(d_gameBoardSize.width+1), ((i*BLOCK_SIZE)+BLOCK_SIZE));
            }
        }
 
        @Override
        public void componentResized(ComponentEvent e) {
            d_gameBoardSize = new Dimension(getWidth()/BLOCK_SIZE-2, getHeight()/BLOCK_SIZE-2);
            updateArraySize();
            setGameBeingPlayed(false);
        }
        @Override
        public void componentMoved(ComponentEvent e) {}
        @Override
        public void componentShown(ComponentEvent e) {}
        @Override
        public void componentHidden(ComponentEvent e) {}
        @Override
        public void mouseClicked(MouseEvent e) {}
        @Override
        public void mousePressed(MouseEvent e) {}
        @Override
        public void mouseReleased(MouseEvent e) {
            eventLabel.setText("mouseEntered()");
        }
        @Override
        public void mouseEntered(MouseEvent e) {
        }
        @Override
        public void mouseExited(MouseEvent e) {}
 
        @Override
        public void mouseDragged(MouseEvent e) {
            eventLabel.setText("mouseEntered()");
        }
        @Override
        public void mouseMoved(MouseEvent e) {}

        public void step(){
            m.activate();
            ArrayList<Point> survivingCells = new ArrayList<Point>(0x0);
            for (int i = 0; i < m.getYSize(); i++) {
                for (int j = 0; j < m.getXSize(); j++) {

                    if(m.getBacterium(i,j) != null)
                        survivingCells.add(new Point(i, j));
                }
            }
            resetBoard();
            point.addAll(survivingCells);
            repaint();
            try {
                Thread.sleep(1000/i_movesPerSecond);
                step();
            } catch (InterruptedException ex) {}
        }

        @Override
        public void run() {
            m = new Ferm(d_gameBoardSize.width,d_gameBoardSize.height,1000,true,true);
            step();
        }
    }
}