package com.example.registromultimedia.data;

import com.example.registromultimedia.domain.MemberRepository;
import com.example.registromultimedia.model.Member;
import java.util.ArrayList;
import java.util.List;

// Implementación en memoria de MemberRepository.
// "En memoria" significa que los datos solo existen mientras la app está
// abierta: se guardan en un ArrayList (RAM), no en un archivo ni base de
// datos, así que se pierden al cerrar la app.
public class MemberRepositoryImpl implements MemberRepository {

    // Lista interna donde se guardan realmente los miembros.
    // Es "final" porque la referencia a la lista no cambia (solo su contenido).
    private final List<Member> members;

    // Constructor: al crear el repositorio, se inicializa la lista vacía.
    public MemberRepositoryImpl() {
        this.members = new ArrayList<>();
    }

    @Override
    public void addMember(Member member) {
        // Se valida que no sea null antes de agregar, por seguridad.
        if (member != null) {
            members.add(member);
        }
    }

    @Override
    public List<Member> getAllMembers() {
        // IMPORTANTE: se retorna una COPIA de la lista (new ArrayList<>(members))
        // en vez de la lista original. Así, si alguien modifica la lista que
        // recibe (por ejemplo el RecyclerView), no altera los datos internos
        // del repositorio por accidente. Esto se llama "encapsulamiento".
        return new ArrayList<>(members);
    }
}