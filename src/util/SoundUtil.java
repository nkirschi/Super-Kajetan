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

    public static void init() {
        try {
            addLibrary(LibraryJavaSound.class);
            setCodec("ogg", CodecJOrbis.class);
        } catch (SoundSystemException e) {
            e.printStackTrace();
        }
        soundSystem = new SoundSystem();

        //Quellen für jeden Sound
        soundSystem.newSource(false, "buttonclick", ClassLoader.getSystemResource("sounds/buttonclick.ogg"),
                "buttonclick.ogg", false, 0F, 0F, 0F, SoundSystemConfig.ATTENUATION_NONE, 0F);
        soundSystem.newSource(true, "sword_attack", ClassLoader.getSystemResource("sounds/sword_attack.ogg"),
                "sword_attack.ogg", false, 0F, 0F, 0F, SoundSystemConfig.ATTENUATION_NONE, 0F);
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
    public static void playEffect(String sourcename) {
        if (soundSystem.playing(sourcename))
            soundSystem.stop(sourcename);
        soundSystem.cull(sourcename);
        soundSystem.activate(sourcename);
        soundSystem.setVolume(sourcename, SettingsView.getInstance().getEffectVolume());
        soundSystem.play(sourcename);
    }
}
