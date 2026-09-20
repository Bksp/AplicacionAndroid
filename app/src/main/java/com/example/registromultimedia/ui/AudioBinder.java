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
 * Supports both G1 and G2 layout IDs safely.
 */
public class AudioBinder {

    private final Button btnRecordAudio;
    private final Button btnPlayAudio;
    private final TextView tvAudioState;

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
    }

    public void bind(LifecycleOwner owner, AudioViewModel audioViewModel, MicPermissionHelper micPermissionHelper, Runnable onAudioRecordedSuccess) {
        if (btnRecordAudio != null) {
            btnRecordAudio.setOnClickListener(v -> {
                if (!micPermissionHelper.hasPermission()) {
                    micPermissionHelper.requestPermission();
                    return;
                }
                audioViewModel.toggleRecording();
            });
        }

        if (btnPlayAudio != null) {
            btnPlayAudio.setOnClickListener(v -> audioViewModel.togglePlayback());
        }

        audioViewModel.getAudioState().observe(owner, state -> {
            if (state == null) return;

            switch (state) {
                case IDLE:
                    if (btnRecordAudio != null) btnRecordAudio.setText("Grabar Audio");
                    if (btnPlayAudio != null) btnPlayAudio.setEnabled(false);
                    if (tvAudioState != null) tvAudioState.setText("Audio: Inactivo");
                    break;
                case RECORDING:
                    if (btnRecordAudio != null) btnRecordAudio.setText("Detener Grabación");
                    if (btnPlayAudio != null) btnPlayAudio.setEnabled(false);
                    if (tvAudioState != null) tvAudioState.setText("Audio: Grabando...");
                    break;
                case RECORDED:
                    if (btnRecordAudio != null) btnRecordAudio.setText("Grabar Nuevo Audio");
                    if (btnPlayAudio != null) {
                        btnPlayAudio.setEnabled(true);
                        btnPlayAudio.setText("Reproducir Audio");
                    }
                    if (tvAudioState != null) tvAudioState.setText("Audio: Grabación Lista");
                    if (onAudioRecordedSuccess != null) {
                        onAudioRecordedSuccess.run();
                    }
                    break;
                case PLAYING:
                    if (btnRecordAudio != null) btnRecordAudio.setEnabled(false);
                    if (btnPlayAudio != null) btnPlayAudio.setText("Detener Reproducción");
                    if (tvAudioState != null) tvAudioState.setText("Audio: Reproduciendo...");
                    break;
                case ERROR:
                    if (btnRecordAudio != null) {
                        Toast.makeText(btnRecordAudio.getContext(), "Error en el módulo de audio", Toast.LENGTH_SHORT).show();
                        btnRecordAudio.setEnabled(true);
                    }
                    if (btnPlayAudio != null) btnPlayAudio.setEnabled(false);
                    if (tvAudioState != null) tvAudioState.setText("Audio: Error");
                    break;
            }
        });
    }
}
