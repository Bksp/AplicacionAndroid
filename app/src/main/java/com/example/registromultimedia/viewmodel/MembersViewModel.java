package com.example.registromultimedia.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.registromultimedia.domain.usecase.AddMemberUseCase;
import com.example.registromultimedia.domain.usecase.GetMembersUseCase;
import com.example.registromultimedia.domain.usecase.RemoveMemberUseCase;
import com.example.registromultimedia.model.Member;
import java.util.ArrayList;
import java.util.List;

public class MembersViewModel extends ViewModel {

    private final MutableLiveData<List<Member>> members = new MutableLiveData<>(new ArrayList<>());
    private final GetMembersUseCase getMembersUseCase;
    private final AddMemberUseCase addMemberUseCase;
    private final RemoveMemberUseCase removeMemberUseCase;

    public MembersViewModel() {
        this(new GetMembersUseCase(), new AddMemberUseCase(), new RemoveMemberUseCase());
    }

    public MembersViewModel(GetMembersUseCase getMembersUseCase, AddMemberUseCase addMemberUseCase, RemoveMemberUseCase removeMemberUseCase) {
        this.getMembersUseCase = getMembersUseCase;
        this.addMemberUseCase = addMemberUseCase;
        this.removeMemberUseCase = removeMemberUseCase;
        loadMembers();
    }

    public LiveData<List<Member>> getMembers() {
        return members;
    }

    public void loadMembers() {
        if (getMembersUseCase != null) {
            List<Member> memberList = getMembersUseCase.execute();
            members.setValue(new ArrayList<>(memberList != null ? memberList : new ArrayList<>()));
        }
    }

    public void loadMembers(List<Member> initialList) {
        if (initialList != null) {
            for (Member m : initialList) {
                if (addMemberUseCase != null) {
                    addMemberUseCase.execute(m);
                }
            }
        }
        loadMembers();
    }

    public void addMember(Member newMember) {
        if (addMemberUseCase != null && newMember != null) {
            addMemberUseCase.execute(newMember);
        }
        loadMembers();
    }

    public void removeMember(Member member) {
        if (removeMemberUseCase != null && member != null) {
            removeMemberUseCase.execute(member);
        }
        loadMembers();
    }
}
