package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmotionDatabase extends SQLiteOpenHelper {
    private static final String DB_NAME = "emotion_diary.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "diary";

    public EmotionDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "emotion TEXT, " +
                "reason TEXT, " +
                "datetime TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertDiary(String emotion, String reason, String datetime) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("emotion", emotion);
        values.put("reason", reason);
        values.put("datetime", datetime);
        db.insert(TABLE_NAME, null, values);
    }

    public List<String> getAllDiaries() {
        List<String> result = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, "datetime DESC");

        while (cursor.moveToNext()) {
            String emotion = cursor.getString(cursor.getColumnIndexOrThrow("emotion"));
            String reason = cursor.getString(cursor.getColumnIndexOrThrow("reason"));
            String datetime = cursor.getString(cursor.getColumnIndexOrThrow("datetime"));
            result.add("[" + datetime + "] " + emotion + " - " + reason);
        }
        cursor.close();
        return result;
    }

    public Map<String, Integer> getEmotionStats() {
        Map<String, Integer> stats = new HashMap<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT emotion, COUNT(*) FROM " + TABLE_NAME + " GROUP BY emotion", null);

        while (cursor.moveToNext()) {
            String emotion = cursor.getString(0);
            int count = cursor.getInt(1);
            stats.put(emotion, count);
        }
        cursor.close();
        return stats;
    }

    public boolean deleteDiary(String datetime, String emotion, String reason) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "datetime = ? AND emotion = ? AND reason = ?";
        String[] whereArgs = new String[]{datetime, emotion, reason};
        int deletedRows = db.delete(TABLE_NAME, whereClause, whereArgs);
        return deletedRows > 0;
    }
}
