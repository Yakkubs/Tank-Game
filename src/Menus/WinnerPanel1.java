package Menus;

import Game.GameConstants;
import Game.Launcher;
import Utilities.ResourceManager;

import javax.swing.*;
import java.awt.*;

public class WinnerPanel1 extends JPanel {
    private final Launcher lf;
    public WinnerPanel1(Launcher lf) {
        this.lf = lf;
        this.setBackground(Color.BLACK);
        this.setLayout(null);

        JButton exit = new JButton("Continue");
        exit.setFont(new Font("Courier New", Font.PLAIN, 12));
        exit.setBounds(10, GameConstants.START_MENU_SCREEN_HEIGHT-200, 100, 100);
        exit.addActionListener((actionEvent -> this.lf.setFrame("end")));
        add(exit);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(ResourceManager.getSprite("winner1"),0,0,null);
    }
}
