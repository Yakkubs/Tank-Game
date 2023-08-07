package Utilities;

import Game.GameConstants;
import Game.Tank;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Hud {
    private Tank tank1;
    private Tank tank2;
    private Graphics2D g2d;

    public Hud(Graphics2D g2d,Tank tank1,Tank tank2) {
        this.tank1 = tank1;
        this.tank2 = tank2;
        this.g2d = g2d;
    }


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
    public void drawBulletCooldown(Tank tank,int pos,int y){
        g2d.setColor(Color.WHITE);
        g2d.drawRect(pos,y+25,200,5);
        g2d.setColor(Color.orange);
        long currentWidth = (long) (200-((tank.getCoolDown()) - System.currentTimeMillis())/2.5);
        if(currentWidth > 200) currentWidth = 200;
        g2d.fillRect(pos,y+25, (int) currentWidth,5);
    }
    //still needs work
    public void drawLifeBar(Tank tank, int pos, int y) {
        g2d.setColor(Color.WHITE);
        g2d.drawRect(pos, y + 25, 200, 15);
        g2d.setColor(Color.GREEN);
        float currentWidth = (tank.getHealth()/ tank.getHealthCap()) * 200;
        g2d.fillRect(pos, y + 25, (int) currentWidth, 15);
    }

    public void drawLifeCount(Tank tank,int pos,int y){
        //hard coded max value for life count since i dont have any power up that adds lives
        for (int i = 0; i < 5; i++) {
            g2d.setColor(Color.WHITE);
            g2d.drawOval(pos+(i*20),y,15,15);
        }
        for (int i = 0; i < tank.getLifeCount(); i++) {
            g2d.setColor(Color.RED);
            g2d.fillOval(pos+(i*20),y,15,15);
        }
    }

    public void drawStats(Tank tank, int pos){
        int yCord = GameConstants.GAME_SCREEN_HEIGHT-GameConstants.HUD_SCREEN_HEIGHT;
        g2d.drawString("Player " + tank.id,pos,yCord+40);
        drawBulletCooldown(tank,pos+125,yCord-5);
        drawLifeBar(tank,pos+125,yCord);
        drawLifeCount(tank,pos+125,yCord+40);
        g2d.setColor(Color.WHITE);
        g2d.drawString(String.format("%-20s : %10d", "LifeCount", tank.getLifeCount()), pos, yCord + 210);
        g2d.drawString(String.format("%-22s : %10d", "Health", tank.getHealth()), pos, yCord + 90);
        g2d.drawString(String.format("%-21s : %10d", "Armor", tank.getArmor()), pos, yCord + 120);
        g2d.drawString(String.format("%-22s : %10d", "Speed", (int) tank.getSpeed()), pos, yCord + 150);
        g2d.drawString(String.format("%-20s : %10d", "Damage", tank.getBulletDamage()), pos, yCord + 180);
    }
    public void createHud(){
        int borderWidth = 439;
        //creating all black outer rectangle for hud background
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, (int) (GameConstants.GAME_SCREEN_HEIGHT*(2/3.0)),GameConstants.HUD_SCREEN_WIDTH-15,GameConstants.HUD_SCREEN_HEIGHT-38);
        //creating border for hud
        g2d.setColor(Color.RED);
        g2d.drawRect(5, (int) (GameConstants.GAME_SCREEN_HEIGHT*(2/3.0))+5,borderWidth-10,GameConstants.HUD_SCREEN_HEIGHT-40-10);
        g2d.setColor(Color.BLUE);
        g2d.drawRect(5+GameConstants.GAME_SCREEN_WIDTH-456, (int) (GameConstants.GAME_SCREEN_HEIGHT*(2/3.0))+5,borderWidth-10,GameConstants.HUD_SCREEN_HEIGHT-40-10);
        Font font = new Font(Font.SERIF, Font.PLAIN,20);
        g2d.setFont(font);
        g2d.setColor(Color.WHITE);
        drawStats(tank1,40);
        drawStats(tank2,40+GameConstants.GAME_SCREEN_WIDTH-456);

    }
}
