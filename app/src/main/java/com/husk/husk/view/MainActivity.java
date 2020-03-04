package com.husk.husk.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.husk.husk.R;
import com.husk.husk.database.DatabaseManager;
import com.husk.husk.model.Item;
import com.husk.husk.adapter.ItemAdapter;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
{
    DatabaseManager databaseManager;
    ListView itemListView;

    ArrayList<Item> itemArrayList;
    ItemAdapter itemAdapter;

    EditText nameEditText, emailEditText, passwordEditText;
    String appName, appEmail, appPassword;

    SharedPreferences sharedPreferences;
    String APP_LANGUAGE = "en";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseManager = new DatabaseManager(this);



        itemListView = (ListView) findViewById(R.id.items_listview);

        View emptyView = findViewById(R.id.empty_view);
        itemListView.setEmptyView(emptyView);


        itemArrayList = new ArrayList<Item>();
        itemArrayList = databaseManager.getAllItems();
        itemAdapter = new ItemAdapter(this, itemArrayList);
        if (itemArrayList.size() == 0)
        {
            itemListView.setEmptyView(emptyView);
        }
        else
        {
            itemListView.setAdapter(itemAdapter);
        }

        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Item item = (Item) parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), AddingActivity.class);
                int currentItemID = item.getId();
                intent.putExtra("item_id" , currentItemID);
                startActivity(intent);
            }
        });

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


    public void setAppLanguage(String lang)
    {
        Locale locale = new Locale(APP_LANGUAGE);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;

        this.getResources().updateConfiguration(config, this.getResources().getDisplayMetrics());

    }


    private void showAlertDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
        .setTitle(R.string.add_new);

        LayoutInflater layoutInflater = getLayoutInflater();
        View v = layoutInflater.inflate(R.layout.alertdialog_layout, null);

        nameEditText = (EditText) v.findViewById(R.id.name_text);
        emailEditText = (EditText) v.findViewById(R.id.email_text);
        passwordEditText = (EditText) v.findViewById(R.id.password_text);


        builder.setView(v)
        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if (checkAllFields())
                {
                    appName = nameEditText.getText().toString();
                    appEmail = emailEditText.getText().toString();
                    appPassword = passwordEditText.getText().toString();
                    insertNewItem();
                }
            }
        })

        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });
        builder.show();
    }

    private void insertNewItem()
    {
        long i = databaseManager.insert(appName, appEmail, appPassword);
        Log.d("inserValue", String.valueOf(i));
        if (i == -1)
        {
            Toast.makeText(MainActivity.this, "try again", Toast.LENGTH_LONG).show();
            itemAdapter.notifyDataSetChanged();
        }
        else
        {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    private boolean checkAllFields()
    {
        boolean returnValue = false;

        if (nameEditText.getText().toString().isEmpty())
        {
            nameEditText.setError("enter name");
            returnValue = false;
        }
        else if (emailEditText.getText().toString().isEmpty())
        {
            emailEditText.setError("enter email");
            returnValue = false;
        }
        else if (passwordEditText.getText().toString().isEmpty())
        {
            passwordEditText.setError("enter password");
            returnValue = false;
        }
        else
        {
            appName = nameEditText.getText().toString();
            appEmail = emailEditText.getText().toString();
            appPassword = passwordEditText.getText().toString();

            returnValue = true ;
        }

        return returnValue;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_delete)
        {
           databaseManager.deleteAllData();
           Intent intent = new Intent(getApplicationContext(), MainActivity.class);
           startActivity(intent);
        }
        if (id == R.id.action_addnew)
        {
           Intent intent = new Intent(getApplicationContext(), AddingActivity.class);
           startActivity(intent);
        }
       /* if (id == R.id.action_changelanguage)
        {
            if (APP_LANGUAGE == "en")
            {
                item.setTitle("اللغة العربية");
                setAppLanguage("en");
                APP_LANGUAGE = "en";
                sharedPreferences.edit().putString("language", APP_LANGUAGE);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
            else
            {
                item.setTitle("English");
                setAppLanguage("ar");
                APP_LANGUAGE = "ar";
                sharedPreferences.edit().putString("language", APP_LANGUAGE);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }*/

        return super.onOptionsItemSelected(item);
    }

}
