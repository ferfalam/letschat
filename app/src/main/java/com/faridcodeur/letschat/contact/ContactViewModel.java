package com.faridcodeur.letschat.contact;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.ArrayMap;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.faridcodeur.letschat.entities.Contact;
import com.faridcodeur.letschat.entities.Surveys;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    public static void Test(){
        List<Map<String, Object>> questions = new ArrayList<>();

        //Pour les TextQuestion
        Map<String, Object> textQuestion = new HashMap<>();
        textQuestion.put("type", "TEXT_QUESTION");
        textQuestion.put("question", "La question");

        Map<String, Object> uniqueQuestion = new HashMap<>();
        uniqueQuestion.put("type", "UNIQUE_QUESTION");
        uniqueQuestion.put("question", "La question");
        uniqueQuestion.put("radiosTextValue", new String[]{"Value1", "Value2","..."});

        Map<String, Object> multipleQuestion = new HashMap<>();
        uniqueQuestion.put("type", "MULTIPLE_QUESTION");
        uniqueQuestion.put("question", "La question");
        uniqueQuestion.put("checkboxTextValue", new String[]{"Value1", "Value2","..."});

        questions.add(textQuestion);
        questions.add(uniqueQuestion);
        questions.add(multipleQuestion);

        String str = new Gson().toJson(questions);

        Surveys surveys = new Surveys("Title", "Question");
        surveys.setQuestions(str);

        Log.e("TEST", "Test: " + surveys.getQuestions());

        //Pour recuperer sous forme e list
        List<Map<String, Object>> list = new Gson().fromJson(surveys.getQuestions(), questions.getClass());
        Log.e("TEST", "Test: " + list);

    }

}