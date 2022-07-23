package com.example.doctrocareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

public class FormInfoActivity extends AppCompatActivity {
    TextView text1, text2, text3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_info);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        text1 = findViewById(R.id.textView2);
        text2 = findViewById(R.id.textView4);
        text3 = findViewById(R.id.textView5);

        text1.setText("Creation date" + getIntent().getStringExtra("dateCreated"));
        text2.setText(getIntent().getStringExtra("doctor"));
        text3.setText(getIntent().getStringExtra("description"));
    }
}
