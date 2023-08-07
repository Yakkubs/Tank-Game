package Game;

import GameObjects.*;
import Utilities.Animation;
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
    public int id;
    private List<Bullet> ammo = new ArrayList<Bullet>();
    long timeSinceLastShot = 0L;
    long coolDown = 500;
    private int lifeCount;
    private int health;
    private int armor;
    private int bulletDamage;
    private float R = 2.5f;
    private float ROTATIONSPEED = 2.5f;
    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean ShootPressed;
    private float healthCap = 10;
    private final float initialAngle;
    private boolean lostLife = false;
    private GameWorld gw;

    Tank(float x, float y, float vx, float vy, float angle, BufferedImage img, int id,GameWorld gw) {
        super(x,y,img);
        this.vx = vx;
        this.vy = vy;
        this.angle = angle;
        this.initialAngle = this.angle;
        this.health = 10;
        this.armor = 0;
        this.id = id;
        this.lifeCount = 5;
        this.bulletDamage = 2;
        this.gw = gw;
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
        if(this.lostLife){
            respawnTank(gw);
            this.lostLife = false;
        }
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
            //safe shoot variables x and y so that bullet shoots outside the tank
            double cx = 35 * Math.cos(Math.toRadians(this.angle));
            double cy = 35 * Math.sin(Math.toRadians(this.angle));
            int bulletX = (int) (x+this.img.getWidth()/2f + cx -4f);
            int bulletY = (int) (y+this.img.getWidth()/2f + cy -4f);
            //pass value into bullets x and y position
            this.timeSinceLastShot = System.currentTimeMillis();
            Bullet b = new Bullet(bulletX,bulletY, ResourceManager.getSprite("bullet"),angle,id,bulletDamage,this.gw);
            this.ammo.add(b);
            gw.addToAnims(new Animation(bulletX-40,bulletY-20,ResourceManager.getAnimation("bulletshoot")));
            gw.gobjs.add(b);
            ResourceManager.getSound("bullet_shoot").playSound();
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
    }

    @Override
    public void collides(GameObject with) {
        if(with instanceof Bullet b && id != ((Bullet) with).tankID){
            float dmg = b.bulletDamage - armor;
            if(dmg <= 0) {
                dmg = 1;
            }
            addHealth(-dmg);
            this.ammo.remove(b);
        }else if(with instanceof Wall || with instanceof Tank){
            if(UpPressed){
                y = y - vy;
                x = x - vx;
            }else if(DownPressed){
                y = y + vy;
                x = x + vx;
            }
        }else if(with instanceof PowerUps pw){
            if(pw.applyPowerUp(this)){
                //for some reason sub x with height and y with width centers animation location better the swapping the sub
                Animation temp = new Animation(x-hitBox.height,y-hitBox.width,ResourceManager.getAnimation("powerpick"));
                temp.setDelay(10);
                gw.addToAnims(temp);
                ResourceManager.getSound("pickup").playSound();
            }
        }
    }

    public float getScreenX() {
        return screenX;
    }

    public float getScreenY() {
        return screenY;
    }

    public int getLifeCount(){
        return lifeCount;
    }

    public void addHealth(float val) {
        //checking for to see if health + added value does not go over 20
        if(!(this.health+val >= 20)){
            //setting health cap value to new val for hud
            if(val > 0 ) this.healthCap += val;
            //adding health
            this.health += val;
        }else{
            //capping heal at 20
            this.health = 20;
            this.healthCap = 20;
        }
        if(this.health <= 0){
            //subtracting life and resetting health
            this.lifeCount--;
            this.lostLife = true;
            this.health = 10;
            this.healthCap  = 10;
        }
    }
    public int getHealth(){
        return this.health;
    }

    public void setArmor() {
        this.armor++;
    }
    public int getArmor(){
        return this.armor;
    }
    public void setSpeed() {
        if(!(this.R >= 5)) {
            this.R++;
        }
    }
    public float getSpeed(){
        return this.R;
    }
    public void addDamage(){
        this.bulletDamage++;
    }
    public int getBulletDamage(){
        return this.bulletDamage;
    }
    public long getCoolDown(){
        return this.timeSinceLastShot + this.coolDown;
    }
    public float getHealthCap(){
        return this.healthCap;
    }
    public void resetTank(){
        this.health = 10;
        this.armor = 0;
        this.lifeCount = 5;
        this.bulletDamage = 2;
        this.healthCap = 10;
        this.angle = this.initialAngle;
        this.R = 2.5f;
    }
    private void respawnTank(GameWorld gw) {
        //setting a minimum respawn distance so tanks dont respawn in-front of each other
        float MIN_RESPAWN_DISTANCE = 100.0f;
        float respawnX, respawnY;
        do {
            // Calculate a random respawn position away from enemy tank
            respawnX = (float) (Math.random() * GameConstants.GAME_WORLD_WIDTH);
            respawnY = (float) (Math.random() * GameConstants.GAME_WORLD_HEIGHT-40); //-40 to account for border of frame
            // Check the distance from enemy tank
            float distance = (float) Math.sqrt(Math.pow(respawnX - x, 2) + Math.pow(respawnY - y, 2));
            if (distance < MIN_RESPAWN_DISTANCE) {
                // If the respawn position is too close, adjust it
                float angle = (float) (Math.random() * 360); // Random angle in degrees
                float offsetX = MIN_RESPAWN_DISTANCE * (float) Math.cos(Math.toRadians(angle));
                float offsetY = MIN_RESPAWN_DISTANCE * (float) Math.sin(Math.toRadians(angle));
                respawnX = x + offsetX;
                respawnY = y + offsetY;
            }
        } while (!isValidRespawnPosition(respawnX, respawnY, gw));
        // Set the new position for the tank
        setX(respawnX);
        setY(respawnY);
        // Reset tank
        //if you want to reset tank stats on death uncomment this out, make sure that lifeCount decreases though
        //by getting rid of that line in the resetTank function
        //resetTank();
    }
    private boolean isValidRespawnPosition(float x, float y, GameWorld gw) {
        //creating hitbox for respawn position
        Rectangle respawnRect = new Rectangle((int) x, (int) y, (int) this.hitBox.getWidth(), (int) this.hitBox.getHeight());
        //making sure that respawn location doesn't intersect with any already drawn tile
        for (GameObject obj : gw.gobjs) {
            if (respawnRect.intersects(obj.getHitBox())) {
                return false; // Invalid respawn position due to collision
            }
        }
        return true; // Valid respawn position
    }
}
