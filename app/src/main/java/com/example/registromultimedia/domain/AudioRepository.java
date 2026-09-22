package com.example.registromultimedia.domain;

/**
 * Interfaz que define el contrato de operaciones de audio en la capa de Domain.
 * Abstrae las funcionalidades de grabación, reproducción y consulta del archivo
 * para desacoplar la UI/ViewModel de las clases concretas de infraestructura (Data).
 */
public interface AudioRepository {

    /**
     * Verifica si existe un archivo de grabación guardado y no vacío.
     * @return true si existe una grabación válida, false en caso contrario.
     */
    boolean hasRecording();

    /**
     * Obtiene la ruta absoluta del archivo de audio grabado.
     * @return Ruta del archivo de audio.
     */
    String getAudioPath();

    /**
     * Inicia la captura de audio desde el micrófono.
     * @return true si la grabación inició con éxito, false en caso contrario.
     */
    boolean startRecording();

    /**
     * Detiene la grabación de audio activa.
     */
    void stopRecording();

    /**
     * Inicia la reproducción del audio grabado.
     * @param onCompletion Callback que se ejecuta cuando finaliza la reproducción.
     * @return true si la reproducción inició correctamente, false en caso contrario.
     */
    boolean startPlayback(Runnable onCompletion);

    /**
     * Detiene la reproducción de audio activa.
     */
    void stopPlayback();
}
