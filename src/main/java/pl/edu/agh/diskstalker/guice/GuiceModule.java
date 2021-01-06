package pl.edu.agh.diskstalker.guice;

import com.google.inject.AbstractModule;
import javafx.fxml.FXMLLoader;
import pl.edu.agh.diskstalker.guice.provider.FXMLLoaderProvider;

public class GuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(FXMLLoader.class).toProvider(FXMLLoaderProvider.class);
    }
}
