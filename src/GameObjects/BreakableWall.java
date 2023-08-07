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
            if(health <= 0){
                this.img = ResourceManager.getSprite("speed");
                this.hasCollided = true;

            }else {
                this.health--;
                this.img = ResourceManager.getSprite("break2");
            }
        }
    }
}
