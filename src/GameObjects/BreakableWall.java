package GameObjects;

import Utilities.ResourceManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BreakableWall extends Wall {
    private int health = 1;
    public BreakableWall(float x, float y, BufferedImage img) {
        super(x,y,img);
    }
    @Override
    public void collides(GameObject with) {
        if(with instanceof Bullet){
            if(health > 0){
                hasCollided = true;
            }
            health--;
            this.img = ResourceManager.getSprite("break2");
        }
    }
    public void drawImage(Graphics buffer) {
        buffer.drawImage(this.img,(int)x,(int)y,null);
    }
}
