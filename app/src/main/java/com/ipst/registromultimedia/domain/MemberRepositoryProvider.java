package com.ipst.registromultimedia.domain;

import com.ipst.registromultimedia.data.MemberRepositoryImpl;

/**
 * Proveedor/Registry para instancias de MemberRepository en la capa Domain.
 * Mantiene la instancia única del repositorio de miembros.
 */
public class MemberRepositoryProvider {

    private static MemberRepository instance;

    public static void setInstance(MemberRepository repository) {
        instance = repository;
    }

    public static synchronized MemberRepository getInstance() {
        if (instance == null) {
            instance = new MemberRepositoryImpl();
        }
        return instance;
    }
}
