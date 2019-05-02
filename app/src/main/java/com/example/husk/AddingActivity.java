package com.example.husk;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.husk.data.AppContract;
import com.example.husk.data.AppProvider;
import com.example.husk.data.DatabaseHelper;

import java.util.zip.Inflater;

public class AddingActivity extends AppCompatActivity
{
    String name, email, password, confirmPassword;
    EditText nameEditText, emailEditText, passwordEditText, confirmPasswordEditText;

    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;
    long value ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseHelper = new DatabaseHelper(this);

        nameEditText = (EditText) findViewById(R.id.name_edittext);
        emailEditText = (EditText) findViewById(R.id.email_edittext);
        passwordEditText = (EditText) findViewById(R.id.password_edittext);
        confirmPasswordEditText = (EditText) findViewById(R.id.confirm_password_edittext);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.adding_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_cancel:
                finish();

            case R.id.action_save:
                addItem();
        }

        return super.onOptionsItemSelected(item);
    }

    public void addItem()
    {
        name = nameEditText.getText().toString();
        email = emailEditText.getText().toString();
        password = passwordEditText.getText().toString();
        confirmPassword = confirmPasswordEditText.getText().toString();


        if (name.isEmpty())
        {
            nameEditText.setError("please enter name");
        }
        else if (email.isEmpty())
        {
            emailEditText.setError("please enter email");
        }
        else if (password.isEmpty())
        {
            passwordEditText.setError("please enter password");
        }
        else if ( !(confirmPassword.equals(password)) )
        {
            confirmPasswordEditText.setError("not match password");
        }
        else
        {
            addItemToDatabase(name, email, password);
        }
    }

    public  void addItemToDatabase(String name , String email , String password)
    {

        sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(AppContract.COLUMN_NAME, name);
        contentValues.put(AppContract.COLUMN_EMAIL, email);
        contentValues.put(AppContract.COLUMN_PASSWORD, password);

        Uri newUri = getContentResolver().insert(AppContract.CONTENT_URI, contentValues);


        if (newUri == null)
        {
            Toast.makeText(this, "failed", Toast.LENGTH_LONG).show();

        }
        else
        {
            Toast.makeText(this, "added: " + newUri, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }


}
