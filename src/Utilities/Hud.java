package Utilities;

import Game.GameConstants;
import Game.Tank;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Hud {
    private Tank tank1;
    private Tank tank2;
    private float scaleFactor = .90F;
    private int borderWidth = 439;

//    public Hud(Graphics2D g2d, BufferedImage world,int x,Color color){
//        g2d.setColor(Color.BLACK);
//        g2d.fillRect(x, (int) (GameConstants.GAME_SCREEN_HEIGHT*(2/3.0)),447,GameConstants.HUD_SCREEN_HEIGHT-40);
//        g2d.setColor(color);
//        g2d.drawRect(x, (int) (GameConstants.GAME_SCREEN_HEIGHT*(2/3.0)),447,GameConstants.HUD_SCREEN_HEIGHT-40);
//        g2d.drawRect(100,GameConstants.GAME_SCREEN_HEIGHT-GameConstants.HUD_SCREEN_HEIGHT+100, (int) 100,15);
//        g2d.fillRect(100,GameConstants.GAME_SCREEN_HEIGHT-GameConstants.HUD_SCREEN_HEIGHT+100, (int) 100,15);
////        double scaleFactor = 0.2;
////        BufferedImage mm = world.getSubimage(0,0,
////                GameConstants.GAME_WORLD_WIDTH,
////                GameConstants.GAME_WORLD_HEIGHT);
////        var mmX = GameConstants.GAME_SCREEN_WIDTH/2 - (GameConstants.GAME_WORLD_WIDTH*scaleFactor)/2;
////        //var mmY = GameConstants.GAME_SCREEN_HEIGHT - (GameConstants.GAME_WORLD_HEIGHT*0.2);
////        var mmY = GameConstants.GAME_SCREEN_HEIGHT - (GameConstants.GAME_WORLD_HEIGHT*scaleFactor);
////        AffineTransform mmTransform = AffineTransform.getTranslateInstance(mmX,mmY-32);
////        //0.005 is to account for the hud size compared to minimap scaling
////        mmTransform.scale(scaleFactor,scaleFactor-0.005);
////        g.drawImage(mm,mmTransform,null);
//
//    }
    public void drawStats(Graphics g2d,Tank t1,int pos){
        g2d.drawString("LifeCount: "+ t1.getLifeCount(),pos,GameConstants.GAME_SCREEN_HEIGHT-GameConstants.HUD_SCREEN_HEIGHT+30);
        g2d.drawString("Health: "   + t1.getHealth(),pos,GameConstants.GAME_SCREEN_HEIGHT-GameConstants.HUD_SCREEN_HEIGHT+60);
        g2d.drawString("Armor: "    + t1.getArmor(),pos,GameConstants.GAME_SCREEN_HEIGHT-GameConstants.HUD_SCREEN_HEIGHT+90);
        g2d.drawString("Speed "     + t1.getSpeed(),pos,GameConstants.GAME_SCREEN_HEIGHT-GameConstants.HUD_SCREEN_HEIGHT+120);
        g2d.drawString("Damage "    + t1.getBulletDamage(),pos,GameConstants.GAME_SCREEN_HEIGHT-GameConstants.HUD_SCREEN_HEIGHT+150);
    }
    public void createHud(Graphics g2d,Tank tank1, Tank tank2){
        //creating all black outer rectangle for hud background
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, (int) (GameConstants.GAME_SCREEN_HEIGHT*(2/3.0)),GameConstants.HUD_SCREEN_WIDTH-15,GameConstants.HUD_SCREEN_HEIGHT-40);
        //creating border for hud
        g2d.setColor(Color.RED);
        g2d.drawRect(0, (int) (GameConstants.GAME_SCREEN_HEIGHT*(2/3.0)),borderWidth,GameConstants.HUD_SCREEN_HEIGHT-40);
        g2d.setColor(Color.BLUE);
        g2d.drawRect(GameConstants.GAME_SCREEN_WIDTH-456, (int) (GameConstants.GAME_SCREEN_HEIGHT*(2/3.0)),borderWidth,GameConstants.HUD_SCREEN_HEIGHT-40);
        //bullet cool down
//        g2d.drawRect(100,GameConstants.GAME_SCREEN_HEIGHT-GameConstants.HUD_SCREEN_HEIGHT+100,100,15);
//        g2d.setColor(Color.GREEN);
//        long currentWidth = 100-((tank1.timeSinceLastShot + tank1.coolDown) - System.currentTimeMillis())/10;
//        if(currentWidth > 100) currentWidth = 100;
//        g2d.fillRect(100,GameConstants.GAME_SCREEN_HEIGHT-GameConstants.HUD_SCREEN_HEIGHT+100, (int) currentWidth,15);
        Font font = new Font(Font.SERIF, Font.BOLD,30);
        g2d.setFont(font);
        g2d.setColor(Color.WHITE);
        //g2d.drawString("Health:",10,GameConstants.GAME_SCREEN_HEIGHT-GameConstants.HUD_SCREEN_HEIGHT+30);
        drawStats(g2d,tank1,10);
        drawStats(g2d,tank2,10+GameConstants.GAME_SCREEN_WIDTH-456);

    }
}
