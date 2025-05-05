package com.example.thesis;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import android.text.InputFilter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class Patient_Reg_Form extends AppCompatActivity {

    ImageView bck;
    Button btn, btnPickDate;
    EditText edtFname, edtLname, editTextContact, editTextEmail;
    Spinner spinnerPatientType, spinnerGender;
    TextView tvAge;
    String selectedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_reg_form);

        // UI References
        bck = findViewById(R.id.backbtn);
        btn = findViewById(R.id.buttonSubmitPatientRegForm);
        btnPickDate = findViewById(R.id.btnPickDate);

        edtFname = findViewById(R.id.edtFname);
        edtLname = findViewById(R.id.edtLname);
        editTextContact = findViewById(R.id.editTextContact);
        editTextEmail = findViewById(R.id.editTextEmail);
        spinnerPatientType = findViewById(R.id.spinnerPatientType);
        spinnerGender = findViewById(R.id.spinnerGender);
        tvAge = findViewById(R.id.tvAge);

        // Limit the input length of the Contact field to 11 characters
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(11);
        editTextContact.setFilters(filters);

        editTextContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0 && !charSequence.toString().startsWith("09")) {
                    editTextContact.setError("Start your Number with 09");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0 && !editable.toString().startsWith("09")) {
                    editTextContact.setError("Start your Number with 09");
                } else {
                    editTextContact.setError(null);
                }
            }
        });

        bck.setOnClickListener(v -> {
            Intent intent = new Intent(Patient_Reg_Form.this, Dashboard.class);
            startActivity(intent);
        });

        ArrayAdapter<String> patientTypeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Select Patient Type", "Pedia", "Adult"});
        spinnerPatientType.setAdapter(patientTypeAdapter);

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Select Gender", "Male", "Female"});
        spinnerGender.setAdapter(genderAdapter);

        btnPickDate.setOnClickListener(v -> showDatePicker());

        btn.setOnClickListener(v -> validateAndProceed());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                (view, year, month, dayOfMonth) -> {
                    selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    btnPickDate.setText(selectedDate);

                    Calendar today = Calendar.getInstance();
                    int age = today.get(Calendar.YEAR) - year;
                    if (today.get(Calendar.MONTH) < month ||
                            (today.get(Calendar.MONTH) == month && today.get(Calendar.DAY_OF_MONTH) < dayOfMonth)) {
                        age--;
                    }
                    tvAge.setText(String.valueOf(age));
                    btnPickDate.setError(null);
                },
                currentYear, currentMonth, currentDay
        );

        datePickerDialog.getDatePicker().setCalendarViewShown(false);
        datePickerDialog.getDatePicker().setSpinnersShown(true);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void validateAndProceed() {
        String fname = edtFname.getText().toString().trim();
        String lname = edtLname.getText().toString().trim();
        String contact = editTextContact.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String patientType = spinnerPatientType.getSelectedItem().toString();
        String gender = spinnerGender.getSelectedItem().toString();
        String age = tvAge.getText().toString();

        boolean hasError = false;

        if (fname.isEmpty()) {
            edtFname.setError("First name is required");
            hasError = true;
        }

        if (lname.isEmpty()) {
            edtLname.setError("Last name is required");
            hasError = true;
        }

        if (patientType.equals("Select Patient Type")) {
            ((TextView) spinnerPatientType.getSelectedView()).setError("Please select patient type");
            hasError = true;
        }

        if (gender.equals("Select Gender")) {
            ((TextView) spinnerGender.getSelectedView()).setError("Please select gender");
            hasError = true;
        }

        if (selectedDate.isEmpty()) {
            btnPickDate.setError("Please select birthdate");
            hasError = true;
        }

        if (contact.isEmpty()) {
            editTextContact.setError("Contact is required");
            hasError = true;
        } else if (contact.length() != 11) {
            editTextContact.setError("Cellphone number must be 11 digits");
            hasError = true;
        } else if (!contact.startsWith("09")) {
            editTextContact.setError("Cellphone number must start with 09");
            hasError = true;
        } else if (!contact.matches("\\d+")) {
            editTextContact.setError("Cellphone number must only contain digits");
            hasError = true;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            hasError = true;
        }

        if (hasError) {
            return;
        }

        // Show confirmation dialog
        new android.app.AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Are you sure you fill up the forms correctly?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Pass data to Date_and_Time
                    Intent intent = new Intent(Patient_Reg_Form.this, Date_and_Time.class);
                    intent.putExtra("fname", fname);
                    intent.putExtra("lname", lname);
                    intent.putExtra("contact", contact);
                    intent.putExtra("email", email);
                    intent.putExtra("patientType", patientType);
                    intent.putExtra("gender", gender);
                    intent.putExtra("birthdate", selectedDate);
                    intent.putExtra("age", age);
                    startActivity(intent);
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
