package com.quedity.qtracker;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class StatsActivity extends AppCompatActivity {

    DataStore store;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        store = new DataStore(this);
        int length = store.getLength();

        TextView textLength = findViewById(R.id.text_length);
        textLength.setText("Using last " + length + " data points for stats (or fewer if not available)");

        ListView list = findViewById(R.id.list_stats);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        List<String> params = store.getParams();
        for (String p : params) {
            List<Double> data = store.getData(p);
            double avg = Stats.mean(data, length);
            double avdev = Stats.meanAbsoluteDeviation(data, length);
            adapter.add(p + " -> avg: " + String.format("%.4f", avg) + "  avedev: " + String.format("%.4f", avdev));
        }
        list.setAdapter(adapter);
    }
}