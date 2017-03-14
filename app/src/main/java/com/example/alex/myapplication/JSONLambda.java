package com.example.alex.myapplication;

import android.content.Context;
import android.widget.Toast;

public interface JSONLambda {
    void onError(Exception e);
    void invoke(String s);
}
