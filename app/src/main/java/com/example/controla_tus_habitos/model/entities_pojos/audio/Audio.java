package com.example.controla_tus_habitos.model.entities_pojos.audio;

public class Audio {
    private long id;
    private long habitoId;
    private String audioPath;

    // Constructor
    public Audio(long id, long habitoId, String audioPath) {
        this.id = id;
        this.habitoId = habitoId;
        this.audioPath = audioPath;
    }

    // Getters y Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getHabitoId() {
        return habitoId;
    }

    public void setHabitoId(long habitoId) {
        this.habitoId = habitoId;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }
}
