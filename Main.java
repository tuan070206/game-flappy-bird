import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class Main extends JPanel implements ActionListener, KeyListener {
    int birdY = 250;
    int velocity = 0;
    int gravity = 1;
    int score = 0;
    boolean gameOver = false;

    ArrayList<Rectangle> pipes = new ArrayList<>();
    Timer timer;


    public Main() {
        JFrame frame = new JFrame("Flappy Bird Java");
        frame.setSize(400, 600);
        frame.add(this);
        frame.addKeyListener(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        timer = new Timer(20, this); // ~50fps
        timer.start();

        addPipe(true);
        addPipe(true);
    }



    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int birdX = 100;  // toạ độ X ban đầu


        // Background
        g.setColor(Color.cyan);
        g.fillRect(0, 0, 400, 600);

// Bird body
        g.setColor(Color.orange);
        g.fillOval(birdX, birdY, 20, 20);

// Eye
        g.setColor(Color.white);
        g.fillOval(birdX + 12, birdY + 5, 5, 5);

// Beak
        g.setColor(Color.yellow);
        int[] xPoints = { birdX + 20, birdX + 25, birdX + 20 };
        int[] yPoints = { birdY + 10, birdY + 13, birdY + 16 };
        g.fillPolygon(xPoints, yPoints, 3);


        // Pipes
        g.setColor(Color.green);
        for (Rectangle pipe : pipes) {
            g.fillRect(pipe.x, pipe.y, pipe.width, pipe.height);
        }

        // Ground
        g.setColor(Color.orange);
        g.fillRect(0, 550, 400, 50);

        // Score
        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Score: " + score, 20, 40);

        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("Game Over!", 100, 300);
        }
    }

    public void addPipe(boolean start) {
        int gap = 150;
        int width = 60;
        int height = 50 + new Random().nextInt(200);

        if (start) {
            pipes.add(new Rectangle(400 + pipes.size() * 200, 0, width, height));
            pipes.add(new Rectangle(400 + pipes.size() * 200, height + gap, width, 600 - height - gap - 50));
        } else {
            pipes.add(new Rectangle(pipes.get(pipes.size() - 1).x + 200, 0, width, height));
            pipes.add(new Rectangle(pipes.get(pipes.size() - 1).x, height + gap, width, 600 - height - gap - 50));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            // Gravity
            velocity += gravity;
            birdY += velocity;

            // Move pipes
            for (int i = 0; i < pipes.size(); i++) {
                Rectangle pipe = pipes.get(i);
                pipe.x -= 5;
            }

            // Remove pipes
            if (pipes.size() > 0 && pipes.get(0).x + pipes.get(0).width < 0) {
                pipes.remove(0);
                pipes.remove(0);
                addPipe(false);
                score++;
            }

            // Collision detection
            for (Rectangle pipe : pipes) {
                if (pipe.intersects(new Rectangle(100, birdY, 20, 20))) {
                    gameOver = true;
                }
            }

            // Ground and ceiling
            if (birdY > 530 || birdY < 0) {
                gameOver = true;
            }

            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameOver && e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocity = -10;
        }

        if (gameOver && e.getKeyCode() == KeyEvent.VK_R) {
            // Restart game
            birdY = 250;
            velocity = 0;
            score = 0;
            pipes.clear();
            addPipe(true);
            addPipe(true);
            gameOver = false;
        }
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        new Main();
    }
}
