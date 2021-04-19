package com.example.myuserapp.bean;

public class MyUser {

    public static final String TABLE_NAME = "Users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_AVATAR = "avatar";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_TELEPHONE = "phone";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_COMMENT = "comment";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID + " "
            + "INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME + " TEXT," + COLUMN_AVATAR + " " +
            "INTEGER," + COLUMN_GENDER + " " + "INTEGER," + COLUMN_TELEPHONE + " " + "TEXT," + COLUMN_ADDRESS + " TEXT" + ")";

    public static final String CREATE_COMPANY_TABLE =
            "CREATE TABLE " + "Company" + "(" + "_id" + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name" + " TEXT," + "phone" + " TEXT," + "address" + " TEXT" + ")";

    private int id;
    private String name;
    private int avatar;
    //男性 : true -> 1,女性 : false -> 0
    private boolean gender;
    private String phone;
    private String address;
    private String comment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public boolean getGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
