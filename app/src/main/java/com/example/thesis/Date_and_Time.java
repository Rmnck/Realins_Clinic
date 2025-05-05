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

    // Variables for receiving data from Patient_Reg_Form
    String fname, lname, contact, email, patientType, gender, birthdate, age;
    String selectedSlot = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_and_time);

        bck = findViewById(R.id.bckimage);
        nxt = findViewById(R.id.SaveAppointment);
        calendarView = findViewById(R.id.calendarView);
        slotsGrid = findViewById(R.id.slotsGrid);

        // Receive data from Patient_Reg_Form
        Intent intent = getIntent();
        fname = intent.getStringExtra("fname");
        lname = intent.getStringExtra("lname");
        contact = intent.getStringExtra("contact");
        email = intent.getStringExtra("email");
        patientType = intent.getStringExtra("patientType");
        gender = intent.getStringExtra("gender");
        birthdate = intent.getStringExtra("birthdate");
        age = intent.getStringExtra("age");

        // Get current date and disable past dates
        Calendar calendar = Calendar.getInstance();
        long today = calendar.getTimeInMillis();
        calendarView.setMinDate(today);

        // Set max selectable date to 1 month from today
        calendar.add(Calendar.MONTH, 1);
        long oneMonthLater = calendar.getTimeInMillis();
        calendarView.setMaxDate(oneMonthLater);

        // Handle time slot selection
        for (int i = 0; i < slotsGrid.getChildCount(); i++) {
            final Button button = (Button) slotsGrid.getChildAt(i);
            button.setOnClickListener(v -> {
                // Change background of selected button
                button.setBackgroundResource(R.drawable.gray_round);

                // Reset previously selected button
                if (lastSelectedButton != null && lastSelectedButton != button) {
                    lastSelectedButton.setBackgroundResource(R.drawable.slot_selector);
                }

                lastSelectedButton = button;
                selectedSlot = button.getText().toString(); // Store selected time
            });
        }

        // Back button → navigate back to Patient_Reg_Form with preserved data
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
            startActivity(backIntent);
            finish();
        });

        // Next button → go to Confirmation activity with all data
        nxt.setOnClickListener(v -> {
            int day = calendarView.getDayOfMonth();
            int month = calendarView.getMonth() + 1; // Months are 0-indexed
            int year = calendarView.getYear();

            String selectedDate = day + "/" + month + "/" + year;

            // Validate date and time selection
            if (selectedSlot.isEmpty()) {
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
            confirmIntent.putExtra("selectedDate", selectedDate);
            confirmIntent.putExtra("selectedTime", selectedSlot);

            startActivity(confirmIntent);
        });
    }
}
