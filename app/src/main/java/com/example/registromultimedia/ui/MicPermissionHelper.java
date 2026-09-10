package com.example.registromultimedia.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;

/**
 * Helper utility encapsulating runtime microphone permission checks and requests.
 */
public class MicPermissionHelper {

    private final ComponentActivity activity;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private Runnable onPermissionGranted;

    public MicPermissionHelper(ComponentActivity activity) {
        this.activity = activity;
        registerLauncher();
    }

    private void registerLauncher() {
        requestPermissionLauncher = activity.registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted && onPermissionGranted != null) {
                        onPermissionGranted.run();
                    }
                }
        );
    }

    public boolean hasPermission() {
        return ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission() {
        requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
    }

    public void setOnPermissionGranted(Runnable callback) {
        this.onPermissionGranted = callback;
    }
}
