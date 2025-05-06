package com.example.thesis;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Confirmation extends AppCompatActivity {

    EditText etFirstName, etLastName, etPatientType, etGender, etBirthdate,
            etAge, etContact, etEmail, etDate, etTime;

    Button btnCancel, btnConfirm;

    // Firebase Firestore instance
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmation);

        // Initialize views
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etPatientType = findViewById(R.id.etPatientType);
        etGender = findViewById(R.id.etGender);
        etBirthdate = findViewById(R.id.etBirthdate);
        etAge = findViewById(R.id.etAge);
        etContact = findViewById(R.id.etContact);
        etEmail = findViewById(R.id.etEmail);
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);

        btnCancel = findViewById(R.id.btnCancel);
        btnConfirm = findViewById(R.id.btnConfirm);

        // Get data from Intent
        String fname = getIntent().getStringExtra("fname");
        String lname = getIntent().getStringExtra("lname");
        String contact = getIntent().getStringExtra("contact");
        String email = getIntent().getStringExtra("email");
        String patientType = getIntent().getStringExtra("patientType");
        String gender = getIntent().getStringExtra("gender");
        String birthdate = getIntent().getStringExtra("birthdate");
        String age = getIntent().getStringExtra("age");
        String selectedDate = getIntent().getStringExtra("selectedDate");
        String selectedTime = getIntent().getStringExtra("selectedTime");

        // Set data to EditTexts
        etFirstName.setText(fname);
        etLastName.setText(lname);
        etPatientType.setText(patientType);
        etGender.setText(gender);
        etBirthdate.setText(birthdate);
        etAge.setText(age);
        etContact.setText(contact);
        etEmail.setText(email);
        etDate.setText(selectedDate);
        etTime.setText(selectedTime);

        // Disable editing
        disableEditing();

        // Confirm button dialog
        btnConfirm.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(Confirmation.this);
            builder.setTitle("Confirm Appointment");
            builder.setMessage("Are you sure you want to confirm this appointment?");

            builder.setPositiveButton("Yes", (dialog, which) -> {
                // Create a map to store data
                Map<String, Object> appointmentData = new HashMap<>();
                appointmentData.put("firstName", fname);
                appointmentData.put("lastName", lname);
                appointmentData.put("contact", contact);
                appointmentData.put("email", email);
                appointmentData.put("patientType", patientType);
                appointmentData.put("gender", gender);
                appointmentData.put("birthdate", birthdate);
                appointmentData.put("age", age);
                appointmentData.put("appointmentDate", selectedDate);
                appointmentData.put("appointmentTime", selectedTime);

                // Save the data to Firestore under the "appointments" collection
                db.collection("appointments")
                        .add(appointmentData) // or use .document(appointmentId).set(appointmentData) if you want to create a custom ID
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(Confirmation.this, "Appointment Confirmed and Saved!", Toast.LENGTH_SHORT).show();

                            // Proceed to next screen
                            Intent intent = new Intent(Confirmation.this, Dashboard.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(Confirmation.this, "Error saving appointment!", Toast.LENGTH_SHORT).show();
                        });
            });

            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            builder.create().show();
        });

        // Cancel button dialog
        btnCancel.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(Confirmation.this);
            builder.setTitle("Cancel Appointment");
            builder.setMessage("Are you sure you want to cancel this appointment? All your Details will be Deleted.");

            builder.setPositiveButton("Yes", (dialog, which) -> {
                Toast.makeText(Confirmation.this, "Appointment Canceled", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Confirmation.this, Dashboard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            });

            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

            builder.create().show();
        });
    }

    private void disableEditing() {
        etFirstName.setEnabled(false);
        etLastName.setEnabled(false);
        etPatientType.setEnabled(false);
        etGender.setEnabled(false);
        etBirthdate.setEnabled(false);
        etAge.setEnabled(false);
        etContact.setEnabled(false);
        etEmail.setEnabled(false);
        etDate.setEnabled(false);
        etTime.setEnabled(false);
    }
}
