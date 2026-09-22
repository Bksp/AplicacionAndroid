package com.ipst.registromultimedia.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.ipst.registromultimedia.domain.usecase.ValidateMemberFormUseCase;
import com.ipst.registromultimedia.model.FormError;

/**
 * ViewModel managing member registration form validation logic.
 * Consumes exclusivamente el Caso de Uso ValidateMemberFormUseCase de la capa Domain.
 * Strictly decoupled from Android UI and Data classes.
 */
public class FormViewModel extends ViewModel {

    private final MutableLiveData<FormError> formError = new MutableLiveData<>(FormError.NONE);
    private final MutableLiveData<Boolean> registrationSuccess = new MutableLiveData<>(false);
    private final ValidateMemberFormUseCase validateMemberFormUseCase;

    public FormViewModel() {
        this(new ValidateMemberFormUseCase());
    }

    public FormViewModel(ValidateMemberFormUseCase validateMemberFormUseCase) {
        this.validateMemberFormUseCase = validateMemberFormUseCase;
    }

    public LiveData<FormError> getFormError() {
        return formError;
    }

    public LiveData<Boolean> getRegistrationSuccess() {
        return registrationSuccess;
    }

    public void registerMember(String name, String role, String email) {
        FormError error = validateMemberFormUseCase != null
                ? validateMemberFormUseCase.execute(name, role, email)
                : FormError.NONE;

        formError.setValue(error);

        if (error == FormError.NONE) {
            registrationSuccess.setValue(true);
        } else {
            registrationSuccess.setValue(false);
        }
    }

    public void resetState() {
        formError.setValue(FormError.NONE);
        registrationSuccess.setValue(false);
    }
}
