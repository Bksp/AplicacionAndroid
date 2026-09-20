package com.example.registromultimedia.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.registromultimedia.model.Member;
import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel holding the observable member list.
 * Decoupled from UI elements.
 */
public class MembersViewModel extends ViewModel {

    private final MutableLiveData<List<Member>> members = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<Member>> getMembers() {
        return members;
    }

    public void loadMembers(List<Member> initialList) {
        members.setValue(new ArrayList<>(initialList));
    }

    public void addMember(Member newMember) {
        List<Member> currentList = members.getValue();
        if (currentList == null) {
            currentList = new ArrayList<>();
        }
        currentList.add(newMember);
        members.setValue(new ArrayList<>(currentList));
    }
}
