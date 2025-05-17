package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_input_emotion).setOnClickListener(v ->
                startActivity(new Intent(this, EmotionInput.class)));

        findViewById(R.id.btn_diary_list).setOnClickListener(v ->
                startActivity(new Intent(this, DiaryList.class)));

        findViewById(R.id.btn_statistics).setOnClickListener(v ->
                startActivity(new Intent(this, Statistics.class)));

        findViewById(R.id.btn_settings).setOnClickListener(v ->
                startActivity(new Intent(this, Settings.class)));
    }
}