package com.example.lab_5_20175557;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.lab_5_20175557.entity.tarea;

import java.util.List;
public class TaskViewModel extends AndroidViewModel {
    private repositorioTarea repository;
    private LiveData<List<tarea>> allTasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new repositorioTarea(application);
        allTasks = repository.getAllTasks();
    }

    public void insertar_tarea(tarea task) {
        repository.insertar_tarea(task);
    }

    public void actualizar_tarea(tarea task) {
        repository.actualizar_tarea(task);
    }

    public void eliminar_tarea(tarea task) {
        repository.eliminar_tarea(task);
    }

    public LiveData<List<tarea>> getAllTasks() {
        return allTasks;
    }
}
