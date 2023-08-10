package GameObjects;


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
        if(with instanceof Tank t && t.getArmor() <= 4) {
            this.hasCollided = true;
        }
    }

    public boolean applyPowerUp(Tank tank) {
        if(tank.getArmor() < 4){
            tank.setArmor();
            collides(tank);
            return true;
        }
        return false;
    }
}
