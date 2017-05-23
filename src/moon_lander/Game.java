package moon_lander;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ronald on 05/12/16.
 */
public class Game {
    /**
     * The space rocket with which player will have to land.
     */
    private PlayerRocket playerRocket;

    /**
     * Landing area on which the rocket will have to land.
     */
    private LandingArea landingArea;

    /**
     * Game background images.
     */
    private BufferedImage backgroundImg;

    /**
     * Red border of the frame. It is used when player crashes the rocket
     */
    private BufferedImage redBorderImg;

    public Game() {
        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;

        Thread threadForInitGame = new Thread() {
            @Override
            public void run() {
                // Sets variables and objects for the game.
                Initialize();
                // Load game files (images, sounds, ...
                LoadContent();

                Framework.gameState = Framework.GameState.PLAYING;
            }
        };
        threadForInitGame.start();
    }

    /**
     * Set variables and objects for the game.
     */
    private void Initialize() {

        playerRocket = new PlayerRocket();
        landingArea = new LandingArea();
    }

    /**
     * Load game files - images, sounds, ...
     */
    public void LoadContent() {
        try {
            URL backgroundImgUrl = this.getClass().getResource("/moon_lander/resources/images/background.jpg");
            backgroundImg = ImageIO.read(backgroundImgUrl);

            URL redBorderImgUrl = this.getClass().getResource("/moon_lander/resources/images/red_border.png");
            redBorderImg = ImageIO.read(redBorderImgUrl);
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    /**
     * Restart game - reset some variables
     */
    public void RestartGame() {
        playerRocket.ResetPlayer();
    }

    /**
     * Update game logic
     * gameTime gameTime of the game
     * mouse position current mouse position
     */
    public void UpdateGame(long gameTime, Point mousePosition) {
        // Move the rocket
        playerRocket.Update();
        //checks where the player rocket is. Is it still in space or is it landed or crashed.
        //First we check bottom y coordinates of the rocket if it is near the landing area.
        if (playerRocket.y + playerRocket.rocketImgHeight - 10 > landingArea.y) {
            // here we check if the rocket is over the landing area.
            if ((playerRocket.x > landingArea.x) && (playerRocket.x < landingArea.x + landingArea.landingAreaWidth -
                    playerRocket.rocketImgWidth)) ;
            {
                //Here we check if the rocket's speed isn't to high.
                if (playerRocket.speedY <= playerRocket.topLandingSpeed) playerRocket.landed = true;
                else
                    playerRocket.crashed = true;
                Framework.gameState = Framework.GameState.GAME_OVER;
            }
        }
    }

    /**
     * Draw the game to the screen.
     * g2d Graphics2D
     * mouse position current position
     */

    public void Draw(Graphics2D g2d, Point mousePosition) {
        g2d.drawImage(backgroundImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
        landingArea.Draw(g2d);
        playerRocket.Draw(g2d);
    }

    /**
     * Draw the game over screen
     * g2d Graphics2D
     * mousePosition Current mouse position
     * gameTime Game time in nanoseconds
     */
    public void DrawGameOver(Graphics2D g2d, Point mousePosition, long gameTime) {
        Draw(g2d, mousePosition);
        g2d.drawString("Press space or enter to start.", Framework.frameWidth / 2 - 100,
                Framework.frameHeight / 3 + 70);
        if (playerRocket.landed) {
            g2d.drawString("You have successfully landed!", Framework.frameWidth / 2 - 100,
                    Framework.frameHeight / 3);
            g2d.drawString("You have landed in " + gameTime / Framework.secInNanosec + " seconds.",
                    Framework.frameWidth / 2 - 100, Framework.frameHeight / 3 + 20);
        } else {
            g2d.setColor(Color.red);
            g2d.drawString("You have crashed the rocket!", Framework.frameWidth / 2 - 95, Framework.frameHeight / 3);
            g2d.drawImage(redBorderImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
        }
    }

}

