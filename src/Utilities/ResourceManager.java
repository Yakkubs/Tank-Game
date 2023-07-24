package Utilities;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ResourceManager {
    private final static Map<String, BufferedImage> sprites = new HashMap<>();
    private final static HashMap<String, List<BufferedImage >> animations = new HashMap<>();
    private final static HashMap<String, Clip> sounds = new HashMap<>();

    private static BufferedImage loadSprite(String path) throws IOException {
        return ImageIO.read(Objects.requireNonNull(ResourceManager.class.getClassLoader().getResource(path)));
    }
    private static Clip loadSound(String path) throws IOException {
        try {
            AudioInputStream is = AudioSystem.getAudioInputStream(Objects.requireNonNull(ResourceManager.class.getClassLoader().getResource(path)));
            return AudioSystem.getClip();
        } catch (UnsupportedAudioFileException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    private static void initSprites(){
        try {
            ResourceManager.sprites.put("bullet",loadSprite("bullet/bullet.jpg"));
            ResourceManager.sprites.put("rocket1",loadSprite("bullet/rocket1.png"));
            ResourceManager.sprites.put("rocket2",loadSprite("bullet/rocket2.png"));
            ResourceManager.sprites.put("background",loadSprite("floor/bg.bmp"));
            ResourceManager.sprites.put("menu", loadSprite("menu/title.png"));
            ResourceManager.sprites.put("health",loadSprite("powerups/health.png"));
            ResourceManager.sprites.put("shield",loadSprite("powerups/shield.png"));
            ResourceManager.sprites.put("speed",loadSprite("powerups/speed.png"));
            ResourceManager.sprites.put("tank1",loadSprite("tank/tank1.png"));
            ResourceManager.sprites.put("tank2",loadSprite("tank/tank2.png"));
            ResourceManager.sprites.put("break1",loadSprite("walls/break1.jpg"));
            ResourceManager.sprites.put("break2",loadSprite("walls/break2.jpg"));
            ResourceManager.sprites.put("unbreak",loadSprite("walls/unbreak.jpg"));



            //skipping past some sounds for the time being
            ResourceManager.sounds.put("bullet",loadSound("Sounds/bullet.wav"));
            //doesnt work for mp3 file types yet
            //ResourceManager.sounds.put("Music",loadSound("Sounds/Music.mp3"));

            //skipping past animations for the time being


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void loadResources(){
        ResourceManager.initSprites();
    }

    public static BufferedImage getSprite(String type) {
        if(!ResourceManager.sprites.containsKey(type)){
            throw new RuntimeException("%s is missing from sprite resources".formatted((type)));
        }
        return ResourceManager.sprites.get(type);
    }

    public static void main(String[] args) {
        ResourceManager.initSprites();
    }
}
