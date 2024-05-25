package com.example.lab_5_20175557;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lab_5_20175557.entity.tarea;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

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

    private Spinner spinnerImportance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_editar_tarea);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        textViewDueDate = findViewById(R.id.textViewDueDate);
        textViewDueTime = findViewById(R.id.textViewDueTime);
        spinnerImportance = findViewById(R.id.spinnerImportance);
        Button buttonSave = findViewById(R.id.buttonSave);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Task");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            textViewDueDate.setText(intent.getStringExtra(EXTRA_DUE_DATE));
            textViewDueTime.setText(intent.getStringExtra(EXTRA_DUE_TIME));
            spinnerImportance.setSelection(getImportanceIndex(intent.getIntExtra("EXTRA_IMPORTANCE", NotificationManager.IMPORTANCE_DEFAULT)));
        } else {
            setTitle("Add Task");
        }

        textViewDueDate.setOnClickListener(v -> showDatePickerDialog());
        textViewDueTime.setOnClickListener(v -> showTimePickerDialog());

        buttonSave.setOnClickListener(v -> saveTask());
        checkAndRequestAlarmPermission();
    }

    private int getImportanceIndex(int importance) {
        switch (importance) {
            case NotificationManager.IMPORTANCE_HIGH:
                return 0;
            case NotificationManager.IMPORTANCE_LOW:
                return 2;
            case NotificationManager.IMPORTANCE_DEFAULT:
            default:
                return 1;
        }
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

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        try {
            Date date = dateFormat.parse(dueDate + " " + dueTime);
            if (date != null) {
                long currentTime = System.currentTimeMillis();
                if (date.getTime() < currentTime) {
                    Toast.makeText(this, "Due date and time cannot be in the past", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Invalid date or time format", Toast.LENGTH_SHORT).show();
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

        // Determine the importance level
        int importance = determineImportance(dueDate, dueTime);

        // Schedule the notification
        scheduleNotification(id, title, description, dueDate, dueTime, importance);

        // Schedule task deletion
        scheduleTaskDeletion(id, dueDate, dueTime);

        setResult(RESULT_OK, data);
        finish();
    }

    private int determineImportance(String dueDate, String dueTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        try {
            Date date = dateFormat.parse(dueDate + " " + dueTime);
            if (date != null) {
                long currentTime = System.currentTimeMillis();
                long timeDifference = date.getTime() - currentTime;

                if (timeDifference <= 3 * 60 * 60 * 1000) { // 3 hours in milliseconds
                    return NotificationManager.IMPORTANCE_HIGH;
                } else if (timeDifference <= 24 * 60 * 60 * 1000) { // 24 hours in milliseconds
                    return NotificationManager.IMPORTANCE_DEFAULT;
                } else {
                    return NotificationManager.IMPORTANCE_LOW;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return NotificationManager.IMPORTANCE_DEFAULT;
    }

    private void scheduleTaskDeletion(int id, String dueDate, String dueTime) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !getSystemService(AlarmManager.class).canScheduleExactAlarms()) {
            Toast.makeText(this, "Se requiere permiso de alarma exacto para programar notificaciones.", Toast.LENGTH_SHORT).show();
            return;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        try {
            Date date = dateFormat.parse(dueDate + " " + dueTime);
            if (date != null) {
                long triggerAtMillis = date.getTime() + 60000; // 1 minute after the due time
                Intent intent = new Intent(this, TaskDeletionReceiver.class);
                intent.putExtra("EXTRA_TASK_ID", id);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        this,
                        id,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                );

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                if (alarmManager != null) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }



    private void scheduleNotification(int id, String title, String description, String dueDate, String dueTime, int importance) {
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("EXTRA_TASK_TITLE", title);
        intent.putExtra("EXTRA_TASK_DESCRIPTION", description);
        intent.putExtra("EXTRA_IMPORTANCE", importance);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Parse the due date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        try {
            Date date = dateFormat.parse(dueDate + " " + dueTime);
            if (date != null) {
                long triggerAtMillis = date.getTime();
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                if (alarmManager != null) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
                }
            } else {
                // Handle the error when the date is null
                Toast.makeText(this, "\n" + "No se pudo analizar la fecha y hora de la notificación", Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "No se pudo analizar la fecha y hora de la notificación", Toast.LENGTH_SHORT).show();
        }
    }

    private int getImportanceValue(int index) {
        switch (index) {
            case 0:
                return NotificationManager.IMPORTANCE_HIGH;
            case 2:
                return NotificationManager.IMPORTANCE_LOW;
            case 1:
            default:
                return NotificationManager.IMPORTANCE_DEFAULT;
        }
    }
    private static final int REQUEST_CODE_SCHEDULE_EXACT_ALARM = 1;

    private void checkAndRequestAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!getSystemService(AlarmManager.class).canScheduleExactAlarms()) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivityForResult(intent, REQUEST_CODE_SCHEDULE_EXACT_ALARM);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCHEDULE_EXACT_ALARM) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (!getSystemService(AlarmManager.class).canScheduleExactAlarms()) {
                    Toast.makeText(this, "Exact alarm permission is required for this feature.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}