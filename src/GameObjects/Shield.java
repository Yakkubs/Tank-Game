package GameObjects;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Shield extends GameObject {
    public Shield(float x, float y, BufferedImage img) {
        super(x,y,img);
    }
    public void drawImage(Graphics buffer) {
        buffer.drawImage(this.img,(int)x,(int)y,null);
    }
}
