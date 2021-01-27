package pl.edu.agh.diskstalker.database.datamapper;

import org.junit.jupiter.api.Test;
import pl.edu.agh.diskstalker.database.model.Item;
import pl.edu.agh.diskstalker.database.model.Root;
import pl.edu.agh.diskstalker.database.model.Type;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ItemDataMapperTest {

    @Test
    public void addItemTest() {
        // given
        ItemDataMapper mapper = new ItemDataMapper();
        Root root = new Root(0, "root", "path", 3234534, 5464, 34647);
        Item item = new Item("name", "path", Type.OTHER, 3423, root);

        // when
        mapper.addItem(item);

        // then
        assertEquals(1, mapper.findAllByRoot(root).size());
        assertTrue(mapper.findAllByRoot(root).contains(item));
    }

    @Test
    public void findAllByRootTest() {
        // given
        ItemDataMapper mapper = new ItemDataMapper();
        Root root = new Root(0, "root", "path", 3234534, 5464, 34647);
        Root root1 = new Root(1, "root1", "path1", 3234343, 5464, 34647);
        Item item = new Item("name", "path", Type.OTHER, 3423, root);
        Item item1 = new Item("name1", "path1", Type.OTHER, 3423, root1);
        mapper.addItem(item);
        mapper.addItem(item1);

        // when
        List<Item> items = mapper.findAllByRoot(root);
        List<Item> items1 = mapper.findAllByRoot(root1);
        List<Item> items2 = mapper.findAllByRoot(null);

        // then
        assertEquals(1, items.size());
        assertEquals(1, items1.size());
        assertEquals(0, items2.size());
        assertTrue(items.contains(item));
        assertTrue(items1.contains(item1));
    }

    @Test
    public void deleteAllByRootTest() {
        // given
        ItemDataMapper mapper = new ItemDataMapper();
        Root root = new Root(0, "root", "path", 3234534, 5464, 34647);
        Item item = new Item("name", "path", Type.OTHER, 3423, root);
        mapper.addItem(item);
        mapper.deleteAllByRoot(root);

        // when
        List<Item> items = mapper.findAllByRoot(root);

        // then
        assertEquals(0, items.size());
        assertFalse(items.contains(item));
    }

    @Test
    public void getChildrenTest() {
        // given
        ItemDataMapper mapper = new ItemDataMapper();
        Root root = new Root(52, "root", File.separator + "home", 76543, 5464, 34647);
        Item itemRoot = new Item("root", File.separator + "home", Type.FOLDER, 34545, root);
        Item itemSub = new Item("subitem", itemRoot.getPathname(), Type.FOLDER, 4334, root);
        Item itemChild = new Item("childitem", itemSub.getPathname(), Type.FOLDER, 434, root);
        mapper.addItem(itemRoot);
        mapper.addItem(itemSub);
        mapper.addItem(itemChild);

        // when
        List<Item> children = mapper.getChildren(itemRoot);

        // then
        assertEquals(1, children.size());
        assertTrue(children.contains(itemSub));
    }

    @Test
    public void getRootItemTest() {
        // given
        ItemDataMapper mapper = new ItemDataMapper();
        Root root = new Root(52, "root", File.separator + "home", 76543, 5464, 34647);
        Item itemRoot = new Item("root", File.separator + "home", Type.FOLDER, 34545, root);
        Item itemSub = new Item("subitem", itemRoot.getPathname(), Type.FOLDER, 4334, root);
        Item itemChild = new Item("childitem", itemSub.getPathname(), Type.FOLDER, 434, root);
        mapper.addItem(itemRoot);
        mapper.addItem(itemSub);
        mapper.addItem(itemChild);

        // when
        Item gotRootItem = mapper.getRootItem(root);

        // then
        assertEquals(itemRoot, gotRootItem);
    }
}
