package pl.edu.agh.diskstalker.guice;

import com.google.inject.AbstractModule;
import javafx.fxml.FXMLLoader;
import pl.edu.agh.diskstalker.controller.MainViewController;
import pl.edu.agh.diskstalker.guice.provider.FXMLLoaderProvider;
import pl.edu.agh.diskstalker.presenter.FolderDetailsHandler;
import pl.edu.agh.diskstalker.presenter.TreeHandler;

public class GuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(FXMLLoader.class).toProvider(FXMLLoaderProvider.class);
        bind(FolderDetailsHandler.class);
        bind(TreeHandler.class);
        bind(MainViewController.class);
    }
}
