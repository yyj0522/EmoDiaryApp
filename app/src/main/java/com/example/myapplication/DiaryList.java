package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import android.widget.ArrayAdapter;

public class DiaryList extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diarylistactivity);

        Button btnToMain = findViewById(R.id.btn_to_main);
        btnToMain.setOnClickListener(v -> finish());

        EmotionDatabase db = new EmotionDatabase(this);
        List<String> entries = db.getAllDiaries();

        ListView listView = findViewById(R.id.list_diary);
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, entries));
    }
}
