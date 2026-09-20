package com.example.appregistromulti;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class RegistroActivity extends AppCompatActivity {

    // Variables para la animación de ondas de tu compañero
    private VisualizarOndas visualizador;
    private Handler handler;
    private Runnable simuladorRunnable;
    private boolean estaGrabando = false;

    // Usamos MaterialButton respetando el estándar UI
    private MaterialButton btnIniciarGrabacion;
    private MaterialButton btnDetenerGrabacion;
    private MaterialButton btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // 1. Enlazamos los elementos del XML
        visualizador = findViewById(R.id.visualizador);
        btnIniciarGrabacion = findViewById(R.id.btn_iniciar_grabacion);
        btnDetenerGrabacion = findViewById(R.id.btn_detener_grabacion);
        btnGuardar = findViewById(R.id.btn_guardar);

        // 2. Preparamos el motor de la animación de las ondas
        handler = new Handler(Looper.getMainLooper());
        simuladorRunnable = new Runnable() {
            @Override
            public void run() {
                if (estaGrabando) {
                    // Genera un valor aleatorio para que la onda suba y baje
                    float amplitudSimulada = (float) (Math.random() * 80) + 10;
                    visualizador.agregarAmplitud(amplitudSimulada);
                    handler.postDelayed(this, 50); // Se repite cada 50 milisegundos
                }
            }
        };

        // 3. Lógica del botón INICIAR
        btnIniciarGrabacion.setOnClickListener(v -> {
            if (!estaGrabando) {
                estaGrabando = true;
                handler.post(simuladorRunnable); // Arranca la animación
                Toast.makeText(RegistroActivity.this, "🎙️ Simulando: Grabación iniciada...", Toast.LENGTH_SHORT).show();
            }
        });

        // 4. Lógica del botón DETENER
        btnDetenerGrabacion.setOnClickListener(v -> {
            estaGrabando = false;
            handler.removeCallbacks(simuladorRunnable); // Frena la animación
            Toast.makeText(RegistroActivity.this, "⏹️ Simulando: Grabación detenida.", Toast.LENGTH_SHORT).show();
        });

        // 5. Lógica del botón GUARDAR (Ajustada para UX)
        btnGuardar.setOnClickListener(v -> {
            Toast.makeText(RegistroActivity.this, "✅ Integrante guardado con éxito", Toast.LENGTH_SHORT).show();

            // Cerramos esta ventana. Al hacerlo, Android nos devuelve
            // naturalmente a la pantalla que estaba de fondo (Dashboard).
            finish();
        });
    }

    // Si el usuario cierra la pantalla de golpe, nos aseguramos de detener el motor de animación
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacks(simuladorRunnable);
        }
    }
}