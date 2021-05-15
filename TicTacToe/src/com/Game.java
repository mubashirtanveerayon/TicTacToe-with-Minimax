package com;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import resourceloader.ResourceLoader;

public class Game implements ActionListener,MouseListener{
    
    JFrame frame=new JFrame("Tic-Tac-Toe");
    JButton[] button=new JButton[9];
    JButton resetButton=new JButton("Reset");
    ImageIcon O = new ImageIcon(ResourceLoader.load("res/O.png"));
    ImageIcon X = new ImageIcon(ResourceLoader.load("res/X.png"));
    JPanel gamePanel=new JPanel(new GridLayout(3,3));
    JLabel winLabel=new JLabel();
    
    boolean ai=false,playable=true;
    
    Game(){
        frame.setLocation(400,150);
        frame.setSize(500,550);
        frame.setDefaultCloseOperation(3);
        frame.setLayout(null);
        frame.setResizable(false);
        
        winLabel.setBounds(170,30,200,20);
        winLabel.setForeground(Color.red);
        winLabel.setFont(new Font("Arial",Font.BOLD,25));
        
        gamePanel.setBounds(45,65,400,360);
        
        for (int i = 0; i < button.length; i++) {
            button[i]=new JButton();
            button[i].setBackground(null);
            button[i].setFocusable(false);            
            button[i].addActionListener(this); 
            button[i].addMouseListener(this);
            gamePanel.add(button[i]);
        }
        
        resetButton.setBounds(145,450,200,50);
        resetButton.setFocusable(false);
        resetButton.setFont(new Font("Ink Free",Font.BOLD,25));
        resetButton.setForeground(Color.black);
        resetButton.addActionListener(this);
        resetButton.addMouseListener(this);
        
        frame.add(resetButton);
        frame.add(gamePanel);
        frame.add(winLabel);
        
        try{
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }catch(Exception ex){
            
        }
        
        frame.setVisible(true);
        
        nextTurn();
    }
    
    void nextTurn(){
        int turn=new Random().nextInt(2);
        if(turn==0){
            ai=true;
            computer();
        }else{
            ai=false;
        }
    }
    
    void computer(){
        if(playable){
        int score,bestMove=4,bestScore=Integer.MAX_VALUE;
            for (int i = 0; i < button.length; i++) {
                if(isAvailable(i)){
                    button[i].setIcon(X);
                    score=minimax(false);
                    System.out.println("for position "+i+" possible case is "+score);
                    button[i].setIcon(null);
                    if(score<bestScore){
                        bestScore=score;
                        bestMove=i;
                    }
                }
            }
            button[bestMove].setIcon(X);
            System.out.println("computer's move "+bestMove);
            ai=false;
            checkWinner();            
        }
    }
    
    boolean computerWinCondition(){
        if(button[0].getIcon()==button[1].getIcon()&&button[1].getIcon()==button[2].getIcon()&&button[0].getIcon()==X){
            return true;
        }else if(button[3].getIcon()==button[4].getIcon()&&button[4].getIcon()==button[5].getIcon()&&button[3].getIcon()==X){
            return true;
        } else if(button[6].getIcon()==button[7].getIcon()&&button[7].getIcon()==button[8].getIcon()&&button[6].getIcon()==X){
            return true;
        }else if(button[0].getIcon()==button[3].getIcon()&&button[3].getIcon()==button[6].getIcon()&&button[0].getIcon()==X){
            return true;
        }else if(button[1].getIcon()==button[4].getIcon()&&button[4].getIcon()==button[7].getIcon()&&button[1].getIcon()==X){
            return true;
        }else if(button[2].getIcon()==button[5].getIcon()&&button[5].getIcon()==button[8].getIcon()&&button[2].getIcon()==X){
            return true;
        }else if(button[0].getIcon()==button[4].getIcon()&&button[4].getIcon()==button[8].getIcon()&&button[0].getIcon()==X){
            return true;
        }else if(button[2].getIcon()==button[4].getIcon()&&button[4].getIcon()==button[6].getIcon()&&button[2].getIcon()==X){
            return true;
        }else {
            return false;
        }
    }
    
    boolean humanWinCondition(){
        if(button[0].getIcon()==button[1].getIcon()&&button[1].getIcon()==button[2].getIcon()&&button[0].getIcon()==O){
            return true;
        }else if(button[3].getIcon()==button[4].getIcon()&&button[4].getIcon()==button[5].getIcon()&&button[3].getIcon()==O){
            return true;
        } else if(button[6].getIcon()==button[7].getIcon()&&button[7].getIcon()==button[8].getIcon()&&button[6].getIcon()==O){
            return true;
        }else if(button[0].getIcon()==button[3].getIcon()&&button[3].getIcon()==button[6].getIcon()&&button[0].getIcon()==O){
            return true;
        }else if(button[1].getIcon()==button[4].getIcon()&&button[4].getIcon()==button[7].getIcon()&&button[1].getIcon()==O){
            return true;
        }else if(button[2].getIcon()==button[5].getIcon()&&button[5].getIcon()==button[8].getIcon()&&button[2].getIcon()==O){
            return true;
        }else if(button[0].getIcon()==button[4].getIcon()&&button[4].getIcon()==button[8].getIcon()&&button[0].getIcon()==O){
            return true;
        }else if(button[2].getIcon()==button[4].getIcon()&&button[4].getIcon()==button[6].getIcon()&&button[2].getIcon()==O){
            return true;
        }else {
            return false;
        }
    }
    
    boolean pseudoisDraw(){
        for (int i = 0; i < button.length; i++) {
            if(isAvailable(i)){
                return false;
            }
        }        
        return true;
    }
    
    int minimax(boolean isMinimizing){        
        if(computerWinCondition()){
            return -1;
        }else if(humanWinCondition()){
            return 1;
        }else if(pseudoisDraw()){
            return 0;
        }
        if(isMinimizing){
            int score,bestScore=Integer.MAX_VALUE;
            for (int i = 0; i < button.length; i++) {
                if(isAvailable(i)){                    
                    button[i].setIcon(X);
                    score=minimax(false);
                    button[i].setIcon(null);
                    bestScore=Math.min(score,bestScore);
                }
            }
            return bestScore;
        }else{
            int score,bestScore=Integer.MIN_VALUE;
            for (int i = 0; i < button.length; i++) {
                if(isAvailable(i)){ 
                    button[i].setIcon(O);
                    score=minimax(true);
                    button[i].setIcon(null);
                    bestScore=Math.max(score, bestScore);
                }
            }
            return bestScore;
        }
    }
    
    boolean checkWinner(){       
        if(button[0].getIcon()==button[1].getIcon()&&button[1].getIcon()==button[2].getIcon()&&button[0].getIcon()!=null){
            endGame(0,1,2);
            return true;
        }else if(button[3].getIcon()==button[4].getIcon()&&button[4].getIcon()==button[5].getIcon()&&button[3].getIcon()!=null){
            endGame(3,4,5);
            return true;
        } else if(button[6].getIcon()==button[7].getIcon()&&button[7].getIcon()==button[8].getIcon()&&button[6].getIcon()!=null){
            endGame(6,7,8);
            return true;
        }else if(button[0].getIcon()==button[3].getIcon()&&button[3].getIcon()==button[6].getIcon()&&button[0].getIcon()!=null){
            endGame(0,3,6);
            return true;
        }else if(button[1].getIcon()==button[4].getIcon()&&button[4].getIcon()==button[7].getIcon()&&button[1].getIcon()!=null){
            endGame(1,4,7);
            return true;
        }else if(button[2].getIcon()==button[5].getIcon()&&button[5].getIcon()==button[8].getIcon()&&button[2].getIcon()!=null){
            endGame(2,5,8);
            return true;
        }else if(button[0].getIcon()==button[4].getIcon()&&button[4].getIcon()==button[8].getIcon()&&button[0].getIcon()!=null){
            endGame(0,4,8);
            return true;
        }else if(button[2].getIcon()==button[4].getIcon()&&button[4].getIcon()==button[6].getIcon()&&button[2].getIcon()!=null){
            endGame(2,4,6);
            return true;
        }else{
            return isDraw();
        }
    }
    
    void endGame(int x,int y,int z){
        button[x].setBackground(Color.green);
        button[y].setBackground(Color.green);
        button[z].setBackground(Color.green);
        winLabel.setForeground(Color.red);
        playable=false;
        if(button[x].getIcon()==O){        
            winLabel.setText("Human won!");
        }else{
            winLabel.setText("Computer won!");
        }
    }
    
    boolean isAvailable(int i){
        if(button[i].getIcon()==null){
            return true;
        }else{
            return false;
        }
    }
    
    boolean isDraw(){
        for (int i = 0; i < button.length; i++) {
            if(isAvailable(i)){
                return false;
            }
        }       
        playable=false;
        winLabel.setLocation(220, winLabel.getY());
        winLabel.setText("Tie!");
        winLabel.setForeground(Color.red);       
        return true;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < button.length; i++) {
            if(e.getSource()==button[i]){
                if(playable){
                    if(!ai){
                        if(isAvailable(i)){                          
                            button[i].setIcon(O);
                            ai=true;
                            if(!checkWinner()){
                                computer();
                            }
                        }
                    }
                }
            }
        }
        
        if(e.getSource()==resetButton){
            for (int i = 0; i < button.length; i++) {
                button[i].setIcon(null);
                button[i].setBackground(null);                
            }
            winLabel.setLocation(170,winLabel.getY());
            winLabel.setForeground(new Color(238,238,238));
            playable=true;
            nextTurn();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        for (int i = 0; i < button.length; i++) {
            if(e.getSource()==button[i]){
                button[i].setBackground(Color.black);
            }
        }
        if(e.getSource()==resetButton){
            resetButton.setBackground(Color.black);
            resetButton.setForeground(Color.white);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        for (int i = 0; i < button.length; i++) {
            if(e.getSource()==button[i]){
                button[i].setBackground(null);
                checkWinner();
            }
        }
        if(e.getSource()==resetButton){
            resetButton.setBackground(null);
            resetButton.setForeground(Color.black);
        }
    }
}