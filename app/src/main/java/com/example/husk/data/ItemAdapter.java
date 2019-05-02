package com.example.husk.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.husk.R;

import java.util.ArrayList;

public class ItemAdapter<I> extends ArrayAdapter<Item>
{

    public ItemAdapter(Context context, ArrayList<Item> item)
    {
        super(context, 0, item);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_format, parent, false);

        Item currentItem = getItem(position);

        TextView appNameTextView = (TextView) itemView.findViewById(R.id.item_name_textview);
        TextView appPasswordTextView = (TextView) itemView.findViewById(R.id.item_password_textview);

        StringBuilder stringBuilder = new StringBuilder();

        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + AppContract.TABLENAME ,null);
        
        // long count = cursor.getCount();
        while (cursor.moveToNext())
        {
            int idColumn = cursor.getColumnIndex(AppContract.COLUMN_ID);
            String id = cursor.getString(idColumn);

            int nameColumn = cursor.getColumnIndex(AppContract.COLUMN_NAME);
            String name = cursor.getString(nameColumn);
            appNameTextView.setText(name);

            int emailColumn = cursor.getColumnIndex(AppContract.COLUMN_EMAIL);
            String email = cursor.getString(emailColumn);

            int passwordColumn = cursor.getColumnIndex(AppContract.COLUMN_PASSWORD);
            String password = cursor.getString(passwordColumn);
            appPasswordTextView.setText(password);

        }
        
        return itemView;
    }
}