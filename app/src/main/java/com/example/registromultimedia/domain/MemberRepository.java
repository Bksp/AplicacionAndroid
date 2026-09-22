package com.example.registromultimedia.domain;

import com.example.registromultimedia.model.Member;
import java.util.List;

// Interfaz que define las operaciones disponibles para los miembros del equipo.
public interface MemberRepository {

    // Agrega un nuevo miembro al repositorio
    void addMember(Member member);

    // Elimina un miembro por su identificador único
    void removeMember(String id);

    // Retorna la lista completa de miembros registrados
    List<Member> getAllMembers();
}
