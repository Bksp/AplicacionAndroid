package com.example.registromultimedia.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.registromultimedia.data.AudioRecorder;
import com.example.registromultimedia.model.AudioState;

/**
 * ViewModel managing audio recording and playback state.
 * Absolutely zero UI dependencies.
 */
public class AudioViewModel extends ViewModel {

    private final MutableLiveData<AudioState> audioState = new MutableLiveData<>(AudioState.IDLE);
    private AudioRecorder audioRecorder;

    public LiveData<AudioState> getAudioState() {
        return audioState;
    }

    public void initRecorder(String outputPath) {
        this.audioRecorder = new AudioRecorder(outputPath);
    }

    public void toggleRecording() {
        if (audioRecorder == null) {
            audioState.setValue(AudioState.ERROR);
            return;
        }

        if (audioState.getValue() == AudioState.RECORDING) {
            audioRecorder.stopRecording();
            audioState.setValue(AudioState.RECORDED);
        } else {
            boolean success = audioRecorder.startRecording();
            if (success) {
                audioState.setValue(AudioState.RECORDING);
            } else {
                audioState.setValue(AudioState.ERROR);
            }
        }
    }

    public void togglePlayback() {
        if (audioRecorder == null) {
            audioState.setValue(AudioState.ERROR);
            return;
        }

        if (audioState.getValue() == AudioState.PLAYING) {
            audioRecorder.stopPlayback();
            audioState.setValue(AudioState.RECORDED);
        } else {
            boolean success = audioRecorder.startPlayback(() -> audioState.postValue(AudioState.RECORDED));
            if (success) {
                audioState.setValue(AudioState.PLAYING);
            } else {
                audioState.setValue(AudioState.ERROR);
            }
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (audioRecorder != null) {
            audioRecorder.stopRecording();
            audioRecorder.stopPlayback();
        }
    }
}
