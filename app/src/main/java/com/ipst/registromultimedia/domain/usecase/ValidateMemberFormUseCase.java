package com.ipst.registromultimedia.domain.usecase;

import com.ipst.registromultimedia.model.FormError;

/**
 * Caso de uso: Validar los datos del formulario de registro de integrantes.
 * Aplica las reglas de negocio para verificar que los datos ingresados sean válidos.
 */
public class ValidateMemberFormUseCase {

    /**
     * Ejecuta la validación de los datos del formulario.
     * @param name Nombre del integrante.
     * @param role Rol seleccionado.
     * @param email Correo electrónico.
     * @return FormError indicando el resultado de la validación.
     */
    public FormError execute(String name, String role, String email) {
        if (name == null || name.trim().isEmpty()) {
            return FormError.EMPTY_NAME;
        }

        if (role == null || role.trim().isEmpty()) {
            return FormError.ROLE_NOT_SELECTED;
        }

        if (email == null || !email.contains("@")) {
            return FormError.INVALID_EMAIL;
        }

        return FormError.NONE;
    }
}
