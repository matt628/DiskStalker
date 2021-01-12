package pl.edu.agh.diskstalker.database.model;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {

    @Test
    public void getPathnameTest() {
        // Given
        Item item = new Item("file", File.separator + "home", "txt", 345, null);

        // When
        String pathname = item.getPathname();

        // Then
        assertEquals(pathname, File.separator + "home" + File.separator + "file");
    }

    @Test
    public void isSubItemTest() {
        // Given
        Item itemRoot = new Item("root", File.separator + "home", null, 34545, null);
        Item itemSub = new Item("subitem", itemRoot.getPathname(), null, 4334, null);
        Item itemChild = new Item("childitem", itemSub.getPathname(), null, 434, null);

        // When
        boolean itemSubSub = itemRoot.isClosestParent(itemSub);
        boolean itemChildNotSub = itemRoot.isClosestParent(itemChild);
        boolean itemRootNotSub = itemRoot.isClosestParent(itemRoot);

        // Then
        assertTrue(itemSubSub);
        assertFalse(itemChildNotSub);
        assertFalse(itemRootNotSub);
    }

    @Test
    public void isChildTest() {
        // Given
        Item itemRoot = new Item("root", File.separator + "home", null, 34545, null);
        Item itemSub = new Item("subitem", itemRoot.getPathname(), null, 4334, null);
        Item itemChild = new Item("childitem", itemSub.getPathname(), null, 434, null);

        // When
        boolean itemSubChild = itemSub.isChild(itemRoot.getPathname());
        boolean itemChildChild = itemChild.isChild(itemRoot.getPathname());
        boolean itemRootNotChild = itemRoot.isChild(itemRoot.getPathname());

        // Then
        assertTrue(itemSubChild);
        assertTrue(itemChildChild);
        assertFalse(itemRootNotChild);
    }
}
