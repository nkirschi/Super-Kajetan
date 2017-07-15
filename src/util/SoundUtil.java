package util;

import gui.SettingsView;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.codecs.CodecJOrbis;
import paulscode.sound.libraries.LibraryJavaSound;

import java.util.concurrent.ThreadLocalRandom;

import static paulscode.sound.SoundSystemConfig.addLibrary;
import static paulscode.sound.SoundSystemConfig.setCodec;

public class SoundUtil {

    public static SoundSystem soundSystem;

    public static String MUSIC_SOURCE = "music";
    public static String EFFECT_SOURCE = "effect";

    public static void init() {
        try {
            addLibrary(LibraryJavaSound.class);
            setCodec("ogg", CodecJOrbis.class);
        } catch (SoundSystemException e) {
            e.printStackTrace();
        }
        soundSystem = new SoundSystem();
        Logger.log("Soundsystem initialisiert", Logger.INFO);
    }

    public static void playRandomBackgroundMusic() {
        String f = "sounds/background/";
        String s = "";
        switch (ThreadLocalRandom.current().nextInt(1, 7)) {
            case 1:
                s = "gott_mit_uns.ogg";
                break;
            case 2:
                s = "no_bullets_fly.ogg";
                break;
            case 3:
                s = "panzerkampf.ogg";
                break;
            case 4:
                s = "resist_and_bite.ogg";
                break;
            case 5:
                s = "shiroyama.ogg";
                break;
            case 6:
                s = "the_last_stand.ogg";
                break;
        }
        soundSystem.backgroundMusic(MUSIC_SOURCE,
                ClassLoader.getSystemResource(f + s), s, true);
        soundSystem.setVolume(MUSIC_SOURCE, SettingsView.getInstance().getMusicVolume());

    }

    /*
    * Methode, um einzelne, kurze (!) Soundeffekte abzuspielen.
    * Werden Soundeffekte zu schnell nacheinander abgespielt, werden manche Sounds ausgelassen.
    * Evtl sollte man dieses System nochmal überdenken ...
    * */
    public static void playEffect(String filepath, String identifier) {
        soundSystem.newSource(true, EFFECT_SOURCE, ClassLoader.getSystemResource(filepath),
                identifier, false, 0F, 0F, 0F, SoundSystemConfig.ATTENUATION_NONE,
                0F);
        soundSystem.setVolume(EFFECT_SOURCE, SettingsView.getInstance().getEffectVolume());
        soundSystem.play(EFFECT_SOURCE);
    }
}
