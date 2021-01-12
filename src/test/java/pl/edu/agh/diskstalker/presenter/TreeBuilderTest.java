package pl.edu.agh.diskstalker.presenter;
import javafx.scene.control.TreeItem;
import org.checkerframework.checker.nullness.Opt;
import org.junit.jupiter.api.*;
import pl.edu.agh.diskstalker.database.connection.ConnectionProvider;
import pl.edu.agh.diskstalker.database.executor.QueryExecutor;
import pl.edu.agh.diskstalker.model.Item;
import pl.edu.agh.diskstalker.model.Root;

import java.io.File;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
public class TreeBuilderTest {
    @Test
    public void buildTreeTest1(){
        TreeBuilder treeBuilder = new TreeBuilder();
        Root root = new Root(52, "newroot", File.separator + "rootfolder", 76543);
        Item item1 = new Item("newroot", File.separator + "rootfolder", null, 7654, root);
        Item item2 = new Item("item1", File.separator + "rootfolder" + File.separator + "newroot", "jpg", 7699, root);
        root.addItem(item1);
        root.addItem(item2);
        TreeItem<Item> rootItem = treeBuilder.buildTree(item1);
        assertEquals(rootItem.getValue(), item1 );
        assertEquals(rootItem.getChildren().stream().findFirst().get().getValue(), item2 );
    }
}
