package com.faridcodeur.letschat.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.faridcodeur.letschat.adapters.SondageAdapter;
import com.faridcodeur.letschat.R;
import com.faridcodeur.letschat.databinding.ActivitySondageBoxBinding;
import com.faridcodeur.letschat.entities.SondageMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SondageBoxActivity extends AppCompatActivity {
    private List<SondageMapping> listSondage = new ArrayList<SondageMapping>();
    ActivitySondageBoxBinding binding;
    SondageAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySondageBoxBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.sondageBoxToolbar);

        generateQuestion();

        Toast.makeText(SondageBoxActivity.this, String.valueOf(listSondage.size()) , Toast.LENGTH_LONG).show();

        adapter = new SondageAdapter(listSondage , SondageBoxActivity.this);

        ListView listView = (ListView) findViewById(R.id.sondageBox);

        listView.setScrollContainer(false);
        listView.setDivider(null);

        listView.setAdapter(adapter);

    }

    void generateQuestion() {

        List<String> strList = Arrays.asList("oui" , "non");
        List<String> strList1 = Arrays.asList("je suis content" , "je suis là", "Nous sommes tous là");
        listSondage.add(new SondageMapping("1 . que pensz vous" , "text"));
        listSondage.add(new SondageMapping("2 . que voulez vous" , "uniChoice",strList.size(),strList));
        listSondage.add(new SondageMapping("2 . Vous aimez le sucre" , "uniChoice",strList.size(),strList));
        listSondage.add(new SondageMapping("3 . Liste de question multiChoice" , "multiChoice",strList1.size(),strList1));
    }
}