package com.ipst.registromultimedia.model;

/**
 * Model entity representing a team member.
 */
public class Member {
    private String id;
    private String name;
    private String role;
    private String email;

    public Member() {
    }

    public Member(String id, String name, String role, String email) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Extrae las iniciales del nombre del integrante siguiendo reglas del modelo de dominio.
     * @return Iniciales en mayúsculas (ej. "CM" para "Carlos Mendoza", "A" para "Ana").
     */
    public String getInitials() {
        if (name == null || name.trim().isEmpty()) {
            return "?";
        }
        String trimmed = name.trim();
        String[] parts = trimmed.split("\\s+");
        if (parts.length == 1) {
            return parts[0].substring(0, 1).toUpperCase();
        }
        String first = parts[0];
        String last = parts[parts.length - 1];
        StringBuilder initials = new StringBuilder();
        if (!first.isEmpty()) initials.append(first.substring(0, 1).toUpperCase());
        if (!last.isEmpty()) initials.append(last.substring(0, 1).toUpperCase());
        return initials.length() > 0 ? initials.toString() : "?";
    }
}
