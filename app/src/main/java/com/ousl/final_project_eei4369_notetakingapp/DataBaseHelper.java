package com.ousl.final_project_eei4369_notetakingapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {

    // Data base information
    static final String DB_Name = "Final_project_DB";

    // Table Name
    public static final String TABLE_NAME = "notes_data";

    //Table Columns
    public static final String _ID = "_id";
    public static final String Title = "title";
    public static final String Date = "date";
    public static final String Location = "location";
    public static final String Content = "content";

    //Table query
    private static final String CREATE_TABLE = " create table " + TABLE_NAME + "("
            + _ID + " INTEGER PRIMARY KEY, "
            + Title + " TEXT, "
            + Date + " TEXT, "
            + Location + " TEXT, "
            + Content + " TEXT);";


    // Constructor
    public DataBaseHelper(@Nullable Context context) {
        super(context, DB_Name, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // Executing query
        db.execSQL(CREATE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
