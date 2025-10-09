package gamedev;

import gamedev.controller.GameController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        GameController controller = new GameController();
        controller.startGame(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
