package util;

import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.codecs.CodecJOrbis;
import paulscode.sound.libraries.LibraryJavaSound;

import java.util.concurrent.ThreadLocalRandom;

public class SoundUtil {

    public static SoundSystem soundSystem;

    public static void init() {
        try {
            SoundSystemConfig.addLibrary(LibraryJavaSound.class);
            SoundSystemConfig.setCodec("ogg", CodecJOrbis.class);
        } catch (SoundSystemException e) {
            e.printStackTrace();
        }
        soundSystem = new SoundSystem();
    }

    public static void playRandomBackgroundMusic() {
        String f = "sounds/";
        String s = "";
        switch (ThreadLocalRandom.current().nextInt(1, 7)) {
            case 0:
                s = "gott_mit_uns.ogg";
                break;
            case 1:
                s = "no_bullets_fly.ogg";
                break;
            case 2:
                s = "panzerkampf.ogg";
                break;
            case 3:
                s = "resist_and_bite.ogg";
                break;
            case 4:
                s = "shiroyama.ogg";
                break;
            case 5:
                s = "the_last_stand.ogg";
                break;
        }
        soundSystem.backgroundMusic("background",
                ClassLoader.getSystemResource(f + s), s, true);
    }
}
