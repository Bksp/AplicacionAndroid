package com.example.registromultimedia.ui;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.registromultimedia.R;
import com.example.registromultimedia.ReproductorActivity;
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

    public Member getMemberAt(int position) {
        if (position >= 0 && position < members.size()) {
            return members.get(position);
        }
        return null;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_integrante, parent, false);
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
        private TextView tvName;
        private TextView tvRole;
        private TextView tvEmail;
        private Button btnPlayAudio;

        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_nombre_item);
            if (tvName == null) tvName = itemView.findViewById(R.id.tvItemNombre);

            tvRole = itemView.findViewById(R.id.tv_rol_item);
            if (tvRole == null) tvRole = itemView.findViewById(R.id.tvItemRol);

            tvEmail = itemView.findViewById(R.id.tv_correo_item);
            if (tvEmail == null) tvEmail = itemView.findViewById(R.id.tvItemCorreo);

            View vBtn = itemView.findViewById(R.id.btn_reproducir_item);
            if (vBtn == null) vBtn = itemView.findViewById(R.id.btn_reproducir_audio_1);
            btnPlayAudio = (vBtn instanceof Button) ? (Button) vBtn : null;
        }

        public void bind(Member member) {
            if (member == null) return;
            if (tvName != null) {
                tvName.setText(member.getName());
            }
            if (tvRole != null) {
                tvRole.setText("Rol: " + member.getRole());
            }
            if (tvEmail != null) {
                tvEmail.setText(member.getEmail() != null ? member.getEmail() : "");
            }
            if (btnPlayAudio != null) {
                btnPlayAudio.setOnClickListener(v -> {
                    Intent intent = new Intent(v.getContext(), ReproductorActivity.class);
                    v.getContext().startActivity(intent);
                });
            }
        }
    }
}
