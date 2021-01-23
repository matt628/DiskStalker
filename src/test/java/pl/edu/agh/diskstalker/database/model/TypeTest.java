package pl.edu.agh.diskstalker.database.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TypeTest {

    @Test
    public void getTypeTest() {
        // given
        String type1 = ".jpg";
        String type2 = ".mp3";
        String type3 = ".txt";
        String type4 = "folder";
        String type5 = ".c";

        // when
        Type gotType1 = Type.getType(type1);
        Type gotType2 = Type.getType(type2);
        Type gotType3 = Type.getType(type3);
        Type gotType4 = Type.getType(type4);
        Type gotType5 = Type.getType(type5);

        // then
        assertEquals(Type.JPG, gotType1);
        assertEquals(Type.MP3, gotType2);
        assertEquals(Type.TXT, gotType3);
        assertEquals(Type.FOLDER, gotType4);
        assertEquals(Type.OTHER, gotType5);
    }
}
