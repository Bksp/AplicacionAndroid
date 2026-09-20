package com.example.registromultimedia.ui;

import android.view.View;
import android.widget.TextView;
import androidx.lifecycle.LifecycleOwner;
import com.example.registromultimedia.R;
import com.example.registromultimedia.viewmodel.IndicatorsViewModel;

/**
 * Custom View Binder connecting indicators TextViews with IndicatorsViewModel emissions.
 * Supports both G1 and G2 layout IDs safely.
 */
public class IndicatorsBinder {

    private final TextView tvTotalMembers;
    private final TextView tvRecordedAudios;

    public IndicatorsBinder(View rootView) {
        View vTotal = rootView.findViewById(R.id.tvTotalIntegrantes);
        if (vTotal == null) vTotal = rootView.findViewById(R.id.tv_total_integrantes);
        tvTotalMembers = (vTotal instanceof TextView) ? (TextView) vTotal : null;

        View vAudios = rootView.findViewById(R.id.tvAudiosGrabados);
        if (vAudios == null) vAudios = rootView.findViewById(R.id.tv_audios_grabados);
        tvRecordedAudios = (vAudios instanceof TextView) ? (TextView) vAudios : null;
    }

    public void bind(LifecycleOwner owner, IndicatorsViewModel indicatorsViewModel) {
        indicatorsViewModel.getTotalMembers().observe(owner, total -> {
            if (tvTotalMembers != null) {
                tvTotalMembers.setText("Integrantes: " + (total != null ? total : 0));
            }
        });

        indicatorsViewModel.getRecordedAudios().observe(owner, audios -> {
            if (tvRecordedAudios != null) {
                tvRecordedAudios.setText("Audios: " + (audios != null ? audios : 0));
            }
        });
    }
}
