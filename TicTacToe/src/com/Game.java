package com;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import resourceloader.ResourceLoader;

public class Game implements ActionListener, MouseListener {

    JFrame frame = new JFrame("Tic-Tac-Toe");

    JButton[] button = new JButton[9];

    JButton resetButton = new JButton("Reset");

    ImageIcon O = new ImageIcon(ResourceLoader.load("res/O.png"));

    ImageIcon X = new ImageIcon(ResourceLoader.load("res/X.png"));

    JPanel gamePanel = new JPanel(new GridLayout(3, 3));

    JLabel winLabel = new JLabel();

    JButton[] diffButton = new JButton[3];

    JPanel diffPanel = new JPanel(new FlowLayout());

    boolean ai = false;

    boolean playable = true;

    int difficulty = 2;

    Game() {
        this.frame.setLocation(400, 100);
        this.frame.setSize(500, 600);
        this.frame.setDefaultCloseOperation(3);
        this.frame.setLayout((LayoutManager) null);
        this.frame.setResizable(false);
        this.winLabel.setBounds(165, 90, 200, 20);
        this.winLabel.setForeground(Color.red);
        this.winLabel.setFont(new Font("Arial", 1, 25));
        this.gamePanel.setBounds(45, 120, 400, 360);
        int i;
        for (i = 0; i < this.button.length; i++) {
            this.button[i] = new JButton();
            this.button[i].setBackground((Color) null);
            this.button[i].setForeground(Color.black);
            this.button[i].setFocusable(false);
            this.button[i].addActionListener(this);
            this.button[i].addMouseListener(this);
            this.gamePanel.add(this.button[i]);
        }
        this.diffPanel.setBounds(95, 45, 300, 40);
        for (i = 0; i < this.diffButton.length; i++) {
            this.diffButton[i] = new JButton();
            this.diffButton[i].setFocusable(false);
            this.diffButton[i].setFont(new Font("Arial", 1, 15));
            this.diffButton[i].setBackground((Color) null);
            this.diffButton[i].setForeground(Color.black);
            this.diffButton[i].addActionListener(this);
            this.diffButton[i].addMouseListener(this);
            this.diffPanel.add(this.diffButton[i]);
        }
        this.diffButton[0].setText("Easy");
        this.diffButton[1].setText("Medium");
        this.diffButton[2].setText("Hard");
        this.diffButton[2].setBackground(Color.cyan);
        this.diffButton[2].setForeground(Color.black);
        this.resetButton.setBounds(145, 500, 200, 50);
        this.resetButton.setFocusable(false);
        this.resetButton.setFont(new Font("Arial", 1, 25));
        this.resetButton.setForeground(Color.black);
        this.resetButton.addActionListener(this);
        this.resetButton.addMouseListener(this);
        this.frame.add(this.diffPanel);
        this.frame.add(this.resetButton);
        this.frame.add(this.gamePanel);
        this.frame.add(this.winLabel);
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception exception) {
        }
        this.frame.setVisible(true);
        nextTurn();
    }

    void nextTurn() {
        int turn = (new Random()).nextInt(2);
        if (turn == 0) {
            computerMove();
        } else {
            this.ai = false;
        }
    }

    void computerMove() {
        this.ai = true;
        if (this.difficulty == 0) {
            easyCPU();
        } else if (this.difficulty == 1) {
            mediumCPU();
        } else {
            computer();
        }
    }

    void computer() {
        if (this.playable) {
            int bestMove = 4, bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < this.button.length; i++) {
                if (isAvailable(i)) {
                    this.button[i].setIcon(this.X);
                    int score = minimax(false);
                    this.button[i].setIcon((Icon) null);
                    if (score < bestScore) {
                        bestScore = score;
                        bestMove = i;
                    }
                    if (score == -1) {
                        break;
                    }
                }
            }
            this.button[bestMove].setIcon(this.X);
            this.ai = false;
            checkWinner();
        }
    }

    void easyCPU() {
        if (this.playable && this.ai) {
            while (this.ai) {
                int move = (new Random()).nextInt(9);
                if (isAvailable(move)) {
                    this.button[move].setIcon(this.X);
                    this.ai = false;
                    checkWinner();
                }
            }
        }
    }

    void mediumCPU() {
        if (this.playable && this.ai) {
            int i;
            for (i = 0; i < this.button.length; i++) {
                if (isAvailable(i)) {
                    this.button[i].setIcon(this.X);
                    if (computerWinCondition()) {
                        this.ai = false;
                        checkWinner();
                        break;
                    }
                    this.button[i].setIcon((Icon) null);
                }
            }
            if (this.ai) {
                for (i = 0; i < this.button.length; i++) {
                    if (isAvailable(i)) {
                        this.button[i].setIcon(this.X);
                        if (!pseudoHuman()) {
                            this.ai = false;
                            checkWinner();
                            break;
                        }
                        this.button[i].setIcon((Icon) null);
                    }
                }
            }
            while (this.ai) {
                int move = (new Random()).nextInt(9);
                if (isAvailable(move)) {
                    this.button[move].setIcon(this.X);
                    this.ai = false;
                    checkWinner();
                    break;
                }
            }
        }
    }

    boolean pseudoHuman() {
        for (int i = 0; i < this.button.length; i++) {
            if (isAvailable(i)) {
                this.button[i].setIcon(this.O);
                if (humanWinCondition()) {
                    this.button[i].setIcon((Icon) null);
                    return true;
                }
                this.button[i].setIcon((Icon) null);
            }
        }
        return false;
    }

    boolean computerWinCondition() {
        if (this.button[0].getIcon() == this.button[1].getIcon() && this.button[1].getIcon() == this.button[2].getIcon() && this.button[0].getIcon() == this.X) {
            return true;
        }
        if (this.button[3].getIcon() == this.button[4].getIcon() && this.button[4].getIcon() == this.button[5].getIcon() && this.button[3].getIcon() == this.X) {
            return true;
        }
        if (this.button[6].getIcon() == this.button[7].getIcon() && this.button[7].getIcon() == this.button[8].getIcon() && this.button[6].getIcon() == this.X) {
            return true;
        }
        if (this.button[0].getIcon() == this.button[3].getIcon() && this.button[3].getIcon() == this.button[6].getIcon() && this.button[0].getIcon() == this.X) {
            return true;
        }
        if (this.button[1].getIcon() == this.button[4].getIcon() && this.button[4].getIcon() == this.button[7].getIcon() && this.button[1].getIcon() == this.X) {
            return true;
        }
        if (this.button[2].getIcon() == this.button[5].getIcon() && this.button[5].getIcon() == this.button[8].getIcon() && this.button[2].getIcon() == this.X) {
            return true;
        }
        if (this.button[0].getIcon() == this.button[4].getIcon() && this.button[4].getIcon() == this.button[8].getIcon() && this.button[0].getIcon() == this.X) {
            return true;
        }
        if (this.button[2].getIcon() == this.button[4].getIcon() && this.button[4].getIcon() == this.button[6].getIcon() && this.button[2].getIcon() == this.X) {
            return true;
        }
        return false;
    }

    boolean humanWinCondition() {
        if (this.button[0].getIcon() == this.button[1].getIcon() && this.button[1].getIcon() == this.button[2].getIcon() && this.button[0].getIcon() == this.O) {
            return true;
        }
        if (this.button[3].getIcon() == this.button[4].getIcon() && this.button[4].getIcon() == this.button[5].getIcon() && this.button[3].getIcon() == this.O) {
            return true;
        }
        if (this.button[6].getIcon() == this.button[7].getIcon() && this.button[7].getIcon() == this.button[8].getIcon() && this.button[6].getIcon() == this.O) {
            return true;
        }
        if (this.button[0].getIcon() == this.button[3].getIcon() && this.button[3].getIcon() == this.button[6].getIcon() && this.button[0].getIcon() == this.O) {
            return true;
        }
        if (this.button[1].getIcon() == this.button[4].getIcon() && this.button[4].getIcon() == this.button[7].getIcon() && this.button[1].getIcon() == this.O) {
            return true;
        }
        if (this.button[2].getIcon() == this.button[5].getIcon() && this.button[5].getIcon() == this.button[8].getIcon() && this.button[2].getIcon() == this.O) {
            return true;
        }
        if (this.button[0].getIcon() == this.button[4].getIcon() && this.button[4].getIcon() == this.button[8].getIcon() && this.button[0].getIcon() == this.O) {
            return true;
        }
        if (this.button[2].getIcon() == this.button[4].getIcon() && this.button[4].getIcon() == this.button[6].getIcon() && this.button[2].getIcon() == this.O) {
            return true;
        }
        return false;
    }

    boolean pseudoisDraw() {
        for (int i = 0; i < this.button.length; i++) {
            if (isAvailable(i)) {
                return false;
            }
        }
        return true;
    }

    int minimax(boolean isMinimizing) {
        if (computerWinCondition()) {
            return -1;
        }
        if (humanWinCondition()) {
            return 1;
        }
        if (pseudoisDraw()) {
            return 0;
        }
        if (isMinimizing) {
            int j = Integer.MAX_VALUE;
            for (int k = 0; k < this.button.length; k++) {
                if (isAvailable(k)) {
                    this.button[k].setIcon(this.X);
                    int score = minimax(false);
                    this.button[k].setIcon((Icon) null);
                    if (score == -1) {
                        return score;
                    }
                    j = Math.min(score, j);
                }
            }
            return j;
        }
        int bestScore = Integer.MIN_VALUE;
        for (int i = 0; i < this.button.length; i++) {
            if (isAvailable(i)) {
                this.button[i].setIcon(this.O);
                int score = minimax(true);
                this.button[i].setIcon((Icon) null);
                if (score == 1) {
                    return score;
                }
                bestScore = Math.max(score, bestScore);
            }
        }
        return bestScore;
    }

    boolean checkWinner() {
        if (this.button[0].getIcon() == this.button[1].getIcon() && this.button[1].getIcon() == this.button[2].getIcon() && this.button[0].getIcon() != null) {
            endGame(0, 1, 2);
            return true;
        }
        if (this.button[3].getIcon() == this.button[4].getIcon() && this.button[4].getIcon() == this.button[5].getIcon() && this.button[3].getIcon() != null) {
            endGame(3, 4, 5);
            return true;
        }
        if (this.button[6].getIcon() == this.button[7].getIcon() && this.button[7].getIcon() == this.button[8].getIcon() && this.button[6].getIcon() != null) {
            endGame(6, 7, 8);
            return true;
        }
        if (this.button[0].getIcon() == this.button[3].getIcon() && this.button[3].getIcon() == this.button[6].getIcon() && this.button[0].getIcon() != null) {
            endGame(0, 3, 6);
            return true;
        }
        if (this.button[1].getIcon() == this.button[4].getIcon() && this.button[4].getIcon() == this.button[7].getIcon() && this.button[1].getIcon() != null) {
            endGame(1, 4, 7);
            return true;
        }
        if (this.button[2].getIcon() == this.button[5].getIcon() && this.button[5].getIcon() == this.button[8].getIcon() && this.button[2].getIcon() != null) {
            endGame(2, 5, 8);
            return true;
        }
        if (this.button[0].getIcon() == this.button[4].getIcon() && this.button[4].getIcon() == this.button[8].getIcon() && this.button[0].getIcon() != null) {
            endGame(0, 4, 8);
            return true;
        }
        if (this.button[2].getIcon() == this.button[4].getIcon() && this.button[4].getIcon() == this.button[6].getIcon() && this.button[2].getIcon() != null) {
            endGame(2, 4, 6);
            return true;
        }
        return isDraw();
    }

    void endGame(int x, int y, int z) {
        this.button[x].setBackground(Color.green);
        this.button[y].setBackground(Color.green);
        this.button[z].setBackground(Color.green);
        this.winLabel.setForeground(Color.red);
        this.playable = false;
        if (this.button[x].getIcon() == this.O) {
            this.winLabel.setText("Human won!");
        } else {
            this.winLabel.setText("Computer won!");
        }
    }

    boolean isAvailable(int i) {
        if (this.button[i].getIcon() == null) {
            return true;
        }
        return false;
    }

    boolean isDraw() {
        for (int i = 0; i < this.button.length; i++) {
            if (isAvailable(i)) {
                return false;
            }
        }
        this.playable = false;
        this.winLabel.setLocation(220, this.winLabel.getY());
        this.winLabel.setText("Tie!");
        this.winLabel.setForeground(Color.red);
        return true;
    }

    void resetGame() {
        for (int i = 0; i < this.button.length; i++) {
            this.button[i].setIcon((Icon) null);
            this.button[i].setBackground((Color) null);
        }
        this.winLabel.setLocation(170, this.winLabel.getY());
        this.winLabel.setForeground(new Color(238, 238, 238));
        this.playable = true;
        nextTurn();
    }

    public void actionPerformed(ActionEvent e) {
        int i;
        for (i = 0; i < this.button.length; i++) {
            if (e.getSource() == this.button[i]
                    && this.playable && !this.ai && isAvailable(i)) {
                this.button[i].setIcon(this.O);
                if (!checkWinner()) {
                    computerMove();
                }
            }
        }
        if (e.getSource() == this.resetButton) {
            resetGame();
        }
        for (i = 0; i < this.diffButton.length; i++) {
            if (e.getSource() == this.diffButton[i] && i != this.difficulty) {
                int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to change game difficulty? It will reset\nthe current game state. Click \"Yes\" to continue or \"No\" to cancel.", "Confirmation", 0);
                if (confirm == 0) {
                    this.difficulty = i;
                    this.diffButton[i].setBackground(Color.cyan);
                    this.diffButton[i].setForeground(Color.black);
                    resetGame();
                }
            }
        }
        for (i = 0; i < this.diffButton.length; i++) {
            if (i != this.difficulty) {
                this.diffButton[i].setBackground((Color) null);
                this.diffButton[i].setForeground(Color.black);
            }
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
        int i;
        for (i = 0; i < this.button.length; i++) {
            if (e.getSource() == this.button[i]) {
                this.button[i].setBackground(Color.black);
            }
        }
        for (i = 0; i < this.diffButton.length; i++) {
            if (e.getSource() == this.diffButton[i]
                    && i != this.difficulty) {
                this.diffButton[i].setForeground(Color.white);
                this.diffButton[i].setBackground(Color.black);
            }
        }
        if (e.getSource() == this.resetButton) {
            this.resetButton.setBackground(Color.black);
            this.resetButton.setForeground(Color.white);
        }
    }

    public void mouseExited(MouseEvent e) {
        int i;
        for (i = 0; i < this.button.length; i++) {
            if (e.getSource() == this.button[i]) {
                this.button[i].setBackground((Color) null);
                checkWinner();
            }
        }
        if (e.getSource() == this.resetButton) {
            this.resetButton.setBackground((Color) null);
            this.resetButton.setForeground(Color.black);
        }
        for (i = 0; i < this.diffButton.length; i++) {
            if (e.getSource() == this.diffButton[i]
                    && i != this.difficulty) {
                this.diffButton[i].setForeground(Color.black);
                this.diffButton[i].setBackground((Color) null);
            }
        }
    }
}
