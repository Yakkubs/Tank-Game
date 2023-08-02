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
        if(!hasCollided) {
            buffer.drawImage(this.img, (int) x, (int) y, null);
        }
    }

    @Override
    public void collides(GameObject with) {
        if(with instanceof Tank) {
            this.hasCollided = true;
        }
    }

    public void applyPowerUp(Tank tank) {
        tank.setArmor();
        collides(tank);
    }
}
