package pl.edu.agh.diskstalker;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.edu.agh.diskstalker.guice.GuiceModule;

import java.io.IOException;
import java.io.InputStream;

public class Main extends Application {

    public static void main(String[] args) {
        Main.launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Injector injector = Guice.createInjector(new GuiceModule());
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(injector::getInstance);

        try (InputStream fxmlInputStream = ClassLoader.getSystemResourceAsStream("MainPane.fxml")) {
            Parent parent = loader.load(fxmlInputStream);
            primaryStage.setScene(new Scene(parent));
            primaryStage.setTitle("Disk Stalker");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
