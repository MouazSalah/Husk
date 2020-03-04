package com.husk.husk.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.husk.husk.R;
import com.husk.husk.database.DatabaseManager;
import com.husk.husk.model.Item;
import com.husk.husk.database.SqliteHelper;

public class AddingActivity extends AppCompatActivity
{
    String name, email, password, confirmPassword;
    EditText nameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    SqliteHelper sqliteHelper;
    int itemId;

    DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseManager = new DatabaseManager(this);
        sqliteHelper = new SqliteHelper(this);


        nameEditText = (EditText) findViewById(R.id.name_edittext);
        emailEditText = (EditText) findViewById(R.id.email_edittext);
        passwordEditText = (EditText) findViewById(R.id.password_edittext);
        confirmPasswordEditText = (EditText) findViewById(R.id.confirm_password_edittext);

        Intent mIntent = getIntent();
        itemId = mIntent.getIntExtra("item_id", 0);

        if (itemId != 0)
        {
            getContact();
        }

        nameEditText.setText(name);
        emailEditText.setText(email);
        passwordEditText.setText(password);
        confirmPasswordEditText.setText(password);
    }


    public void getContact()
    {
        Item item = databaseManager.getItem(itemId);
        name = item.getName();
        email = item.getEmail();
        password = item.getPassword();
        confirmPassword = item.getPassword();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.adding_menu, menu);

        if (itemId == 0)
        {
            MenuItem item = menu.findItem(R.id.action_delete);
            item.setVisible(false);
        }

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
                CheckFields();
                break;

            case R.id.action_delete:

                deleteItemDialog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void CheckFields()
    {
        if (nameEditText.getText().toString().isEmpty())
        {
            nameEditText.setError("enter name");
        }
        else if (emailEditText.getText().toString().isEmpty())
        {
            emailEditText.setError("enter email");
        }
        else if (passwordEditText.getText().toString().isEmpty())
        {
            passwordEditText.setError("enter password");
        }
        else if (confirmPasswordEditText.getText().toString().isEmpty())
        {
            passwordEditText.setError("enter password again");
        }
        else if (!(confirmPasswordEditText.getText().toString().equals(passwordEditText.getText().toString())) )
        {
            confirmPasswordEditText.setError("not match");
        }
        else
        {
            name = nameEditText.getText().toString();
            email = emailEditText.getText().toString();
            password = passwordEditText.getText().toString();

            if (itemId == 0)
            {
                insertNewItem();
            }
            else
            {
                updateItem(name, email, password);
            }
        }
    }

    private void insertNewItem()
    {
        long i = databaseManager.insert(name, email, password);
        Log.d("inserValue", String.valueOf(i));
        if (i != -1)
        {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }


    public void updateItem(String name , String email , String password)
    {
        long value = databaseManager.update(itemId, name, email, password);
        Log.d("updateValue", String.valueOf(value));
        if (value != -1)
        {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    public void deleteItem()
    {
        long deletedItemValue = databaseManager.deleteItem(itemId);

        Log.d("delete value: " , String.valueOf(deletedItemValue));
        if (deletedItemValue != -1)
        {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

    }



    public void deleteItemDialog()
    {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle(R.string.deleting)
                    .setMessage(R.string.are_you_sure)
                    .setCancelable(false)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            deleteItem();
                        }
                    })
                    .setNegativeButton(R.string.No, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                        }
                    });
            builder.show();
    }
}
