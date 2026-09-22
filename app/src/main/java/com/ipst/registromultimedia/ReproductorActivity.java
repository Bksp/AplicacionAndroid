package com.ipst.registromultimedia;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.ipst.registromultimedia.data.AudioPlayerManager;
import com.ipst.registromultimedia.model.AudioState;
import com.ipst.registromultimedia.viewmodel.AudioViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.util.Locale;

public class ReproductorActivity extends AppCompatActivity {

    private AudioViewModel audioViewModel;
    private AudioPlayerManager audioPlayerManager;
    private MaterialButton btnPlayPause;
    private ProgressBar pbProgreso;
    private TextView tvTiempo;

    private Handler progressHandler;
    private Runnable progressRunnable;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor);

        MaterialToolbar toolbar = findViewById(R.id.toolbar_reproductor);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> finish());
        }

        MaterialButton btnCerrar = findViewById(R.id.btn_repro_close);
        if (btnCerrar != null) {
            btnCerrar.setOnClickListener(v -> finish());
        }

        btnPlayPause = findViewById(R.id.btn_repro_play_pause);
        pbProgreso = findViewById(R.id.pb_repro_progreso);
        tvTiempo = findViewById(R.id.tv_repro_tiempo);

        audioPlayerManager = new AudioPlayerManager(this);
        audioViewModel = new ViewModelProvider(this).get(AudioViewModel.class);

        String pathExtra = getIntent().getStringExtra("AUDIO_PATH");
        String audioPath = (pathExtra != null && !pathExtra.isEmpty()) ? pathExtra : audioPlayerManager.getAudioPath();
        audioViewModel.initRecorder(audioPath);

        updateInitialTime();

        progressHandler = new Handler(Looper.getMainLooper());
        progressRunnable = new Runnable() {
            @Override
            public void run() {
                if (audioViewModel.getAudioState().getValue() == AudioState.PLAYING) {
                    int currentPos = audioViewModel.getCurrentPosition();
                    int totalDuration = audioViewModel.getDuration();

                    if (totalDuration > 0 && pbProgreso != null) {
                        pbProgreso.setMax(totalDuration);
                        pbProgreso.setProgress(currentPos);
                    }
                    if (tvTiempo != null) {
                        tvTiempo.setText(formatTime(currentPos) + " / " + formatTime(totalDuration));
                    }
                    progressHandler.postDelayed(this, 100);
                }
            }
        };

        if (btnPlayPause != null) {
            btnPlayPause.setOnClickListener(v -> {
                if (!audioPlayerManager.hasRecording()) {
                    Toast.makeText(this, "No hay grabación de audio disponible", Toast.LENGTH_SHORT).show();
                    return;
                }
                audioViewModel.togglePlayback();
            });
        }

        audioViewModel.getAudioState().observe(this, state -> {
            if (state == null) return;
            if (state == AudioState.PLAYING) {
                if (btnPlayPause != null) {
                    btnPlayPause.setText(R.string.btn_detener);
                    btnPlayPause.setIconResource(android.R.drawable.ic_media_pause);
                }
                progressHandler.post(progressRunnable);
            } else {
                if (btnPlayPause != null) {
                    btnPlayPause.setText("Reproducir");
                    btnPlayPause.setIconResource(android.R.drawable.ic_media_play);
                }
                progressHandler.removeCallbacks(progressRunnable);
                if (pbProgreso != null) {
                    pbProgreso.setProgress(0);
                }
                updateInitialTime();
            }
        });
    }

    private void updateInitialTime() {
        int totalDuration = audioViewModel != null ? audioViewModel.getDuration() : 0;
        if (pbProgreso != null) {
            pbProgreso.setMax(totalDuration > 0 ? totalDuration : 100);
            pbProgreso.setProgress(0);
        }
        if (tvTiempo != null) {
            tvTiempo.setText("00:00 / " + formatTime(totalDuration));
        }
    }

    private String formatTime(int timeMs) {
        if (timeMs <= 0) return "00:00";
        int seconds = (timeMs / 1000) % 60;
        int minutes = (timeMs / (1000 * 60)) % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (audioViewModel != null && audioViewModel.getAudioState().getValue() == AudioState.PLAYING) {
            audioViewModel.togglePlayback();
        }
    }
}
