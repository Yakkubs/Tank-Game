package GameObjects;


import Game.GameConstants;
import Game.GameWorld;
import Game.Tank;
import Utilities.Animation;
import Utilities.ResourceManager;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bullet extends GameObject {
    private float vx;
    private float vy;
    private float angle;
    private float R = 5;
    public int tankID;
    public float bulletDamage;
    private GameWorld gw;

    public Bullet(float x, float y, BufferedImage img, float angle, int tankId,float bulletDamage,GameWorld gw) {
        super(x,y,img);
        this.vx = 0;
        this.vy = 0;
        this.angle = angle;
        this.tankID = tankId;
        this.bulletDamage = bulletDamage;
        this.gw = gw;
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
        if (x < 20) {
            x = 20;
        }
        if (x >= GameConstants.GAME_WORLD_WIDTH - 20) {
            x = GameConstants.GAME_WORLD_WIDTH - 20;
        }
        if (y < 20) {
            y = 20;
        }
        if (y >= GameConstants.GAME_WORLD_HEIGHT - 20) {
            y = GameConstants.GAME_WORLD_HEIGHT - 20;
        }
    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }


    public void drawImage(Graphics g) {
        if(!hasCollided) {
            AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
            rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(this.img, rotation, null);
            g2d.setColor(Color.RED);
        }
    }

    @Override
    public void collides(GameObject with) {
        if( with instanceof Wall w){
            if(w instanceof BreakableWall){
                w.collides(this);
            }
            else if(w instanceof RandomDrop){

                w.collides(this);
            }
            gw.addToAnims(new Animation(x-20,y-20,ResourceManager.getAnimation("bullethit")));
            this.hasCollided = true;
        }else if(with instanceof Tank && ((Tank) with).id != tankID){
            this.hasCollided = true;
            gw.addToAnims(new Animation(x-20,y-20,ResourceManager.getAnimation("bullethit")));
        }
        ResourceManager.getSound("explosion").playSound();
    }

}

