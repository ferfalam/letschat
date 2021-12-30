package com.faridcodeur.letschat.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.faridcodeur.letschat.adapters.ContactListAdapter;
import com.faridcodeur.letschat.databinding.ActivityMyContactBinding;
import com.faridcodeur.letschat.entities.Contact;
import com.faridcodeur.letschat.utiles.Global;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyContactActivity extends AppCompatActivity {

    private ActivityMyContactBinding binding;
    private ContactListAdapter contactListAdapter;
    private List<Contact> contacts = new ArrayList<>();
    private Handler handler;
    private ProgressDialog dialog;
    final int PERMISSIONS_REQUEST = 2215;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String[] PERMISSIONS_CONTACT = {Manifest.permission.READ_CONTACTS};

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                Snackbar.make(binding.getRoot(), "L'application requiert l'accès aux contacts", Snackbar.LENGTH_LONG).setAction("Activer", view -> ActivityCompat.requestPermissions(this, PERMISSIONS_CONTACT, PERMISSIONS_REQUEST)).show();
            } else {
                ActivityCompat.requestPermissions(this, PERMISSIONS_CONTACT, PERMISSIONS_REQUEST);
            }
        }

        if (Global.contacts.size() <= 0){
            fetchContacts();
        }else{
            contacts = Global.contacts;
        }
        buildCustomAdapter();
        binding.listContactReturnButton.setOnClickListener(
                v -> {
                    Intent intent = getIntent();
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
        );
    }


    private void buildCustomAdapter() {
        contactListAdapter = new ContactListAdapter(this, contacts);
        binding.listContacts.setAdapter(contactListAdapter);
    }

    @SuppressLint("HandlerLeak")
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
                new String[]{ContactsContract.Data.DISPLAY_NAME, ContactsContract.Data.HAS_PHONE_NUMBER, ContactsContract.Data._ID}, null, null, null);
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
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST) {
            if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(binding.getRoot(), "No Permission", Snackbar.LENGTH_LONG).setAction("Ok", container_view -> {
                    Log.e("onRequest", "onRequestPermissionsResult: No permission");
                }).show();
            }
        }
    }
}