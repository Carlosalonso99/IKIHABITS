package com.example.controla_tus_habitos.model;

public class Habito {
    private long id;
    private String titulo;
    private String descripcion;
    private boolean completado;

    private CategoriaHabitoEnum categoria;

    public Habito() {}

    public Habito(long id, String titulo, String descripcion, boolean completado, CategoriaHabitoEnum categoria) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.completado = completado;
        this.categoria = categoria;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isCompletado() {
        return completado;
    }

    public void setCompletado(boolean completado) {
        this.completado = completado;
    }

    public CategoriaHabitoEnum getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaHabitoEnum categoria) {
        this.categoria = categoria;
    }
}

