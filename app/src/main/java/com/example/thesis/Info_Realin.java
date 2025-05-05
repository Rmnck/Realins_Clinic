package com.example.thesis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class Info_Realin extends AppCompatActivity {
    LinearLayout bck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_realin);

        bck = findViewById(R.id.back);


        // Handle back button click to navigate to Dashboard
        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Info_Realin.this, Dashboard.class);
                startActivity(intent);
            }
        });


    }
}
