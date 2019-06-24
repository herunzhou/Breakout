import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

class Block {
    Shape shape = new Rectangle2D.Double();
    Color blockLineColor = Color.BLUE;
    Color blockFillColor = Color.RED;

    Block() {

    };
}

public class GameView extends JPanel implements KeyListener, ActionListener {
    Model model;
    private Timer timer;
    private Ellipse2D.Double theBall;
    public ArrayList<Block> blocks = new ArrayList<>();
    public int arrayOfBlocks[][];

    public void createBlocks(int row, int col) {

        arrayOfBlocks = new int[row][col];
        for (int i = 0; i < arrayOfBlocks.length; ++i) {
            for (int j = 0; j < arrayOfBlocks[0].length; ++j) {
                arrayOfBlocks[i][j] = 1;
            }
        }
    }

    public void repositionBlocks(int val, int row, int col) {
        arrayOfBlocks[row][col] = val;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Draw Background
        g2.setColor(model.backgroundColor);
        g2.fillRect(0,0, model.screenSizeX, model.screenSizeY);

        // Draw Paddle
        g2.setColor(model.paddleColor);
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
                    g2.setColor(model.blockColor);
                    g2.fillRect(j * model.blockWidth + 50, i * model.blockHeight + 50, model.blockWidth, model.blockHeight);
                    g2.setStroke(new BasicStroke(3));
                    g2.setColor(Color.BLACK);
                    g2.drawRect(j * model.blockWidth + 50, i * model.blockHeight + 50, model.blockWidth, model.blockHeight);
                }
            }
        }
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

            model.ballPositionX += model.ballChangePositionX;
            model.ballPositionY += model.ballChangePositionY;

            // Side Border Check
            if (model.ballPositionX < 0 || model.ballPositionX > model.screenSizeX - model.ballWidth) {
                model.ballChangePositionX = -model.ballChangePositionX;
            }

            // Top Border Check
            if (model.ballPositionY < 0) {
                model.ballChangePositionY = -model.ballChangePositionY;
            }

        }

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
                executingGame();
            }
        };
        timer.schedule(task, 0, (1000/model.fps));
    }

    public void movePaddleRight() {
        model.paddlePositionX += model.paddleChangePosition;
    }

    public void movePaddleLeft() {
        model.paddlePositionX -= model.paddleChangePosition;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (model.paddlePositionX >= model.screenSizeX - model.paddleWidth) {
                model.paddlePositionX = model.screenSizeX - model.paddleWidth;
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
