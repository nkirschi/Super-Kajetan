package util;

import javax.sound.sampled.*;
import javax.sound.sampled.DataLine.Info;
import java.io.IOException;
import java.util.Random;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;
import static javax.sound.sampled.AudioSystem.getAudioInputStream;

public class AudioPlayer {
    private Random random;
    private static boolean playing;
    private static boolean paused;
    private float volume;

    public AudioPlayer() {
        random = new Random();
        volume = 0.0F; // Hier würde dann die Lautstärke aus den Einstellungen geladen werden!
    }

    public void playOnce(String filePath) {
        new Thread(() -> play(filePath)).start();
    }

    public void playLoop() {
        new Thread(() -> {
            playing = true;
            while (playing) {
                String filePath = "sounds/";
                switch (random.nextInt(6)) {
                    case 0:
                        filePath += "gott_mit_uns.ogg";
                        break;
                    case 1:
                        filePath += "no_bullets_fly.ogg";
                        break;
                    case 2:
                        filePath += "panzerkampf.ogg";
                        break;
                    case 3:
                        filePath += "resist_and_bite.ogg";
                        break;
                    case 4:
                        filePath += "shiroyama.ogg";
                        break;
                    case 5:
                        filePath += "the_last_stand.ogg";
                        break;
                }
                play(filePath);
            }
        }).start();
    }

    public void stop() {
        playing = false;
    }

    public void pause() {
        paused = true;
    }

    public void unpause() {
        paused = false;
    }

    private void play(String filePath) {
        try (final AudioInputStream in = getAudioInputStream(ClassLoader.getSystemResourceAsStream(filePath))) {

            final int ch = in.getFormat().getChannels();
            final float rate = in.getFormat().getSampleRate();
            final AudioFormat outFormat = new AudioFormat(PCM_SIGNED, rate, 16, ch, ch * 2, rate, false);

            final Info info = new Info(SourceDataLine.class, outFormat);

            try (final SourceDataLine line =
                         (SourceDataLine) AudioSystem.getLine(info)) {

                if (line != null) {
                    line.open(outFormat);
                    try {
                        FloatControl volumeControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
                        volumeControl.setValue(volume);
                    } catch (Exception e) {
                        try {
                            FloatControl volumeControl = (FloatControl) line.getControl(FloatControl.Type.VOLUME);
                            volumeControl.setValue(volume);
                        } catch (Exception f) {
                            e.printStackTrace();
                            Logger.log("Volume Control wird auf dem System nicht unterstützt", Logger.WARNING);
                            Logger.log(e, Logger.WARNING);
                        }
                    }
                    line.start();
                    stream(getAudioInputStream(outFormat, in), line);
                    line.drain();
                    line.stop();
                }
            }

        } catch (UnsupportedAudioFileException
                | LineUnavailableException
                | IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void stream(AudioInputStream in, SourceDataLine line) throws IOException {
        final byte[] buffer = new byte[4096]; // original 65536
        for (int n = 0; n != -1; n = in.read(buffer, 0, buffer.length)) {
            if (!playing)
                break;
            while (paused) {
                Thread.yield();
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            line.write(buffer, 0, n);
        }
    }
}