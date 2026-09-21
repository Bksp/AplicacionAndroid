package com.example.registromultimedia.domain.usecase;

import androidx.lifecycle.LiveData;
import com.example.registromultimedia.data.MemberRepository;
import com.example.registromultimedia.model.Member;
import java.util.List;

/**
 * Propósito: Intermediario estricto para solicitar la lista de integrantes.
 */
public class GetMembersUseCase {

    // Referencia al almacén de datos (Repositorio)
    private MemberRepository memberRepository;

    // Conectamos el repositorio a este caso de uso
    public void setMemberRepository(MemberRepository repository) {
        this.memberRepository = repository;
    }

    /**
     * Ejecuta el caso de uso para conseguir los datos.
     * @return El flujo de datos reactivo (LiveData) con la lista de miembros.
     */
    public LiveData<List<Member>> execute() {
        // Validación preventiva para evitar bloqueos si olvidamos inyectar el repositorio
        if (memberRepository != null) {
            // Pedimos los datos y los pasamos hacia arriba (hacia el ViewModel)
            return memberRepository.getMembers();
        }
        // Si no hay repositorio, retornamos nulo.
        return null;
    }
}