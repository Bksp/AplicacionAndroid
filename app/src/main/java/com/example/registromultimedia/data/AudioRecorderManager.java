package com.example.registromultimedia.data;

import android.media.MediaRecorder;
import android.util.Log;
import java.io.IOException;

/**
 * Propósito: Gestionar el ciclo de vida del hardware del micrófono (MediaRecorder)
 * para grabar audios aplicando configuraciones estrictas de codificación.
 */
public class AudioRecorderManager {

    private MediaRecorder mediaRecorder;
    private static final String TAG = "AudioRecorderManager";

    /**
     * Inicia la grabación del micrófono y guarda el resultado en la ruta indicada.
     * @param outputFilePath Ruta completa donde se guardará el archivo (.m4a / .mp4)
     */
    public void startRecording(String outputFilePath) {
        try {
            // 1. Inicializamos el objeto MediaRecorder
            mediaRecorder = new MediaRecorder();

            // 2. CONFIGURACIÓN ESTRICTA DE HARDWARE REQUERIDA
            // Nota: El orden es vital en Android (Source -> Format -> Encoder)
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

            // 3. Asignamos la ruta de salida
            mediaRecorder.setOutputFile(outputFilePath);

            // 4. Preparamos e iniciamos la grabación de forma segura
            mediaRecorder.prepare();
            mediaRecorder.start();

            Log.d(TAG, "Grabación iniciada con éxito en: " + outputFilePath);

        } catch (IOException e) {
            // Captura errores si la ruta de archivo es inválida o no hay permisos
            Log.e(TAG, "Error de Entrada/Salida al preparar MediaRecorder: " + e.getMessage());
            release(); // Forzamos liberación en caso de fallo crítico

        } catch (IllegalStateException e) {
            // Captura errores si se intentó iniciar en un estado incorrecto del ciclo de vida
            Log.e(TAG, "Estado ilegal del MediaRecorder: " + e.getMessage());
            release();
        }
    }

    /**
     * Detiene la grabación actual y libera los recursos de hardware.
     */
    public void stopRecording() {
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
                Log.d(TAG, "Grabación detenida correctamente.");
            } catch (RuntimeException stopException) {
                // RuntimeException ocurre típicamente si llamas a stop() inmediatamente
                // después de start() sin darle tiempo al hardware de recibir datos.
                Log.e(TAG, "Error al detener MediaRecorder (posible detención prematura): " + stopException.getMessage());
            } finally {
                // SIEMPRE debemos liberar el micrófono al detener, pase lo que pase.
                release();
            }
        }
    }

    /**
     * Libera los recursos del hardware.
     * Es obligatorio llamarlo para evitar fugas de memoria o bloquear el micrófono a otras apps.
     */
    public void release() {
        if (mediaRecorder != null) {
            try {
                mediaRecorder.release();
                mediaRecorder = null; // Lo volvemos nulo para reiniciar el ciclo de vida limpio
                Log.d(TAG, "Recursos de MediaRecorder liberados.");
            } catch (Exception e) {
                Log.e(TAG, "Error intentando liberar los recursos: " + e.getMessage());
            }
        }
    }
}