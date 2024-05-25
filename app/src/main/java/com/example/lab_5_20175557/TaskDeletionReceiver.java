package com.example.lab_5_20175557;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TaskDeletionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int taskId = intent.getIntExtra("EXTRA_TASK_ID", -1);
        if (taskId != -1) {
            repositorioTarea repository = new repositorioTarea((Application) context.getApplicationContext());
            repository.eliminar_tarea_por_id(taskId);
        }
    }
}

