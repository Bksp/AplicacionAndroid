package com.example.registromultimedia;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.registromultimedia.ui.AudioBinder;
import com.example.registromultimedia.ui.FormBinder;
import com.example.registromultimedia.ui.IndicatorsBinder;
import com.example.registromultimedia.ui.MemberAdapter;
import com.example.registromultimedia.ui.MicPermissionHelper;
import com.example.registromultimedia.ui.MockData;
import com.example.registromultimedia.viewmodel.AudioViewModel;
import com.example.registromultimedia.viewmodel.FormViewModel;
import com.example.registromultimedia.viewmodel.IndicatorsViewModel;
import com.example.registromultimedia.viewmodel.MembersViewModel;
import java.io.File;

/**
 * Root View (G1).
 * Instantiates ViewModels and custom Binders, initializes layout components and orchestrates UI flows.
 */
public class MainActivity extends AppCompatActivity {

    private FormViewModel formViewModel;
    private IndicatorsViewModel indicatorsViewModel;
    private AudioViewModel audioViewModel;
    private MembersViewModel membersViewModel;

    private FormBinder formBinder;
    private IndicatorsBinder indicatorsBinder;
    private AudioBinder audioBinder;

    private MemberAdapter memberAdapter;
    private MicPermissionHelper micPermissionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Initialize ViewModels
        formViewModel = new ViewModelProvider(this).get(FormViewModel.class);
        indicatorsViewModel = new ViewModelProvider(this).get(IndicatorsViewModel.class);
        audioViewModel = new ViewModelProvider(this).get(AudioViewModel.class);
        membersViewModel = new ViewModelProvider(this).get(MembersViewModel.class);

        // 2. Setup Role Spinner
        Spinner spRole = findViewById(R.id.spRol);
        if (spRole != null) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    this, R.array.roles_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spRole.setAdapter(adapter);
        }

        // 3. Setup RecyclerView
        RecyclerView rvMembers = findViewById(R.id.rvIntegrantes);
        memberAdapter = new MemberAdapter();
        if (rvMembers != null) {
            rvMembers.setLayoutManager(new LinearLayoutManager(this));
            rvMembers.setAdapter(memberAdapter);
        }

        // Observe member list changes and update metrics
        membersViewModel.getMembers().observe(this, list -> {
            memberAdapter.setMembers(list);
            indicatorsViewModel.setTotalMembers(list != null ? list.size() : 0);
        });

        // Load initial mock data
        membersViewModel.loadMembers(MockData.getInitialMembers());

        // 4. Setup Audio Recorder and Mic Permission Helper
        String audioPath = new File(getExternalCacheDir(), "recording_registry.3gp").getAbsolutePath();
        audioViewModel.initRecorder(audioPath);
        micPermissionHelper = new MicPermissionHelper(this);

        // 5. Instantiate UI Binders
        formBinder = new FormBinder(findViewById(android.R.id.content));
        indicatorsBinder = new IndicatorsBinder(findViewById(android.R.id.content));
        audioBinder = new AudioBinder(findViewById(android.R.id.content));

        // 6. Bind UI components
        formBinder.bind(this, formViewModel, membersViewModel);
        indicatorsBinder.bind(this, indicatorsViewModel);
        audioBinder.bind(this, audioViewModel, micPermissionHelper, () -> indicatorsViewModel.incrementAudios());
    }
}
