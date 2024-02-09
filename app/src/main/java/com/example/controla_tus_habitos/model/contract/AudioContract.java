package com.example.controla_tus_habitos.model.contract;

import android.provider.BaseColumns;

public class AudioContract {

    private AudioContract() {}


    public static class AudioEntry implements BaseColumns {
        public static final String TABLE_NAME = "Audios";
        public static final String COLUMN_NAME_HABITO_ID = "HabitoId";
        public static final String COLUMN_NAME_AUDIO_PATH = "AudioPath";
    }
}
