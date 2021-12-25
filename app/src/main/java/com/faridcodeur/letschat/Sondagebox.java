package com.faridcodeur.letschat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.faridcodeur.letschat.Adapter.SondageAdapter;
import com.faridcodeur.letschat.databinding.ActivityMain2Binding;
import com.faridcodeur.letschat.entities.SondageMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sondagebox extends AppCompatActivity {
    private List<SondageMapping> listSondage = new ArrayList<SondageMapping>();
    ActivityMain2Binding binding;
    SondageAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_main2);

        generateQuestion();

        Toast.makeText(Sondagebox.this, String.valueOf(listSondage.size()) , Toast.LENGTH_LONG).show();

        adapter = new SondageAdapter(listSondage , Sondagebox.this);

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
        listSondage.add(new SondageMapping("2 . que voulez vous" , "uniChoice",strList.size(),strList));
        listSondage.add(new SondageMapping("2 . que voulez vous" , "uniChoice",strList.size(),strList));
        listSondage.add(new SondageMapping("2 . que voulez vous" , "uniChoice",strList.size(),strList));
        listSondage.add(new SondageMapping("3 . Liste de question multiChoice" , "multiChoice",strList1.size(),strList1));
    }
}