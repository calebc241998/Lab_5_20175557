package com.example.lab_5_20175557;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddEditTaskActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.example.todolist.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.example.todolist.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.example.todolist.EXTRA_DESCRIPTION";
    public static final String EXTRA_DUE_DATE = "com.example.todolist.EXTRA_DUE_DATE";
    public static final String EXTRA_DUE_TIME = "com.example.todolist.EXTRA_DUE_TIME";

    private EditText editTextTitle;
    private EditText editTextDescription;
    private TextView textViewDueDate;
    private TextView textViewDueTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_editar_tarea);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        textViewDueDate = findViewById(R.id.textViewDueDate);
        textViewDueTime = findViewById(R.id.textViewDueTime);
        Button buttonSave = findViewById(R.id.buttonSave);

        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Task");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            textViewDueDate.setText(intent.getStringExtra(EXTRA_DUE_DATE));
            textViewDueTime.setText(intent.getStringExtra(EXTRA_DUE_TIME));
        } else {
            setTitle("Add Task");
        }

        textViewDueDate.setOnClickListener(v -> showDatePickerDialog());
        textViewDueTime.setOnClickListener(v -> showTimePickerDialog());

        buttonSave.setOnClickListener(v -> saveTask());
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            String dueDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
            textViewDueDate.setText(dueDate);
        }, year, month, day);

        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute1) -> {
            String dueTime = hourOfDay + ":" + String.format("%02d", minute1);
            textViewDueTime.setText(dueTime);
        }, hour, minute, true);

        timePickerDialog.show();
    }

    private void saveTask() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        String dueDate = textViewDueDate.getText().toString();
        String dueTime = textViewDueTime.getText().toString();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description) || TextUtils.isEmpty(dueDate) || TextUtils.isEmpty(dueTime)) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_DUE_DATE, dueDate);
        data.putExtra(EXTRA_DUE_TIME, dueTime);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }
}