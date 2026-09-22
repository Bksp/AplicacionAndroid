package com.example.registromultimedia.data;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.IOException;

public class AudioPlayerManager {

    private static final String TAG = "AudioPlayerManager";
    public static final String NOMBRE_ARCHIVO = "presentacion_equipo.m4a";

    private final Context appContext;
    private MediaPlayer mediaPlayer;

    public AudioPlayerManager(Context context) {
        this.appContext = context.getApplicationContext();
    }

    public File getAudioFile() {
        File directorio = appContext.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        if (directorio == null) {
            directorio = appContext.getFilesDir();
        }
        if (!directorio.exists() && !directorio.mkdirs()) {
            Log.w(TAG, "No se pudo crear el directorio " + directorio.getAbsolutePath());
        }
        return new File(directorio, NOMBRE_ARCHIVO);
    }

    public String getAudioPath() {
        return getAudioFile().getAbsolutePath();
    }

    public boolean hasRecording() {
        File archivo = getAudioFile();
        return archivo.exists() && archivo.length() > 0;
    }

    // --- LÓGICA DE REPRODUCCIÓN (MediaPlayer) ---

    public boolean startPlayback(Runnable onCompletion) {
        if (!hasRecording()) {
            return false;
        }
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(getAudioPath());
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
            Log.e(TAG, "Error al preparar MediaPlayer: " + e.getMessage());
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

    public void releasePlayer() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
            } catch (Exception e) {
                Log.e(TAG, "Error al liberar MediaPlayer: " + e.getMessage());
            } finally {
                mediaPlayer = null;
            }
        }
    }
}