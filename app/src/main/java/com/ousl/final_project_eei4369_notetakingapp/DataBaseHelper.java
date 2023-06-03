package com.ousl.final_project_eei4369_notetakingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {

    // Data base information
    static final String DB_Name = "Final_project_DB";

    // Note Table Name
    public static final String TABLE_NOTES = "notes_data";


    // Note Table Columns
    public static final String NOTES_ID = "_id";
    public static final String Title = "title";
    public static final String Date = "date";
    public static final String Location = "location";
    public static final String Content = "content";

    // sign table
    public static final String TABLE_SIGN = "login_data";

    // sign table columns
    public static final String SIGN_ID = "sign_id";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String IS_REMEMBER = "isRemember";


    //Note Table creating query
    private static final String CREATE_TABLE_NOTE = " CREATE TABLE " + TABLE_NOTES + "("
            + NOTES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + Title + " TEXT, "
            + Date + " TEXT, "
            + Location + " TEXT, "
            + Content + " TEXT);";


    //sign table creating query
    private static final String CREATE_TABLE_SIGN = " CREATE TABLE " + TABLE_SIGN + " (" +
            SIGN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            NAME + " TEXT," +
            EMAIL + " TEXT," +
            PASSWORD + " TEXT," +
            IS_REMEMBER + " BOOLEAN )";


    // Constructor
    public DataBaseHelper(@Nullable Context context) {
        super(context, DB_Name, null, 8);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // Executing query
        db.execSQL(CREATE_TABLE_NOTE);
        db.execSQL(CREATE_TABLE_SIGN);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SIGN);
        onCreate(db);
    }

    public void addProfile(ProfileModel profileModel){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(NAME, profileModel.getName());
        cv.put(EMAIL, profileModel.getEmail());
        cv.put(PASSWORD, profileModel.getPassword());
        cv.put(IS_REMEMBER, profileModel.getIsRemember());

        db.insert(TABLE_SIGN, null, cv);
        db.close();
    }

    public boolean checkEmail(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(" SELECT * FROM " + TABLE_SIGN + " WHERE email = ?", new String[]{email});
        if (cursor.getCount() > 0){
            return true;
        }
        else {
            return false;
        }
    }

    public boolean checkEmailPassword(String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(" SELECT * FROM " + TABLE_SIGN + " WHERE email = ? AND password = ?", new String[]{email, password});
        if (cursor.getCount() > 0){
            return true;
        }
        else {
            return false;
        }
    }

}
