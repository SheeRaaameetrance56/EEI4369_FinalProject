package com.ousl.final_project_eei4369_notetakingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DB_Manager {
    private DataBaseHelper dbHelper;
    private final Context context;
    private SQLiteDatabase database;

    //Constructor
    public DB_Manager(Context c) {
        context = c;
    }

    public DB_Manager open() throws SQLException {
        dbHelper = new DataBaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    public void insert(String title, String date, String location, String content){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseHelper.Title, title);
        contentValues.put(DataBaseHelper.Date, date);
        contentValues.put(DataBaseHelper.Location, location);
        contentValues.put(DataBaseHelper.Content, content);

        database.insert(DataBaseHelper.TABLE_NOTES, null, contentValues);

    }

    public Cursor fetch(){
        String[] columns = new String[] {
            DataBaseHelper.NOTES_ID,
            DataBaseHelper.Title,
            DataBaseHelper.Date,
            DataBaseHelper.Location,
            DataBaseHelper.Content};

        Cursor cursor = database.query(
                DataBaseHelper.TABLE_NOTES,
                columns,
                null,
                null,
                null,
                null,
                null

        );

        if (cursor!=null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String title, String date, String location, String content){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseHelper.Title, title);
        contentValues.put(DataBaseHelper.Date, date);
        contentValues.put(DataBaseHelper.Location, location);
        contentValues.put(DataBaseHelper.Content, content);

        int updateActivity = database.update(DataBaseHelper.TABLE_NOTES, contentValues, DataBaseHelper.NOTES_ID + "=" + _id, null);
        return updateActivity;
    }

    public void delete(long _id){
        database.delete(DataBaseHelper.TABLE_NOTES, DataBaseHelper.NOTES_ID + "=" + _id, null);
    }

}
