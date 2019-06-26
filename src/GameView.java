import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.font.TextAttribute;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.Timer;

public class GameView extends JPanel implements KeyListener, ActionListener, IView {
    Model model;
    private Timer timer;
    private Ellipse2D.Double theBall;
    private JLabel scoreLabel;
    public int arrayOfBlocks[][];
    public Map<TextAttribute, Object> myAttributes = new HashMap<>();

    public void createBlocks(int row, int col) {

        arrayOfBlocks = new int[row][col];
        for (int i = 0; i < arrayOfBlocks.length; ++i) {
            for (int j = 0; j < arrayOfBlocks[0].length; ++j) {
                if (i == 3 && j == 7) {
                    arrayOfBlocks[i][j] = 2;
                } else if (i == 0 && j == 0) {
                    arrayOfBlocks[i][j] = 2;
                } else {
                    arrayOfBlocks[i][j] = 1;
                }
            }
        }
    }

    public void repositionBlocks(int val, int row, int col) {
        arrayOfBlocks[row][col] = val;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(model.scoreFont);

        // Draw Background
        g2.setColor(model.backgroundColor);
        g2.fillRect(0,0, this.getWidth(), this.getHeight());

        if (model.isPlaying) {
            // Draw Paddle
            g2.setColor(model.paddleColor);

            if ((model.firstBlueBlockHit && model.firstBlueBlockCount < 10) || (model.secondBlueBlockHit && model.secondBlueBlockCount < 10)) {
                model.paddleWidth = this.getWidth() / 4;
            } else {
                model.paddleWidth = this.getWidth() / 8;
            }

            if (model.paddlePositionX >= this.getWidth() - model.paddleWidth) {
                model.paddlePositionX = this.getWidth() - model.paddleWidth;
            }
            g2.fillRect(model.paddlePositionX, model.paddlePositionY, model.paddleWidth, model.paddleHeight);

            // Draw Ball
            // Drawing Circle Code obtained from: https://stackoverflow.com/questions/2509561/how-to-draw-a-filled-circle-in-java
            g2.setColor(model.ballColor);
            theBall = new Ellipse2D.Double(model.ballPositionX, model.ballPositionY, model.ballWidth, model.ballHeight);
            g2.fill(theBall);

            // Draw Blocks
            for (int i = 0; i < arrayOfBlocks.length; ++i) {
                for (int j = 0; j < arrayOfBlocks[0].length; ++j) {
                    if (arrayOfBlocks[i][j] > 0) {

                        model.blockWidth = (this.getWidth() - 100) / 8;

                        if ((i == 3 && j == 7) || (i == 0 && j ==0)) {
                            g2.setColor(model.specialBlockColor);
                        } else {
                            g2.setColor(model.blockColor);
                        }
                        g2.fillRect(j * model.blockWidth + 50, i * model.blockHeight + 50, model.blockWidth, model.blockHeight);
                        g2.setStroke(new BasicStroke(3));
                        g2.setColor(Color.BLACK);
                        g2.drawRect(j * model.blockWidth + 50, i * model.blockHeight + 50, model.blockWidth, model.blockHeight);
                    }
                }
            }

            // Draw Score
            g2.setColor(Color.WHITE);

            if (model.didLose) {
                g2.drawString("YOU LOST!", 50, 35);
            } else if (model.numBlocks > 0) {
                g2.drawString("YOUR SCORE: " + model.score, 50, 35);
            } else {
                g2.drawString("YOU WON!", 50, 35);
            }

        } else {
            g2.setColor(Color.WHITE);
            g2.drawString("Use Left and Right Arrow Keys to move paddle.", 10, 40);
            g2.drawString("Prevent the ball from going below the paddle.", 10, 70);
            g2.drawString("Try to destroy all the Blocks.", 10, 100);
            g2.drawString("A Blue Block increases Paddle size for 10 secs.", 10, 130);
            g2.drawString("Press S to Begin the Game...", 10, 160);
            g2.drawString("Created By: Herun Zhou (h74zhou)", 10, this.getHeight() - 100);
        }
    }

    public void executingFrame() {
        repaint();
    }

    public void executingGame() {
        if (model.isPlaying) {

            // Paddle Intersection Check
            Rectangle2D ballPlaceholder = new Rectangle(model.ballPositionX, model.ballPositionY, model.ballWidth, model.ballHeight);
            Rectangle2D paddlePlaceholder = new Rectangle(model.paddlePositionX, model.paddlePositionY, model.paddleWidth, model.paddleHeight);

            if (ballPlaceholder.intersects(paddlePlaceholder)) {
                model.ballChangePositionY = -model.ballChangePositionY;
            }

            // Block Check
            boolean finishedUpdatigBlocks = false;
            for (int i = 0; i < arrayOfBlocks.length && !finishedUpdatigBlocks; ++i) {
                for (int j = 0; j < arrayOfBlocks[0].length; ++j) {
                    if (arrayOfBlocks[i][j] > 0) {

                        Rectangle2D ballPlaceholder2 = new Rectangle(model.ballPositionX, model.ballPositionY, model.ballWidth, model.ballHeight);
                        Rectangle2D blockPlaceholder = new Rectangle(j * model.blockWidth + 50, i * model.blockHeight + 50, model.blockWidth, model.blockHeight);

                        if (ballPlaceholder2.intersects(blockPlaceholder)) {

                            if (i == 0 && j ==0) {
                                model.firstBlueBlockHit = true;
                                timer = new Timer();
                                TimerTask task = new TimerTask() {
                                    @Override
                                    public void run() {
                                        model.firstBlueBlockCount += 1;
                                    }
                                };
                                timer.schedule(task, 1000, 1000);
                            }

                            if (i == 3 && j == 7) {
                                model.secondBlueBlockHit = true;
                                timer = new Timer();
                                TimerTask task = new TimerTask() {
                                    @Override
                                    public void run() {
                                        model.secondBlueBlockCount += 1;
                                    }
                                };
                                timer.schedule(task, 1000, 1000);
                            }

                            repositionBlocks(0, i, j);
                            model.score += 1;
                            model.numBlocks -= 1;

                            int ballOffsetLeft = model.ballPositionX + model.ballWidth - 1;
                            int ballOffsetRight = model.ballPositionX + 1;
                            Double blockOffsetX = blockPlaceholder.getX() + blockPlaceholder.getWidth();

                            if (ballOffsetLeft <= blockPlaceholder.getX() || ballOffsetRight >= blockOffsetX) {
                                model.ballChangePositionX = -model.ballChangePositionX;
                            } else {
                                model.ballChangePositionY = -model.ballChangePositionY;
                            }
                            finishedUpdatigBlocks = true;
                            break;
                        }
                    }
                }
            }


            if (model.numBlocks == 0) {
                model.ballChangePositionY = 0;
                model.ballChangePositionX = 0;
            }

            // Bottom Check
            if (model.ballPositionY > this.getHeight() - 20 && model.numBlocks > 0) {
                model.ballChangePositionY = 0;
                model.ballChangePositionX = 0;
                model.didLose = true;
            }

            model.ballPositionX += model.ballChangePositionX;
            model.ballPositionY += model.ballChangePositionY;

            // Side Border Check
            if (model.ballPositionX < 0 || model.ballPositionX > this.getWidth() - model.ballWidth) {
                model.ballChangePositionX = -model.ballChangePositionX;
            }

            // Top Border Check
            if (model.ballPositionY < 0) {
                model.ballChangePositionY = -model.ballChangePositionY;
            }

        }

    }

    public void updateView() {
        repaint();
    }

    public GameView(Model m) {
        model = m;

        // init blocks
        createBlocks(5, 8);

        addKeyListener(this);
        setFocusable(true);

        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                executingFrame();
            }
        };
        timer.schedule(task, 0, (1000/model.fps));


        timer = new Timer();
        TimerTask task2 = new TimerTask() {
            @Override
            public void run() {
                executingGame();
            }
        };

        int ballFPS;
        if (model.ballSpeed == 1) {
            ballFPS = 25;
        } else if (model.ballSpeed == 2) {
            ballFPS = 38;
        } else {
            ballFPS = 50;
        }
        timer.schedule(task2, 0, (1000/ballFPS));
    }

    public void movePaddleRight() {
        model.paddleChangePosition = this.getWidth() / 45;
        model.paddlePositionX += model.paddleChangePosition;
    }

    public void movePaddleLeft() {
        model.paddleChangePosition = this.getWidth() / 45;
        model.paddlePositionX -= model.paddleChangePosition;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_S) {
            model.isPlaying = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (model.paddlePositionX >= this.getWidth() - model.paddleWidth) {
                model.paddlePositionX = this.getWidth() - model.paddleWidth;
            } else {
                movePaddleRight();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (model.paddlePositionX <= 0) {
                model.paddlePositionX = 0;
            } else {
                movePaddleLeft();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
       repaint();
    }
}
