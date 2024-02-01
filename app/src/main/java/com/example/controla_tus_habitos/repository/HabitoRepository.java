package com.example.controla_tus_habitos.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.controla_tus_habitos.contract.HabitoContract;
import com.example.controla_tus_habitos.model.Habito;

import java.util.ArrayList;
import java.util.List;


/*
Hago esta clase para que haya cuan menos dependencia posible entre la base de datos y mi app , por si la paso a firebase
 */
public class HabitoRepository {
    private static HabitoRepository instance;
    private HabitoDbHelper dbHelper;

    private Context context;

    private HabitoRepository(Context context) {
        this.context = context;
        dbHelper = HabitoDbHelper.getInstance(context.getApplicationContext());
    }

    public static synchronized HabitoRepository getInstance(Context context) {
        if (instance == null) {
            instance = new HabitoRepository(context);
        }
        return instance;
    }

    public List<Habito> obtenerTodosLosHabitos() {
        HabitoDbHelper dbHelper = HabitoDbHelper.getInstance(context);
        Cursor cursor = dbHelper.obtenerHabitos();

        List<Habito> listaHabitos = cursorAListaHabitos(cursor);
        cursor.close();
        return listaHabitos;
    }

    private List<Habito> cursorAListaHabitos(Cursor cursor) {
        // Implementación del método como se describió anteriormente

        List<Habito> listaHabitos = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(HabitoContract.HabitoEntry._ID));
                String titulo = cursor.getString(cursor.getColumnIndexOrThrow(HabitoContract.HabitoEntry.COLUMN_NAME_TITULO));
                String descripcion = cursor.getString(cursor.getColumnIndexOrThrow(HabitoContract.HabitoEntry.COLUMN_NAME_DESCRIPCION));
                int completadoInt = cursor.getInt(cursor.getColumnIndexOrThrow(HabitoContract.HabitoEntry.COLUMN_NAME_COMPLETADO));
                boolean completado = (completadoInt == 1);

                Habito habito = new Habito(id, titulo, descripcion, completado);

                listaHabitos.add(habito);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return listaHabitos;

    }

     public void cambiarCompletado(long id, boolean completado){
            int completadoInt = completado ? 1: 0;

            dbHelper.actualizarEstadoCompletado(id, completadoInt);
    }


}

