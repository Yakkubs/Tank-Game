package GameObjects;


import Game.GameConstants;
import Game.GameWorld;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bullet extends GameObject {
    private float vx;
    private float vy;
    private float angle;
    private float R = 5;

    public Bullet(float x, float y, BufferedImage img, float angle) {
        super(x,y,img);
        this.vx = 0;
        this.vy = 0;
        this.angle = angle;
    }

    public void update() {
        vx = Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
        this.hitBox.setLocation((int)x,(int)y);
    }

    private void checkBorder() {
        if (x < 30) {
            x = 30;
        }
        if (x >= GameConstants.GAME_WORLD_WIDTH - 40) {
            x = GameConstants.GAME_WORLD_WIDTH - 40;
        }
        if (y < 30) {
            y = 30;
        }
        if (y >= GameConstants.GAME_WORLD_HEIGHT - 40) {
            y = GameConstants.GAME_WORLD_HEIGHT - 40;
        }
    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }


    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
        g2d.setColor(Color.RED);
    }

    @Override
    public void collides(GameObject with) {
        if( with instanceof Wall){
            
        }
    }

}

