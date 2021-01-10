package pl.edu.agh.diskstalker.guice;

import com.google.inject.AbstractModule;
import javafx.fxml.FXMLLoader;
import pl.edu.agh.diskstalker.controller.MainViewController;
import pl.edu.agh.diskstalker.database.datamapper.ItemDataMapper;
import pl.edu.agh.diskstalker.database.datamapper.ItemDataMapperImpl;
import pl.edu.agh.diskstalker.database.datamapper.RootDataMapper;
import pl.edu.agh.diskstalker.database.datamapper.RootDataMapperImpl;
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
        bind(RootDataMapper.class).to(RootDataMapperImpl.class);
        bind(ItemDataMapper.class).to(ItemDataMapperImpl.class);
    }
}
