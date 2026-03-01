package snakegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Board extends JPanel implements ActionListener {

    private Image head, dot, apple;
    private final int ALL_DOTS = 900;
    private final int DOT_SIZE = 10;
    private final int RANDOM_POSITION = 29;

    private int apple_x, apple_y;
    private final int[] x = new int[ALL_DOTS];
    private final int[] y = new int[ALL_DOTS];

    private int dots, score = 0, highScore = 0;
    private Timer timer;
    private boolean inGame = true;

    private boolean rightDirection = true;
    private boolean leftDirection = false;
    private boolean upDirection = false;
    private boolean downDirection = false;

    public Board() {
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new TAdapter());

        loadImages();
        initGame();
    }

    private void loadImages() {
        head = new ImageIcon(getClass().getResource("/icons/head.png")).getImage();
        dot = new ImageIcon(getClass().getResource("/icons/dot.png")).getImage();
        apple = new ImageIcon(getClass().getResource("/icons/apple.png")).getImage();
    }

    private void initGame() {
        score = 0;
        dots = 3;
        inGame = true;
        rightDirection = true;
        leftDirection = false;
        upDirection = false;
        downDirection = false;

        for (int i = 0; i < dots; i++) {
            y[i] = 50;
            x[i] = 50 - i * DOT_SIZE;
        }

        locateApple();

        if (timer == null) {
            timer = new Timer(140, this);
            timer.start();
        } else {
            timer.setDelay(140);
            timer.restart();
        }
    }

    private void locateApple() {
        int r = (int) (Math.random() * RANDOM_POSITION);
        apple_x = r * DOT_SIZE;
        r = (int) (Math.random() * RANDOM_POSITION);
        apple_y = r * DOT_SIZE;
    }

    private void draw(Graphics g) {
        if (inGame) {
            g.drawImage(apple, apple_x, apple_y, this);
            for (int i = 0; i < dots; i++) {
                g.drawImage(i == 0 ? head : dot, x[i], y[i], this);
            }

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 18));
            g.drawString("Score: " + score, 10, 20);
            g.drawString("High Score: " + highScore, 10, 45);

            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        if (score > highScore) {
            highScore = score;  // ✅ Only in-memory
        }

        String msg = "Game Over";
        String scoreMsg = "Final Score: " + score;
        String highScoreMsg = "High Score: " + highScore;
        String replayMsg = "Press ENTER to play again";

        Font font = new Font("Arial", Font.BOLD, 36);
        FontMetrics metrics = getFontMetrics(font);
        g.setColor(Color.WHITE);
        g.setFont(font);

        g.drawString(msg, (getWidth() - metrics.stringWidth(msg)) / 2, getHeight() / 2 - 40);
        g.drawString(scoreMsg, (getWidth() - metrics.stringWidth(scoreMsg)) / 2, getHeight() / 2);
        g.drawString(highScoreMsg, (getWidth() - metrics.stringWidth(highScoreMsg)) / 2, getHeight() / 2 + 40);

        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString(replayMsg, (getWidth() - getFontMetrics(g.getFont()).stringWidth(replayMsg)) / 2, getHeight() / 2 + 80);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        if (rightDirection) x[0] += DOT_SIZE;
        else if (leftDirection) x[0] -= DOT_SIZE;
        else if (upDirection) y[0] -= DOT_SIZE;
        else if (downDirection) y[0] += DOT_SIZE;
    }

    private void checkApple() {
        if (x[0] == apple_x && y[0] == apple_y) {
            dots++;
            score++;
            locateApple();

            int newDelay = timer.getDelay() - 5;
            if (newDelay > 40) {
                timer.setDelay(newDelay);  // ✅ Increase speed
            }
        }
    }

    private void checkCollision() {
        for (int i = dots; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) inGame = false;
        }

        if (x[0] < 0 || x[0] >= getWidth() || y[0] < 0 || y[0] >= getHeight()) {
            inGame = false;
        }

        if (!inGame) timer.stop();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkApple();
            checkCollision();
            move();
        }
        repaint();
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (inGame) {
                if (key == KeyEvent.VK_LEFT && !rightDirection) {
                    leftDirection = true; upDirection = false; downDirection = false;
                }
                if (key == KeyEvent.VK_RIGHT && !leftDirection) {
                    rightDirection = true; upDirection = false; downDirection = false;
                }
                if (key == KeyEvent.VK_UP && !downDirection) {
                    upDirection = true; rightDirection = false; leftDirection = false;
                }
                if (key == KeyEvent.VK_DOWN && !upDirection) {
                    downDirection = true; rightDirection = false; leftDirection = false;
                }
            } else if (key == KeyEvent.VK_ENTER) {
                initGame();
            }
        }
    }
}
