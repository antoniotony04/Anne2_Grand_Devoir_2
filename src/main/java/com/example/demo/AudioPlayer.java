package com.example.demo;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioPlayer {

    private Clip clip;
    private FloatControl volumeControl;

    public void play(String filePath) {
        try {
            // Încărcăm fișierul audio
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

            clip = AudioSystem.getClip();
            clip.open(audioStream);

            // Obținem controlul de volum
            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            // Pornim redarea
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Redare continuă
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Eroare la redarea audio: " + e.getMessage());
        }
    }

    public void setVolume(float volume) {
        if (volumeControl != null) {
            // Valoarea volumului trebuie să fie în intervalul permis de control
            float minVolume = volumeControl.getMinimum();
            float maxVolume = volumeControl.getMaximum();
            float newVolume = minVolume + (volume * (maxVolume - minVolume));
            volumeControl.setValue(newVolume);
            System.out.println("Volumul a fost setat la: " + newVolume);
        } else {
            System.err.println("Controlul de volum nu este disponibil.");
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }
}
