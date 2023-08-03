package Game;

import GameObjects.Bullet;
import GameObjects.GameObject;
import GameObjects.PowerUps;
import GameObjects.Wall;
import Utilities.Animation;
import Utilities.Hud;
import Utilities.ResourceManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author anthony-pc
 */
public class GameWorld extends JPanel implements Runnable {

    private BufferedImage world;
    private Tank t1;
    private Tank t2;
    private final Launcher lf;
    private long tick = 0;
    private boolean winner = false;
    List<GameObject> gobjs = new ArrayList<>(1000);
    List<Animation> anims = new ArrayList<>();

    /**
     *
     */
    public GameWorld(Launcher lf) {
        this.lf = lf;
    }


    @Override
    public void run() {
        try {
            this.resetGame();
            while (true) {
                this.tick++;
                this.t1.update(this); // update tank
                this.t2.update(this);
                this.anims.forEach(animation -> animation.update());
                this.checkCollision();
                this.gobjs.removeIf(GameObject::hasCollided);
                this.repaint();   // redraw game
                /*
                 * Sleep for 1000/144 ms (~6.9ms). This is done to have our 
                 * loop run at a fixed rate per/sec. 
                */
                if(this.tick % 720 == 0){
                    this.lf.setFrame("end");
                    return;
                }
                Thread.sleep(1000 / 144);
            }
        } catch (InterruptedException ignored) {
            System.out.println(ignored);
        }
    }

    private void checkCollision() {
        for (int i = 0; i < this.gobjs.size(); i++) {
            GameObject obj1 = this.gobjs.get(i);
            if(obj1 instanceof Wall || obj1 instanceof PowerUps) continue;
            for (int j = 0; j < this.gobjs.size(); j++) {
                if(i==j) continue;
                GameObject obj2 = this.gobjs.get(j);
                if(obj1.getHitBox().intersects(obj2.getHitBox())){
                    obj1.collides(obj2);
                }
            }
        }
    }

    /**
     * Reset game to its initial state.
     */
    public void resetGame() {
        this.tick = 0;
        this.t1.setX(300);
        this.t1.setY(300);
    }

    /**
     * Load all resources for Tank Wars Game. Set all Game Objects to their
     * initial state as well.
     */
    public void InitializeGame() {
        this.world = new BufferedImage(GameConstants.GAME_WORLD_WIDTH,
                GameConstants.GAME_WORLD_HEIGHT,
                BufferedImage.TYPE_INT_RGB);

        InputStreamReader isr = new InputStreamReader(ResourceManager.class.getClassLoader().getResourceAsStream("maps/map1.csv"));
//        this.anims.add(new Animation(300,300,ResourceManager.getAnimation("bullethit")));
//        this.anims.add(new Animation(350,300,ResourceManager.getAnimation("bulletshoot")));
//        this.anims.add(new Animation(400,300,ResourceManager.getAnimation("powerpick")));
//        this.anims.add(new Animation(450,300,ResourceManager.getAnimation("puffsmoke")));
//        this.anims.add(new Animation(500,300,ResourceManager.getAnimation("rocketflame")));
//        this.anims.add(new Animation(550,300,ResourceManager.getAnimation("rockethit")));
        try(BufferedReader mapReader = new BufferedReader(isr)) {
            int row = 0;
            String[] gameItems;
            while(mapReader.ready()){
                gameItems = mapReader.readLine().strip().split(",");
                for(int col = 0; col < gameItems.length;col++){
                    String gameObject = gameItems[col];
                    if("0".equals(gameObject)) continue;
                    this.gobjs.add(GameObject.newInstance(gameObject,col*30,row*30));
                }
                row++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        t1 = new Tank(300, 300, 0, 0, (short) 0, ResourceManager.getSprite("tank1"),1);
        TankControl tc1 = new TankControl(t1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
        this.lf.getJf().addKeyListener(tc1);
        t2 = new Tank(300, 300, 0, 0, (short) 0, ResourceManager.getSprite("tank2"),2);
        TankControl tc2 = new TankControl(t2, KeyEvent.VK_I, KeyEvent.VK_K, KeyEvent.VK_J, KeyEvent.VK_L, KeyEvent.VK_O);
        this.lf.getJf().addKeyListener(tc2);
        this.gobjs.add(t1);this.gobjs.add(t2);
    }
    public void renderFloor(Graphics g){
        for (int i = 0; i < GameConstants.GAME_WORLD_WIDTH; i+=320) {
            for (int j = 0; j < GameConstants.GAME_WORLD_HEIGHT; j+=240){
                g.drawImage(ResourceManager.getSprite("floor"),i,j,null);
            }
        }
    }
    public void renderMiniMap(Graphics2D g){
        double scaleFactor = 0.2;
        BufferedImage mm = this.world.getSubimage(0,0,
                GameConstants.GAME_WORLD_WIDTH,
                GameConstants.GAME_WORLD_HEIGHT);

        var mmX = GameConstants.GAME_SCREEN_WIDTH/2 - (GameConstants.GAME_WORLD_WIDTH*scaleFactor)/2;
        //var mmY = GameConstants.GAME_SCREEN_HEIGHT - (GameConstants.GAME_WORLD_HEIGHT*0.2);
        var mmY = GameConstants.GAME_SCREEN_HEIGHT - (GameConstants.GAME_WORLD_HEIGHT*scaleFactor);
        AffineTransform mmTransform = AffineTransform.getTranslateInstance(mmX-8,mmY-32);
        //0.005 is to account for the hud size compared to minimap scaling
        mmTransform.scale(scaleFactor,scaleFactor-0.005);
        g.drawImage(mm,mmTransform,null);
    }
    public void splitScreens(Graphics2D g2){
        BufferedImage lhs = world.getSubimage((int)this.t1.getScreenX(),(int)this.t1.getScreenY(),GameConstants.GAME_SCREEN_WIDTH/2,GameConstants.GAME_SCREEN_HEIGHT-GameConstants.HUD_SCREEN_HEIGHT);
        BufferedImage rhs = world.getSubimage((int)this.t2.getScreenX(),(int)this.t2.getScreenY(),GameConstants.GAME_SCREEN_WIDTH/2,GameConstants.GAME_SCREEN_HEIGHT-GameConstants.HUD_SCREEN_HEIGHT);
        g2.drawImage(lhs, 0, 0, null);
        g2.drawImage(rhs, GameConstants.GAME_SCREEN_WIDTH/2+1, 0, null);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Graphics2D buffer = world.createGraphics();
        renderFloor(buffer);
        this.gobjs.forEach(gameObject -> gameObject.drawImage(buffer));
        this.t1.drawImage(buffer);
        this.t2.drawImage(buffer);
        this.anims.forEach(animation -> animation.drawImage(buffer));
        splitScreens(g2);
        Hud p1Hud = new Hud();
        p1Hud.createHud(g2,t1,t2);

        renderMiniMap(g2);
    }
}
