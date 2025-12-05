package com.quedity.qtracker;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    DataStore store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        store = new DataStore(this);

        Button btnAdd = findViewById(R.id.btn_add_data);
        Button btnStats = findViewById(R.id.btn_show_stats);
        Button btnResult = findViewById(R.id.btn_results);

        btnAdd.setOnClickListener(v -> showAddParamDialog());
        btnStats.setOnClickListener(v -> startActivity(new Intent(this, StatsActivity.class)));
        btnResult.setOnClickListener(v -> startActivity(new Intent(this, ResultsActivity.class)));
    }

    private void showAddParamDialog() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Parameter name");

        final EditText input = new EditText(this);
        input.setHint("e.g. temperature");
        b.setView(input);

        b.setPositiveButton("Next", (dialog, which) -> {
            String param = input.getText().toString().trim();
            if (param.isEmpty()) {
                Toast.makeText(this, "Parameter name required", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                store.addParamIfMissing(param);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            showNumberDialog(param);
        });
        b.setNegativeButton("Cancel", null);
        b.show();
    }

    private void showNumberDialog(String param) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Enter number for " + param);

        final EditText input2 = new EditText(this);
        input2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        b.setView(input2);

        b.setPositiveButton("Add", (dialog, which) -> {
            String s = input2.getText().toString().trim();
            try {
                double v = Double.parseDouble(s);
                store.addDataPoint(param, v);
                Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid number", Toast.LENGTH_SHORT).show();
            }
        });

        b.setNegativeButton("Cancel", null);
        b.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit_params) {
            startActivity(new Intent(this, ParamsEditActivity.class));
            return true;
        } else if (id == R.id.action_edit_data) {
            startActivity(new Intent(this, DataEditActivity.class));
            return true;
        } else if (id == R.id.action_length) {
            showLengthDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLengthDialog() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Length of data in stats");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setText(String.valueOf(store.getLength()));
        b.setView(input);

        b.setPositiveButton("Save", (dialog, which) -> {
            try {
                int len = Integer.parseInt(input.getText().toString());
                if (len <= 0) {
                    Toast.makeText(this, "Length must be > 0", Toast.LENGTH_SHORT).show();
                    return;
                }
                store.setLength(len);
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid number", Toast.LENGTH_SHORT).show();
            }
        });

        b.setNegativeButton("Cancel", null);
        b.show();
    }
}