package Menus;

import Game.Launcher;
import Utilities.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class KeyBindsPanel extends JPanel {
    BufferedImage keyBindMenu;
    Launcher lf;
    public KeyBindsPanel(Launcher lf){
        this.lf = lf;
        keyBindMenu = ResourceManager.getSprite("menu");
        this.setBackground(Color.BLACK);
        this.setLayout(null);

    }

}
