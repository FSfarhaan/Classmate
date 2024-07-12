package com.example.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.finalproject.models.showAvailableGroupsModel;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "GroupsDB";
    public static final String TABLE_NAME = "Groups";
    public static final String KEY_ID = "id";
    public static final String GROUP_NAME = "groupName";
    public static final String GROUP_ID = "groupId";
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // CREATE TABLE Groups (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, groupId TEXT)
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + GROUP_NAME + " TEXT, " + GROUP_ID + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void insertGroups(String groupName, String groupId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GROUP_NAME, groupName);
        values.put(GROUP_ID, groupId);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    ArrayList<showAvailableGroupsModel> getGroups() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<showAvailableGroupsModel> groupList = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * from " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                showAvailableGroupsModel groupModel = new showAvailableGroupsModel();
                // groupModel.setImage(Integer.parseInt(cursor.getString(0)));
                groupModel.setGroupName(cursor.getString(1));
                groupModel.setGroupId(cursor.getString(2));
                groupModel.setImage(R.drawable.avatar_img);
                groupModel.setGroupText("Enter the chat");
                // Adding contact to list
                groupList.add(groupModel);
            } while (cursor.moveToNext());
        }
        db.close();
        return groupList;
    }

    public void deleteGroup(String groupName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, GROUP_NAME + " = ?", new String[] {groupName});
        db.close();
    }
}
