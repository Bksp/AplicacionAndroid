package com.ipst.registromultimedia.domain.usecase;

import com.ipst.registromultimedia.domain.MemberRepository;
import com.ipst.registromultimedia.domain.MemberRepositoryProvider;
import com.ipst.registromultimedia.model.Member;

public class RemoveMemberUseCase {
    private final MemberRepository repository;

    public RemoveMemberUseCase() {
        this.repository = MemberRepositoryProvider.getInstance();
    }

    public RemoveMemberUseCase(MemberRepository repository) {
        this.repository = repository;
    }

    public void execute(Member member) {
        if (member != null && repository != null) {
            repository.removeMember(member.getId());
        }
    }
}
