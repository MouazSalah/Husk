package com.example.husk.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.strictmode.SqliteObjectLeakedViolation;

import java.util.ArrayList;
import java.util.List;

public  class DatabaseHelper  extends SQLiteOpenHelper
  {
        private final static String DATABASENAME = "APPDATABASE";
        private final static int DATABASEVERSION = 9;


      public DatabaseHelper(Context context)
      {
          super(context, DATABASENAME, null, DATABASEVERSION);

      }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(AppContract.CREATE_DATABASE_QUERY);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            db.execSQL(AppContract.DROP_DATABASE_QUERY);

            onCreate(db);
        }


      public String getAllData()
      {

          StringBuffer stringBuffer = new StringBuffer();

          String [] columns = {AppContract.COLUMN_ID , AppContract.COLUMN_NAME, AppContract.COLUMN_EMAIL, AppContract.COLUMN_PASSWORD};

          Cursor cursor = this.getWritableDatabase().query(AppContract.TABLENAME, columns , null, null, null, null, null );

          stringBuffer.append("count: " + cursor.getCount() + " \n");

          while (cursor.moveToNext())
          {
              int id = cursor.getInt(0);
              String name = cursor.getString(1);
              String email = cursor.getString(2);
              String password = cursor.getString(3);

              stringBuffer.append(id + " - " + name + " " + email + " " + password + "\n" );
          }

          return stringBuffer.toString();
      }

      public List<Item> getAllItems()
      {
          List<Item> items = new ArrayList<>();

          // Select All Query
          String selectQuery = "SELECT  * FROM " + AppContract.TABLENAME;

          SQLiteDatabase db = this.getWritableDatabase();
          Cursor cursor = db.rawQuery(selectQuery, null);

          // looping through all rows and adding to list
          if (cursor.moveToFirst())
          {
              do
              {
                  Item item = new Item();
                  item.setId(cursor.getInt(cursor.getColumnIndex(AppContract.COLUMN_ID)));
                  item.setName(cursor.getString(cursor.getColumnIndex(AppContract.COLUMN_NAME)));
                  item.setEmail(cursor.getString(cursor.getColumnIndex(AppContract.COLUMN_EMAIL)));
                  item.setPassword(cursor.getString(cursor.getColumnIndex(AppContract.COLUMN_PASSWORD)));

                  items.add(item);
              }

              while (cursor.moveToNext());
          }

          // close db connection
          db.close();

          // return notes list
          return items;
      }


  }
