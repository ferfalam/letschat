package com.faridcodeur.letschat.contact;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.faridcodeur.letschat.entities.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    protected MutableLiveData<List<Contact>> mutableLiveData = new MutableLiveData<>();

    public ContactViewModel(Application application) {
        super(application);
    }

    public void retrieveContacts(ContentResolver contentResolver){
        @SuppressLint("Recycle") final Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, new String[]{ContactsContract.Data.DISPLAY_NAME, ContactsContract.Data.HAS_PHONE_NUMBER, ContactsContract.Data._ID}, null, null, null);
        if (cursor == null){
            Log.e("retrieveContacts", "retrieveContacts: Cannot retrieve the contacts");
            return;
        }
        List<Contact> contactList = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                @SuppressLint("Range") final String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
                @SuppressLint("Range") final int phoneNumber = cursor.getInt(cursor.getColumnIndex(ContactsContract.Data.HAS_PHONE_NUMBER));
                @SuppressLint("Range") final long id = cursor.getInt(cursor.getColumnIndex(ContactsContract.Data._ID));
                if(phoneNumber > 0) {
                    contactList.add(new Contact(name, phoneNumber, id));
                }
            }while (cursor.moveToNext());
        }

        mutableLiveData.postValue(contactList);

        if (!cursor.isClosed()){
            cursor.close();
        }
    }

}