package com.example.thesis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class Dashboard extends AppCompatActivity {
    LinearLayout Realin, Tolentino, Bundal;
    Button nxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        Realin = findViewById(R.id.DrRealin);
        Tolentino = findViewById(R.id.DrTolentino);
        Bundal = findViewById(R.id.DrBundal);
        nxt = findViewById(R.id.DashboardBTN);


        Realin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, Info_Realin.class);
                startActivity(intent);
            }
        });

        Tolentino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, Info_Tolentino.class);
                startActivity(intent);
            }
        });

        Bundal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, Info_Bundal.class);
                startActivity(intent);
            }
        });
        nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, Patient_Reg_Form.class);
                startActivity(intent);
            }
        });

    }
}
