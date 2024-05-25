package com.example.lab_5_20175557.entity;
//package com.example.todolist;

import java.io.Serializable;

public class tarea implements Serializable {
    private int id;
    private String titulo;
    private String descripcion;
    private String fecha_aviso;
    private String hora_aviso;

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public tarea(String titulo, String descripcion, String fecha_aviso, String hora_aviso) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha_aviso = fecha_aviso;
        this.hora_aviso = hora_aviso;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getFecha_aviso() {
        return fecha_aviso;
    }

    public String getHora_aviso() {
        return hora_aviso;
    }
}

