package com.ipst.registromultimedia;

import com.ipst.registromultimedia.domain.AudioRepository;
import com.ipst.registromultimedia.domain.usecase.ManageAudioUseCase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ManageAudioUseCaseTest {

    private ManageAudioUseCase useCase;

    private static class FakeAudioRepository implements AudioRepository {
        private String path;

        @Override
        public void setAudioPath(String path) {
            this.path = path;
        }

        @Override
        public boolean hasRecording() {
            return path != null && !path.isEmpty();
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
        useCase = new ManageAudioUseCase(fakeRepository);
    }

    @Test
    public void testSetAndGetAudioPath() {
        useCase.init("/sdcard/audio.m4a");
        assertEquals("/sdcard/audio.m4a", useCase.getAudioPath());
        assertTrue(useCase.hasRecording());
    }

    @Test
    public void testStartAndStopRecording() {
        useCase.init("/sdcard/audio.m4a");
        assertTrue(useCase.startRecording());
        useCase.stopRecording();
    }

    @Test
    public void testStartAndStopPlayback() {
        useCase.init("/sdcard/audio.m4a");
        assertTrue(useCase.startPlayback(null));
        useCase.stopPlayback();
    }
}
