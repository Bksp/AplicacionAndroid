package com.example.registromultimedia.ui;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.lifecycle.LifecycleOwner;
import com.example.registromultimedia.R;
import com.example.registromultimedia.model.FormError;
import com.example.registromultimedia.model.Member;
import com.example.registromultimedia.viewmodel.FormViewModel;
import com.example.registromultimedia.viewmodel.MembersViewModel;
import java.util.UUID;

/**
 * Custom View Binder connecting Form UI elements to FormViewModel and MembersViewModel.
 */
public class FormBinder {

    private final EditText etName;
    private final Spinner spRole;
    private final EditText etEmail;
    private final Button btnSave;

    public FormBinder(View rootView) {
        etName = rootView.findViewById(R.id.etNombre);
        spRole = rootView.findViewById(R.id.spRol);
        etEmail = rootView.findViewById(R.id.etCorreo);
        btnSave = rootView.findViewById(R.id.btnGuardar);
    }

    public void bind(LifecycleOwner owner, FormViewModel formViewModel, MembersViewModel membersViewModel) {
        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String role = spRole.getSelectedItem() != null ? spRole.getSelectedItem().toString() : "";
            String email = etEmail.getText().toString();
            formViewModel.registerMember(name, role, email);
        });

        formViewModel.getFormError().observe(owner, error -> {
            if (error == FormError.NONE) {
                etName.setError(null);
                etEmail.setError(null);
            } else if (error == FormError.EMPTY_NAME) {
                etName.setError("El nombre es requerido");
            } else if (error == FormError.ROLE_NOT_SELECTED) {
                Toast.makeText(btnSave.getContext(), "Seleccione un rol", Toast.LENGTH_SHORT).show();
            } else if (error == FormError.INVALID_EMAIL) {
                etEmail.setError("Ingrese un correo válido");
            }
        });

        formViewModel.getRegistrationSuccess().observe(owner, success -> {
            if (success) {
                String id = UUID.randomUUID().toString().substring(0, 8);
                String name = etName.getText().toString();
                String role = spRole.getSelectedItem() != null ? spRole.getSelectedItem().toString() : "";
                String email = etEmail.getText().toString();

                membersViewModel.addMember(new Member(id, name, role, email));

                etName.setText("");
                etEmail.setText("");
                Toast.makeText(btnSave.getContext(), "Integrante registrado con éxito", Toast.LENGTH_SHORT).show();
                formViewModel.resetState();
            }
        });
    }
}
