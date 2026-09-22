package com.example.registromultimedia.data;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Audio Data Source managing MediaRecorder and MediaPlayer instances without any UI dependencies.
 */
public class AudioRecorder {

    private static final String TAG = "AudioRecorder";

    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private final String outputFilePath;

    public AudioRecorder(String outputFilePath) {
        this.outputFilePath = outputFilePath;
    }

    public boolean startRecording() {
        if (mediaRecorder != null) {
            Log.w(TAG, "startRecording() called while a recording is already in progress");
            return false;
        }
        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.setOutputFile(outputFilePath);
            mediaRecorder.prepare();
            mediaRecorder.start();
            return true;
        } catch (IOException | IllegalStateException e) {
            Log.e(TAG, "Failed to start recording", e);
            releaseRecorder();
            return false;
        }
    }

    public void stopRecording() {
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
            } catch (RuntimeException e) {
                Log.w(TAG, "stop() failed, likely no valid audio data was captured", e);
            } finally {
                releaseRecorder();
            }
        }
    }

    private void releaseRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    public boolean startPlayback(Runnable onCompletion) {
        if (outputFilePath == null || !(new File(outputFilePath).exists())) {
            Log.w(TAG, "startPlayback() called but output file does not exist");
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
            Log.e(TAG, "Failed to start playback", e);
            releasePlayer();
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

    public int getDuration() {
        if (mediaPlayer != null) {
            try {
                return mediaPlayer.getDuration();
            } catch (Exception e) {
                Log.w(TAG, "Error getting mediaPlayer duration", e);
            }
        }
        if (outputFilePath != null && new File(outputFilePath).exists()) {
            try (MediaMetadataRetriever mmr = new MediaMetadataRetriever()) {
                mmr.setDataSource(outputFilePath);
                String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                if (durationStr != null) {
                    return Integer.parseInt(durationStr);
                }
            } catch (Exception e) {
                Log.w(TAG, "Error extracting duration with MediaMetadataRetriever", e);
            }
        }
        return 0;
    }

    public int getCurrentPosition() {
        if (mediaPlayer != null) {
            try {
                return mediaPlayer.getCurrentPosition();
            } catch (Exception e) {
                Log.w(TAG, "Error getting mediaPlayer current position", e);
            }
        }
        return 0;
    }
}