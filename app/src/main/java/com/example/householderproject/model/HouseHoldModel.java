package com.example.householderproject.model;

public class HouseHoldModel {

    private int no;
    private String date;
    private String credit;
    private String detail;
    private String category;
    private String location;

    public HouseHoldModel(String credit) {
        this.credit = credit;
    }

    public HouseHoldModel(int no, String credit, String detail, String category) {
        this.no = no;
        this.credit = credit;
        this.detail = detail;
        this.category = category;
    }

    //문자를 받아오면 저장 할 생성자
    public HouseHoldModel(String credit, String detail, String category, String location) {
        this.credit = credit;
        this.detail = detail;
        this.category = category;
        this.location = location;
    }

    //alert 에서 저장 할 생성자
    public HouseHoldModel(String date, String credit, String detail, String category, String location) {
        this.date = date;
        this.credit = credit;
        this.detail = detail;
        this.category = category;
        this.location = location;

    }

    public HouseHoldModel(int no, String date, String credit, String detail, String category, String location) {
        this.no = no;
        this.date = date;
        this.credit = credit;
        this.detail = detail;
        this.category = category;
        this.location = location;
    }

    public int getNo() { return no; }

    public void setNo(int no) { this.no = no; }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


}
