package com.example.registromultimedia;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Enlazamos el botón verde flotante (+) para ir a Registro
        FloatingActionButton fabAgregar = findViewById(R.id.fab_agregar);
        fabAgregar.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, RegistroActivity.class);
            startActivity(intent);
        });

        // Enlazamos el botón de reproducir audio de la tarjeta 1
        MaterialButton btnReproducirTu = findViewById(R.id.btn_reproducir_audio_1);
        btnReproducirTu.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, ReproductorActivity.class);
            startActivity(intent);
        });
    }
}