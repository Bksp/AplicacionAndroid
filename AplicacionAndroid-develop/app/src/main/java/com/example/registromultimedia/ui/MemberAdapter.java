package com.example.registromultimedia.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.registromultimedia.R;
import com.example.registromultimedia.model.Member;
import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView Adapter for rendering Member list items.
 */
public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {

    private List<Member> members = new ArrayList<>();

    public void setMembers(List<Member> newMembers) {
        this.members = newMembers != null ? newMembers : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_member, parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        Member member = members.get(position);
        holder.bind(member);
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    static class MemberViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private final TextView tvRole;
        private final TextView tvEmail;

        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvItemNombre);
            tvRole = itemView.findViewById(R.id.tvItemRol);
            tvEmail = itemView.findViewById(R.id.tvItemCorreo);
        }

        public void bind(Member member) {
            tvName.setText(member.getName());
            tvRole.setText(member.getRole());
            tvEmail.setText(member.getEmail());
        }
    }
}
