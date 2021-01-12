package pl.edu.agh.diskstalker.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import javafx.fxml.FXMLLoader;
import pl.edu.agh.diskstalker.database.datamapper.ItemDataMapper;
import pl.edu.agh.diskstalker.database.datamapper.ItemDataMapperImpl;
import pl.edu.agh.diskstalker.database.datamapper.RootDataMapper;
import pl.edu.agh.diskstalker.database.datamapper.RootDataMapperImpl;
import pl.edu.agh.diskstalker.guice.provider.FXMLLoaderProvider;

public class GuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(FXMLLoader.class).toProvider(FXMLLoaderProvider.class);
        bind(RootDataMapper.class).to(RootDataMapperImpl.class).in(Singleton.class);
        bind(ItemDataMapper.class).to(ItemDataMapperImpl.class).in(Singleton.class);
    }
}
