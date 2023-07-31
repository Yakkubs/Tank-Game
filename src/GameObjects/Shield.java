package GameObjects;


import Game.Tank;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Shield extends GameObject implements PowerUps {
    public Shield(float x, float y, BufferedImage img) {
        super(x,y,img);
    }

    @Override
    public void drawImage(Graphics buffer) {
        buffer.drawImage(this.img,(int)x,(int)y,null);
    }

    @Override
    public void collides(GameObject with) {

    }
    public void applyPowerUp(Tank tank) {

    }
}
