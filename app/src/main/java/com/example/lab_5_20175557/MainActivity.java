package com.example.lab_5_20175557;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String UNIVERSITY_CODE = "123456"; // Replace with your university code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editTextCode = findViewById(R.id.editTextCode);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(v -> {
            String inputCode = editTextCode.getText().toString().trim();
            if (inputCode.equals(UNIVERSITY_CODE)) {
                Intent intent = new Intent(MainActivity.this, TaskListActivity.class);
                startActivity(intent);
                finish(); // Finish MainActivity so the user can't go back to the login screen
            } else {
                Toast.makeText(MainActivity.this, "Invalid code", Toast.LENGTH_SHORT).show();
            }
        });
    }
}