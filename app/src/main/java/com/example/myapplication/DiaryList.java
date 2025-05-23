package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
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

            View dialogView = getLayoutInflater().inflate(R.layout.dialog_diary, null);

            TextView tvDatetime = dialogView.findViewById(R.id.tv_diary_datetime);
            TextView tvEmotion = dialogView.findViewById(R.id.tv_diary_emotion);
            TextView tvReason = dialogView.findViewById(R.id.tv_diary_reason);
            Button btnClose = dialogView.findViewById(R.id.btn_close);
            Button btnDelete = dialogView.findViewById(R.id.btn_delete);

            String datetime = "";
            String emotion = "";
            String reason = "";

            try {
                int firstBracket = diaryEntry.indexOf("]");
                datetime = diaryEntry.substring(1, firstBracket);
                String rest = diaryEntry.substring(firstBracket + 2);
                int dashIndex = rest.indexOf(" - ");
                emotion = rest.substring(0, dashIndex);
                reason = rest.substring(dashIndex + 3);

                tvDatetime.setText("작성일시 : " + datetime);
                tvEmotion.setText("감정 : " + emotion);
                tvReason.setText(reason);
            } catch (Exception e) {
                tvDatetime.setText("");
                tvEmotion.setText("");
                tvReason.setText(diaryEntry);
            }

            androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setView(dialogView)
                    .create();

            btnClose.setOnClickListener(v -> dialog.dismiss());

            String finalDatetime = datetime;
            String finalEmotion = emotion;
            String finalReason = reason;

            btnDelete.setOnClickListener(v -> {
                EmotionDatabase database = new EmotionDatabase(this);
                boolean deleted = database.deleteDiary(finalDatetime, finalEmotion, finalReason);
                if (deleted) {
                    entries.remove(position);
                    ((ArrayAdapter)listView.getAdapter()).notifyDataSetChanged();
                    dialog.dismiss();
                }
            });

            dialog.show();

            if (dialog.getWindow() != null) {
                int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.8);
                int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.7);
                dialog.getWindow().setLayout(width, height);
            }
        });
    }
}
