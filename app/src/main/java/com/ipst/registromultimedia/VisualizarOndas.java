package com.ipst.registromultimedia;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.google.android.material.color.MaterialColors;

import java.util.ArrayList;
import java.util.List;

public class VisualizarOndas extends View {

    private Paint lapizOndas;
    private Paint lapizCentro;
    private List<Float> amplitudes = new ArrayList<>();

    // Configuraciones estéticas
    private final int ESPACIADO_BARRAS = 15; // Espacio entre cada línea
    private final int ANCHO_BARRA = 8;
    private final int MAX_BARRAS = 100; // Límite para no saturar la memoria

    public VisualizarOndas(Context context, AttributeSet attrs) {
        super(context, attrs);
        inicializar();
    }

    private void inicializar() {
        int colorTema = MaterialColors.getColor(this, androidx.appcompat.R.attr.colorPrimary, Color.parseColor("#006341"));

        // Estilo de las ondas grises
        lapizOndas = new Paint();
        lapizOndas.setColor(Color.parseColor("#9E9E9E")); // Gris claro
        lapizOndas.setStrokeWidth(ANCHO_BARRA);
        lapizOndas.setStrokeCap(Paint.Cap.ROUND); // Puntas redondeadas
        lapizOndas.setAntiAlias(true);

        // Estilo de la línea central con color del tema
        lapizCentro = new Paint();
        lapizCentro.setColor(colorTema);
        lapizCentro.setStrokeWidth(4f);
        lapizCentro.setAntiAlias(true);
    }

    // Este es el método que llamarás para alimentar la vista
    public void agregarAmplitud(float amplitud) {
        // Si es 0 (silencio), le damos un tamaño mínimo de 2 pixeles para que se vea un puntito/línea plana
        float altura = Math.max(amplitud, 2f);
        amplitudes.add(altura);

        // Mantenemos la lista limpia
        if (amplitudes.size() > MAX_BARRAS) {
            amplitudes.remove(0);
        }

        // Obliga a la vista a redibujarse
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int ancho = getWidth();
        int alto = getHeight();
        int centroY = alto / 2;
        int centroX = ancho / 2;

        // 1. Dibujar la línea fija en el centro con el color del tema
        canvas.drawLine(centroX, centroY - 80, centroX, centroY + 80, lapizCentro);

        // 2. Dibujar el historial de ondas moviéndose hacia la izquierda
        int posicionX = centroX - ESPACIADO_BARRAS;

        // Recorremos la lista de amplitudes de la más reciente a la más antigua
        for (int i = amplitudes.size() - 1; i >= 0; i--) {
            float alturaBarra = amplitudes.get(i);

            // Dibuja la línea hacia arriba y hacia abajo desde el centro
            canvas.drawLine(posicionX, centroY - alturaBarra, posicionX, centroY + alturaBarra, lapizOndas);

            posicionX -= ESPACIADO_BARRAS; // Nos movemos a la izquierda para la siguiente barra

            // Si nos salimos de la pantalla por la izquierda, dejamos de dibujar
            if (posicionX < 0) {
                break;
            }
        }
    }
}
