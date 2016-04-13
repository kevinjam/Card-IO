package com.kevinjanvier.latterglory.cardverification;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ThankYou extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);

        Intent iscool = getIntent();
        if (iscool.getCharSequenceExtra("Card Number") != null) {
            final TextView setmsg = (TextView)findViewById(R.id.resultTextView);
            setmsg.setText(iscool.getCharSequenceExtra("Card Number"));
        }
    }
}
