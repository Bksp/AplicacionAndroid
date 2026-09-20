package com.example.appregistromulti;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etUsuario;
    private EditText etPassword;
    private Button btnIngresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsuario = findViewById(R.id.et_usuario_login);
        etPassword = findViewById(R.id.et_password_login);
        btnIngresar = findViewById(R.id.btn_ingresar_login);

        btnIngresar.setOnClickListener(view -> {
            String usuario = etUsuario.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // 1. Validamos que no estén vacíos
            if (usuario.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();

                // 2. Validamos las credenciales oficiales
            } else if (usuario.equals("admin") && password.equals("1234")) {
                Toast.makeText(MainActivity.this, "¡Credenciales correctas! Entrando...", Toast.LENGTH_SHORT).show();

                // Ahora el Login nos lleva al Dashboard
                android.content.Intent intent = new android.content.Intent(MainActivity.this, DashboardActivity.class);
                startActivity(intent);
                // 3. Bloqueamos credenciales incorrectas
            } else {
                Toast.makeText(MainActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}