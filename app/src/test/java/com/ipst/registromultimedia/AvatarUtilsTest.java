package com.ipst.registromultimedia;

import com.ipst.registromultimedia.model.Member;
import com.ipst.registromultimedia.ui.AvatarUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class AvatarUtilsTest {

    @Test
    public void member_getInitials_returnsCorrectInitials() {
        Member m1 = new Member("1", "Carlos Mendoza", "Líder", "carlos@example.com");
        assertEquals("CM", m1.getInitials());

        Member m2 = new Member("2", "Ana Torres", "Dev", "ana@example.com");
        assertEquals("AT", m2.getInitials());

        Member m3 = new Member("3", "Luis", "Dev", "luis@example.com");
        assertEquals("L", m3.getInitials());

        Member m4 = new Member("4", "  Juan  Carlos   Perez  ", "Dev", "juan@example.com");
        assertEquals("JP", m4.getInitials());

        Member mNull = new Member("5", null, "Dev", "null@example.com");
        assertEquals("?", mNull.getInitials());
    }

    @Test
    public void avatarUtils_getInitials_fallbackWorksCorrectly() {
        assertEquals("CM", AvatarUtils.getInitials("Carlos Mendoza"));
        assertEquals("A", AvatarUtils.getInitials("Ana"));
        assertEquals("?", AvatarUtils.getInitials(null));
        assertEquals("?", AvatarUtils.getInitials("   "));
    }

    @Test
    public void avatarUtils_getColorForName_isDeterministicAndConsistent() {
        int color1 = AvatarUtils.getColorForName("Carlos Mendoza");
        int color2 = AvatarUtils.getColorForName("Carlos Mendoza");
        assertEquals(color1, color2);

        int colorAna = AvatarUtils.getColorForName("Ana Torres");
        // Color shouldn't be 0
        assertNotEquals(0, colorAna);
    }
}
