package com.example.controla_tus_habitos.repository;

import android.content.Context;
import android.database.Cursor;

import com.example.controla_tus_habitos.model.db_helpers.HabitoDbHelper;
import com.example.controla_tus_habitos.model.contract.AudioContract;
import com.example.controla_tus_habitos.model.contract.HabitoContract;
import com.example.controla_tus_habitos.model.entities_pojos.audio.Audio;
import com.example.controla_tus_habitos.model.entities_pojos.habito.CategoriaHabitoEnum;
import com.example.controla_tus_habitos.model.entities_pojos.habito.Habito;

import java.util.ArrayList;
import java.util.List;


/*
Hago esta clase para que haya cuan menos dependencia posible entre la base de datos y mi app , por si la paso a firebase

Esta clase es la que actua como view model , pero esta implementada por mi
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

                CategoriaHabitoEnum categoria ;

                if (categoriaStr != null) {
                    categoria = CategoriaHabitoEnum.valueOf(categoriaStr.toUpperCase());
                } else {
                    categoria = CategoriaHabitoEnum.GENERICO;
                }


                Habito habito = new Habito(id, titulo, descripcion, completado, categoria);

                listaHabitos.add(habito);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return listaHabitos;

    }

    public float [] obtenerConteoHabitosCategoria() {
        List<Habito> habitosList = obtenerTodosLosHabitos();
        float[] stats = new float[4];
        for (Habito h : habitosList) {
            switch (h.getCategoria()) {
                case GENERICO:
                    stats[0] += 1;
                    break;
                case DEPORTE:
                    stats[1] += 1;
                    break;
                case TRABAJO:
                    stats[2] += 1;
                    break;
                case ALIMENTACION:
                    stats[3] += 1;
                    break;
            }
        }
        return stats;
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

    public long agregarHabito(String tituloContent,String descripcionContent, int completadoValue, String categoriaContent){
        return dbHelper.agregarHabito(tituloContent, descripcionContent, completadoValue, categoriaContent);
    }
    public long actualizarHabito(Long idHabito, String tituloContent,String descripcionContent, int completadoValue, String categoriaContent){
        dbHelper.actualizarHabito(idHabito, tituloContent, descripcionContent, completadoValue, categoriaContent);
        return idHabito;
    }

    public boolean eliminarHabito(Long id) {
         return dbHelper.eliminarHabito(id);
    }

    public void agregarAudioAHabito(long habitoId, String audioPath) {
        dbHelper.agregarAudio(habitoId, audioPath);
    }
    public List<Audio> obtenerAudiosDeHabitoId(long habitoId) {
        Cursor cursor = dbHelper.obtenerAudiosDeHabitoId(habitoId);
        List<Audio> audios = new ArrayList<>();

        while (cursor.moveToNext()) {
            String audioPath = cursor.getString(cursor.getColumnIndexOrThrow(AudioContract.AudioEntry.COLUMN_NAME_AUDIO_PATH));
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(AudioContract.AudioEntry._ID));
            audios.add(new Audio(id,habitoId,audioPath));

        }
        cursor.close();
        return audios;
    }

}

