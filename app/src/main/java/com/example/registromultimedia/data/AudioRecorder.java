package com.example.registromultimedia.data;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import java.io.File;
import java.io.IOException;

/**
 * Audio Data Source managing MediaRecorder and MediaPlayer instances without any UI dependencies.
 */
public class AudioRecorder {

    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private final String outputFilePath;

    public AudioRecorder(String outputFilePath) {
        this.outputFilePath = outputFilePath;
    }

    public boolean startRecording() {
        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(outputFilePath);
            mediaRecorder.prepare();
            mediaRecorder.start();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void stopRecording() {
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
            } catch (RuntimeException e) {
                // Handle possible lack of audio data captured
            } finally {
                mediaRecorder.release();
                mediaRecorder = null;
            }
        }
    }

    public boolean startPlayback(Runnable onCompletion) {
        if (outputFilePath == null || !(new File(outputFilePath).exists())) {
            return false;
        }
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(outputFilePath);
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(mp -> {
                releasePlayer();
                if (onCompletion != null) {
                    onCompletion.run();
                }
            });
            mediaPlayer.start();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void stopPlayback() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        releasePlayer();
    }

    private void releasePlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
