package moon_lander;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ronald on 05/12/16.
 * <p>
 * Framework that controls the game that created it, updates it and draws it to the screen
 */
public class Framework extends Canvas {

    /**
     * Width of the frame.
     */
    public static int frameWidth;

    /**
     * Height of the frame
     */
    public static int frameHeight;

    /**
     * Time of one second in nano seconds
     * 1 second = 1 000 000 000 nanoseconds
     */
    public static final long secInNanosec = 1000000000L;

    /**
     * Time of one millisecond in nanoseconds
     * 1 millisecond = 1000000 nanosecond
     */
    public static final long milisecNanosec = 1000000L;

    /**
     * FPS - Frames per second
     * How many times per second the game should update?
     */
    private final int GAME_FPS = 16;

    /**
     * Pause between updates. It is in nanoseconds
     */
    private final long GAME_UPDATE_PERIOD = secInNanosec / GAME_FPS;

    /**
     * Possible state of the game
     */
    public enum GameState {
        STARTING, VISUALIZING, GAME_CONTENT_LOADING, MAIN_MENU, OPTIONS, PLAYING, GAME_OVER, DESTROYED

    }

    /**
     * Current state of the game
     */
    public static GameState gameState;

    /**
     * Elapsed game time in nanoseconds
     */
    private long gameTime;

    //It is used for calculating elapsed time.

    private long lastTime;

    // The actual game time

    private Game game;

    /**
     * Image for menu
     */
    private BufferedImage moonLanderMenuImg;

    public Framework() {
        super();
        gameState = GameState.VISUALIZING;
        //We start the game in new Thread
        Thread gameThread = new Thread() {
            @Override
            public void run() {
                GameLoop();
            }

        };
        gameThread.start();
    }

    /**
     * Set variables and objects.
     * This method is intended to set the variables and the objects for this class, variables and objects for the actual
     * game can be set in Game.java.
     */
    private void Initialize() {

    }

    /**
     * Load files - images,  sounds , ...
     * This method is intended to load files for this class, files for the actual game can be loaded in Game.java.
     */
    private void LoadContent() {
        try {
            URL moonLanderMenuImgUrl = this.getClass().getResource("/moon_lander/resources/images/menu.jpg");
            moonLanderMenuImg = ImageIO.read(moonLanderMenuImgUrl);
        } catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * In specific intervals of time (GAME_UPDATE_PERIOD) the game/logic is updated and then the game is drawn
     * on the screen
     */
    private void GameLoop() {

        //These two variables are used in VISUALIZING state of the game. We used them to wait some time so that we can get
        // correct frame /window resolution.

        long visualizingTime = 0, lastVisualizingTime = System.nanoTime();

        //These variables are used for calculating the time that defines for how long we should put thread to sleep to meet
        // the GAME_FPS.

        long beginTime, timeTaken, timeLeft;
        while (true) {
            beginTime = System.nanoTime();
            switch (gameState) {
                case PLAYING:
                    gameTime += System.nanoTime() - lastTime;
                    game.UpdateGame(gameTime, getMousePosition());
                    lastTime = System.nanoTime();
                    break;
                case GAME_OVER:
                    break;
                case MAIN_MENU:
                    break;
                case OPTIONS:
                    break;
                case GAME_CONTENT_LOADING:
                    break;
                case STARTING:
                    //Sets variables and objects
                    Initialize();
                    //Load files - images , sounds, ...
                    LoadContent();

                    //When all things that are called above are finished, we can change game status to main menu.

                    gameState = GameState.MAIN_MENU;
                    break;

                case VISUALIZING:
                    if (this.getWidth() > 1 && visualizingTime > secInNanosec) {
                        frameWidth = this.getWidth();
                        frameHeight = this.getHeight();

                        // When we get size of frame, we change status.

                        gameState = GameState.STARTING;
                    } else {
                        visualizingTime += System.nanoTime() - lastVisualizingTime;
                        lastVisualizingTime = System.nanoTime();
                    }
                    break;
            }
            //Repaint the screen
            repaint();

            //Here we calculate the time that defines for how long we should put thread to sleep to meet the GAME_FPS.
            timeTaken = System.nanoTime() - beginTime;
            timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / milisecNanosec; // In milliseconds
            // if the time is less than 10 milliseconds, then we will put thread to sleep for 10 milliseconds so that
            // some other tread can do some work
            if (timeLeft < 10)
                timeLeft = 10; //  set a minimum
            try {
                //Provides the necessary delay and also yields control so that other thread can do work.
                Thread.sleep(timeLeft);
            } catch (InterruptedException ex) {

            }
        }
        /**
         * Draw the game to the screen. It is called through repaint() method in GameLoop() method
         */
    }

    @Override
    public void Draw(Graphics2D g2d) {
        switch (gameState) {
            case PLAYING:
                game.Draw(g2d, getMousePosition());
                break;
            case GAME_OVER:
                game.DrawGameOver(g2d, getMousePosition(), gameTime);
                break;
            case MAIN_MENU:
                g2d.drawImage(moonLanderMenuImg, 0, 0, frameWidth, frameHeight, null);
                g2d.setColor(Color.WHITE);
                g2d.drawString("Use w a d keys to control the rocket.", frameWidth / 2 - 117, frameHeight / 2);
                g2d.drawString("Press any key to start the game.", frameWidth / 2 - 100, frameHeight / 2 + 30);
                g2d.drawString("WWW.GAMETUTORIAL.NET", 7, frameHeight - 5);
                break;
            case OPTIONS:
                // ...
                break;
            case GAME_CONTENT_LOADING:
                g2d.setColor(Color.white);
                g2d.drawString("Game is loading", frameWidth / 2 - 50, frameHeight / 2);
                break;
        }

    }

    /**
     * Starts new game.
     */
    private void newGame() {
        // We set gameTime to zero and lastTime to current time for later calculations

        gameTime = 0;
        lastTime = System.nanoTime();

        game = new Game();
    }

    /**
     * Restart game - reset game time and call RestartGame() method of game object so that reset some variables
     */

    private void restartGame() {
        // We set gameTime to zero and lastTime to current time for later calculations.

        gameTime = 0;
        lastTime = System.nanoTime();

        game.RestartGame();

        // We change game status so that the game can start.

        gameState = GameState.PLAYING;
    }

    /**
     * Returns the position of the mouse pointer in game frame/window.
     * If mouse position is null, then this method return 0 , 0 coordinate.
     *
     * @return Point of mouse coordinates.
     */
    private Point mousePosition() {
        try {
            Point mp = this.getMousePosition();

            if (mp != null)
                return this.getMousePosition();
            else
                return new Point(0, 0);
        } catch (Exception e) {
            return new Point(0, 0);
        }
    }

    /**
     * This method is called when keyboard key is released.
     */

    @Override
    public void keyReleasedFramework(KeyEvent e) {
        switch (gameState) {
            case MAIN_MENU:
                newGame();
                break;
            case GAME_OVER:
                if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER) restartGame();
                break;
        }
    }
/**
 * This method is called when mouse button is clicked.
 */
    @Override
    public void mouseClicked(MouseEvent e) {

    }
}
