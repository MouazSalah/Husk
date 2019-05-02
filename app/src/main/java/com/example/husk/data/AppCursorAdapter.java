package com.example.husk.data;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.husk.R;

public class AppCursorAdapter extends CursorAdapter {


    public AppCursorAdapter(Context context, Cursor c)
    {
        super(context, c /* flags */);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        return LayoutInflater.from(context).inflate(R.layout.item_format,parent, false);
        // return LayoutInflater.from(context).inflate(R.layout.item_format, parent, false);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        TextView nameTextView = (TextView) view.findViewById(R.id.item_name_textview);
        TextView passwordTextView = (TextView) view.findViewById(R.id.item_password_textview);

        int nameColumnIndex = cursor.getColumnIndex(AppContract.COLUMN_NAME);
        int breedColumnIndex = cursor.getColumnIndex(AppContract.COLUMN_PASSWORD);

        String appName = cursor.getString(nameColumnIndex);
        String appPassword = cursor.getString(breedColumnIndex);

        nameTextView.setText(appName);
        passwordTextView.setText(appPassword);

    }

}
