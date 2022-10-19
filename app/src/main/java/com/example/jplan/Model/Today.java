package com.example.jplan.Model;

public class Today {
    private String title_Today;
    private String start_Today;
    private String finish_Today;
    private String memo_Today;
    private int time_Today;
    public Today() {

    }

    public String getTitle_Today() {
        return title_Today;
    }

    public void setTitle_Today(String title_Today) {
        this.title_Today = title_Today;
    }

    public String getStart_Today() {
        return start_Today;
    }

    public void setStart_Today(String start_Today) {
        this.start_Today = start_Today;
    }

    public String getFinish_Today() {
        return finish_Today;
    }

    public void setFinish_Today(String finish_Today) {
        this.finish_Today = finish_Today;
    }

    public String getMemo_Today() {
        return memo_Today;
    }

    public void setMemo_Today(String memo_Today) {
        this.memo_Today = memo_Today;
    }

    public int getTime_Today() {
        return time_Today;
    }

    public void setTime_Today(int time_Today) {
        this.time_Today = time_Today;
    }


    public Today(int time_Today, String title_Today, String start_Today, String finish_Today, String memo_Today) {
        this.title_Today = title_Today;
        this.start_Today = start_Today;
        this.finish_Today = finish_Today;
        this.memo_Today = memo_Today;
    }

    public void setAll(int time_Today, String title_Today, String start_Today, String finish_Today, String memo_Today) {
        this.title_Today = title_Today;
        this.start_Today = start_Today;
        this.finish_Today = finish_Today;
        this.memo_Today = memo_Today;
    }
}
