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

    public void startRecording(String outputFilePath) {
        try {
            mediaRecorder = new MediaRecorder();

            // CONCEPTO CLAVE DE ESTUDIO: La Máquina de Estados (State Machine)
            // MediaRecorder requiere que las configuraciones se hagan en un orden EXACTO.
            // Si cambias el orden (ej. poner el Encoder antes del Format), la app hace "crash".

            // 1. Origen (De dónde viene el sonido) -> Micrófono
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

            // 2. Formato del contenedor (Cómo se empaqueta el archivo) -> MPEG_4 (.m4a / .mp4)
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

            // 3. Codificador (Cómo se comprime el audio adentro del paquete) -> AAC (Alta calidad)
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

            // 4. Destino (Dónde se guarda)
            mediaRecorder.setOutputFile(outputFilePath);

            // prepare() reserva el micrófono a nivel de hardware.
            mediaRecorder.prepare();
            // start() comienza a escribir bytes en el archivo.
            mediaRecorder.start();

            Log.d(TAG, "Grabación iniciada con éxito en: " + outputFilePath);

        } catch (IOException e) {
            // Ocurre si la ruta de archivo es inválida, el disco está lleno o faltan permisos de almacenamiento.
            Log.e(TAG, "Error de Entrada/Salida al preparar MediaRecorder: " + e.getMessage());
            release();

        } catch (IllegalStateException e) {
            // Ocurre si violaste la "Máquina de estados" (ej. llamaste a start() antes de prepare()).
            Log.e(TAG, "Estado ilegal del MediaRecorder: " + e.getMessage());
            release();
        }
    }

    public void stopRecording() {
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
                Log.d(TAG, "Grabación detenida correctamente.");
            } catch (RuntimeException stopException) {
                // CONCEPTO CLAVE DE ESTUDIO: Detención prematura
                // Si llamas a start() e inmediatamente a stop() (en milisegundos), Android lanza un error
                // porque el archivo de audio no alcanzó a recibir metadatos y está corrupto.
                Log.e(TAG, "Error al detener MediaRecorder (posible detención prematura): " + stopException.getMessage());
            } finally {
                // Pase lo que pase en el try o en el catch, el finally se ejecuta SIEMPRE.
                // Esto garantiza que el micrófono quede libre para otras apps (como WhatsApp o llamadas).
                release();
            }
        }
    }

    public void release() {
        if (mediaRecorder != null) {
            try {
                mediaRecorder.release(); // Libera el micrófono en el sistema operativo.
                mediaRecorder = null;    // Corta la referencia en Java para que el "Garbage Collector" limpie la RAM.
                Log.d(TAG, "Recursos de MediaRecorder liberados.");
            } catch (Exception e) {
                Log.e(TAG, "Error intentando liberar los recursos: " + e.getMessage());
            }
        }
    }
}