package com.example.alex.myapplication;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;

abstract public class JSONArrLambda implements JSONLambda {
    abstract public void onData(JSONArray a);

    public void invoke(String s) {
        try {
            onData(new JSONArray(s));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
