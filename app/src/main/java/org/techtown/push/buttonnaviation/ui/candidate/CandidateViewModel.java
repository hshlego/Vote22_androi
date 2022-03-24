package org.techtown.push.buttonnaviation.ui.candidate;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CandidateViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CandidateViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}