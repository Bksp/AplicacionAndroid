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
 * Supports both G1 and G2 layout IDs safely.
 */
public class FormBinder {

    private final EditText etName;
    private final Spinner spRole;
    private final EditText etEmail;
    private final Button btnSave;

    public FormBinder(View rootView) {
        View vName = rootView.findViewById(R.id.etNombre);
        if (vName == null) vName = rootView.findViewById(R.id.et_nombre);
        etName = (vName instanceof EditText) ? (EditText) vName : null;

        View vRole = rootView.findViewById(R.id.spRol);
        if (vRole == null) vRole = rootView.findViewById(R.id.spn_rol);
        spRole = (vRole instanceof Spinner) ? (Spinner) vRole : null;

        View vEmail = rootView.findViewById(R.id.etCorreo);
        if (vEmail == null) vEmail = rootView.findViewById(R.id.et_correo);
        etEmail = (vEmail instanceof EditText) ? (EditText) vEmail : null;

        View vSave = rootView.findViewById(R.id.btnGuardar);
        if (vSave == null) vSave = rootView.findViewById(R.id.btn_guardar);
        btnSave = (vSave instanceof Button) ? (Button) vSave : null;
    }

    public void bind(LifecycleOwner owner, FormViewModel formViewModel, MembersViewModel membersViewModel) {
        if (btnSave != null) {
            btnSave.setOnClickListener(v -> {
                String name = etName != null ? etName.getText().toString() : "";
                String role = (spRole != null && spRole.getSelectedItem() != null) ? spRole.getSelectedItem().toString() : "";
                String email = etEmail != null ? etEmail.getText().toString() : "";
                formViewModel.registerMember(name, role, email);
            });
        }

        formViewModel.getFormError().observe(owner, error -> {
            if (error == FormError.NONE) {
                if (etName != null) etName.setError(null);
                if (etEmail != null) etEmail.setError(null);
            } else if (error == FormError.EMPTY_NAME) {
                if (etName != null) etName.setError("El nombre es requerido");
            } else if (error == FormError.ROLE_NOT_SELECTED) {
                if (btnSave != null) {
                    Toast.makeText(btnSave.getContext(), "Seleccione un rol", Toast.LENGTH_SHORT).show();
                }
            } else if (error == FormError.INVALID_EMAIL) {
                if (etEmail != null) etEmail.setError("Ingrese un correo válido");
            }
        });

        formViewModel.getRegistrationSuccess().observe(owner, success -> {
            if (success) {
                String id = UUID.randomUUID().toString().substring(0, 8);
                String name = etName != null ? etName.getText().toString() : "";
                String role = (spRole != null && spRole.getSelectedItem() != null) ? spRole.getSelectedItem().toString() : "";
                String email = etEmail != null ? etEmail.getText().toString() : "";

                membersViewModel.addMember(new Member(id, name, role, email));

                if (etName != null) etName.setText("");
                if (etEmail != null) etEmail.setText("");
                if (btnSave != null) {
                    Toast.makeText(btnSave.getContext(), "Integrante registrado con éxito", Toast.LENGTH_SHORT).show();
                }
                formViewModel.resetState();
            }
        });
    }
}
