package com.ipst.registromultimedia.data;

import android.content.Context;
import android.content.SharedPreferences;
import com.ipst.registromultimedia.domain.MemberRepository;
import com.ipst.registromultimedia.model.Member;
import java.util.ArrayList;
import java.util.List;

// Implementación de MemberRepository con persistencia básica (SharedPreferences).
public class MemberRepositoryImpl implements MemberRepository {

    private final List<Member> members;
    private final SharedPreferences prefs;

    // Constructor vacío por defecto
    public MemberRepositoryImpl() {
        this.members = new ArrayList<>();
        this.prefs = null;
    }

    // Constructor con contexto para habilitar la persistencia
    public MemberRepositoryImpl(Context context) {
        this.members = new ArrayList<>();
        if (context != null) {
            this.prefs = context.getSharedPreferences("miembros_db", Context.MODE_PRIVATE);
            cargarDePreferencias();
        } else {
            this.prefs = null;
        }
    }

    @Override
    public void addMember(Member member) {
        if (member != null) {
            members.add(member);
            guardarEnPreferencias();
        }
    }

    @Override
    public void removeMember(String id) {
        if (id == null) return;
        boolean removed = members.removeIf(m -> m != null && id.equals(m.getId()));
        if (removed) {
            guardarEnPreferencias();
        }
    }

    @Override
    public List<Member> getAllMembers() {
        return new ArrayList<>(members);
    }

    private void cargarDePreferencias() {
        if (prefs == null) return;
        String data = prefs.getString("members_list", "");
        if (!data.isEmpty()) {
            String[] lines = data.split(";;");
            for (String line : lines) {
                String[] parts = line.split("\\|", -1); // El -1 preserva los strings vacíos
                if (parts.length == 4) {
                    members.add(new Member(parts[0], parts[1], parts[2], parts[3]));
                }
            }
        }
    }

    private void guardarEnPreferencias() {
        if (prefs == null) return;
        StringBuilder sb = new StringBuilder();
        for (Member m : members) {
            sb.append(m.getId() != null ? m.getId() : "").append("|")
              .append(m.getName() != null ? m.getName() : "").append("|")
              .append(m.getRole() != null ? m.getRole() : "").append("|")
              .append(m.getEmail() != null ? m.getEmail() : "").append(";;");
        }
        prefs.edit().putString("members_list", sb.toString()).apply();
    }
}
