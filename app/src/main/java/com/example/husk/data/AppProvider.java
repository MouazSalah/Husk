package com.example.husk.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class AppProvider extends ContentProvider
{

    DatabaseHelper databaseHelper;

    /** Tag for the log messages */
    public static final String LOG_TAG = AppProvider.class.getSimpleName();

    private static final int ITEMS = 100;
    private static final int ITEM_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);


    // Static initializer. This is run the first time anything is called from this class.
    static
    {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        // The content URI of the form "content://com.example.android.pets/pets" will map to the
        // integer code {@link #PETS}. This URI is used to provide access to MULTIPLE rows
        // of the pets table.
        sUriMatcher.addURI(AppContract.CONTENT_AUTHORITY, AppContract.PATH_ITEMS, ITEMS);

        // The content URI of the form "content://com.example.android.pets/pets/#" will map to the
        // integer code {@link #PET_ID}. This URI is used to provide access to ONE single row
        // of the pets table.
        //
        // In this case, the "#" wildcard is used where "#" can be substituted for an integer.
        // For example, "content://com.example.android.pets/pets/3" matches, but
        // "content://com.example.android.pets/pets" (without a number at the end) doesn't match.
        sUriMatcher.addURI(AppContract.CONTENT_AUTHORITY, AppContract.PATH_ITEMS + "/#", ITEM_ID);
    }

    public AppProvider()
    {
        databaseHelper = new DatabaseHelper(getContext());
    }

    @Override
    public boolean onCreate()
    {
        return false;
    }

    @Override
    public Cursor query( Uri uri, String[] projection, String selection, String[] selectionArgs,  String sortOrder)
    {

        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        // Get readable database
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                // For the PETS code, query the pets table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the pets table.
                cursor = database.query(AppContract.TABLENAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case ITEM_ID:
                // For the PET_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.pets/pets/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = AppContract.COLUMN_ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(AppContract.TABLENAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the cursor
        return cursor;
    }

    @Override
    public String getType( Uri uri)
    {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return AppContract.CONTENT_LIST_TYPE;
            case ITEM_ID:
                return AppContract.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        final int match = sUriMatcher.match(uri);
        switch (match)
        {
            case ITEMS:
                return insertPet(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }


    private Uri insertPet(Uri uri, ContentValues values)
    {
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        long id = database.insert(AppContract.TABLENAME, null, values);

        if (id == -1)
        {
            Log.e("1", "Failed to insert row for " + uri);
            return null;
        }


        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete( Uri uri,String selection, String[] selectionArgs)
    {
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        int deletedRaw;
        int match = sUriMatcher.match(uri);
        switch (match)
        {
            case ITEMS:
                deletedRaw = database.delete(AppContract.TABLENAME, null, null);
                break;

            case ITEM_ID:
                selection = AppContract.COLUMN_ID + "?=";
                selectionArgs = new String [] { String.valueOf(ContentUris.parseId(uri))};
                deletedRaw = database.delete(AppContract.TABLENAME,selection, selectionArgs );
                break;

            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }


        return deletedRaw;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        return 0;
    }



}
