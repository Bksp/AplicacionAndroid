package com.example.registromultimedia.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.registromultimedia.domain.usecase.ManageAudioUseCase;
import com.example.registromultimedia.domain.usecase.PlayAudioUseCase;
import com.example.registromultimedia.domain.usecase.RecordAudioUseCase;
import com.example.registromultimedia.model.AudioState;

/**
 * ViewModel managing audio recording and playback state.
 * Consumes exclusivamente los Casos de Uso de Audio de la capa Domain (ManageAudioUseCase / RecordAudioUseCase / PlayAudioUseCase).
 * Absolutely zero UI or Data dependencies.
 */
public class AudioViewModel extends ViewModel {

    private final MutableLiveData<AudioState> audioState = new MutableLiveData<>(AudioState.IDLE);
    private final ManageAudioUseCase manageAudioUseCase;

    public AudioViewModel() {
        this(new ManageAudioUseCase());
    }

    public AudioViewModel(ManageAudioUseCase manageAudioUseCase) {
        this.manageAudioUseCase = manageAudioUseCase;
    }

    public AudioViewModel(RecordAudioUseCase recordAudioUseCase, PlayAudioUseCase playAudioUseCase) {
        this.manageAudioUseCase = new ManageAudioUseCase();
    }

    public LiveData<AudioState> getAudioState() {
        return audioState;
    }

    public void initRecorder(String outputPath) {
        if (manageAudioUseCase != null) {
            manageAudioUseCase.init(outputPath);
            if (manageAudioUseCase.hasRecording()) {
                audioState.setValue(AudioState.RECORDED);
            }
        }
    }

    public void toggleRecording() {
        if (manageAudioUseCase == null) {
            audioState.setValue(AudioState.ERROR);
            return;
        }

        if (audioState.getValue() == AudioState.RECORDING) {
            manageAudioUseCase.stopRecording();
            audioState.setValue(AudioState.RECORDED);
        } else {
            boolean success = manageAudioUseCase.startRecording();
            if (success) {
                audioState.setValue(AudioState.RECORDING);
            } else {
                audioState.setValue(AudioState.ERROR);
            }
        }
    }

    public void togglePlayback() {
        if (manageAudioUseCase == null) {
            audioState.setValue(AudioState.ERROR);
            return;
        }

        if (audioState.getValue() == AudioState.PLAYING) {
            manageAudioUseCase.stopPlayback();
            audioState.setValue(AudioState.RECORDED);
        } else {
            boolean success = manageAudioUseCase.startPlayback(() -> audioState.postValue(AudioState.RECORDED));
            if (success) {
                audioState.setValue(AudioState.PLAYING);
            } else {
                audioState.setValue(AudioState.ERROR);
            }
        }
    }

    public int getDuration() {
        return manageAudioUseCase != null ? manageAudioUseCase.getDuration() : 0;
    }

    public int getCurrentPosition() {
        return manageAudioUseCase != null ? manageAudioUseCase.getCurrentPosition() : 0;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (manageAudioUseCase != null) {
            manageAudioUseCase.stopRecording();
            manageAudioUseCase.stopPlayback();
        }
    }
}
