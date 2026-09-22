package com.example.registromultimedia.domain.usecase;

import com.example.registromultimedia.domain.AudioRepository;
import com.example.registromultimedia.domain.AudioRepositoryProvider;

/**
 * Caso de uso: Grabar Audio.
 * Encapsula la lógica de negocio para iniciar, detener e inicializar la grabación de audio.
 */
public class RecordAudioUseCase {

    private AudioRepository repository;

    public RecordAudioUseCase() {
        this(AudioRepositoryProvider.getInstance());
    }

    public RecordAudioUseCase(AudioRepository repository) {
        this.repository = repository;
    }

    public void init(String outputPath) {
        if (repository == null) {
            repository = AudioRepositoryProvider.getInstance();
        }
        if (repository != null) {
            repository.setAudioPath(outputPath);
        }
    }

    public boolean startRecording() {
        if (repository != null) {
            return repository.startRecording();
        }
        return false;
    }

    public void stopRecording() {
        if (repository != null) {
            repository.stopRecording();
        }
    }
}
