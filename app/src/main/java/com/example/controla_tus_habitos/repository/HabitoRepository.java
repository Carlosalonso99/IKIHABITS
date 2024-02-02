package com.example.controla_tus_habitos.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.controla_tus_habitos.contract.HabitoContract;
import com.example.controla_tus_habitos.model.CategoriaHabito;
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

    /**
     * Implementacion de un Singleton para mayor control
     * @param context
     */
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

    /**
     * Obtiene todos los habitos y usa el metodo cursorAListaHabitos(Cursor cursor) para pasarlos a lista
     * @return lista de habitos
     */
    public List<Habito> obtenerTodosLosHabitos() {
        HabitoDbHelper dbHelper = HabitoDbHelper.getInstance(context);
        Cursor cursor = dbHelper.obtenerHabitos();

        List<Habito> listaHabitos = cursorAListaHabitos(cursor);
        cursor.close();
        return listaHabitos;
    }

    /**
     * Pasa de los datos obtenidos de la bd a una lista de java
     * @param cursor
     * @return devuelva una lista de habitos
     */
    private List<Habito> cursorAListaHabitos(Cursor cursor) {


        List<Habito> listaHabitos = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(HabitoContract.HabitoEntry._ID));
                String titulo = cursor.getString(cursor.getColumnIndexOrThrow(HabitoContract.HabitoEntry.COLUMN_NAME_TITULO));
                String descripcion = cursor.getString(cursor.getColumnIndexOrThrow(HabitoContract.HabitoEntry.COLUMN_NAME_DESCRIPCION));
                int completadoInt = cursor.getInt(cursor.getColumnIndexOrThrow(HabitoContract.HabitoEntry.COLUMN_NAME_COMPLETADO));
                String categoriaStr = cursor.getString(cursor.getColumnIndexOrThrow(HabitoContract.HabitoEntry.COLUMN_NAME_CATEGORIA));
                boolean completado = (completadoInt == 1);

                CategoriaHabito categoria ;

                if (categoriaStr != null) {
                    categoria = CategoriaHabito.valueOf(categoriaStr.toUpperCase());
                } else {
                    categoria = CategoriaHabito.GENERICO;
                }


                Habito habito = new Habito(id, titulo, descripcion, completado, categoria);

                listaHabitos.add(habito);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return listaHabitos;

    }

    /**
     * Gestiona la equivalendia entre el boleano y el dato real que se pasa a la bd
     * @param id
     * @param completado
     */
     public void cambiarCompletado(long id, boolean completado){
            int completadoInt = completado ? 1: 0;

            dbHelper.actualizarEstadoCompletado(id, completadoInt);
    }


}

