package com.faridcodeur.letschat;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.faridcodeur.letschat.adapter.ListAdapterSetting;
import com.faridcodeur.letschat.entities.Setting;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity {
    public List<Setting> settings = new ArrayList<>();
    public ListAdapterSetting listAdapterSetting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ListView simpleList = (ListView) findViewById(R.id.listSetting);
        settings.add(new Setting("Nom de profile","Gohoue Rodias",R.drawable.ic_action_profile));
        settings.add(new Setting("Photo de profile","Changer ma photo de profile",R.drawable.photo_camera));
        listAdapterSetting=  new ListAdapterSetting(getApplicationContext(), settings);

        simpleList.setAdapter(listAdapterSetting);
    }
}