package com.example.alex.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public abstract class JSONApiWrapper {
    public final static boolean ENABLE_IMAGE_CACHE = false;
    static Map<String, Drawable> imageCache = new HashMap<String, Drawable>();
    // loosely based on https://developer.android.com/reference/android/os/AsyncTask.html
    static private class GetPage extends AsyncTask<Void, Void, String> {
        private String page;
        private JSONLambda after;

        protected GetPage(String page, JSONLambda after) {
            this.page = page;
            this.after = after;
        }

        @Override
        protected String doInBackground(Void... nothing) {
            try {
                // I learned how to use HttpURLConnection from
                // https://docs.oracle.com/javase/tutorial/networking/urls/readingWriting.html
                URL url = new URL(page);
                URLConnection c = url.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream(), "UTF-8"));

                // Creating the JSON object based on
                // http://stackoverflow.com/questions/22461663/convert-inputstream-to-jsonobject
                StringBuilder stringBuilder = new StringBuilder();
                String input;
                while((input = in.readLine()) != null) {
                    stringBuilder.append(input);
                }

                return stringBuilder.toString();
            } catch(Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if(s != null)
                after.invoke(s);
            else
                after.onError(new Exception("Unknown error, likely an internet problem."));
        }
    }

    static void get(String page, JSONLambda l) {
        new GetPage(page, l).execute();
    }

    static private class GetImage extends AsyncTask<Void, Void, Drawable> {
        private String page;
        private ImageLambda after;

        protected GetImage(String page, ImageLambda after) {
            this.page = page;
            this.after = after;
        }

        @Override
        protected Drawable doInBackground(Void... nothing) {
            try {
                // based on http://stackoverflow.com/a/5776903/321792
                URL url = new URL(page);
                URLConnection c = url.openConnection();
                return BitmapDrawable.createFromStream(c.getInputStream(), page);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Drawable img) {
            if (img != null)
                after.onData(img);
            else
                after.onError(new Exception("Unknown error, likely an internet problem."));
        }
    }

    static public void getImage(final String page, final ImageLambda l) {
        if(!ENABLE_IMAGE_CACHE)
            new GetImage(page, l).execute();
        else if(imageCache.containsKey(page))
            l.onData(imageCache.get(page));
        else
            new GetImage(page, new ImageLambda() {
                @Override
                public void onError(Exception e) {
                    l.onError(e);
                }

                @Override
                public void onData(Drawable img) {
                    imageCache.put(page, img);
                    l.onData(img);
                }
            }).execute();
    }
}
