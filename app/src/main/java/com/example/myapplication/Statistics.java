package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Statistics extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statisticsactivity);

        Button btnToMain = findViewById(R.id.btn_to_main);
        btnToMain.setOnClickListener(v -> finish());

        PieChart chart = findViewById(R.id.pie_chart);
        EmotionDatabase db = new EmotionDatabase(this);
        Map<String, Integer> stats = db.getEmotionStats();

        List<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Integer> e : stats.entrySet()) {
            entries.add(new PieEntry(e.getValue(), e.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "감정 통계");
        dataSet.setValueTextSize(14f);
        PieData pieData = new PieData(dataSet);
        chart.setData(pieData);
        chart.invalidate();
    }
}
