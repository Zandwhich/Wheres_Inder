package com.example.alex.myapplication;

import android.graphics.drawable.Drawable;

public interface ImageLambda {
    void onError(Exception e);
    void onData(Drawable img);
}
