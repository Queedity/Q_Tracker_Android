package com.quedity.qtracker;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.util.List;

public class ParamsEditActivity extends AppCompatActivity {

    DataStore store;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_params_edit);

        store = new DataStore(this);

        EditText editNew = findViewById(R.id.edit_new_param);
        Button btnAdd = findViewById(R.id.btn_add_param);
        ListView list = findViewById(R.id.list_params);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, store.getParams());
        list.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> {
            String name = editNew.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(this, "Name required", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                store.addParamIfMissing(name);
                refresh();
                editNew.setText("");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        list.setOnItemLongClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            String param = adapter.getItem(position);
            store.removeParam(param);
            refresh();
            Toast.makeText(this, "Deleted " + param, Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    private void refresh() {
        adapter.clear();
        adapter.addAll(store.getParams());
        adapter.notifyDataSetChanged();
    }
}