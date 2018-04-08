package program;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;

public class Sound extends Thread{
    private String filename;
    private boolean loop;

    public Sound(String filename){
        this(filename, false);
    }

    public Sound(String filename, boolean loop){
        this.filename = filename;
        this.loop = loop;
    }

    /**
     * The sound is load and play in a thread no slow down the engine.
     * */
    @Override
    public void run() {
        try {
            AudioInputStream in = AudioSystem.getAudioInputStream(new File(this.filename + ".wav"));
            Clip clip = AudioSystem.getClip();

            clip.open(in);
            if (this.loop){
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            clip.start();
        }catch (Exception e){
            System.err.println(e);
        }

    }
}