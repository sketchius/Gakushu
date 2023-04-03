package huhtala.bryce.jpgpt.service;
import javax.sound.sampled.*;
import java.io.*;

public class RecorderService {


        static final long RECORD_TIME = 60000;  // 1 minute

        File wavFile = new File("output.wav");
        AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;


        TargetDataLine line;


        AudioFormat getAudioFormat() {
            float sampleRate = 16000;
            int sampleSizeInBits = 8;
            int channels = 2;
            boolean signed = true;
            boolean bigEndian = true;
            AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
                    channels, signed, bigEndian);
            return format;
        }

        public void start() {
            try {
                AudioFormat format = getAudioFormat();
                DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

                // checks if system supports the data line
                if (!AudioSystem.isLineSupported(info)) {
                    System.out.println("Line not supported");
                    System.exit(0);
                }
                line = (TargetDataLine) AudioSystem.getLine(info);
                line.open(format);
                line.start();   // start capturing

                AudioInputStream ais = new AudioInputStream(line);

                System.out.println("Start speaking.");

                // start recording
                AudioSystem.write(ais, fileType, wavFile);

            } catch (LineUnavailableException ex) {
                ex.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        public void finish() {
            line.stop();
            line.close();
            System.out.println("Finished");
        }

        public void record(int durationInMillis) {

            Thread stopper = new Thread(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(durationInMillis);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    finish();
                    System.out.println("Stop speaking.");
                }
            });

            stopper.start();

            start();
        }
}
