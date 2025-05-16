package com.example.thesis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Clinic_Services extends AppCompatActivity {

    ImageView bck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clinic_services);
        bck = findViewById(R.id.backbtn);

        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Clinic_Services.this, Dashboard.class);
                startActivity(intent);
            }
        });
    }
}