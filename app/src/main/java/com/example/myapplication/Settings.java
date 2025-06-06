package com.example.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class Settings extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;
    private static boolean isDarkMode = false;

    private EmotionDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(isDarkMode ?
                AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.settingsactivity);

        db = new EmotionDatabase(this);

        findViewById(R.id.btn_to_main).setOnClickListener(v -> finish());

        findViewById(R.id.btn_theme).setOnClickListener(v -> {
            isDarkMode = !isDarkMode;
            AppCompatDelegate.setDefaultNightMode(isDarkMode ?
                    AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
            recreate();
        });

        findViewById(R.id.btn_backup).setOnClickListener(v -> {
            if (hasStoragePermission()) {
                backupDiariesToFile();
            } else {
                requestStoragePermission();
            }
        });
    }

    private boolean hasStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return true;
        }
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
        } else {
            backupDiariesToFile();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                backupDiariesToFile();
            } else {
                Toast.makeText(this, "저장 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void backupDiariesToFile() {
        List<String> diaries = db.getAllDiaries();
        StringBuilder sb = new StringBuilder();
        for (String diary : diaries) sb.append(diary).append("\n");

        try {
            File dir = getExternalFilesDir(android.os.Environment.DIRECTORY_DOCUMENTS);
            if (dir != null && !dir.exists()) dir.mkdirs();

            File file = new File(dir, "emotion_diary_backup.txt");
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(sb.toString().getBytes());
            }

            Toast.makeText(this, "백업 완료: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "백업 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
