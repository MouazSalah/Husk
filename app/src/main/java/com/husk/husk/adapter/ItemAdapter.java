package com.husk.husk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.husk.husk.R;
import com.husk.husk.model.Item;

import java.util.ArrayList;

public class ItemAdapter<I> extends ArrayAdapter<Item>
{

    ArrayList<Item> itemArrayList = new ArrayList<>();

    public ItemAdapter(Context context, ArrayList<Item> item)
    {
        super(context, 0, item);

        this.itemArrayList = item;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_format, parent, false);

        Item currentItem = itemArrayList.get(position);

        TextView appNameTextView = (TextView) itemView.findViewById(R.id.item_name_textview);
        TextView appPasswordTextView = (TextView) itemView.findViewById(R.id.item_password_textview);

        String name = currentItem.getName();
        appNameTextView.setText(name);

        String password = currentItem.getPassword();
        appPasswordTextView.setText(password);

        return itemView;
    }
}