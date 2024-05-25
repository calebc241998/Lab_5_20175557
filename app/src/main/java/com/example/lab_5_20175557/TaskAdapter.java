package com.example.lab_5_20175557;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab_5_20175557.entity.tarea;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {
    private List<tarea> tasks = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_tareas, parent, false);
        return new TaskHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        tarea currentTask = tasks.get(position);
        holder.textViewTitle.setText(currentTask.getTitulo());
        holder.textViewDescription.setText(currentTask.getDescripcion());
        holder.textViewDueDate.setText(currentTask.getFecha_aviso());
        holder.textViewDueTime.setText(currentTask.getHora_aviso());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setTasks(List<tarea> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public tarea getTaskAt(int position) {
        return tasks.get(position);
    }

    class TaskHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewDueDate;
        private TextView textViewDueTime;

        public TaskHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewDueDate = itemView.findViewById(R.id.textViewDueDate);
            textViewDueTime = itemView.findViewById(R.id.textViewDueTime);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(tasks.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(tarea task);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}