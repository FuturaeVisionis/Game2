package moon_lander;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ronald on 06/12/16.
 */
public class PlayerRocket {

    /**
     * We use this to generate a random for starting x coordinate of the rocket.
     */
    private Random random;

    /**
     * x coordinate of the rocket.
     */
    public int x;

    /**
     * y coordinate of the rocket.
     */

    public int y;

    /**
     * Rocket is landed?
     */

    public boolean landed;

    /**
     * Has rocket crashed?
     */

    public boolean crashed;

    /**
     * Accelerating speed of the rocket.
     */
    private int speedAccelerating;

    /**
     * Stopping / falling speed of the rocket. Falling speed because the gravity pulls the rocket down to the moon?
     */
    private int speedStopping;

    /**
     * Maximum speed that the rocket can have without having a crash when landing.
     */
    public int topLandingSpeed;

    /**
     * How fast and to which direction rocket is moving on x coordinate?
     */
    private int speedX;

    /**
     * How fast and to which direction rocket is moving on y coordinate.
     */
    public int speedY;

    /**
     * Image of rocket in the air.
     */
    private BufferedImage rocketImg;

    /**
     * Image of rocket when landed.
     */
    private BufferedImage rocketLandedImg;

    /**
     * Image of rocket when crashed.
     */

    private BufferedImage rocketCrashedImg;

    /**
     * Image of rocket fire.
     */
    private BufferedImage rocketFireImg;

    /**
     * Width of rocket
     */
    public int rocketImgWidth;

    /**
     * Hight of rocket
     */
    public int rocketImgHeight;

    public PlayerRocket() {
        Initialize();
        LoadContent();
        //Now that we have rocketImgWidth we set starting x coordinate.

        x = random.nextInt(Framework.frameWidth - rocketImgWidth);
    }

    private void Initialize() {
        random = new Random();
        ResetPlayer();
        speedAccelerating = 2;
        speedStopping = 1;
        topLandingSpeed = 5;
    }

    private void LoadContent() {
        try {
            URL rocketImgUrl = this.getClass().getResource("/moon_lander/resources/images/rocket.png");
            rocketImg = ImageIO.read(rocketImgUrl);
            rocketImgWidth = rocketImg.getWidth();
            rocketImgHeight = rocketImg.getHeight();

            URL rocketLandedImgUrl = this.getClass().getResource("/moon_lander/resources/images/rocket_landed.png");
            rocketLandedImg = ImageIO.read(rocketLandedImgUrl);

            URL rocketCrashedImgUrl = this.getClass().getResource("/moon_lander/resources/images/rocket_crashed.png");
            rocketCrashedImg = ImageIO.read(rocketCrashedImgUrl);

            URL rocketFireImgUrl = this.getClass().getResource("/moon_lander/resources/images/rocket_fire.png");
            rocketFireImg = ImageIO.read((rocketFireImgUrl));
        } catch (IOException ex) {
            Logger.getLogger(PlayerRocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Here we set up the rocket when we are starting a new page.
     */
    public void ResetPlayer() {
        landed = false;
        crashed = false;

        x = random.nextInt(Framework.frameWidth - rocketImgWidth);

        y = 10;

        speedX = 0;
        speedY = 0;
    }

    /**
     * Here we move the rocket.
     */

    public void Update() {
        //calculating speed for moving up or down.
        if (Canvas.keyboardKeyState(KeyEvent.VK_W))
            speedY -= speedAccelerating;
        else
            speedY += speedStopping;

        //calculating speed for moving or stopping to left.
        if (Canvas.keyboardKeyState((KeyEvent.VK_A)))
            speedX -= speedAccelerating;
        else if (speedX < 0)
            speedX += speedStopping;

        //calculating speed for moving or stopping to the right.
        if (Canvas.keyboardKeyState((KeyEvent.VK_D)))
            speedX += speedAccelerating;
        else if (speedX > 0)
            speedX -= speedStopping;

        // Moves the rocket
        x += speedX;
        y += speedY;
    }

    public void Draw(Graphics2D g2d) {
        g2d.setColor(Color.white);
        g2d.drawString("Rocket coordinates: " + x + " : " + y, 5, 15);
        // if the rocket is landed
        if (landed) {
            g2d.drawImage(rocketLandedImg, x, y, null);
        }
        // if the rocket is crashed
        else if (crashed) {
            g2d.drawImage(rocketCrashedImg, x, y + rocketImgHeight - rocketCrashedImg.getHeight(), null);
        }
        // If the rocket is still in space
        else {
            //if player holds down a W key we draw rocket fire.
            if (Canvas.keyboardKeyState(KeyEvent.VK_W))
                g2d.drawImage(rocketFireImg, x + 12, y + 66, null);
            g2d.drawImage(rocketImg, x, y, null);
        }
    }
}

