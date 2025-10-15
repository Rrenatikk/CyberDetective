package gamedev.model;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

public class SoundPlayer {

    private static boolean muted = false;

    public static void setMuted(boolean mute) {
        muted = mute;
    }

    public static void play(String fileName) {
        if (muted) return; // 🔇 если выключено — не воспроизводим звук

        try {
            URL resource = SoundPlayer.class.getResource("/sounds/" + fileName);
            if (resource != null) {
                Media media = new Media(resource.toString());
                MediaPlayer player = new MediaPlayer(media);
                player.setVolume(0.2);
                player.play();
            } else {
                System.err.println("Sound file not found: " + fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
