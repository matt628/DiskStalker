package pl.edu.agh.diskstalker.database.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

public class RootTest {

    @Test
    public void getPathnameTest() {
        // given
        String name = "name";
        String path = "path";
        Item item = new Item(name, path, Type.OTHER, 2344, null);

        // when
        String pathname = item.getPathname();

        // then
        Assertions.assertEquals(path + File.separator + name, pathname);
    }
}
