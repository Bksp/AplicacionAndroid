package com.ipst.registromultimedia.domain.usecase;

import com.ipst.registromultimedia.domain.AudioRepository;
import com.ipst.registromultimedia.domain.AudioRepositoryProvider;

/**
 * Caso de uso: Gestión de Grabación y Reproducción de Audio para la Presentación del Equipo.
 * Encapsula las reglas de negocio para la captura y reproducción de voz,
 * consumiendo AudioRepository y comunicándose con la capa Presentation (AudioViewModel).
 */
public class ManageAudioUseCase {

    private AudioRepository repository;

    public ManageAudioUseCase() {
        this(AudioRepositoryProvider.getInstance());
    }

    public ManageAudioUseCase(AudioRepository repository) {
        this.repository = repository;
    }

    public void init(String outputPath) {
        setAudioPath(outputPath);
    }

    public void setAudioPath(String path) {
        if (repository == null) {
            repository = AudioRepositoryProvider.getInstance();
        }
        if (repository != null) {
            repository.setAudioPath(path);
        }
    }

    public boolean hasRecording() {
        if (repository != null) {
            return repository.hasRecording();
        }
        return false;
    }

    public String getAudioPath() {
        if (repository != null) {
            return repository.getAudioPath();
        }
        return null;
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

    public int getDuration() {
        if (repository != null) {
            return repository.getDuration();
        }
        return 0;
    }

    public int getCurrentPosition() {
        if (repository != null) {
            return repository.getCurrentPosition();
        }
        return 0;
    }
}
