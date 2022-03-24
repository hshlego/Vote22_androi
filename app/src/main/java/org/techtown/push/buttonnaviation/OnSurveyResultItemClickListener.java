package org.techtown.push.buttonnaviation;

import android.view.View;

import org.techtown.push.buttonnaviation.ui.home.SurveyResultAdapter;

public interface OnSurveyResultItemClickListener {
    public void onSurveyResultItemClick(SurveyResultAdapter.SurveyViewHolder holder, View view, int position);
    public void onSurveyResultSpecifyClick(SurveyResultAdapter.SurveyViewHolder holder, View view, int position);
}
