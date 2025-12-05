package com.quedity.qtracker;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Simple SharedPreferences-backed JSON storage:
 * { "params": { "param1": [1.0,2.0], "param2": [3.0] }, "length": 50 }
 */
public class DataStore {
    private static final String PREF = "qtracker_store";
    private static final String KEY_PARAMS = "params";
    private static final String KEY_LENGTH = "length";

    private SharedPreferences prefs;

    public DataStore(Context ctx) {
        prefs = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        if (!prefs.contains(KEY_PARAMS)) {
            prefs.edit().putString(KEY_PARAMS, new JSONObject().toString()).apply();
        }
        if (!prefs.contains(KEY_LENGTH)) {
            prefs.edit().putInt(KEY_LENGTH, 50).apply();
        }
    }

    public synchronized void addParamIfMissing(String param) throws JSONException {
        JSONObject all = new JSONObject(prefs.getString(KEY_PARAMS, "{}"));
        if (!all.has(param)) {
            all.put(param, new JSONArray());
            prefs.edit().putString(KEY_PARAMS, all.toString()).apply();
        }
    }

    public synchronized List<String> getParams() {
        String raw = prefs.getString(KEY_PARAMS, "{}");
        List<String> out = new ArrayList<>();
        try {
            JSONObject all = new JSONObject(raw);
            Iterator<String> it = all.keys();
            while (it.hasNext()) out.add(it.next());
        } catch (JSONException ignored) {}
        return out;
    }

    public synchronized List<Double> getData(String param) {
        String raw = prefs.getString(KEY_PARAMS, "{}");
        List<Double> out = new ArrayList<>();
        try {
            JSONObject all = new JSONObject(raw);
            if (!all.has(param)) return out;
            JSONArray arr = all.getJSONArray(param);
            for (int i = 0; i < arr.length(); i++) out.add(arr.getDouble(i));
        } catch (JSONException ignored) {}
        return out;
    }

    public synchronized void addDataPoint(String param, double value) {
        try {
            JSONObject all = new JSONObject(prefs.getString(KEY_PARAMS, "{}"));
            JSONArray arr = all.has(param) ? all.getJSONArray(param) : new JSONArray();
            arr.put(value);
            all.put(param, arr);
            prefs.edit().putString(KEY_PARAMS, all.toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public synchronized void removeParam(String param) {
        try {
            JSONObject all = new JSONObject(prefs.getString(KEY_PARAMS, "{}"));
            if (all.has(param)) {
                all.remove(param);
                prefs.edit().putString(KEY_PARAMS, all.toString()).apply();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public synchronized void removeDataPoint(String param, int index) {
        try {
            JSONObject all = new JSONObject(prefs.getString(KEY_PARAMS, "{}"));
            if (!all.has(param)) return;
            JSONArray arr = all.getJSONArray(param);
            JSONArray out = new JSONArray();
            for (int i = 0; i < arr.length(); i++) {
                if (i != index) out.put(arr.getDouble(i));
            }
            all.put(param, out);
            prefs.edit().putString(KEY_PARAMS, all.toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getLength() {
        return prefs.getInt(KEY_LENGTH, 50);
    }

    public void setLength(int l) {
        prefs.edit().putInt(KEY_LENGTH, l).apply();
    }
}