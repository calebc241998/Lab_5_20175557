package com.example.lab_5_20175557;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab_5_20175557.entity.tarea;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class TaskListActivity extends AppCompatActivity {

    public static final int ADD_TASK_REQUEST = 1;
    public static final int EDIT_TASK_REQUEST = 2;

    private TaskViewModel taskViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_tareas);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        TaskAdapter adapter = new TaskAdapter();
        recyclerView.setAdapter(adapter);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);


        taskViewModel.getAllTasks().observe(this, new Observer<List<tarea>>() {
            @Override
            public void onChanged(@Nullable List<tarea> tasks) {
                adapter.setTasks(tasks);
            }
        });

        FloatingActionButton buttonAddTask = findViewById(R.id.buttonAddTask);
        buttonAddTask.setOnClickListener(v -> {
            Intent intent = new Intent(TaskListActivity.this, AddEditTaskActivity.class);
            startActivityForResult(intent, ADD_TASK_REQUEST);
        });

        adapter.setOnItemClickListener(tarea -> {
            Intent intent = new Intent(TaskListActivity.this, AddEditTaskActivity.class);
            intent.putExtra(AddEditTaskActivity.EXTRA_ID, tarea.getId());
            intent.putExtra(AddEditTaskActivity.EXTRA_TITLE, tarea.getTitulo());
            intent.putExtra(AddEditTaskActivity.EXTRA_DESCRIPTION, tarea.getDescripcion());
            intent.putExtra(AddEditTaskActivity.EXTRA_DUE_DATE, tarea.getFecha_aviso());
            intent.putExtra(AddEditTaskActivity.EXTRA_DUE_TIME, tarea.getHora_aviso());
            startActivityForResult(intent, EDIT_TASK_REQUEST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_TASK_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditTaskActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditTaskActivity.EXTRA_DESCRIPTION);
            String dueDate = data.getStringExtra(AddEditTaskActivity.EXTRA_DUE_DATE);
            String dueTime = data.getStringExtra(AddEditTaskActivity.EXTRA_DUE_TIME);

            tarea task = new tarea(title, description, dueDate, dueTime);
            taskViewModel.insertar_tarea(task);

        } else if (requestCode == EDIT_TASK_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditTaskActivity.EXTRA_ID, -1);

            if (id == -1) {
                return;
            }

            String title = data.getStringExtra(AddEditTaskActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditTaskActivity.EXTRA_DESCRIPTION);
            String dueDate = data.getStringExtra(AddEditTaskActivity.EXTRA_DUE_DATE);
            String dueTime = data.getStringExtra(AddEditTaskActivity.EXTRA_DUE_TIME);

            tarea task = new tarea(title, description, dueDate, dueTime);
            task.setId(id);
            taskViewModel.actualizar_tarea(task);
        }
    }
}