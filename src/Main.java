import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.io.*;
import java.util.*;

public class Main extends JFrame {
    private JFrame mainFrame;
    private JPanel gameView;
    private JLabel scoreLabel;

    public Main(int fpsInput, int ballSpeedInput) {
        Model model = new Model();
        model.fps = fpsInput;
        model.ballSpeed = ballSpeedInput;

        mainFrame = new JFrame("Breakout");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setMinimumSize(new Dimension(600, 600));
        mainFrame.setMaximumSize(new Dimension(1250, 700));
        gameView = new GameView(model);

        mainFrame.add(gameView, BorderLayout.CENTER);

        mainFrame.setSize(model.screenSizeX,model.screenSizeY);
        mainFrame.setResizable(true);
        mainFrame.setVisible(true);
    }

    public static void main(String[] args) {
//        new Main(30, 1);
        if (args.length != 2) {
            System.out.println("YOU NEED TWO ARGUMENTS - frame rate and ball speed, NO MORE, NO LESS!\n");
        } else {
            int fpsInput = Integer.parseInt(args[0]);
            int ballSpeedInput = Integer.parseInt(args[1]);

            if (fpsInput < 25 || fpsInput > 50) {
                System.out.println("FPS needs to be between 25 and 50!\n");
            } else if (ballSpeedInput != 1 && ballSpeedInput != 2 && ballSpeedInput != 3) {
                System.out.println("Ball speed needs to be between 1-3\n");
            } else {
                new Main(fpsInput, ballSpeedInput);
            }
        }
    }
}
