package com.example.jplan.Todo;

public class Todo {


    String title;
    String isSelected;

    public Todo(String title, String isSelected) {
        this.title = title;
        this.isSelected = isSelected;

    }
    public Todo() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSelected()
    {
        return isSelected;
    }
    public void setSelected(String selected)
    {
        isSelected = selected;
    }}

