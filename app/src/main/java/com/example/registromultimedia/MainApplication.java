package com.example.registromultimedia;

import android.app.Application;
import com.example.registromultimedia.data.MemberRepositoryImpl;
import com.example.registromultimedia.domain.MemberRepositoryProvider;
import com.google.android.material.color.DynamicColors;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Inicializar repositorio central inyectando el Context para lograr persistencia real
        MemberRepositoryProvider.setInstance(new MemberRepositoryImpl(this));
        
        // Aplicar colores dinámicos del sistema (Material You) si el dispositivo lo soporta (Android 12+)
        DynamicColors.applyToActivitiesIfAvailable(this);
    }
}
