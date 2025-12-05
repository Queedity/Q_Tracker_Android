package com.quedity.qtracker;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ResultsActivity extends AppCompatActivity {

    DataStore store;
    ArrayAdapter<String> adapter;
    TextView textOverall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        store = new DataStore(this);
        int length = store.getLength();

        textOverall = findViewById(R.id.text_overall);
        ListView list = findViewById(R.id.list_scores);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        List<String> params = store.getParams();
        double sumScores = 0;
        int count = 0;
        for (String p : params) {
            List<Double> data = store.getData(p);
            double score = Stats.paramScore(data, length);
            adapter.add(p + " -> score: " + String.format("%.4f", score));
            sumScores += score;
            count++;
        }
        double overall = count == 0 ? 0 : (sumScores / count) * 100.0;
        textOverall.setText("Overall score: " + String.format("%.4f", overall));
        list.setAdapter(adapter);
    }
}