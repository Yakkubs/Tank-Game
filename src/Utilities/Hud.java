package Utilities;

import Game.GameConstants;
import Game.Tank;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Hud {
    private Tank tank1;
    private Tank tank2;

    public Hud(Graphics2D g2d, BufferedImage world){
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, GameConstants.GAME_SCREEN_HEIGHT-(GameConstants.GAME_SCREEN_HEIGHT/3),GameConstants.HUD_SCREEN_WIDTH,GameConstants.HUD_SCREEN_HEIGHT);
        g2d.setColor(Color.GREEN);
        g2d.drawRect(100,GameConstants.GAME_SCREEN_HEIGHT-GameConstants.HUD_SCREEN_HEIGHT+100, (int) 100,15);
        g2d.fillRect(100,GameConstants.GAME_SCREEN_HEIGHT-GameConstants.HUD_SCREEN_HEIGHT+100, (int) 100,15);
//        double scaleFactor = 0.2;
//        BufferedImage mm = world.getSubimage(0,0,
//                GameConstants.GAME_WORLD_WIDTH,
//                GameConstants.GAME_WORLD_HEIGHT);
//        var mmX = GameConstants.GAME_SCREEN_WIDTH/2 - (GameConstants.GAME_WORLD_WIDTH*scaleFactor)/2;
//        //var mmY = GameConstants.GAME_SCREEN_HEIGHT - (GameConstants.GAME_WORLD_HEIGHT*0.2);
//        var mmY = GameConstants.GAME_SCREEN_HEIGHT - (GameConstants.GAME_WORLD_HEIGHT*scaleFactor);
//        AffineTransform mmTransform = AffineTransform.getTranslateInstance(mmX,mmY-32);
//        //0.005 is to account for the hud size compared to minimap scaling
//        mmTransform.scale(scaleFactor,scaleFactor-0.005);
//        g.drawImage(mm,mmTransform,null);
    }
}
