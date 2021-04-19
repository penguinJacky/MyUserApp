package com.example.myuserapp.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.myuserapp.R;
import com.example.myuserapp.bean.MyUser;

import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "user_db";

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MyUser.CREATE_TABLE);
        db.execSQL(MyUser.CREATE_COMPANY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            upgradeVersionTwo(db);
        }
    }

    private void upgradeVersionTwo(SQLiteDatabase db) {
        db.execSQL("ALTER TABLE " + MyUser.TABLE_NAME + " ADD COLUMN " + MyUser.COLUMN_COMMENT +
                " TEXT");
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion > 2) {
            db.execSQL("DROP TABLE IF EXISTS company");
        }
//        super.onDowngrade(db, oldVersion, newVersion);
    }

    public long insertUser(MyUser user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MyUser.COLUMN_NAME, user.getName());
        values.put(MyUser.COLUMN_AVATAR, user.getGender() ? R.drawable.male_avatar_icon :
                R.drawable.female_avatar_icon);
        values.put(MyUser.COLUMN_GENDER, user.getGender() ? 1 : 0);
        values.put(MyUser.COLUMN_TELEPHONE, user.getPhone());
        values.put(MyUser.COLUMN_ADDRESS, user.getAddress());
        long i = db.insert(MyUser.TABLE_NAME, null, values);
        db.close();
        return i;
    }

    public ArrayList<MyUser> getAllUsers() {
        ArrayList<MyUser> userArrayList = new ArrayList<>();
        String sql =
                "SELECT * FROM " + MyUser.TABLE_NAME + " ORDER BY " + MyUser.COLUMN_ID + " " +
                        "ASC";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                MyUser user = new MyUser();
                int id = cursor.getInt(cursor.getColumnIndex(MyUser.COLUMN_ID));
                int avatar = cursor.getInt(cursor.getColumnIndex(MyUser.COLUMN_AVATAR));
                int gender = cursor.getInt(cursor.getColumnIndex(MyUser.COLUMN_GENDER));
                String name = cursor.getString(cursor.getColumnIndex(MyUser.COLUMN_NAME));
                String phone = cursor.getString(cursor.getColumnIndex(MyUser.COLUMN_TELEPHONE));
                String address = cursor.getString(cursor.getColumnIndex(MyUser.COLUMN_ADDRESS));
                user.setId(id);
                user.setName(name);
                user.setAvatar(avatar);
                user.setGender(gender == 1);
                user.setPhone(phone);
                user.setAddress(address);
                userArrayList.add(user);
            }
        }
        db.close();
        return userArrayList;
    }

    public int updateUser(int uid, MyUser user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MyUser.COLUMN_NAME, user.getName());
        values.put(MyUser.COLUMN_AVATAR, user.getAvatar());
        values.put(MyUser.COLUMN_GENDER, user.getGender() ? 1 : 0);
        values.put(MyUser.COLUMN_TELEPHONE, user.getPhone());
        values.put(MyUser.COLUMN_ADDRESS, user.getAddress());
        int i = db.update(MyUser.TABLE_NAME, values, MyUser.COLUMN_ID + " =? ",
                new String[]{String.valueOf(uid)});
        db.close();
        return i;
    }

    public int deleteUser(int uid) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(MyUser.TABLE_NAME, MyUser.COLUMN_ID + "=? ",
                new String[]{String.valueOf(uid)});
        db.close();
        return i;
    }
}
