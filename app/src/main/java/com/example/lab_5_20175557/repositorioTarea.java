package com.example.lab_5_20175557;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.lab_5_20175557.entity.tarea;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class repositorioTarea {
    private Application application;
    private MutableLiveData<List<tarea>> allTasks;

    public repositorioTarea(Application application) {
        this.application = application;
        allTasks = new MutableLiveData<>();
        cargarTarea();
    }

    private void cargarTarea() {
        List<tarea> tasks = JsonUtil.loadTasks(application.getApplicationContext());
        long currentTime = System.currentTimeMillis();
        List<tarea> validTasks = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

        for (tarea task : tasks) {
            try {
                Date date = dateFormat.parse(task.getFecha_aviso() + " " + task.getHora_aviso());
                if (date != null && date.getTime() >= currentTime) {
                    validTasks.add(task);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // Save the valid tasks back to storage
        JsonUtil.saveTasks(application.getApplicationContext(), validTasks);
        allTasks.postValue(validTasks);
    }


    public void insertar_tarea(tarea task) {
        List<tarea> currentTasks = allTasks.getValue();
        if (currentTasks != null) {
            task.setId(currentTasks.size() + 1);
            currentTasks.add(task);
            guardar(currentTasks);
        }
    }
    public void eliminar_tarea_por_id(int taskId) {
        List<tarea> currentTasks = allTasks.getValue();
        if (currentTasks != null) {
            for (tarea task : currentTasks) {
                if (task.getId() == taskId) {
                    currentTasks.remove(task);
                    guardar(currentTasks);
                    break;
                }
            }
        }
    }


    public void actualizar_tarea(tarea task) {
        List<tarea> currentTasks = allTasks.getValue();
        if (currentTasks != null) {
            for (int i = 0; i < currentTasks.size(); i++) {
                if (currentTasks.get(i).getId() == task.getId()) {
                    currentTasks.set(i, task);
                    guardar(currentTasks);
                    break;
                }
            }
        }
    }

    public void eliminar_tarea(tarea task) {
        List<tarea> currentTasks = allTasks.getValue();
        if (currentTasks != null) {
            currentTasks.remove(task);
            guardar(currentTasks);
        }
    }

    public LiveData<List<tarea>> getAllTasks() {
        return allTasks;
    }

    private void guardar(List<tarea> tasks) {
        JsonUtil.saveTasks(application.getApplicationContext(), tasks);
        allTasks.postValue(tasks);
    }
}