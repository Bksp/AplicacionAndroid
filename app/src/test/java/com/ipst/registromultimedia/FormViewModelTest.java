package com.ipst.registromultimedia;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import com.ipst.registromultimedia.domain.usecase.ValidateMemberFormUseCase;
import com.ipst.registromultimedia.model.FormError;
import com.ipst.registromultimedia.viewmodel.FormViewModel;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class FormViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private FormViewModel viewModel;

    @Before
    public void setUp() {
        ValidateMemberFormUseCase useCase = new ValidateMemberFormUseCase();
        viewModel = new FormViewModel(useCase);
    }

    @Test
    public void testRegisterMemberSuccess() {
        viewModel.registerMember("Maria Lopez", "Designer", "maria@test.com");
        assertEquals(FormError.NONE, viewModel.getFormError().getValue());
        assertEquals(Boolean.TRUE, viewModel.getRegistrationSuccess().getValue());
    }

    @Test
    public void testRegisterMemberEmptyName() {
        viewModel.registerMember("", "Designer", "maria@test.com");
        assertEquals(FormError.EMPTY_NAME, viewModel.getFormError().getValue());
        assertEquals(Boolean.FALSE, viewModel.getRegistrationSuccess().getValue());
    }

    @Test
    public void testRegisterMemberInvalidEmail() {
        viewModel.registerMember("Maria Lopez", "Designer", "maria_invalid");
        assertEquals(FormError.INVALID_EMAIL, viewModel.getFormError().getValue());
        assertEquals(Boolean.FALSE, viewModel.getRegistrationSuccess().getValue());
    }
}
