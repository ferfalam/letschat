package com.faridcodeur.letschat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

import com.faridcodeur.letschat.adapters.ContactListAdapter;
import com.faridcodeur.letschat.adapters.DiscussionListAdapter;
import com.faridcodeur.letschat.databinding.ActivityMyContactBinding;

import com.faridcodeur.letschat.R;
import com.faridcodeur.letschat.entities.Contact;
import com.faridcodeur.letschat.entities.Discussion;
import com.faridcodeur.letschat.utiles.Global;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MyContactActivity extends AppCompatActivity {

    private ActivityMyContactBinding binding;
    private ContactListAdapter contactListAdapter;
    private List<Contact> contacts = new ArrayList<>();
    private Handler handler;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Global.contacts.size() <= 0){
            fetchContacts();
        }else{
            contacts = Global.contacts;
        }
        buildCustomAdapter();
        binding.listContactReturnButton.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = getIntent();
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        );
    }


    private void buildCustomAdapter() {
        contactListAdapter = new ContactListAdapter(this, contacts);
        binding.listContacts.setAdapter(contactListAdapter);
    }

    private void fetchContacts(){
        dialog = new ProgressDialog(this);
        dialog.setMessage("Chargement des contacts");
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();

        new Thread() {
            public void run() {
                getContactList();
                handler.sendEmptyMessage(0);
            }
        }.start();

        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                Log.d("TAG", "handleMessage: " + contacts.size());
                Collections.sort(contacts);
                Global.contacts = contacts;
                contactListAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        };

    }


    @SuppressLint("Range")
    private void getContactList() {
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        List<Contact> temp = new ArrayList<>();
        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur.moveToNext()) {
                 String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                 String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        phoneNo = phoneNo.replaceAll("\\s+","");
                        contacts.add(new Contact(name, phoneNo));
                    }
                    pCur.close();
                }
            }
        }
        if(cur!=null){
            cur.close();
        };
    }
}