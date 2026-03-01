package snakegame;

import javax.swing.*;

public class SnakeGame extends JFrame {

    SnakeGame() {
        super("Snake Game");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new Board());
        setVisible(true);
    }

    public static void main(String[] args) {
        new SnakeGame();
    }
}
