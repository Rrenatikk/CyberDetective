package gamedev;

import gamedev.controller.GameController;
import gamedev.model.Game;
import gamedev.model.view.GameView;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Collections;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        Game game = new Game(Collections.emptyList()); // создаём игру без уровней
        GameController controller = new GameController(game); // передаём игру в контроллер
        GameView view = new GameView(controller, primaryStage); // создаём View с контроллером и Stage
        controller.setView(view); // связываем контроллер с View
        controller.startGame(primaryStage); // запускаем игру
    }

    public static void main(String[] args) {
        launch(args);
    }
}