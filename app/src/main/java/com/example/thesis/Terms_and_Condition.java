package com.example.thesis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

public class Terms_and_Condition extends AppCompatActivity {
    ImageView bck;
    Button Nxt;
    CheckBox Cbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_and_condition);

        bck = findViewById(R.id.backbtn);
        Cbox = findViewById(R.id.checkbox_terms);
        Nxt = findViewById(R.id.TCBTN); // Make sure the ID matches your layout XML


        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Terms_and_Condition.this, Start.class);
                startActivity(intent);
            }
        });

        Nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Cbox.isChecked()) {
                    Intent intent = new Intent(Terms_and_Condition.this, Dashboard.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(Terms_and_Condition.this, "Please accept the Terms and Conditions", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
