package com.faridcodeur.letschat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Space;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.faridcodeur.letschat.R;
import com.faridcodeur.letschat.entities.SondageMapping;

import java.util.ArrayList;
import java.util.List;

public class SondageAdapter extends BaseAdapter {
    public List<SondageMapping> listSondageMapping;
    public Context context;

    // constuctor

    public SondageAdapter(List<SondageMapping> listSondageMapping, Context context) {
        this.listSondageMapping = listSondageMapping;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listSondageMapping.size();
    }

    @Override
    public Object getItem(int position) {
        return listSondageMapping.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

//    public List<RadioButton> generateListRadio(int nuberOfRadioButton , List<String> listOfString){
//        List<RadioButton> radioButtonList = new ArrayList<RadioButton>();
//
//        for (int i = 0; i < nuberOfRadioButton; i++) {
//            RadioButton radio = new RadioButton(context);
//            radio.setText(listOfString.get(i));
//            radioButtonList.add(radio);
//        }
//        return radioButtonList;
//    }

    @Override
    public View getView(int index, View view, ViewGroup viewGroup) {



        if (listSondageMapping.get(index).getQuestionType() == "text") {
            LinearLayout myView = (LinearLayout)  LayoutInflater.from(this.context).inflate(R.layout.question_text_type , viewGroup ,false);

            TextView textView = myView.findViewById(R.id.question);
            textView.setText(listSondageMapping.get(index).getQuestion());

            return myView;
        }else if(listSondageMapping.get(index).getQuestionType() == "uniChoice") {
            LinearLayout myView = (LinearLayout)  LayoutInflater.from(this.context).inflate(R.layout.question_unichoice_type , viewGroup ,false);
            TextView textView = myView.findViewById(R.id.question);
            textView.setText(listSondageMapping.get(index).getQuestion());

            RadioGroup radioGroup = myView.findViewById(R.id.radioOption);

            for (int i = 0; i < listSondageMapping.get(index).getNumberOfIndexQuestionType(); i++) {
                RadioButton radioButton = new RadioButton(context);
                radioButton.setText(listSondageMapping.get(index).getListString().get(i));
                radioGroup.addView(radioButton);
            }

            return myView;
        } else if (listSondageMapping.get(index).getQuestionType() == "multiChoice") {
            LinearLayout myView = (LinearLayout)  LayoutInflater.from(this.context).inflate(R.layout.question_multichoice_type , viewGroup ,false);

            TextView textView = myView.findViewById(R.id.questionmultiple);
            textView.setText(listSondageMapping.get(index).getQuestion());

            LinearLayout linearLayout = myView.findViewById(R.id.enter);

            for (int i = 0; i < listSondageMapping.get(index).getNumberOfIndexQuestionType(); i++) {
                CheckBox checkBox = new CheckBox(context);
                checkBox.setText(listSondageMapping.get(index).getListString().get(i));
                linearLayout.addView(checkBox);
            }

            return myView;
        }
        return null;
    }
}