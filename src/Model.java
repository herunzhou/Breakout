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

public class Model {
    public boolean isPlaying = false;
    public boolean didLose = false;
    public int score = 0;
    public int numBlocks = 40;
    public int fps = 30;
    public int ballSpeed;

    public int ballPositionX = 500;
    public int ballPositionY = 500;
    public int ballChangePositionX = -4;
    public int ballChangePositionY = -8;
    public int ballWidth = 20;
    public int ballHeight = 20;

    public int paddlePositionX = 375;
    public int paddlePositionY = 550;
    public int paddleWidth = 100;
    public int paddleHeight = 10;
    public int paddleChangePosition = 20;

    public int blockWidth = 100;
    public int blockHeight = 50;
    public Color blockColor = Color.RED;
    public Color specialBlockColor = Color.BLUE;

    public int screenSizeX = 900;
    public int screenSizeY = 600;

    public Color paddleColor = Color.GREEN;
    public Color backgroundColor = Color.BLACK;
    public Color ballColor = Color.YELLOW;

    public Font scoreFont = new Font("Arial", Font.BOLD, 25);

    public int firstBlueBlockCount = 0;
    public boolean firstBlueBlockHit = false;
    public int secondBlueBlockCount = 0;
    public boolean secondBlueBlockHit = false;

}
