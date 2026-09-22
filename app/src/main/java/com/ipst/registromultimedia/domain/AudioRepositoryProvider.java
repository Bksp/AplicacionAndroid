package com.ipst.registromultimedia.domain;

import com.ipst.registromultimedia.data.AudioRepositoryImpl;

/**
 * Proveedor/Registry para instancias de AudioRepository en la capa Domain.
 * Mantiene la instancia única del repositorio de audio.
 */
public class AudioRepositoryProvider {

    private static AudioRepository instance;

    public static void setInstance(AudioRepository repository) {
        instance = repository;
    }

    public static synchronized AudioRepository getInstance() {
        if (instance == null) {
            instance = new AudioRepositoryImpl();
        }
        return instance;
    }
}
