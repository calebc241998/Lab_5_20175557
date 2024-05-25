package com.example.lab_5_20175557;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
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
        taskViewModel.getAllTasks().observe(this, adapter::setTasks);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                tarea taskToDelete = adapter.getTaskAt(viewHolder.getAdapterPosition());
                taskViewModel.eliminar_tarea(taskToDelete);
                adapter.removeTask(viewHolder.getAdapterPosition());
                cancelNotification(taskToDelete);
            }
        }).attachToRecyclerView(recyclerView);

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
            intent.putExtra("EXTRA_IMPORTANCE", tarea.getImportancia());
            startActivityForResult(intent, EDIT_TASK_REQUEST);
        });
    }
    private void cancelNotification(tarea task) {
        int notificationId = task.getId();
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                notificationId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            String title = data.getStringExtra(AddEditTaskActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditTaskActivity.EXTRA_DESCRIPTION);
            String dueDate = data.getStringExtra(AddEditTaskActivity.EXTRA_DUE_DATE);
            String dueTime = data.getStringExtra(AddEditTaskActivity.EXTRA_DUE_TIME);
            int importance = data.getIntExtra("EXTRA_IMPORTANCE", NotificationManager.IMPORTANCE_DEFAULT);

            tarea task = new tarea(title, description, dueDate, dueTime, importance);

            if (requestCode == ADD_TASK_REQUEST) {
                taskViewModel.insertar_tarea(task);
            } else if (requestCode == EDIT_TASK_REQUEST) {
                int id = data.getIntExtra(AddEditTaskActivity.EXTRA_ID, -1);
                if (id == -1) {
                    Toast.makeText(this, "Task can't be updated", Toast.LENGTH_SHORT).show();
                    return;
                }
                task.setId(id);
                taskViewModel.actualizar_tarea(task);
            }
        }
    }
}