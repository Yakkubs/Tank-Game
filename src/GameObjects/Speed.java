package GameObjects;

import Game.Tank;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Speed extends GameObject implements PowerUps {
    public Speed(float x, float y, BufferedImage img) {
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
        this.hasCollided = true;
    }

    @Override
    public boolean applyPowerUp(Tank tank) {
        if(tank.getSpeed() <= 5) {
            tank.setSpeed();
            collides(tank);
            return true;
        }
        return false;
    }

}
