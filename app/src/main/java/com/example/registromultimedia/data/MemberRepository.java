package com.example.registromultimedia.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.registromultimedia.model.Member;
import java.util.ArrayList;
import java.util.List;

/**
 * CONCEPTO CLAVE DE ESTUDIO: Fuente Única de Verdad (Single Source of Truth)
 * Esta clase es el único lugar de toda la app que tiene permiso para modificar la lista.
 * Las demás clases solo pueden "mirarla".
 */
public class MemberRepository {

    // CONCEPTO CLAVE DE ESTUDIO: MutableLiveData vs LiveData
    // MutableLiveData: Es una caja de datos que SÍ se puede modificar (setValue o postValue).
    // Es privada porque no queremos que la Vista cambie los datos a su antojo.
    private final MutableLiveData> membersLiveData = new MutableLiveData<>(new ArrayList<>());

    // LiveData: Es una caja de datos de "solo lectura".
    // Es pública para que el ViewModel y la UI se suscriban a ella (Patrón Observador).
    // Cuando MutableLiveData cambia adentro, este LiveData avisa hacia afuera automáticamente.
    public LiveData> getMembers() {
        return membersLiveData;
    }

    public void addMember(Member member) {
        // Extraemos la lista actual de la caja
        List currentList = membersLiveData.getValue();

        // Validación de nulidad (Null Safety): Asegura que la app no haga crash
        if (currentList != null) {
            currentList.add(member); // Agregamos el nuevo miembro a la lista normal

            // Volvemos a meter la lista actualizada a la caja.
            // ¡IMPORTANTE! Al hacer setValue, LiveData dispara una notificación automática
            // a la pantalla para decirle: "Hey, cambié, dibuja el RecyclerView de nuevo".
            membersLiveData.setValue(currentList);
        }
    }
}