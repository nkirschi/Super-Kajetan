package util;

import gui.SettingsView;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.codecs.CodecJOrbis;
import paulscode.sound.libraries.LibraryJavaSound;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SoundUtil {
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private boolean paused = false;
    private boolean playing = true;
    private Random random = new Random();

    public static SoundSystem soundSystem = new SoundSystem();

    private void play(String filename) {
        playing = true;
        try {
            // Get AudioInputStream from given file.
            AudioInputStream in = AudioSystem.getAudioInputStream(ClassLoader.getSystemResourceAsStream(filename));
            AudioInputStream din = null;
            if (in != null) {
                AudioFormat baseFormat = in.getFormat();
                AudioFormat decodedFormat = new AudioFormat(
                        AudioFormat.Encoding.PCM_SIGNED,
                        baseFormat.getSampleRate(),
                        16,
                        baseFormat.getChannels(),
                        baseFormat.getChannels() * 2,
                        baseFormat.getSampleRate(),
                        false);
                // Get AudioInputStream that will be decoded by underlying VorbisSPI
                din = AudioSystem.getAudioInputStream(decodedFormat, in);
                // Play now !
                rawplay(decodedFormat, din);
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playOnce(String filePath) {
        executorService.submit(() -> play(filePath));
    }

    public void playRandom() {
        executorService.submit(() -> {
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
        });
    }

    public void stop() {
        paused = false;
        playing = false;
    }

    public void pause() {
        paused = true;
    }

    public void unpause() {
        paused = false;
    }

    private synchronized void rawplay(AudioFormat targetFormat,
                                      AudioInputStream din) throws IOException, LineUnavailableException {
        byte[] data = new byte[4096];
        SourceDataLine line = getLine(targetFormat);
        if (line != null) {
            try {
                FloatControl control = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
                control.setValue(SettingsView.getInstance().getVolume());
            } catch (Exception e) {
                //e.printStackTrace();
            }
            // Start
            line.start();
            int nBytesRead = 0, nBytesWritten = 0;
            while (nBytesRead != -1) {
                nBytesRead = din.read(data, 0, data.length);
                if (nBytesRead != -1) nBytesWritten = line.write(data, 0, nBytesRead);
                if (!playing) {
                    return;
                }
                while (paused) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            // Stop
            line.drain();
            line.stop();
            line.close();
            din.close();
        }
    }

    private SourceDataLine getLine(AudioFormat audioFormat) throws LineUnavailableException {
        SourceDataLine res = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        res = (SourceDataLine) AudioSystem.getLine(info);
        res.open(audioFormat);
        return res;
    }
}
