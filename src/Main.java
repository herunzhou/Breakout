import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;

public class Main extends JFrame {
    private JFrame mainFrame;
    private JPanel gameView;

    public Main() {
        Model model = new Model();
        mainFrame = new JFrame("Breakout");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gameView = new GameView(model);

        mainFrame.add(gameView);

        mainFrame.setSize(model.screenSizeX,model.screenSizeY);
        mainFrame.setResizable(true);
        mainFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }
}
