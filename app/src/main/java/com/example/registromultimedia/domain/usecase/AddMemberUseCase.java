package com.example.registromultimedia.domain.usecase;

import com.example.registromultimedia.domain.MemberRepository;
import com.example.registromultimedia.model.Member;

/**
 * Caso de uso: Agregar Integrante al sistema.
 * Aplica el principio de responsabilidad única (SRP), validando que la entidad sea válida
 * antes de delegar el guardado al repositorio de dominio.
 */
public class AddMemberUseCase {

    private final MemberRepository repository;

    /**
     * Constructor para inyección del repositorio.
     * @param repository Interfaz de abstracción del repositorio de miembros.
     */
    public AddMemberUseCase(MemberRepository repository) {
        this.repository = repository;
    }

    /**
     * Ejecuta la regla de negocio para agregar un integrante.
     * @param member Entidad del miembro a guardar.
     */
    public void execute(Member member) {
        if (repository != null && member != null && member.getName() != null && !member.getName().trim().isEmpty()) {
            repository.addMember(member);
        }
    }
}
