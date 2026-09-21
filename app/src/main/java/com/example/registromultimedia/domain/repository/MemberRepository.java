package com.example.registromultimedia.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.registromultimedia.model.Member;
import java.util.ArrayList;
import java.util.List;

/**
 * Propósito: Actuar como la "Fuente Única de Verdad" (Single Source of Truth) de la aplicación.
 * El repositorio es el único lugar que sabe cómo y dónde se guardan los datos, aislando esta responsabilidad del resto del sistema.
 */
public class MemberRepository {

    // Utilizamos MutableLiveData para que la lista sea "reactiva".
    // Esto significa que cuando agreguemos un miembro, este contenedor avisará
    // automáticamente a la pantalla para que se actualice, sin tener que programar ese aviso a mano.
    private final MutableLiveData<List<Member>> membersLiveData = new MutableLiveData<>(new ArrayList<>());

    /**
     * Retorna la lista observable de integrantes.
     * Nota importante de arquitectura: Devolvemos 'LiveData' (que es de solo lectura)
     * y NO 'MutableLiveData' (que permite lectura y escritura).
     * Así protegemos los datos y evitamos que otras clases los modifiquen directamente.
     */
    public LiveData<List<Member>> getMembers() {
        return membersLiveData;
    }

    /**
     * Agrega un nuevo integrante a la lista actual de forma segura.
     */
    public void addMember(Member member) {
        // 1. Obtenemos la lista actual que está guardada en la memoria del LiveData
        List<Member> currentList = membersLiveData.getValue();

        // 2. Verificamos que la lista no sea nula para evitar un "NullPointerException" (caída de la app)
        if (currentList != null) {
            // 3. Agregamos el nuevo objeto integrante a la lista
            currentList.add(member);
            // 4. Volvemos a inyectar la lista actualizada al LiveData.
            // ¡Este es el momento exacto en el que la UI recibe la notificación de cambio!
            membersLiveData.setValue(currentList);
        }
    }
}