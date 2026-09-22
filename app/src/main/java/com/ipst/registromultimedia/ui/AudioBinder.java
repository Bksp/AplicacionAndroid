package com.ipst.registromultimedia.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.lifecycle.LifecycleOwner;
import com.ipst.registromultimedia.R;
import com.ipst.registromultimedia.VisualizarOndas;
import com.ipst.registromultimedia.data.MicPermissionHelper;
import com.ipst.registromultimedia.model.AudioState;
import com.ipst.registromultimedia.viewmodel.AudioViewModel;

/**
 * Custom View Binder connecting Audio UI controls with AudioViewModel.
 * Supports both G1 and G2 layout IDs safely.
 */
public class AudioBinder {

    private final Button btnRecordAudio;
    private final Button btnPlayAudio;
    private final TextView tvAudioState;
    private final VisualizarOndas visualizer;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable waveRunnable;
    private boolean isAnimating = false;

    public AudioBinder(View rootView) {
        View vRecord = rootView.findViewById(R.id.btnGrabarAudio);
        if (vRecord == null) vRecord = rootView.findViewById(R.id.btn_iniciar_grabacion);
        btnRecordAudio = (vRecord instanceof Button) ? (Button) vRecord : null;

        View vPlay = rootView.findViewById(R.id.btnReproducirAudio);
        if (vPlay == null) vPlay = rootView.findViewById(R.id.btn_detener_grabacion);
        if (vPlay == null) vPlay = rootView.findViewById(R.id.btn_reproducir_audio_1);
        btnPlayAudio = (vPlay instanceof Button) ? (Button) vPlay : null;

        View vState = rootView.findViewById(R.id.tvEstadoAudio);
        if (vState == null) vState = rootView.findViewById(R.id.tv_estado_audio);
        tvAudioState = (vState instanceof TextView) ? (TextView) vState : null;

        visualizer = rootView.findViewById(R.id.visualizador);

        waveRunnable = new Runnable() {
            @Override
            public void run() {
                if (isAnimating && visualizer != null) {
                    float amplitude = (float) (Math.random() * 80) + 10;
                    visualizer.agregarAmplitud(amplitude);
                    handler.postDelayed(this, 50);
                }
            }
        };
    }

    private void startWaveAnimation() {
        if (!isAnimating) {
            isAnimating = true;
            handler.post(waveRunnable);
        }
    }

    private void stopWaveAnimation() {
        isAnimating = false;
        handler.removeCallbacks(waveRunnable);
    }

    public void bind(LifecycleOwner owner, AudioViewModel audioViewModel, MicPermissionHelper micPermissionHelper, Runnable onAudioRecordedSuccess) {
        Context context = (btnRecordAudio != null) ? btnRecordAudio.getContext() : null;

        if (btnRecordAudio != null) {
            btnRecordAudio.setOnClickListener(v -> {
                if (micPermissionHelper != null && !micPermissionHelper.hasPermission()) {
                    micPermissionHelper.requestPermission();
                    return;
                }
                audioViewModel.toggleRecording();
            });
        }

        if (btnPlayAudio != null) {
            btnPlayAudio.setOnClickListener(v -> {
                AudioState state = audioViewModel.getAudioState().getValue();
                if (state == AudioState.RECORDING) {
                    audioViewModel.toggleRecording();
                } else {
                    audioViewModel.togglePlayback();
                }
            });
        }

        audioViewModel.getAudioState().observe(owner, state -> {
            if (state == null) return;

            switch (state) {
                case IDLE:
                    stopWaveAnimation();
                    if (btnRecordAudio != null) {
                        btnRecordAudio.setEnabled(true);
                        btnRecordAudio.setText("Iniciar");
                    }
                    if (btnPlayAudio != null) {
                        btnPlayAudio.setEnabled(false);
                        btnPlayAudio.setText("Detener");
                    }
                    if (tvAudioState != null) tvAudioState.setText("Audio: Inactivo");
                    break;

                case RECORDING:
                    startWaveAnimation();
                    if (btnRecordAudio != null) {
                        btnRecordAudio.setEnabled(false);
                        btnRecordAudio.setText("Grabando...");
                    }
                    if (btnPlayAudio != null) {
                        btnPlayAudio.setEnabled(true);
                        btnPlayAudio.setText("Detener");
                    }
                    if (tvAudioState != null) tvAudioState.setText("Audio: Grabando...");
                    if (context != null) {
                        Toast.makeText(context, "Grabación iniciada...", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case RECORDED:
                    stopWaveAnimation();
                    if (btnRecordAudio != null) {
                        btnRecordAudio.setEnabled(true);
                        btnRecordAudio.setText("Nuevo Audio");
                    }
                    if (btnPlayAudio != null) {
                        btnPlayAudio.setEnabled(true);
                        btnPlayAudio.setText("Reproducir");
                    }
                    if (tvAudioState != null) tvAudioState.setText("Audio: Grabación Lista");
                    if (context != null) {
                        Toast.makeText(context, "Grabación finalizada y guardada.", Toast.LENGTH_SHORT).show();
                    }
                    if (onAudioRecordedSuccess != null) {
                        onAudioRecordedSuccess.run();
                    }
                    break;

                case PLAYING:
                    stopWaveAnimation();
                    if (btnRecordAudio != null) {
                        btnRecordAudio.setEnabled(false);
                    }
                    if (btnPlayAudio != null) {
                        btnPlayAudio.setEnabled(true);
                        btnPlayAudio.setText("Detener Reproducción");
                    }
                    if (tvAudioState != null) tvAudioState.setText("Audio: Reproduciendo...");
                    break;

                case ERROR:
                    stopWaveAnimation();
                    if (btnRecordAudio != null) {
                        btnRecordAudio.setEnabled(true);
                        btnRecordAudio.setText("Iniciar");
                    }
                    if (btnPlayAudio != null) {
                        btnPlayAudio.setEnabled(false);
                    }
                    if (tvAudioState != null) tvAudioState.setText("Audio: Error");
                    if (context != null) {
                        Toast.makeText(context, "Error en el módulo de audio", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        });
    }
}
