package com.ironn.bslogger;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Sky_Alligator on 12/24/2015.
 * org SkyWarpTechnology
 */
public class IO {

    private static final String TAG = "IO";

    public static String readStream(InputStream ins) {
        BufferedReader bufferedReader = null;
        String text = "empty stream";
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(ins));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            text = builder.toString();
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        } finally {
            if (bufferedReader != null)
                try {
                    bufferedReader.close();
                } catch (IOException ignored) {
                }
        }
        return text;
    }
}
