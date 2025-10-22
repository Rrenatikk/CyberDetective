package gamedev.model;

import javafx.scene.media.MediaPlayer;
import java.util.ArrayList;
import java.util.List;

public class SoundPlayer {

    private static boolean muted = false;
    private static final List<MediaPlayer> activePlayers = new ArrayList<>();

    public static void setMuted(boolean mute) {
        muted = mute;
        if (mute) stopAll();
    }

    public static void play(String fileName) {
        if (muted) return;

        try {
            var resource = SoundPlayer.class.getResource("/sounds/" + fileName);
            if (resource != null) {
                var media = new javafx.scene.media.Media(resource.toString());
                var player = new javafx.scene.media.MediaPlayer(media);
                player.setVolume(0.2);
                activePlayers.add(player);
                player.setOnEndOfMedia(() -> activePlayers.remove(player));
                player.setOnStopped(() -> activePlayers.remove(player));
                player.play();
            } else {
                System.err.println("Sound file not found: " + fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopAll() {
        for (MediaPlayer player : new ArrayList<>(activePlayers)) {
            player.stop();
        }
        activePlayers.clear();
    }
}
