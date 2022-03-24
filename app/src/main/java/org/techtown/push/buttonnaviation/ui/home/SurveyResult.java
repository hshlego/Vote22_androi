package org.techtown.push.buttonnaviation.ui.home;

import org.json.JSONObject;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SurveyResult{
    final String[] monthStrs = {"ZERO", "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
    private long id;
    private String uploadMonth, uploadDay;
    private String requester;
    private String executer;
    private String url;
    private String c1;
    private String c2;
    private String c3;
    private String c4;

    public SurveyResult(Long id, String uploadMonth, String uploadDay, String requester, String executer, String url, String c1, String c2, String c3, String c4) {
        this.uploadMonth = uploadMonth;
        this.uploadDay = uploadDay;
        this.requester = requester;
        this.executer = executer;
        this.url = url;
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
        this.c4 = c4;

    }

    public String getMonthStr() {
        return monthStrs[Integer.parseInt(uploadMonth)];
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUploadMonth() {return uploadMonth;}

    public String getUploadDay() {return uploadDay;}

    public String getUrl() {return url;}

    public void setUrl(String url) {this.url = url;}

    public String getC1() {
        return c1;
    }

    public void setC1(String c1) {
        this.c1 = c1;
    }

    public String getC2() {
        return c2;
    }

    public void setC2(String c2) {
        this.c2 = c2;
    }

    public String getC3() {
        return c3;
    }

    public void setC3(String c3) {
        this.c3 = c3;
    }

    public String getC4() {
        return c4;
    }

    public void setC4(String c4) {
        this.c4 = c4;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getExecuter() {
        return executer;
    }

    public void setExecuter(String executer) {
        this.executer = executer;
    }
}
