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

    public static final String MUSIC_SOURCE = "music";

    public static void init() {
        SoundSystemConfig.setLogger(new NoLogger());
        try {
            addLibrary(LibraryJavaSound.class);
            setCodec("ogg", CodecJOrbis.class);
        } catch (SoundSystemException e) {
            e.printStackTrace();
        }
        soundSystem = new SoundSystem();

        //Quellen für jeden Sound
        register("buttonclick", "sounds/buttonclick.ogg", "buttonclick.ogg");
        register("sword_attack", "sounds/sword_attack.ogg", "sword_attack.ogg");
        register("hit", "sounds/hit.ogg", "hit.ogg");
        register("coin", "sounds/coin.ogg", "coin.ogg");
        register("death", "sounds/death.ogg", "death.ogg");
        register("victory", "sounds/victory.ogg", "victory.ogg");
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

    private static void register(String sourcename, String soundPath, String identifier) {
        soundSystem.newSource(false, sourcename, ClassLoader.getSystemResource(soundPath),
                identifier, false, 0F, 0F, 0F, SoundSystemConfig.ATTENUATION_NONE, 0F);
    }
}
