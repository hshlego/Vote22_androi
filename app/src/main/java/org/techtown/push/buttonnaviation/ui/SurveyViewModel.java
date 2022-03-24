package org.techtown.push.buttonnaviation.ui;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.techtown.push.buttonnaviation.ui.home.SurveyResult;

import java.util.List;

public class SurveyViewModel extends ViewModel {
    private final MutableLiveData<List<SurveyResult>> surveyResultList = new MutableLiveData<>();

    public void setSurveyResultList(List<SurveyResult> list) {
        surveyResultList.setValue(list);
    }

    public LiveData<List<SurveyResult>> getSurveyResultList(){
        return surveyResultList;
    }
}
