package Game;

import GameObjects.*;
import Utilities.Animation;
import Utilities.Hud;
import Utilities.ResourceManager;
import Utilities.Sound;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLOutput;
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
    private Sound bg = ResourceManager.getSound("bg");
    List<GameObject> gobjs = new ArrayList<>(1000);
    private List<Animation> anims = new ArrayList<>();
    private boolean hasWon = false;


    /**
     *
     */
    public GameWorld(Launcher lf) {
        this.lf = lf;
    }


    @Override
    public void run() {
        this.resetGame();
        try {
            bg.setVolume(.2f);
            bg.setLooping();
            bg.playSound();
            while (true) {
                this.tick++;
                this.t1.update(); // update tank
                this.t2.update();
                this.anims.forEach(animation -> animation.update());
                this.checkCollision();
                this.checkDestroyedWalls();
                this.gobjs.removeIf(GameObject::hasCollided);
                this.repaint();   // redraw game
                /*
                 * Sleep for 1000/144 ms (~6.9ms). This is done to have our 
                 * loop run at a fixed rate per/sec. 
                */

                if (t2.getLifeCount() <= 0 ) {
                    bg.stop();
                    this.lf.setFrame("winner1");
                    return;
                } else if (t1.getLifeCount() <= 0) {
                    bg.stop();
                    this.lf.setFrame("winner2");
                    return;
                }
                Thread.sleep(1000 / 144);
            }
        } catch (InterruptedException ignored) {
            System.out.println(ignored);
        }
    }
    public void checkDestroyedWalls() {
        //finding all destroyed RandomDrop walls and adding them to a list
        List<RandomDrop> destroyedWalls = new ArrayList<>();
        for (GameObject obj : gobjs) {
            if (obj instanceof RandomDrop r && obj.hasCollided()) {
                destroyedWalls.add((RandomDrop) obj);
            }
        }
        //iterating through the list to remove it from gobjs
        for (RandomDrop wall : destroyedWalls) {
            gobjs.remove(wall);
            //random value from 4-7 that has a %20 chance to not be 0
            double randomPowerUp = Math.random();
            if (randomPowerUp < 0.2) {
                randomPowerUp = (int) (Math.random() * 4) + 4;
            }else{
                randomPowerUp = (int)0;
            }
            //if value is not 0, spawn powerup at that location
            if(randomPowerUp!=0){
                GameObject randomPowerUp2 = GameObject.newInstance(String.valueOf((int)randomPowerUp), wall.getX(), wall.getY());
                System.out.println("added new object");
                gobjs.add(randomPowerUp2);
            }
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
        //clearing the gobjs list
        this.gobjs.clear();
        //reinitialize the map and tanks
        initializeTanks();
        this.t1.resetTank();
        this.t2.resetTank();
        this.createMap();
    }

    /**
     * Load all resources for Tank Wars Game. Set all Game Objects to their
     * initial state as well.
     */
    public void createMap(){
        //reading excel file to populate gobjs, which makes map, with gameobjects base on switch case value
        InputStreamReader isr = new InputStreamReader(ResourceManager.class.getClassLoader().getResourceAsStream("maps/map1.csv"));
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
    }
    public void initializeTanks(){
        //hard coding tanks
        t1 = new Tank(100, 100, 0, 0, (short) 0, ResourceManager.getSprite("tank1"),1,this);
        TankControl tc1 = new TankControl(t1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
        this.lf.getJf().addKeyListener(tc1);
        t2 = new Tank(GameConstants.GAME_WORLD_WIDTH-140, GameConstants.GAME_WORLD_HEIGHT-140, 0, 0, (short) 180, ResourceManager.getSprite("tank2"),2,this);
        TankControl tc2 = new TankControl(t2, KeyEvent.VK_I, KeyEvent.VK_K, KeyEvent.VK_J, KeyEvent.VK_L, KeyEvent.VK_O);
        this.lf.getJf().addKeyListener(tc2);
        this.gobjs.add(t1);this.gobjs.add(t2);
    }
    public void InitializeGame() {
        this.world = new BufferedImage(GameConstants.GAME_WORLD_WIDTH,
                GameConstants.GAME_WORLD_HEIGHT,
                BufferedImage.TYPE_INT_RGB);

        createMap();
        initializeTanks();
    }
    public void addToAnims(Animation a){
        anims.add(a);
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
        Hud hud = new Hud(g2,t1,t2);
        hud.createHud();
        renderMiniMap(g2);
    }
}
