package com.example.registromultimedia.ui;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.lifecycle.LifecycleOwner;
import com.example.registromultimedia.R;
import com.example.registromultimedia.model.AudioState;
import com.example.registromultimedia.viewmodel.AudioViewModel;

/**
 * Custom View Binder connecting Audio UI controls with AudioViewModel.
 */
public class AudioBinder {

    private final Button btnRecordAudio;
    private final Button btnPlayAudio;
    private final TextView tvAudioState;

    public AudioBinder(View rootView) {
        btnRecordAudio = rootView.findViewById(R.id.btnGrabarAudio);
        btnPlayAudio = rootView.findViewById(R.id.btnReproducirAudio);
        tvAudioState = rootView.findViewById(R.id.tvEstadoAudio);
    }

    public void bind(LifecycleOwner owner, AudioViewModel audioViewModel, MicPermissionHelper micPermissionHelper, Runnable onAudioRecordedSuccess) {
        btnRecordAudio.setOnClickListener(v -> {
            if (!micPermissionHelper.hasPermission()) {
                micPermissionHelper.requestPermission();
                return;
            }
            audioViewModel.toggleRecording();
        });

        btnPlayAudio.setOnClickListener(v -> audioViewModel.togglePlayback());

        audioViewModel.getAudioState().observe(owner, state -> {
            if (state == null) return;

            switch (state) {
                case IDLE:
                    btnRecordAudio.setText("Grabar Audio");
                    btnPlayAudio.setEnabled(false);
                    tvAudioState.setText("Audio: Inactivo");
                    break;
                case RECORDING:
                    btnRecordAudio.setText("Detener Grabación");
                    btnPlayAudio.setEnabled(false);
                    tvAudioState.setText("Audio: Grabando...");
                    break;
                case RECORDED:
                    btnRecordAudio.setText("Grabar Nuevo Audio");
                    btnPlayAudio.setEnabled(true);
                    btnPlayAudio.setText("Reproducir Audio");
                    tvAudioState.setText("Audio: Grabación Lista");
                    if (onAudioRecordedSuccess != null) {
                        onAudioRecordedSuccess.run();
                    }
                    break;
                case PLAYING:
                    btnRecordAudio.setEnabled(false);
                    btnPlayAudio.setText("Detener Reproducción");
                    tvAudioState.setText("Audio: Reproduciendo...");
                    break;
                case ERROR:
                    Toast.makeText(btnRecordAudio.getContext(), "Error en el módulo de audio", Toast.LENGTH_SHORT).show();
                    btnRecordAudio.setEnabled(true);
                    btnPlayAudio.setEnabled(false);
                    tvAudioState.setText("Audio: Error");
                    break;
            }
        });
    }
}
