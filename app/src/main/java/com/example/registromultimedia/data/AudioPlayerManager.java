package com.example.registromultimedia.data;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import java.io.File;

/**
 * Tarea #11 - Sistema de Archivos y Reproduccion.
 * Define donde se guarda la presentacion de voz.
 */
public class AudioPlayerManager {

    private static final String TAG = "AudioPlayerManager";

    public static final String NOMBRE_ARCHIVO = "presentacion_equipo.m4a";

    // Se guarda el Context de la aplicacion, no la Activity, para no retenerla en memoria.
    private final Context appContext;

    public AudioPlayerManager(Context context) {
        this.appContext = context.getApplicationContext();
    }

    /**
     * Devuelve el archivo donde se graba y desde donde se reproduce el audio:
     * carpeta de musica de la app + NOMBRE_ARCHIVO.
     */
    public File getAudioFile() {
        File directorio = appContext.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        if (directorio == null) {
            // getExternalFilesDir devuelve null si el almacenamiento externo no esta disponible.
            directorio = appContext.getFilesDir();
        }
        if (!directorio.exists() && !directorio.mkdirs()) {
            Log.w(TAG, "No se pudo crear el directorio " + directorio.getAbsolutePath());
        }
        return new File(directorio, NOMBRE_ARCHIVO);
    }

    /**
     * Ruta absoluta del archivo. El grabador (tarea #10) debe usar esta misma ruta
     * en setOutputFile() para que la reproduccion encuentre la grabacion.
     */
    public String getAudioPath() {
        return getAudioFile().getAbsolutePath();
    }

    /**
     * Hay grabacion si el archivo existe y no esta vacio.
     */
    public boolean hasRecording() {
        File archivo = getAudioFile();
        return archivo.exists() && archivo.length() > 0;
    }
}
