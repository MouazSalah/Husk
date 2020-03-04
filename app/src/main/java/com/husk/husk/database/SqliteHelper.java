package com.husk.husk.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteHelper extends SQLiteOpenHelper
{
    public final static String DATABASE_NAME = "notesDatabase";
    public final static String TABLE_NAME = "notesTable";

    public final  static String COLUMN_ID = "_ID";
    public final  static String COLUMN_NAME = "NAME";
    public final  static String COLUMN_EMAIL = "EMAIL";
    public final  static String COLUMN_PASSWORD = "PASSWORD";

    static String CREATE_DATABASE_QUERY = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                                           COLUMN_NAME + " TEXT," + COLUMN_EMAIL + " Text, " + COLUMN_PASSWORD + " TEXT " + ")";

    final static String DROP_STATMENT = "DROP TABLE IF EXISTS " + TABLE_NAME;


    final static int DATABASE_VERSION = 1;

    public SqliteHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_DATABASE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(DROP_STATMENT);

        onCreate(db);
    }
}
