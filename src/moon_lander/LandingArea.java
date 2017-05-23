package moon_lander;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by ronald on 06/12/16.
 */
public class LandingArea {

    /**
     * X coordinate of the landing area
     */
    public int x;

    /**
     * Y coordinate of landing area
     */
    public int y;

    /**
     * Image of the landing area
     */
    private BufferedImage landingAreaImg;

    /**
     * Width of landing area
     */
    public int landingAreaWidth;

    public LandingArea() {
        Initialize();
        LoadContent();
    }

    private void Initialize() {
        //x coordinate of the landing area is at 46% frame width.
        x = (int) (Framework.frameWidth * 0.46);
        //y coordinate of the landing area is 86 frame height
        y = (int) (Framework.frameHeight * 0.86);
    }

    private void LoadContent() {
        try {
            URL landingAreaImgUrl = this.getClass().getResource("/moon_lander/resources/images/landing_area.png");
            landingAreaImg = ImageIO.read(landingAreaImgUrl);
            landingAreaWidth = landingAreaImg.getWidth();
        } catch (IOException ex) {
            Logger.getLogger(LandingArea.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Draw(Graphics2D g2d) {
        g2d.drawImage(landingAreaImg, x, y, null);
    }
}
