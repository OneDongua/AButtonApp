package com.onedongua.smartcane.ui.pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PairViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PairViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is pair fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}