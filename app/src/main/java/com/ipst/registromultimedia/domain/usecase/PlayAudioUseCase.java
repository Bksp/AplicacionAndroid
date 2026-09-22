package com.ipst.registromultimedia.domain.usecase;

import com.ipst.registromultimedia.domain.AudioRepository;
import com.ipst.registromultimedia.domain.AudioRepositoryProvider;

/**
 * Caso de uso: Reproducir Audio.
 * Encapsula la lógica de negocio para iniciar y detener la reproducción de audio.
 */
public class PlayAudioUseCase {

    private AudioRepository repository;

    public PlayAudioUseCase() {
        this(AudioRepositoryProvider.getInstance());
    }

    public PlayAudioUseCase(AudioRepository repository) {
        this.repository = repository;
    }

    public boolean startPlayback(Runnable onCompletion) {
        if (repository != null) {
            return repository.startPlayback(onCompletion);
        }
        return false;
    }

    public void stopPlayback() {
        if (repository != null) {
            repository.stopPlayback();
        }
    }
}
