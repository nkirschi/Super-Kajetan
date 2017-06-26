package util;

import javax.sound.sampled.*;
import javax.sound.sampled.DataLine.Info;
import java.io.IOException;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;
import static javax.sound.sampled.AudioSystem.getAudioInputStream;

public class AudioPlayer {

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

    public void loop(String filePath) {
        while (true)
            play(filePath);
    }

    private void stream(AudioInputStream in, SourceDataLine line)
            throws IOException {
        final byte[] buffer = new byte[65536];
        for (int n = 0; n != -1; n = in.read(buffer, 0, buffer.length)) {
            line.write(buffer, 0, n);
        }
    }
}