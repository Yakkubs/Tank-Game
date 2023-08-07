package GameObjects;

import java.awt.*;
import java.awt.image.BufferedImage;

public class RandomDrop extends Wall {
    public RandomDrop(float x, float y, BufferedImage img) {
        super(x,y,img);
    }
    public float getX(){return this.x;}
    public float getY(){return this.y;}

}
