package com.ipst.registromultimedia;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Usamos un Handler para crear un retraso de 3 segundos (3000 milisegundos)
        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            // Preparamos el salto hacia el MainActivity (El Login)
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);

            // Cerramos el Splash para que el usuario no pueda volver atrás
            finish();

        }, 3000);
    }
}