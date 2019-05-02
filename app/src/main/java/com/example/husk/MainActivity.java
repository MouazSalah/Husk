package com.example.husk;


import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.husk.data.AppContract;
import com.example.husk.data.AppCursorAdapter;
import com.example.husk.data.DatabaseHelper;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>
{

    private static final int ITEM_LOADER = 0;
    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;
    AppCursorAdapter appCursorAdapter;

    String appName, appEmail, appPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        databaseHelper = new DatabaseHelper(this);
        sqLiteDatabase = databaseHelper.getReadableDatabase();

        ImageView deleteImgView = (ImageView) findViewById(R.id.delete_imageview);

        ListView itemListView = (ListView) findViewById(R.id.items_listview);

        View emptyView = findViewById(R.id.empty_view);
        itemListView.setEmptyView(emptyView);

        appCursorAdapter = new AppCursorAdapter(this, null);
        itemListView.setAdapter(appCursorAdapter);

        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
               Intent intent = new Intent(MainActivity.this, AddingActivity.class);

                Uri currentPetUri = ContentUris.withAppendedId(AppContract.CONTENT_URI, id);
                intent.setData(currentPetUri);
                 startActivity(intent);
            }
        });

        // Kick off the loader
        getLoaderManager().initLoader(ITEM_LOADER, null, this);


        ImageView addImageView  = (ImageView) findViewById(R.id.fab);
        addImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showAlertDialog();
            }
        });

    }

    private void showAlertDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
        .setTitle("Add a new");

        LayoutInflater layoutInflater = getLayoutInflater();
        View v = layoutInflater.inflate(R.layout.alertdialog_layout, null);

        final EditText nameEditText = (EditText) v.findViewById(R.id.name_text);
        final EditText emailEditText = (EditText) v.findViewById(R.id.email_text);
        final EditText passwordEditText = (EditText) v.findViewById(R.id.password_text);


        builder.setView(v)
        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

                appName = nameEditText.getText().toString();
                appEmail = emailEditText.getText().toString();
                appPassword = passwordEditText.getText().toString();

                ContentValues contentValues = new ContentValues();
                contentValues.put(AppContract.COLUMN_NAME, appName);
                contentValues.put(AppContract.COLUMN_EMAIL, appEmail);
                contentValues.put(AppContract.COLUMN_PASSWORD, appPassword);

                Uri uri = getContentResolver().insert(AppContract.CONTENT_URI, contentValues);
                // long successValue = sqLiteDatabase.insert(AppContract.TABLENAME, null, contentValues);
                Toast.makeText(MainActivity.this, "Uri: " + uri , Toast.LENGTH_SHORT).show();
            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                Toast.makeText(MainActivity.this, "Not added", Toast.LENGTH_SHORT).show();

            }
        });
        builder.show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete)
        {
           deleteallData();
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_addnew)
        {
            Intent intent = new Intent(getApplicationContext(), AddingActivity.class);

            Uri uri = ContentUris.withAppendedId(AppContract.CONTENT_URI, id);
            intent.setData(uri);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }



    private void deleteallData()
    {

        int successValue = getContentResolver().delete(AppContract.CONTENT_URI,null, null );
        Toast.makeText(this, "Uri: " + successValue, Toast.LENGTH_SHORT).show();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
// Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                AppContract.COLUMN_NAME,
                AppContract.COLUMN_EMAIL,
                AppContract.COLUMN_PASSWORD };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                AppContract.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        appCursorAdapter = new AppCursorAdapter(this, data);
        appCursorAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        appCursorAdapter.swapCursor(null);
    }
}
