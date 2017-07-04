package util;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.IOException;
import java.util.Random;

public class SoundUtil {
    private static List<Thread> threads = new List<>();
    private static boolean paused = false;
    private static Random random = new Random();

    synchronized private static void play(String filename) {
        paused = false;
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

    synchronized public static void playOnce(String filePath) {
        Thread thread = new Thread(() -> play(filePath));
        threads.add(thread);
        thread.start();
    }

    synchronized public static void loop() {
        Thread thread = new Thread(() -> {
            while (true) {
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
        });
        threads.add(thread);
        thread.start();
    }

    public static void stop() {
        for (Thread thread : threads)
            thread.stop();
    }

    public static void pause() {
        paused = true;
    }

    public static void unpause() {
        paused = false;
    }

    synchronized private static void rawplay(AudioFormat targetFormat,
                                             AudioInputStream din) throws IOException, LineUnavailableException {
        byte[] data = new byte[4096];
        SourceDataLine line = getLine(targetFormat);
        if (line != null) {
            // Start
            line.start();
            int nBytesRead = 0, nBytesWritten = 0;
            while (nBytesRead != -1) {
                while (paused) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                nBytesRead = din.read(data, 0, data.length);
                if (nBytesRead != -1) nBytesWritten = line.write(data, 0, nBytesRead);
            }
            // Stop
            line.drain();
            line.stop();
            line.close();
            din.close();
        }
    }

    synchronized private static SourceDataLine getLine(AudioFormat audioFormat) throws LineUnavailableException {
        SourceDataLine res = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        res = (SourceDataLine) AudioSystem.getLine(info);
        res.open(audioFormat);
        return res;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        JButton start = new JButton("Start");
        start.addActionListener(a -> loop());
        JButton pause = new JButton("(Un-)Pause");
        pause.addActionListener(a -> paused = !paused);
        JButton stop = new JButton("Stop");
        stop.addActionListener(a -> stop());
        panel.add(start);
        panel.add(pause);
        panel.add(stop);
        frame.add(panel);
        frame.setVisible(true);
    }
}
