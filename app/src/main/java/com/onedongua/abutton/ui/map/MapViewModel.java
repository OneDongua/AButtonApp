package com.onedongua.abutton.ui.map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MapViewModel extends ViewModel {

    private final MutableLiveData<Boolean> mLocating;

    public MapViewModel() {
        mLocating = new MutableLiveData<>(true);
    }

    public LiveData<Boolean> getLocating() {
        return mLocating;
    }

    public void switchLocating() {
        mLocating.setValue(!mLocating.getValue());
    }
}