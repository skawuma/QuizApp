package com.skawuma.quizup.utility;

import android.content.Context;
import android.util.Log;

import com.skawuma.quizup.model.MCQSet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

public class BDUtility {

    public static List<MCQSet> readJsonFromRaw(Context context, int rawResourceId) {
        try {
            InputStream inputStream = context.getResources().openRawResource(rawResourceId);

            InputStreamReader reader = new InputStreamReader(inputStream);
            StringBuilder stringBuilder = new StringBuilder();
            int character;
            while ((character = reader.read()) != -1) {
                stringBuilder.append((char) character);
            }

            String jsonData = stringBuilder.toString();
            Gson gson = new Gson();
            Type setListType = new TypeToken<List<MCQSet>>(){}.getType();
            return gson.fromJson(jsonData, setListType);

        } catch (Exception e) {
            Log.e("MainActivity", "Error reading JSON file", e);
            return null;
        }
    }
}
