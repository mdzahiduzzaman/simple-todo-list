package com.example.thingstodo.Model;

public class ListModel {
    private String listItem;
    private int id, checkedStatus;

    public String getListItem() {
        return listItem;
    }

    public void setListItem(String listItem) {
        this.listItem = listItem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCheckedStatus() {
        return checkedStatus;
    }

    public void setCheckedStatus(int checkedStatus) {
        this.checkedStatus = checkedStatus;
    }
}
