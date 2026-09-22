package com.ipst.registromultimedia.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * ViewModel managing system metrics and indicators.
 * Fully decoupled from UI classes.
 */
public class IndicatorsViewModel extends ViewModel {

    private final MutableLiveData<Integer> totalMembers = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> recordedAudios = new MutableLiveData<>(0);

    public LiveData<Integer> getTotalMembers() {
        return totalMembers;
    }

    public LiveData<Integer> getRecordedAudios() {
        return recordedAudios;
    }

    public void setTotalMembers(int count) {
        totalMembers.setValue(count);
    }

    public void incrementMembers() {
        Integer current = totalMembers.getValue();
        totalMembers.setValue(current != null ? current + 1 : 1);
    }

    public void incrementAudios() {
        Integer current = recordedAudios.getValue();
        recordedAudios.setValue(current != null ? current + 1 : 1);
    }
}
