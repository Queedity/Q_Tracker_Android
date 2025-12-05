package com.quedity.qtracker;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class DataEditActivity extends AppCompatActivity {

    DataStore store;
    ArrayAdapter<String> paramsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_edit);

        store = new DataStore(this);

        ListView listParams = findViewById(R.id.list_data);
        TextView textParam = findViewById(R.id.text_param_name);

        // The simplest approach: show a dialog to choose a param, then show its data
        List<String> params = store.getParams();
        if (params.isEmpty()) {
            textParam.setText("No parameters. Use menu -> Edit parameters to add.");
            return;
        }

        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Pick parameter to edit its data");
        String[] arr = params.toArray(new String[0]);
        b.setItems(arr, (dialog, which) -> {
            String chosen = arr[which];
            textParam.setText("Param: " + chosen);
            showDataForParam(chosen);
        });
        b.setCancelable(false);
        b.show();
    }

    private void showDataForParam(String param) {
        List<Double> data = store.getData(param);
        ListView list = findViewById(R.id.list_data);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        for (Double d : data) dataAdapter.add(String.valueOf(d));
        list.setAdapter(dataAdapter);

        list.setOnItemLongClickListener((AdapterView<?> parent, android.view.View view, int position, long id) -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete entry")
                    .setMessage("Delete this data point?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        store.removeDataPoint(param, position);
                        dataAdapter.remove(dataAdapter.getItem(position));
                        dataAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        });
    }
}