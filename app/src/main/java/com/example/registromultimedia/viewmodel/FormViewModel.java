package com.example.registromultimedia.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.registromultimedia.model.FormError;

/**
 * ViewModel managing member registration form validation logic.
 * Strictly decoupled from Android UI classes.
 */
public class FormViewModel extends ViewModel {

    private final MutableLiveData<FormError> formError = new MutableLiveData<>(FormError.NONE);
    private final MutableLiveData<Boolean> registrationSuccess = new MutableLiveData<>(false);

    public LiveData<FormError> getFormError() {
        return formError;
    }

    public LiveData<Boolean> getRegistrationSuccess() {
        return registrationSuccess;
    }

    public void registerMember(String name, String role, String email) {
        if (name == null || name.trim().isEmpty()) {
            formError.setValue(FormError.EMPTY_NAME);
            return;
        }

        if (role == null || role.trim().isEmpty()) {
            formError.setValue(FormError.ROLE_NOT_SELECTED);
            return;
        }

        if (email == null || !email.contains("@")) {
            formError.setValue(FormError.INVALID_EMAIL);
            return;
        }

        formError.setValue(FormError.NONE);
        registrationSuccess.setValue(true);
    }

    public void resetState() {
        formError.setValue(FormError.NONE);
        registrationSuccess.setValue(false);
    }
}
