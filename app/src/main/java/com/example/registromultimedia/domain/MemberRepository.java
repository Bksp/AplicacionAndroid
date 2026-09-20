package com.example.registromultimedia.domain;

import com.example.registromultimedia.model.Member;
import java.util.List;

// Interfaz que define que operaciones se pueden hacer con los datos de los
// miembros, pero no como  se implementan (eso lo decide la capa Data).
// Esto permite cambiar el almacenamiento en el futuro (memoria, base de
// datos, red) sin tocar el resto de la app.
public interface MemberRepository {

    // Agrega un nuevo miembro al repositorio
    void addMember(Member member);

    // Retorna la lista completa de miembros registrados
    List<Member> getAllMembers();
}