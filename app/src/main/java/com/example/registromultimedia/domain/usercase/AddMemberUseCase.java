package com.example.registromultimedia.domain.usecase;

import com.example.registromultimedia.data.MemberRepository;
import com.example.registromultimedia.model.Member;

/**
 * Propósito: Representar una única acción de negocio (Caso de Uso: "Agregar Integrante").
 * Aplica el Principio de Responsabilidad Única (SRP): esta clase hace una sola cosa,
 * evaluar si un integrante es válido y mandarlo a guardar.
 */
public class AddMemberUseCase {

    // Necesitamos el repositorio porque el Caso de Uso sabe las reglas,
    // pero no sabe cómo guardar en la memoria. Esa es tarea del repositorio.
    private MemberRepository memberRepository;

    // Inyectamos la dependencia mediante este método (Setter) para conectar las capas.
    public void setMemberRepository(MemberRepository repository) {
        this.memberRepository = repository;
    }

    /**
     * Ejecuta la lógica del caso de uso.
     * Aquí programamos las "Reglas de Negocio" antes de tocar la base de datos.
     */
    public void execute(Member member) {
        // REGLA DE SEGURIDAD LÓGICA:
        // Solo procedemos si el repositorio está conectado, si el miembro existe,
        // y si el nombre no es nulo ni está vacío (usamos trim() para ignorar espacios en blanco).
        if (memberRepository != null && member != null && member.getName() != null && !member.getName().trim().isEmpty()) {

            // Si el integrante pasa la prueba, le pasamos la responsabilidad al repositorio para que lo guarde.
            memberRepository.addMember(member);
        }
    }
}