package com.example.registromultimedia.domain.usecase;

import com.example.registromultimedia.domain.MemberRepository;
import com.example.registromultimedia.domain.MemberRepositoryProvider;
import com.example.registromultimedia.model.Member;
import java.util.Collections;
import java.util.List;

/**
 * Caso de uso: Obtener la lista de integrantes registrados.
 */
public class GetMembersUseCase {

    private final MemberRepository repository;

    /**
     * Constructor por defecto que obtiene el repositorio registrado.
     */
    public GetMembersUseCase() {
        this(MemberRepositoryProvider.getInstance());
    }

    /**
     * Constructor para inyección del repositorio.
     * @param repository Interfaz de abstracción del repositorio de miembros.
     */
    public GetMembersUseCase(MemberRepository repository) {
        this.repository = repository;
    }

    /**
     * Ejecuta la consulta de la lista de integrantes.
     * @return Lista de integrantes registrados.
     */
    public List<Member> execute() {
        if (repository != null) {
            return repository.getAllMembers();
        }
        return Collections.emptyList();
    }
}
