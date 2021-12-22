package com.faridcodeur.letschat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private Button btnClick;
    private EditText Name, Date;
    private TextView msg, NameOut, DateOut;

    public void onClick(View v)
    {
        if (v == btnClick)
        {
            if (Name.equals("") == false )
            {
                NameOut = Name;
                msg.setVisibility(View.VISIBLE);
            }
            else
            {
                msg.setText("Please complete both fields");

            }
        }
        return;

    }
}