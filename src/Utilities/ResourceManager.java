package Utilities;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

public class ResourceManager {
    private final static Map<String, BufferedImage> sprites = new HashMap<>();
    private final static HashMap<String, Clip> sounds = new HashMap<>();
    private final static HashMap<String, List<BufferedImage >> animations = new HashMap<>();
    private static final Map<String,Integer> animationInfo = new HashMap<>(){{
                put("bullethit",24);
                put("bulletshoot",24);
                put("powerpick",32);
                put("puffsmoke",32);
                put("rocketflame",16);
                put("rockethit",32);
    }};

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
    private static void initAnimations(){
        animationInfo.forEach((animationName,frameCount)-> {
            List<BufferedImage> frames = new ArrayList<>();
            String baseName = "Animations/%s/%s_%04d.png";
            try {
                for (int i = 0; i < frameCount; i++) {
                    String spritePath = baseName.formatted(animationName,animationName,i);
                    frames.add(loadSprite(spritePath));
                }
                ResourceManager.animations.put(animationName,frames);
            } catch (IOException e) {
                System.out.println(e);
                throw new RuntimeException(e);
            }
        });
    }
    public static List<BufferedImage> getAnimation(String type){
        return ResourceManager.animations.get(type);
    }

    private static void initSprites(){
        try {
            ResourceManager.sprites.put("bullet",loadSprite("bullet/bullet.jpg"));
            ResourceManager.sprites.put("rocket1",loadSprite("bullet/rocket1.png"));
            ResourceManager.sprites.put("rocket2",loadSprite("bullet/rocket2.png"));
            ResourceManager.sprites.put("floor",loadSprite("floor/bg.bmp"));
            ResourceManager.sprites.put("menu", loadSprite("menu/title.png"));
            ResourceManager.sprites.put("health",loadSprite("powerups/health.png"));
            ResourceManager.sprites.put("shield",loadSprite("powerups/shield.png"));
            ResourceManager.sprites.put("speed",loadSprite("powerups/speed.png"));
            ResourceManager.sprites.put("damage",loadSprite("powerups/damage.png"));
            ResourceManager.sprites.put("tank1",loadSprite("Sprites-yak/TanksMRed.png"));
            ResourceManager.sprites.put("tank2",loadSprite("Sprites-yak/TanksMBlue.png"));
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
        ResourceManager.initAnimations();
    }

    public static BufferedImage getSprite(String type) {
        if(!ResourceManager.sprites.containsKey(type)){
            throw new RuntimeException("%s is missing from sprite resources".formatted((type)));
        }
        return ResourceManager.sprites.get(type);
    }
}
