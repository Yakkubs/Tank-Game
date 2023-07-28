package Game;

import GameObjects.GameObject;
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
    private final Launcher lf;
    private long tick = 0;
    List<GameObject> gobjs = new ArrayList<>(1000);

    /**
     *
     */
    public GameWorld(Launcher lf) {
        this.lf = lf;
    }

    @Override
    public void run() {
        try {
            while (true) {
                this.tick++;
                this.t1.update(); // update tank
                this.repaint();   // redraw game
                /*
                 * Sleep for 1000/144 ms (~6.9ms). This is done to have our 
                 * loop run at a fixed rate per/sec. 
                */
                Thread.sleep(1000 / 144);
            }
        } catch (InterruptedException ignored) {
            System.out.println(ignored);
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

        t1 = new Tank(300, 300, 0, 0, (short) 0, ResourceManager.getSprite("tank1"));
        TankControl tc1 = new TankControl(t1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
        this.lf.getJf().addKeyListener(tc1);
    }
    public void renderFloor(Graphics g){
        for (int i = 0; i < GameConstants.GAME_SCREEN_WIDTH; i+=320) {
            for (int j = 0; j < GameConstants.GAME_SCREEN_HEIGHT; j+=240){
                g.drawImage(ResourceManager.getSprite("floor"),i,j,null);
            }
        }
    }
    public void renderMiniMap(Graphics2D g){
        BufferedImage mm = this.world.getSubimage(0,0,
                GameConstants.GAME_WORLD_WIDTH,
                GameConstants.GAME_WORLD_HEIGHT);
        var mmX = GameConstants.GAME_SCREEN_WIDTH/2 - (GameConstants.GAME_WORLD_WIDTH*0.2)/2;
        var mmY = GameConstants.GAME_SCREEN_HEIGHT - (GameConstants.GAME_WORLD_HEIGHT*0.2);
        AffineTransform mmTransform = AffineTransform.getTranslateInstance(mmX,mmY-40);
        mmTransform.scale(0.2,0.2);
        g.drawImage(mm,mmTransform,null);

    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Graphics2D buffer = world.createGraphics();
        renderFloor(buffer);
        //buffer.drawImage(ResourceManager.getSprite("floor"),0,0,GameConstants.GAME_SCREEN_WIDTH,GameConstants.GAME_SCREEN_HEIGHT,null);
        this.gobjs.forEach(gameObject -> gameObject.drawImage(buffer));
        this.t1.drawImage(buffer);
        g2.drawImage(world, 0, 0, null);
        renderMiniMap(g2);
    }
}
