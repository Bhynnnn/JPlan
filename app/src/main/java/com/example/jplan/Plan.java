package com.example.jplan;

public class Plan {
    private String title_Plan;
    private int count_Plan;
    private String memo_Plan;
    private String icon_Plan;
    private int total_Plan;

    public boolean isCheck_Plan() {
        return check_Plan;
    }

    public void setCheck_Plan(boolean check_Plan) {
        this.check_Plan = check_Plan;
    }

    private boolean check_Plan;

    public Plan(String title_Plan, String memo_Plan, String icon_Plan, int total_Plan, int count_Plan, boolean check_Plan) {
        this.title_Plan = title_Plan;
        this.memo_Plan = memo_Plan;
        this.icon_Plan = icon_Plan;
        this.total_Plan = total_Plan;
        this.count_Plan = count_Plan;
        this.check_Plan = check_Plan;
    }

    public Plan() {

    }
    public int getCount_Plan() {
        return count_Plan;
    }

    public void setCount_Plan(int count_Plan) {
        this.count_Plan = count_Plan;
    }

    public int getTotal_Plan() {
        return total_Plan;
    }

    public void setTotal_Plan(int total_Plan) {
        this.total_Plan = total_Plan;
    }

    public String getIcon_Plan() {
        return icon_Plan;
    }

    public void setIcon_Plan(String icon_Plan) {
        this.icon_Plan = icon_Plan;
    }

    public String getTitle_Plan() {
        return title_Plan;
    }

    public void setTitle_Plan(String title_Plan) {
        this.title_Plan = title_Plan;
    }

    public String getMemo_Plan() {
        return memo_Plan;
    }

    public void setMemo_Plan(String memo_Plan) {
        this.memo_Plan = memo_Plan;
    }
}
