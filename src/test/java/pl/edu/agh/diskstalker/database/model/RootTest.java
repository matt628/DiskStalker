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
        Root root = new Root(0, name, path, 453543, 2344, 34647);

        // when
        String pathname = root.getPathname();

        // then
        Assertions.assertEquals(path + File.separator + name, pathname);
    }
}
