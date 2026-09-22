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

    // ¿Por qué guardamos el Context aquí?
    // Lo necesitamos para acceder a las carpetas del sistema.
    private final Context appContext;
    private MediaPlayer mediaPlayer;

    public AudioPlayerManager(Context context) {
        // CONCEPTO CLAVE DE ESTUDIO: getApplicationContext()
        // Nunca guardes el contexto de un "Activity" directamente porque si la pantalla se gira o se cierra,
        // la Activity se destruye, pero esta clase la mantendría "viva" en la memoria por accidente (Memory Leak).
        // getApplicationContext() obtiene un contexto global que vive lo mismo que toda la app.
        this.appContext = context.getApplicationContext();
    }

    public File getAudioFile() {
        // Buscamos la carpeta pública de música exclusiva de nuestra app.
        File directorio = appContext.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        if (directorio == null) {
            // Plan B: Si no hay tarjeta SD o almacenamiento externo, usamos la memoria interna oculta.
            directorio = appContext.getFilesDir();
        }
        // Si la carpeta no existe, .mkdirs() crea toda la ruta necesaria.
        if (!directorio.exists() && !directorio.mkdirs()) {
            Log.w(TAG, "No se pudo crear el directorio " + directorio.getAbsolutePath());
        }
        // Retornamos el objeto File que representa "RutaCarpeta/presentacion_equipo.m4a"
        return new File(directorio, NOMBRE_ARCHIVO);
    }

    public String getAudioPath() {
        // Transforma el objeto File en una ruta de texto (String) que necesita el MediaPlayer.
        return getAudioFile().getAbsolutePath();
    }

    public boolean hasRecording() {
        File archivo = getAudioFile();
        // Validación doble: Que el archivo exista físicamente y que su peso sea mayor a 0 bytes.
        return archivo.exists() && archivo.length() > 0;
    }

    // --- LÓGICA DE REPRODUCCIÓN (MediaPlayer) ---

    // Runnable onCompletion es un bloque de código que la Vista nos pasa para que lo ejecutemos
    // cuando el audio termine (por ejemplo, para volver a habilitar el botón de "Play").
    public boolean startPlayback(Runnable onCompletion) {
        if (!hasRecording()) {
            return false; // No hay nada que reproducir
        }
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(getAudioPath()); // Le decimos de dónde leer el audio

            // prepare() bloquea el hilo temporalmente hasta que el archivo esté listo.
            // Para archivos locales pequeños es rápido, para internet se usaría prepareAsync().
            mediaPlayer.prepare();

            // Callback: ¿Qué hacer cuando la pista llegue a su fin?
            mediaPlayer.setOnCompletionListener(mp -> {
                releasePlayer(); // 1. Soltamos la memoria
                if (onCompletion != null) {
                    onCompletion.run(); // 2. Avisamos a la pantalla que ya terminamos
                }
            });

            mediaPlayer.start(); // Comienza a sonar
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Error al preparar MediaPlayer: " + e.getMessage());
            releasePlayer(); // Si falla al prepararse, limpiamos la memoria inmediatamente
            return false;
        }
    }

    public void stopPlayback() {
        // Solo podemos detenerlo si existe y actualmente está sonando
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        releasePlayer(); // Siempre liberamos al detener
    }

    public void releasePlayer() {
        if (mediaPlayer != null) {
            try {
                // CONCEPTO CLAVE DE ESTUDIO: release()
                // MediaPlayer usa recursos nativos del sistema operativo (C++ por debajo).
                // Si no llamas a release(), la batería se drena y la RAM se llena aunque cierres la app.
                mediaPlayer.release();
            } catch (Exception e) {
                Log.e(TAG, "Error al liberar MediaPlayer: " + e.getMessage());
            } finally {
                // finally asegura que esta variable se vuelva null, haya ocurrido un error o no.
                mediaPlayer = null;
            }
        }
    }
}