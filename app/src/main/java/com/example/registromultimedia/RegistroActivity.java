package com.example.registromultimedia;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.registromultimedia.data.AudioPlayerManager;
import com.example.registromultimedia.data.MicPermissionHelper;
import com.example.registromultimedia.ui.AudioBinder;
import com.example.registromultimedia.ui.FormBinder;
import com.example.registromultimedia.viewmodel.AudioViewModel;
import com.example.registromultimedia.viewmodel.FormViewModel;
import com.example.registromultimedia.viewmodel.MembersViewModel;
import com.google.android.material.appbar.MaterialToolbar;

public class RegistroActivity extends AppCompatActivity {

    // ViewModels y Binders
    private FormViewModel formViewModel;
    private MembersViewModel membersViewModel;
    private AudioViewModel audioViewModel;

    private FormBinder formBinder;
    private AudioBinder audioBinder;
    private MicPermissionHelper micPermissionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Configurar la barra superior
        MaterialToolbar toolbar = findViewById(R.id.toolbar_registro);
        if (toolbar != null) {
            // Flecha Volver (Izquierda) -> regresa a la pantalla anterior
            toolbar.setNavigationOnClickListener(v -> finish());
        }

        // Cargar los roles en el combobox desplegable
        View vRol = findViewById(R.id.spn_rol);
        if (vRol instanceof AutoCompleteTextView) {
            String[] roles = getResources().getStringArray(R.array.roles_array);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, roles);
            ((AutoCompleteTextView) vRol).setAdapter(adapter);
        }

        // Inicializar ViewModels
        formViewModel = new ViewModelProvider(this).get(FormViewModel.class);
        membersViewModel = new ViewModelProvider(this).get(MembersViewModel.class);
        audioViewModel = new ViewModelProvider(this).get(AudioViewModel.class);

        // Configurar el sistema de grabado de audio real
        AudioPlayerManager audioPlayerManager = new AudioPlayerManager(this);
        audioViewModel.initRecorder(audioPlayerManager.getAudioPath());

        micPermissionHelper = new MicPermissionHelper(this);
        micPermissionHelper.setOnPermissionGranted(audioViewModel::toggleRecording);

        // Conectar los Binders a las Vistas
        formBinder = new FormBinder(getWindow().getDecorView().getRootView());
        formBinder.bind(this, formViewModel, membersViewModel);

        audioBinder = new AudioBinder(getWindow().getDecorView().getRootView());
        audioBinder.bind(this, audioViewModel, micPermissionHelper, null);

        // Observamos el éxito para cerrar la ventana después del guardado
        formViewModel.getRegistrationSuccess().observe(this, success -> {
            if (success) {
                new Handler(Looper.getMainLooper()).postDelayed(this::finish, 500);
            }
        });
    }
}
