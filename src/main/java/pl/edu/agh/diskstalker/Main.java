package pl.edu.agh.diskstalker;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.edu.agh.diskstalker.guice.GuiceModule;
import pl.edu.agh.diskstalker.guice.provider.FXMLLoaderProvider;

import java.io.IOException;
import java.io.InputStream;

public class Main extends Application {

    public static final String APPLICATION_NAME = "Disk Stalker";
    public static final int NOTIFICATION_FREQUENCY_IN_HOURS = 2;

    public static void main(String[] args) {
        Main.launch();
    }

    @Override
    public void start(Stage primaryStage) {
        Injector injector = Guice.createInjector(new GuiceModule());
        FXMLLoaderProvider fxmlLoaderProvider = new FXMLLoaderProvider();
        FXMLLoader loader = fxmlLoaderProvider.get();
        loader.setControllerFactory(injector::getInstance);

        try (InputStream fxmlInputStream = ClassLoader.getSystemResourceAsStream("MainPane.fxml")) {
            Parent parent = loader.load(fxmlInputStream);
            primaryStage.setScene(new Scene(parent));
            primaryStage.setTitle(APPLICATION_NAME);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
