package com.example.registromultimedia.ui;

import com.example.registromultimedia.model.Member;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class providing initial mock Member data for testing.
 */
public class MockData {

    public static List<Member> getInitialMembers() {
        List<Member> list = new ArrayList<>();
        list.add(new Member("1", "Carlos Mendoza", "Líder de Proyecto", "carlos@example.com"));
        list.add(new Member("2", "Ana Torres", "Desarrollador Android", "ana@example.com"));
        list.add(new Member("3", "Luis Gómez", "Diseñador UX/UI", "luis@example.com"));
        return list;
    }
}
