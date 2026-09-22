package com.ipst.registromultimedia;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import com.ipst.registromultimedia.domain.AudioRepository;
import com.ipst.registromultimedia.domain.usecase.ManageAudioUseCase;
import com.ipst.registromultimedia.model.AudioState;
import com.ipst.registromultimedia.viewmodel.AudioViewModel;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class AudioViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private AudioViewModel viewModel;

    private static class FakeAudioRepository implements AudioRepository {
        private String path;

        @Override
        public void setAudioPath(String path) {
            this.path = path;
        }

        @Override
        public boolean hasRecording() {
            return false;
        }

        @Override
        public String getAudioPath() {
            return path;
        }

        @Override
        public boolean startRecording() {
            return true;
        }

        @Override
        public void stopRecording() {
        }

        @Override
        public boolean startPlayback(Runnable onCompletion) {
            return true;
        }

        @Override
        public void stopPlayback() {
        }

        @Override
        public int getDuration() {
            return 10000;
        }

        @Override
        public int getCurrentPosition() {
            return 0;
        }
    }

    @Before
    public void setUp() {
        FakeAudioRepository fakeRepository = new FakeAudioRepository();
        ManageAudioUseCase useCase = new ManageAudioUseCase(fakeRepository);
        viewModel = new AudioViewModel(useCase);
    }

    @Test
    public void testToggleRecordingStateChange() {
        viewModel.initRecorder("/tmp/test.m4a");
        assertEquals(AudioState.IDLE, viewModel.getAudioState().getValue());

        viewModel.toggleRecording();
        assertEquals(AudioState.RECORDING, viewModel.getAudioState().getValue());

        viewModel.toggleRecording();
        assertEquals(AudioState.RECORDED, viewModel.getAudioState().getValue());
    }

    @Test
    public void testTogglePlaybackStateChange() {
        viewModel.initRecorder("/tmp/test.m4a");
        viewModel.toggleRecording(); // RECORDING
        viewModel.toggleRecording(); // RECORDED

        viewModel.togglePlayback();
        assertEquals(AudioState.PLAYING, viewModel.getAudioState().getValue());

        viewModel.togglePlayback();
        assertEquals(AudioState.RECORDED, viewModel.getAudioState().getValue());
    }
}
