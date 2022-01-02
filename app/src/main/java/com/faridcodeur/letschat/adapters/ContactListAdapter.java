package com.faridcodeur.letschat.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.faridcodeur.letschat.R;
import com.faridcodeur.letschat.activity.ChatScreenActivity;
import com.faridcodeur.letschat.databinding.ContactItemsBinding;
import com.faridcodeur.letschat.entities.Contact;
import com.faridcodeur.letschat.entities.UserLocal;
import com.faridcodeur.letschat.utiles.Global;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class ContactListAdapter extends BaseAdapter {
    final List<Contact> contacts;
    private ContactItemsBinding binding;
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
    public Contact getItem(int i) {
        return this.contacts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") MaterialCardView myView = (MaterialCardView)LayoutInflater.from(context).inflate(R.layout.contact_items, viewGroup, false) ;

        TextView nameContact = myView.findViewById(R.id.list_contact_item_name);
        TextView phone = myView.findViewById(R.id.list_contact_item_phone_number);
        nameContact.setText(contacts.get(i).getName());
        phone.setText(contacts.get(i).getPhoneNumber());

        myView.setOnClickListener(view1 -> {
            Intent intent = new Intent(context, ChatScreenActivity.class);
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();
            Gson gson = builder.create();
            String indata;
            for (UserLocal user : Global.userLocals) {
                if (user.getId().equals(contacts.get(i).getId())){
                    indata = gson.toJson(user);
                    intent.putExtra("user", indata);
                }
            }
            context.startActivity(intent);
        });
        return myView;
    }
}
