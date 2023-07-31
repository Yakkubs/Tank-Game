package Game;

import GameObjects.*;
import Utilities.ResourceManager;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author anthony-pc
 */
public class Tank extends GameObject {

    private float screenX;
    private float screenY;
    private float vx;
    private float vy;
    private float angle;
    private List<Bullet> ammo = new ArrayList<Bullet>();
    long timeSinceLastShot = 0L;
    long coolDown = 1000;
    private int health;
    private int armor;
    private int bulletDamage;
    private float R = 5;
    private float ROTATIONSPEED = 3.0f;

    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean ShootPressed;

    Tank(float x, float y, float vx, float vy, float angle, BufferedImage img) {
        super(x,y,img);
        this.vx = vx;
        this.vy = vy;
        this.angle = angle;
        this.health = 5;
        this.armor = 0;
    }

    void setX(float x) {
        this.x = x;
    }

    void setY(float y) {
        this.y = y;
    }

    void toggleUpPressed() {
        this.UpPressed = true;
    }

    void toggleDownPressed() {
        this.DownPressed = true;
    }

    void toggleRightPressed() {
        this.RightPressed = true;
    }

    void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    void toggleShootPressed() {
        this.ShootPressed = true;
    }

    void unToggleUpPressed() {
        this.UpPressed = false;
    }

    void unToggleDownPressed() {
        this.DownPressed = false;
    }

    void unToggleRightPressed() {
        this.RightPressed = false;
    }

    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    void unToggleShootPressed() {
        this.ShootPressed = false;
    }

    void update() {
        if (this.UpPressed) {
            this.moveForwards();
        }

        if (this.DownPressed) {
            this.moveBackwards();
        }

        if (this.LeftPressed) {
            this.rotateLeft();
        }

        if (this.RightPressed) {
            this.rotateRight();
        }
        if (this.ShootPressed && ((this.timeSinceLastShot + this.coolDown) < System.currentTimeMillis())) {
            this.timeSinceLastShot = System.currentTimeMillis();
            this.ammo.add(new Bullet(x,y, ResourceManager.getSprite("bullet"),angle));

        }
        this.ammo.forEach(Bullet::update);
        this.hitBox.setLocation((int)x,(int)y);
    }

    private void rotateLeft() {
        this.angle -= this.ROTATIONSPEED;
    }

    private void rotateRight() {
        this.angle += this.ROTATIONSPEED;
    }

    private void moveBackwards() {
        vx = Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = Math.round(R * Math.sin(Math.toRadians(angle)));
        x -= vx;
        y -= vy;
        checkBorder();
    }

    private void moveForwards() {
        vx = Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
    }


    private void checkBorder() {
        if (x < 30) {
            x = 30;
        }
        if (x >= GameConstants.GAME_WORLD_WIDTH - 80) {
            x = GameConstants.GAME_WORLD_WIDTH - 80;
        }
        if (y < 30) {
            y = 30;
        }
        if (y >= GameConstants.GAME_WORLD_HEIGHT - 80) {
            y = GameConstants.GAME_WORLD_HEIGHT - 80;
        }
    }

    public void centerScreen() {
        this.screenX = x - GameConstants.GAME_SCREEN_WIDTH / 4.0f;
        this.screenY = y - GameConstants.GAME_SCREEN_HEIGHT / 2.0f;
        if (screenX < 0) this.screenX = 0;
        if (screenY < 0) this.screenY = 0;
        if (screenX > GameConstants.GAME_WORLD_WIDTH - GameConstants.GAME_SCREEN_WIDTH / 2.0f) {
            screenX = GameConstants.GAME_WORLD_WIDTH - GameConstants.GAME_SCREEN_WIDTH / 2.0f;
        }
        if (screenY > GameConstants.GAME_WORLD_HEIGHT - GameConstants.GAME_SCREEN_HEIGHT + GameConstants.HUD_SCREEN_HEIGHT) {
            screenY = GameConstants.GAME_WORLD_HEIGHT - GameConstants.GAME_SCREEN_HEIGHT + GameConstants.HUD_SCREEN_HEIGHT;
        }
    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }

    @Override
    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
        g2d.setColor(Color.RED);
        centerScreen();
        this.ammo.forEach(b->b.drawImage(g2d));
        //cooldown timer for shots
        g2d.setColor(Color.GREEN);
        g2d.drawRect((int)x,(int)y-20,100,15);
        long currentWidth = 100-((this.timeSinceLastShot + this.coolDown) - System.currentTimeMillis())/10;
        if(currentWidth > 100) currentWidth = 100;
        g2d.fillRect((int)x,(int)y-20, (int) currentWidth,15);
    }

    @Override
    public void collides(GameObject with) {
        if(with instanceof Bullet){
            this.health--;
            System.out.println(this.health);
        }else if(with instanceof Wall wall){
            //stop
        }else if(with instanceof PowerUps pw){
            pw.applyPowerUp(this);
        }
    }

    public float getScreenX() {
        return screenX;
    }

    public float getScreenY() {
        return screenY;
    }

    public void addHealth() {
        if(!(this.health >= 10)){
            this.health++;
            System.out.println(health);
        }
    }
}
