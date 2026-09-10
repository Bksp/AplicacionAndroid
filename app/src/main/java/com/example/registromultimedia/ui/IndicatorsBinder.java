package com.example.registromultimedia.ui;

import android.view.View;
import android.widget.TextView;
import androidx.lifecycle.LifecycleOwner;
import com.example.registromultimedia.R;
import com.example.registromultimedia.viewmodel.IndicatorsViewModel;

/**
 * Custom View Binder connecting indicators TextViews with IndicatorsViewModel emissions.
 */
public class IndicatorsBinder {

    private final TextView tvTotalMembers;
    private final TextView tvRecordedAudios;

    public IndicatorsBinder(View rootView) {
        tvTotalMembers = rootView.findViewById(R.id.tvTotalIntegrantes);
        tvRecordedAudios = rootView.findViewById(R.id.tvAudiosGrabados);
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
