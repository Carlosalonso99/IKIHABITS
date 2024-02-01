package com.example.controla_tus_habitos.contract;

import android.provider.BaseColumns;

public final class HabitoContract {

    private HabitoContract() {}

    public static class HabitoEntry implements BaseColumns {
        public static final String TABLE_NAME = "habito";
        public static final String COLUMN_NAME_TITULO = "titulo";
        public static final String COLUMN_NAME_DESCRIPCION = "descripcion";
        public static final String COLUMN_NAME_COMPLETADO = "completado";
    }
}

