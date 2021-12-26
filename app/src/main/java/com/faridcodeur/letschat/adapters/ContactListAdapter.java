package com.faridcodeur.letschat.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.faridcodeur.letschat.R;
import com.faridcodeur.letschat.entities.Contact;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class ContactListAdapter extends BaseAdapter {
    final List<Contact> contacts;
    final Context context;

    public ContactListAdapter(Context context, List<Contact> contacts) {
        this.contacts = contacts;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.contacts.size();
    }

    @Override
    public Object getItem(int i) {
        return this.contacts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") MaterialCardView myView = (MaterialCardView) LayoutInflater.from(context).inflate(R.layout.contact_items, viewGroup, false);
        TextView conatactName = myView.findViewById(R.id.contact_name);
        conatactName.setText(contacts.get(i).getName());
        myView.setOnClickListener(view1 -> {
            Log.e("CLIKCED", "LIST ITEM");
            Toast.makeText(context,"Clicked Contact " + i, Toast.LENGTH_SHORT).show();
        });
        return myView;
    }
}
