package com.example.husk.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseManager
{

    SqliteHelper sqliteHelper;
    SQLiteDatabase sqLiteDatabase;
    Context context;


    public DatabaseManager(Context c)
    {
        this.context = c;
    }

    public ArrayList<Item> getAllItems()
    {
        SqliteHelper sqliteHelper = new SqliteHelper(context);
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();

        ArrayList<Item> movieDetailsList = new ArrayList();

        String selectQuery = "SELECT * FROM " + SqliteHelper.TABLE_NAME;


        Cursor cursor = db.rawQuery(selectQuery, null);
        int i = 0;

        //if TABLE has rows
        if (cursor.moveToFirst())
        {
            //Loop through the table rows
            do
            {
                Item movieDetails = new Item();
                movieDetails.setId(cursor.getInt(0));
                movieDetails.setName(cursor.getString(1));
                movieDetails.setEmail(cursor.getString(2));
                movieDetails.setPassword(cursor.getString(3));

                //Add Item details to list
                movieDetailsList.add(movieDetails);

                i++;
            }
            while (cursor.moveToNext());
        }

       // movieDetailsList.get(0);

        db.close();
        Log.d("counts" , String.valueOf(i));
        return movieDetailsList;
    }


    public Item getItem(long itemId)
    {
        SqliteHelper sqliteHelper = new SqliteHelper(context);
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + SqliteHelper.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        Item item = new Item();

        if (cursor.moveToFirst())
        {
            do
            {
                if (cursor.getInt(0) == itemId)
                {
                    item.name = cursor.getString(1);
                    item.email = cursor.getString(2);
                    item.password = cursor.getString(3);
                }
            }
            while (cursor.moveToNext());
        }
        return item;
    }

    public long insert(String name, String email, String password )
    {
        SqliteHelper sqliteHelper = new SqliteHelper(context);
        sqLiteDatabase = sqliteHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(SqliteHelper.COLUMN_NAME , name);
        contentValues.put(SqliteHelper.COLUMN_EMAIL , email);
        contentValues.put(SqliteHelper.COLUMN_PASSWORD , password);

        long insertValue = sqLiteDatabase.insert(SqliteHelper.TABLE_NAME , null, contentValues);
        return insertValue;
    }


    public long update(long _id, String name, String email, String password )
    {
        SqliteHelper sqliteHelper = new SqliteHelper(context);
        sqLiteDatabase = sqliteHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(SqliteHelper.COLUMN_NAME, name);
        contentValues.put(SqliteHelper.COLUMN_EMAIL, email);
        contentValues.put(SqliteHelper.COLUMN_PASSWORD, password);

        long updateValue = sqLiteDatabase.update(SqliteHelper.TABLE_NAME, contentValues, SqliteHelper.COLUMN_ID + " = " + _id, null);

        return updateValue;

    }

    public long deleteItem(long _id)
    {
        SqliteHelper sqliteHelper = new SqliteHelper(context);
        sqLiteDatabase = sqliteHelper.getWritableDatabase();

        long value = sqLiteDatabase.delete(SqliteHelper.TABLE_NAME,  SqliteHelper.COLUMN_ID + "=" + _id, null);
        return value;
    }


    public void deleteAllData()
    {
        SqliteHelper sqliteHelper = new SqliteHelper(context);
        sqLiteDatabase = sqliteHelper.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from "+ SqliteHelper.TABLE_NAME );

    }



}
