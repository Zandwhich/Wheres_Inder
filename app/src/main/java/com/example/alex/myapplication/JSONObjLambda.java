package com.example.alex.myapplication;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

abstract public class JSONObjLambda implements JSONLambda {
    abstract public void onData(JSONObject o);

    public void invoke(String s) {
        try {
            onData(new JSONObject(s));
        } catch (JSONException e) {
            e.printStackTrace();
            onError(e);
        }
    }
}
