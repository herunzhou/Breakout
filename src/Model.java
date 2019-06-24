import java.awt.*;

public class Model {
    public boolean isPlaying = true;
    public int score = 0;
    public int numBlocks = 30;
    public int fps = 30;

    public int ballPositionX = 200;
    public int ballPositionY = 300;
    public int ballChangePositionX = -4;
    public int ballChangePositionY = -8;
    public int ballWidth = 20;
    public int ballHeight = 20;

    public int paddlePositionX = 300;
    public int paddlePositionY = 550;
    public int paddleWidth = 100;
    public int paddleHeight = 10;
    public int paddleChangePosition = 20;

    public int screenSizeX = 900;
    public int screenSizeY = 600;

    public Color paddleColor = Color.GREEN;
    public Color backgroundColor = Color.BLACK;
    public Color ballColor = Color.YELLOW;



}
