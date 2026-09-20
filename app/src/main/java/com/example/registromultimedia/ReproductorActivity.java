package com.example.registromultimedia;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

// Cambiamos el import del botón genérico por el de Material Design
import com.google.android.material.button.MaterialButton;

public class ReproductorActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor);

        // Enlazamos el botón y le pasamos la instrucción de tu compañero
        MaterialButton btnCerrar = findViewById(R.id.btn_repro_close);
        btnCerrar.setOnClickListener(v -> finish());
    }
}