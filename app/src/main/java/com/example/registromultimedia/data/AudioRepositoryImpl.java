package com.example.registromultimedia.data;

import com.example.registromultimedia.domain.AudioRepository;
import java.io.File;

/**
 * Implementación concreta de AudioRepository en la capa Data.
 * Encapsula la gestión de AudioRecorder para desacoplar la capa de Domain.
 */
public class AudioRepositoryImpl implements AudioRepository {

    private AudioRecorder audioRecorder;
    private String audioPath;

    public AudioRepositoryImpl() {
    }

    public AudioRepositoryImpl(String audioPath) {
        setAudioPath(audioPath);
    }

    @Override
    public void setAudioPath(String path) {
        this.audioPath = path;
        if (path != null) {
            this.audioRecorder = new AudioRecorder(path);
        }
    }

    @Override
    public boolean hasRecording() {
        if (audioPath == null) return false;
        File file = new File(audioPath);
        return file.exists() && file.length() > 0;
    }

    @Override
    public String getAudioPath() {
        return audioPath;
    }

    @Override
    public boolean startRecording() {
        if (audioRecorder == null && audioPath != null) {
            audioRecorder = new AudioRecorder(audioPath);
        }
        if (audioRecorder != null) {
            return audioRecorder.startRecording();
        }
        return false;
    }

    @Override
    public void stopRecording() {
        if (audioRecorder != null) {
            audioRecorder.stopRecording();
        }
    }

    @Override
    public boolean startPlayback(Runnable onCompletion) {
        if (audioRecorder == null && audioPath != null) {
            audioRecorder = new AudioRecorder(audioPath);
        }
        if (audioRecorder != null) {
            return audioRecorder.startPlayback(onCompletion);
        }
        return false;
    }

    @Override
    public void stopPlayback() {
        if (audioRecorder != null) {
            audioRecorder.stopPlayback();
        }
    }

    @Override
    public int getDuration() {
        if (audioRecorder == null && audioPath != null) {
            audioRecorder = new AudioRecorder(audioPath);
        }
        if (audioRecorder != null) {
            return audioRecorder.getDuration();
        }
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (audioRecorder != null) {
            return audioRecorder.getCurrentPosition();
        }
        return 0;
    }
}
