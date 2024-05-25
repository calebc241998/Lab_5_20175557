package com.example.lab_5_20175557;

import android.content.Context;

import com.example.lab_5_20175557.entity.tarea;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonUtil {
    private static final String FILE_NAME = "tareas.json";

    public static void saveTasks(Context context, List<tarea> tasks) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(tasks);

        try (FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
             OutputStreamWriter writer = new OutputStreamWriter(fos)) {
            writer.write(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<tarea> loadTasks(Context context) {
        List<tarea> tasks = new ArrayList<>();
        Gson gson = new Gson();

        try (FileInputStream fis = context.openFileInput(FILE_NAME);
             InputStreamReader reader = new InputStreamReader(fis)) {
            Type taskListType = new TypeToken<ArrayList<tarea>>(){}.getType();
            tasks = gson.fromJson(reader, taskListType);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tasks;
    }
}