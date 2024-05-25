package com.example.lab_5_20175557;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.lab_5_20175557.entity.tarea;

import java.util.List;

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
        allTasks.postValue(tasks);
    }

    public void insertar_tarea(tarea task) {
        List<tarea> currentTasks = allTasks.getValue();
        if (currentTasks != null) {
            task.setId(currentTasks.size() + 1);
            currentTasks.add(task);
            guardar(currentTasks);
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