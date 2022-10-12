package com.example.jplan;

public class Todo {


    private String title;
    private String check;

    public Todo(String title, String check) {
        this.title = title;
        this.check = check;

    }
    public Todo() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String isCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }
}

