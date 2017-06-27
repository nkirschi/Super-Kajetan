package util;

import javax.sound.sampled.*;
import javax.sound.sampled.DataLine.Info;
import java.io.IOException;
import java.util.Random;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;
import static javax.sound.sampled.AudioSystem.getAudioInputStream;

public class AudioPlayer {
    private Random random;
    private boolean playing;

    public AudioPlayer() {
        random = new Random();
    }

    public void play(String filePath) {
        try (final AudioInputStream in = getAudioInputStream(ClassLoader.getSystemResourceAsStream(filePath))) {

            final int ch = in.getFormat().getChannels();
            final float rate = in.getFormat().getSampleRate();
            final AudioFormat outFormat = new AudioFormat(PCM_SIGNED, rate, 16, ch, ch * 2, rate, false);

            final Info info = new Info(SourceDataLine.class, outFormat);

            try (final SourceDataLine line =
                         (SourceDataLine) AudioSystem.getLine(info)) {

                if (line != null) {
                    line.open(outFormat);
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

    public void playLoop() {
        playing = true;
        while (playing) {
            String filePath = "sounds/";
            switch (random.nextInt(4)) {
                case 0:
                    filePath += "gott_mit_uns.ogg";
                    break;
                case 1:
                    filePath += "panzerkampf.ogg";
                    break;
                case 2:
                    filePath += "shiroyama.ogg";
                    break;
                case 3:
                    filePath += "the_last_stand.ogg";
                    break;
            }
            play(filePath);
        }

    }

    public void stop() {
        playing = false;
    }

    private void stream(AudioInputStream in, SourceDataLine line)
            throws IOException {
        final byte[] buffer = new byte[65536];
        for (int n = 0; n != -1; n = in.read(buffer, 0, buffer.length)) {
            if (!playing)
                break;
            line.write(buffer, 0, n);
        }
    }
}