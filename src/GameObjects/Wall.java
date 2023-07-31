package GameObjects;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Wall extends GameObject {

    public Wall(float x, float y, BufferedImage img) {
        super(x,y,img);
    }

    public void drawImage(Graphics buffer) {
        buffer.drawImage(this.img,(int)x,(int)y,null);
    }
}
