package com.ipst.registromultimedia;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import com.ipst.registromultimedia.domain.MemberRepository;
import com.ipst.registromultimedia.domain.usecase.AddMemberUseCase;
import com.ipst.registromultimedia.domain.usecase.GetMembersUseCase;
import com.ipst.registromultimedia.domain.usecase.RemoveMemberUseCase;
import com.ipst.registromultimedia.model.Member;
import com.ipst.registromultimedia.viewmodel.MembersViewModel;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MembersViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private GetMembersUseCase getMembersUseCase;
    private AddMemberUseCase addMemberUseCase;
    private RemoveMemberUseCase removeMemberUseCase;
    private MembersViewModel viewModel;

    private static class FakeMemberRepository implements MemberRepository {
        private final List<Member> list = new ArrayList<>();

        @Override
        public void addMember(Member member) {
            if (member != null) list.add(member);
        }

        @Override
        public void removeMember(String id) {
            if (id != null) list.removeIf(m -> id.equals(m.getId()));
        }

        @Override
        public List<Member> getAllMembers() {
            return new ArrayList<>(list);
        }
    }

    @Before
    public void setUp() {
        FakeMemberRepository fakeRepository = new FakeMemberRepository();
        getMembersUseCase = new GetMembersUseCase(fakeRepository);
        addMemberUseCase = new AddMemberUseCase(fakeRepository);
        removeMemberUseCase = new RemoveMemberUseCase(fakeRepository);
        viewModel = new MembersViewModel(getMembersUseCase, addMemberUseCase, removeMemberUseCase);
    }

    @Test
    public void testAddMemberThroughUseCase() {
        Member member = new Member("1", "Juan Perez", "Developer", "juan@test.com");
        viewModel.addMember(member);

        List<Member> members = viewModel.getMembers().getValue();
        assertNotNull(members);
        assertEquals(1, members.size());
        assertEquals("Juan Perez", members.get(0).getName());
    }

    @Test
    public void testRemoveMemberThroughUseCase() {
        Member member = new Member("1", "Juan Perez", "Developer", "juan@test.com");
        viewModel.addMember(member);
        assertEquals(1, viewModel.getMembers().getValue().size());

        viewModel.removeMember(member);
        assertEquals(0, viewModel.getMembers().getValue().size());
    }
}
