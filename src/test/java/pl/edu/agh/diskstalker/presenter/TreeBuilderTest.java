package pl.edu.agh.diskstalker.presenter;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.scene.control.TreeItem;
import org.junit.jupiter.api.Test;
import pl.edu.agh.diskstalker.database.datamapper.ItemDataMapper;
import pl.edu.agh.diskstalker.database.model.Item;
import pl.edu.agh.diskstalker.database.model.Root;
import pl.edu.agh.diskstalker.database.model.Type;
import pl.edu.agh.diskstalker.guice.GuiceModule;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TreeBuilderTest {

    private final Injector injector = Guice.createInjector(new GuiceModule());

    @Test
    public void buildTreeTest1(){
        // Given
        Root root = new Root(52, "newroot", File.separator + "rootfolder", 76543);
        Item item1 = new Item("newroot", File.separator + "rootfolder", Type.FOLDER, 76543, root);
        Item item2 = new Item("item1", File.separator + "rootfolder" + File.separator + "newroot", Type.JPG, 7699, root);

        ItemDataMapper itemDataMapper = injector.getInstance(ItemDataMapper.class);
        itemDataMapper.addItem(item1);
        itemDataMapper.addItem(item2);

        // When
        TreeBuilder treeBuilder = injector.getInstance(TreeBuilder.class);
        TreeItem<Item> rootItem = treeBuilder.buildTree(item1);

        // Then
        assertEquals(rootItem.getValue(), item1);
        assertEquals(rootItem.getChildren().stream().findFirst().get().getValue(), item2);
    }
}
