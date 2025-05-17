package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Date;

public class EmotionInput extends AppCompatActivity {
    private String selectedEmotion = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emotioninputactivity);

        View.OnClickListener emojiClickListener = v -> {
            Button btn = (Button) v;
            selectedEmotion = btn.getText().toString();
        };

        findViewById(R.id.emotion_happy).setOnClickListener(emojiClickListener);
        findViewById(R.id.emotion_sad).setOnClickListener(emojiClickListener);
        findViewById(R.id.emotion_angry).setOnClickListener(emojiClickListener);
        findViewById(R.id.emotion_scared).setOnClickListener(emojiClickListener);
        findViewById(R.id.emotion_love).setOnClickListener(emojiClickListener);
        findViewById(R.id.emotion_neutral).setOnClickListener(emojiClickListener);

        findViewById(R.id.btn_save).setOnClickListener(v -> {
            String reason = ((EditText) findViewById(R.id.edit_reason)).getText().toString();
            String datetime = java.text.DateFormat.getDateTimeInstance().format(new Date());

            EmotionDatabase db = new EmotionDatabase(this);
            db.insertDiary(selectedEmotion, reason, datetime);
            finish();
        });

        Button btnToMain = findViewById(R.id.btn_to_main);
        btnToMain.setOnClickListener(v -> finish());
    }
}
