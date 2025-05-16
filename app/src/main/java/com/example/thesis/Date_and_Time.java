package com.example.thesis;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Calendar;

public class Date_and_Time extends AppCompatActivity {

    ImageView bck;
    Button nxt;
    DatePicker calendarView;
    GridLayout slotsGrid;
    Button lastSelectedButton = null;

    String fname, lname, contact, email, patientType, gender, birthdate, age;
    String selectedSlot = "";
    String selectedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_and_time);

        bck = findViewById(R.id.bckimage);
        nxt = findViewById(R.id.SaveAppointment);
        calendarView = findViewById(R.id.calendarView);
        slotsGrid = findViewById(R.id.slotsGrid);

        // Retrieve intent extras
        Intent intent = getIntent();
        fname = intent.getStringExtra("fname");
        lname = intent.getStringExtra("lname");
        contact = intent.getStringExtra("contact");
        email = intent.getStringExtra("email");
        patientType = intent.getStringExtra("patientType");
        gender = intent.getStringExtra("gender");
        birthdate = intent.getStringExtra("birthdate");
        age = intent.getStringExtra("age");
        selectedDate = intent.getStringExtra("selectedDate");
        selectedSlot = intent.getStringExtra("selectedTime");

        // Limit calendar to today through one month from today
        Calendar calendar = Calendar.getInstance();
        calendarView.setMinDate(calendar.getTimeInMillis());
        calendar.add(Calendar.MONTH, 1);
        calendarView.setMaxDate(calendar.getTimeInMillis());

        // If returning from confirmation, restore previously selected date
        if (selectedDate != null && !selectedDate.isEmpty()) {
            String[] parts = selectedDate.split("/");
            if (parts.length == 3) {
                int day = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]) - 1;
                int year = Integer.parseInt(parts[2]);
                calendarView.updateDate(year, month, day);
            }
        }

        // Time slot selection
        for (int i = 0; i < slotsGrid.getChildCount(); i++) {
            final Button slotButton = (Button) slotsGrid.getChildAt(i);

            if (selectedSlot != null && selectedSlot.equals(slotButton.getText().toString())) {
                slotButton.setBackgroundResource(R.drawable.gray_round);
                lastSelectedButton = slotButton;
            }

            slotButton.setOnClickListener(v -> {
                slotButton.setBackgroundResource(R.drawable.gray_round);
                if (lastSelectedButton != null && lastSelectedButton != slotButton) {
                    lastSelectedButton.setBackgroundResource(R.drawable.slot_selector);
                }
                lastSelectedButton = slotButton;
                selectedSlot = slotButton.getText().toString();
            });
        }

        // Handle back button
        bck.setOnClickListener(v -> {
            Intent backIntent = new Intent(Date_and_Time.this, Patient_Reg_Form.class);
            backIntent.putExtra("fname", fname);
            backIntent.putExtra("lname", lname);
            backIntent.putExtra("contact", contact);
            backIntent.putExtra("email", email);
            backIntent.putExtra("patientType", patientType);
            backIntent.putExtra("gender", gender);
            backIntent.putExtra("birthdate", birthdate);
            backIntent.putExtra("age", age);
            backIntent.putExtra("selectedDate", selectedDate);
            backIntent.putExtra("selectedTime", selectedSlot);
            startActivity(backIntent);
            finish();
        });

        // Handle next/save button
        nxt.setOnClickListener(v -> {
            int day = calendarView.getDayOfMonth();
            int month = calendarView.getMonth() + 1;
            int year = calendarView.getYear();
            String dateToSend = day + "/" + month + "/" + year;

            if (selectedSlot == null || selectedSlot.isEmpty()) {
                Toast.makeText(Date_and_Time.this, "Please select a time slot", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent confirmIntent = new Intent(Date_and_Time.this, Confirmation.class);
            confirmIntent.putExtra("fname", fname);
            confirmIntent.putExtra("lname", lname);
            confirmIntent.putExtra("contact", contact);
            confirmIntent.putExtra("email", email);
            confirmIntent.putExtra("patientType", patientType);
            confirmIntent.putExtra("gender", gender);
            confirmIntent.putExtra("birthdate", birthdate);
            confirmIntent.putExtra("age", age);
            confirmIntent.putExtra("selectedDate", dateToSend);
            confirmIntent.putExtra("selectedTime", selectedSlot);
            startActivity(confirmIntent);
        });
    }
}
