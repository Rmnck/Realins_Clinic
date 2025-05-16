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
    String selectedTime = ""; // 🔄 NEW: to store time slot

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_reg_form);

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

        // Set contact number filter
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(11);
        editTextContact.setFilters(filters);

        editTextContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

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

        // ✅ Restore data if coming back from Date_and_Time
        Intent intent = getIntent();

        if (intent != null) {
            String fname = intent.getStringExtra("fname");
            String lname = intent.getStringExtra("lname");
            String contact = intent.getStringExtra("contact");
            String email = intent.getStringExtra("email");
            String patientType = intent.getStringExtra("patientType");
            String gender = intent.getStringExtra("gender");
            String birthdate = intent.getStringExtra("birthdate");
            String age = intent.getStringExtra("age");

            selectedDate = intent.getStringExtra("selectedDate");
            selectedTime = intent.getStringExtra("selectedTime");

            if (fname != null) edtFname.setText(fname);
            if (lname != null) edtLname.setText(lname);
            if (contact != null) editTextContact.setText(contact);
            if (email != null) editTextEmail.setText(email);

            if (birthdate != null) {
                selectedDate = birthdate;
                btnPickDate.setText(birthdate);
            } else if (selectedDate != null) {
                btnPickDate.setText(selectedDate);
            }

            if (age != null) {
                tvAge.setText(age);
            } else if (selectedDate != null) {
                // Recalculate age from selectedDate
                try {
                    String[] parts = selectedDate.split("/");
                    int day = Integer.parseInt(parts[0]);
                    int month = Integer.parseInt(parts[1]) - 1;
                    int year = Integer.parseInt(parts[2]);

                    Calendar today = Calendar.getInstance();
                    int calculatedAge = today.get(Calendar.YEAR) - year;
                    if (today.get(Calendar.MONTH) < month ||
                            (today.get(Calendar.MONTH) == month && today.get(Calendar.DAY_OF_MONTH) < day)) {
                        calculatedAge--;
                    }
                    tvAge.setText(String.valueOf(calculatedAge));
                } catch (Exception e) {
                    tvAge.setText("");
                }
            }

            if (patientType != null) {
                ArrayAdapter adapter = (ArrayAdapter) spinnerPatientType.getAdapter();
                int pos = adapter.getPosition(patientType);
                if (pos >= 0) spinnerPatientType.setSelection(pos);
            }

            if (gender != null) {
                ArrayAdapter adapter = (ArrayAdapter) spinnerGender.getAdapter();
                int pos = adapter.getPosition(gender);
                if (pos >= 0) spinnerGender.setSelection(pos);
            }
        }

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
        try {
            String fname = edtFname.getText() != null ? edtFname.getText().toString().trim() : "";
            String lname = edtLname.getText() != null ? edtLname.getText().toString().trim() : "";
            String contact = editTextContact.getText() != null ? editTextContact.getText().toString().trim() : "";
            String email = editTextEmail.getText() != null ? editTextEmail.getText().toString().trim() : "";
            String patientType = spinnerPatientType.getSelectedItem() != null ? spinnerPatientType.getSelectedItem().toString() : "Select Patient Type";
            String gender = spinnerGender.getSelectedItem() != null ? spinnerGender.getSelectedItem().toString() : "Select Gender";
            String age = tvAge.getText() != null ? tvAge.getText().toString() : "";

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
                View selectedView = spinnerPatientType.getSelectedView();
                if (selectedView instanceof TextView) {
                    ((TextView) selectedView).setError("Please select patient type");
                }
            }

            if (gender.equals("Select Gender")) {
                View selectedView = spinnerGender.getSelectedView();
                if (selectedView instanceof TextView) {
                    ((TextView) selectedView).setError("Please select gender");
                }

            }

            if (selectedDate == null || selectedDate.isEmpty()) {
                btnPickDate.setError("Please select birthdate");
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
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editTextEmail.setError("Invalid Email format");
                hasError = true;
            }

            if (hasError) {
                return;
            }

            new android.app.AlertDialog.Builder(this)
                    .setTitle("Confirmation")
                    .setMessage("Are you sure you filled up the form correctly?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        Intent intent = new Intent(Patient_Reg_Form.this, Date_and_Time.class);
                        intent.putExtra("fname", fname);
                        intent.putExtra("lname", lname);
                        intent.putExtra("contact", contact);
                        intent.putExtra("email", email);
                        intent.putExtra("patientType", patientType);
                        intent.putExtra("gender", gender);
                        intent.putExtra("birthdate", selectedDate);
                        intent.putExtra("age", age);
                        intent.putExtra("selectedDate", selectedDate);
                        intent.putExtra("selectedTime", selectedTime);
                        startActivity(intent);
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        } catch (Exception e) {
            Toast.makeText(this, "An unexpected error occurred. Please check your inputs.", Toast.LENGTH_LONG).show();
            e.printStackTrace(); // Log error to Logcat
        }
    }
}
