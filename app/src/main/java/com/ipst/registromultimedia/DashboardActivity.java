package com.ipst.registromultimedia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ipst.registromultimedia.model.Member;
import com.ipst.registromultimedia.ui.MemberAdapter;
import com.ipst.registromultimedia.viewmodel.MembersViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DashboardActivity extends AppCompatActivity {

    private MembersViewModel membersViewModel;
    private MemberAdapter memberAdapter;
    private RecyclerView rvIntegrantes;
    private TextView tvEmptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        MaterialToolbar toolbar = findViewById(R.id.toolbar_dashboard);
        if (toolbar != null) {
            // Botón Volver (flecha izquierda) -> ir al Login
            toolbar.setNavigationOnClickListener(v -> irALogin());
        }

        // Interceptar botón físico o gesto de volver atrás del sistema
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                irALogin();
            }
        });

        rvIntegrantes = findViewById(R.id.rv_integrantes);
        tvEmptyState = findViewById(R.id.tv_empty_state);

        memberAdapter = new MemberAdapter();
        if (rvIntegrantes != null) {
            rvIntegrantes.setLayoutManager(new LinearLayoutManager(this));
            rvIntegrantes.setAdapter(memberAdapter);

            // Agregar ItemTouchHelper para eliminar elementos deslizando a izquierda o derecha (Swipe)
            ItemTouchHelper.SimpleCallback swipeCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    int position = viewHolder.getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && memberAdapter != null && membersViewModel != null) {
                        Member memberToDelete = memberAdapter.getMemberAt(position);
                        if (memberToDelete != null) {
                            membersViewModel.removeMember(memberToDelete);
                            Toast.makeText(DashboardActivity.this, "Registro eliminado: " + memberToDelete.getName(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            };
            new ItemTouchHelper(swipeCallback).attachToRecyclerView(rvIntegrantes);
        }

        membersViewModel = new ViewModelProvider(this).get(MembersViewModel.class);
        membersViewModel.getMembers().observe(this, members -> {
            boolean isEmpty = (members == null || members.isEmpty());
            if (memberAdapter != null) {
                memberAdapter.setMembers(members);
            }
            if (tvEmptyState != null) {
                tvEmptyState.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
            }
            if (rvIntegrantes != null) {
                rvIntegrantes.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
            }
        });

        FloatingActionButton fabAgregar = findViewById(R.id.fab_agregar);
        if (fabAgregar != null) {
            fabAgregar.setOnClickListener(v -> {
                Intent intent = new Intent(DashboardActivity.this, RegistroActivity.class);
                startActivity(intent);
            });
        }
    }

    private void irALogin() {
        Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (membersViewModel != null) {
            membersViewModel.loadMembers();
        }
    }
}
