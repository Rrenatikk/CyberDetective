package gamedev;

import gamedev.controller.GameController;
import gamedev.model.Game;
import gamedev.model.view.MainMenu;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        GameController controller = new GameController();
        MainMenu mainMenu = new MainMenu(primaryStage, controller);
        mainMenu.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
