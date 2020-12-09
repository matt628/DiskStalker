package pl.edu.agh.diskstalker.Guice;

import com.google.inject.AbstractModule;
import javafx.fxml.FXMLLoader;
import pl.edu.agh.diskstalker.Guice.Provider.FXMLLoaderProvider;

public class GuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(FXMLLoader.class).toProvider(FXMLLoaderProvider.class);
    }
}
