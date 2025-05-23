package com.example.myapplication;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

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

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String diaryEntry = entries.get(position);

            String date = "";
            String emotion = "";
            String reason = "";

            try {
                int firstBracketEnd = diaryEntry.indexOf("]");
                date = diaryEntry.substring(1, firstBracketEnd);
                String rest = diaryEntry.substring(firstBracketEnd + 2);
                int dashIndex = rest.indexOf(" - ");
                if (dashIndex > 0) {
                    emotion = rest.substring(0, dashIndex);
                    reason = rest.substring(dashIndex + 3);
                } else {
                    emotion = rest;
                    reason = "";
                }
            } catch (Exception e) {
                reason = diaryEntry;
            }

            View dialogView = getLayoutInflater().inflate(R.layout.dialog_diary, null);
            TextView tvDate = dialogView.findViewById(R.id.tvDate);
            TextView tvEmotion = dialogView.findViewById(R.id.tvEmotion);
            TextView tvReason = dialogView.findViewById(R.id.tvReason);
            Button btnClose = dialogView.findViewById(R.id.btnClose);

            tvDate.setText("작성일시 : " + date);
            tvEmotion.setText("감정 : " + emotion);
            tvReason.setText(reason);

            Dialog dialog = new Dialog(this);
            dialog.setContentView(dialogView);

            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.8);
            int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.8);
            dialog.getWindow().setLayout(width, height);

            btnClose.setOnClickListener(v -> dialog.dismiss());

            dialog.show();
        });
    }
}
