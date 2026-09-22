package com.example.registromultimedia.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.registromultimedia.model.Member;
import java.util.ArrayList;
import java.util.List;

/**
 * Almacena la lista de integrantes de forma reactiva (LiveData).
 */
public class MemberRepository {

    private final MutableLiveData> membersLiveData = new MutableLiveData<>(new ArrayList<>());

    public LiveData> getMembers() {
        return membersLiveData;
    }

    public void addMember(Member member) {
        List currentList = membersLiveData.getValue();
        if (currentList != null) {
            currentList.add(member);
            membersLiveData.setValue(currentList);
        }
    }
}