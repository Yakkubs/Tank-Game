package Utilities;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

public class ResourceManager {
    private final static Map<String, BufferedImage> sprites = new HashMap<>();
    private final static HashMap<String, Sound> sound = new HashMap<>();
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
    private static Sound loadSound(String path) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        AudioInputStream ais = AudioSystem.getAudioInputStream(Objects.requireNonNull(ResourceManager.class.getClassLoader().getResource(path)));
        Clip c = AudioSystem.getClip();
        c.open(ais);
        Sound s = new Sound(c);
        s.setVolume(.2f);
        return s;
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
        if(!ResourceManager.animations.containsKey(type)){
            throw new RuntimeException("%s resource is missing.".formatted(type));
        }
        return ResourceManager.animations.get(type);
    }
    public static Sound getSound(String type){
        if(!ResourceManager.sound.containsKey(type)){
            throw new RuntimeException("%s resource is missing.".formatted(type));
        }
        return ResourceManager.sound.get(type);
    }
    private static void initSprites(){
        try {
            ResourceManager.sprites.put("bullet",loadSprite("bullet/bullet.jpg"));
            ResourceManager.sprites.put("rocket1",loadSprite("bullet/rocket1.png"));
            ResourceManager.sprites.put("rocket2",loadSprite("bullet/rocket2.png"));
            ResourceManager.sprites.put("floor",loadSprite("floor/bg.bmp"));
            ResourceManager.sprites.put("menu", loadSprite("menu/title.png"));
            ResourceManager.sprites.put("winner1", loadSprite("menu/Winner1.png"));
            ResourceManager.sprites.put("winner2", loadSprite("menu/Winner2.png"));
            ResourceManager.sprites.put("health",loadSprite("powerups/health.png"));
            ResourceManager.sprites.put("shield",loadSprite("powerups/shield.png"));
            ResourceManager.sprites.put("speed",loadSprite("powerups/speed.png"));
            ResourceManager.sprites.put("damage",loadSprite("powerups/damage.png"));
            ResourceManager.sprites.put("tank1",loadSprite("SpritesYak/TanksMRed.png"));
            ResourceManager.sprites.put("tank2",loadSprite("SpritesYak/TanksMBlue.png"));
            ResourceManager.sprites.put("break1",loadSprite("walls/Castle_Wall.jpg"));
            ResourceManager.sprites.put("break2",loadSprite("walls/Moon_Rock.jpg"));
            ResourceManager.sprites.put("unbreak",loadSprite("walls/Black_Brick.jpg"));
            ResourceManager.sprites.put("random",loadSprite("walls/Random_Block.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void initSounds(){
        try {
            ResourceManager.sound.put("bullet_shoot", loadSound("Sounds/bullet_shoot.wav"));
            ResourceManager.sound.put("explosion", loadSound("Sounds/explosion.wav"));
            ResourceManager.sound.put("bg", loadSound("Sounds/Music.mid"));
            ResourceManager.sound.put("pickup", loadSound("Sounds/pickup.wav"));
            ResourceManager.sound.put("shotfire", loadSound("Sounds/shotfiring.wav"));
            ResourceManager.sound.put("shotexplosion", loadSound("Sounds/shotexplosion.wav"));
        }catch(Exception e){
            System.out.println(e);
        }
    }
    public static void loadResources(){
        ResourceManager.initSprites();
        ResourceManager.initAnimations();
        ResourceManager.initSounds();
    }

    public static BufferedImage getSprite(String type) {
        if(!ResourceManager.sprites.containsKey(type)){
            throw new RuntimeException("%s is missing from sprite resources".formatted((type)));
        }
        return ResourceManager.sprites.get(type);
    }
}
