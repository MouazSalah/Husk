package com.example.husk.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

public class AppContract
{

    public static final String CONTENT_AUTHORITY = "com.example.husk";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.android.pets/pets/ is a valid path for
     * looking at pet data. content://com.example.android.pets/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */
    public static final String PATH_ITEMS = "husk";

        /** The content URI to access the pet data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ITEMS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;


    static SQLiteDatabase sqLiteDatabase;
    DatabaseHelper databaseHelper ;
    static Context context;

    public final static String TABLENAME = "APPTABLENAME";

    public final static String COLUMN_ID = "_id";
    public final static String COLUMN_NAME = "APPNAME";
    public final static String COLUMN_PASSWORD = "PASSWORD";
    public final static String COLUMN_EMAIL = "APPEMAIL";

    static String CREATE_DATABASE_QUERY = "CREATE TABLE " + TABLENAME + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_NAME + " TEXT," + COLUMN_EMAIL + " Text, " + COLUMN_PASSWORD + " TEXT " + ")";

    static String DROP_DATABASE_QUERY = "DROP TABLE IF EXISTS " + TABLENAME;


    public AppContract()
    {

    }


    public static long insert(String name, String email, String password)
    {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name );
        contentValues.put(COLUMN_EMAIL, email );
        contentValues.put(COLUMN_PASSWORD, password );

        long insert_query = sqLiteDatabase.insert(TABLENAME, null , contentValues);
        return insert_query;
    }


    public static long getCount()
    {

        String [] columns = {COLUMN_ID , COLUMN_NAME, COLUMN_EMAIL, COLUMN_PASSWORD};
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + AppContract.TABLENAME ,null);
        long count = cursor.getCount();
        return  count;
    }




    public void deleteAllData()
    {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
    }
}
