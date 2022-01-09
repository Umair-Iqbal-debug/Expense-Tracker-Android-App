package com.example.expensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    SharedPreferences.Editor editor;
    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = getSharedPreferences("Expense",MODE_PRIVATE);

        editor=prefs.edit();

        name = prefs.getString(MainActivity.KEY_NAME,"new name");

        EditText nameText = findViewById(R.id.nameTextView);
        nameText.setText(name);

        Button btn = findViewById(R.id.changeNameBtn);
        btn.setOnClickListener(v ->{
            editor.putString(MainActivity.KEY_NAME,nameText.getText().toString());
            editor.commit();
            finish();
        });

    }
}