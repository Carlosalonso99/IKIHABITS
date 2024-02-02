package com.example.controla_tus_habitos.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.controla_tus_habitos.contract.HabitoContract;

/*
Implementa un singleton para que todas las acciones crud sean ejecutadas por el mismo objeto aporta eficiencia y coherencia a la arquitectura
 */
public class HabitoDbHelper extends SQLiteOpenHelper {

    private static HabitoDbHelper instance;
    private static final String DATABASE_NAME = "habitos.db";
    private static final int DATABASE_VERSION = 2;
    final static String SQL_CREATE_HABITOS_TABLE =
            "CREATE TABLE " + HabitoContract.HabitoEntry.TABLE_NAME + " (" +
                    HabitoContract.HabitoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    HabitoContract.HabitoEntry.COLUMN_NAME_TITULO + " TEXT NOT NULL," +
                    HabitoContract.HabitoEntry.COLUMN_NAME_DESCRIPCION + " TEXT," +
                    HabitoContract.HabitoEntry.COLUMN_NAME_COMPLETADO + " INTEGER NOT NULL," +
                    HabitoContract.HabitoEntry.COLUMN_NAME_CATEGORIA + " TEXT" + ")";;

    /**
     * Implementacion de un Singleton para mayor control
     * @param context
     */
    private HabitoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.getReadableDatabase();
    }

    public static synchronized HabitoDbHelper getInstance(Context context) {
        if (instance == null) {
            // Usa getApplicationContext() para evitar fugas de memoria
            instance = new HabitoDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {db.execSQL(SQL_CREATE_HABITOS_TABLE);}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Aqui se gestionan las actualizaciones de version
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + HabitoContract.HabitoEntry.TABLE_NAME + " ADD COLUMN " + HabitoContract.HabitoEntry.COLUMN_NAME_CATEGORIA + " TEXT");
        }
    }

    // CRUD

    /**
     * Aqui se gestiona la creacion de un nuevo habito
     * @param titulo
     * @param descripcion
     * @param completado
     * @param categoria
     * @return
     */
    public long agregarHabito(String titulo, String descripcion, int completado, String categoria) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HabitoContract.HabitoEntry.COLUMN_NAME_TITULO, titulo);
        values.put(HabitoContract.HabitoEntry.COLUMN_NAME_DESCRIPCION, descripcion);
        values.put(HabitoContract.HabitoEntry.COLUMN_NAME_COMPLETADO, completado);
        values.put(HabitoContract.HabitoEntry.COLUMN_NAME_CATEGORIA, categoria);

        return db.insert(HabitoContract.HabitoEntry.TABLE_NAME, null, values);
    }

    /**
     * Obtienes los habitos de la bd
     * @return un objeto Cursor con los datos de mi tabla
     */
    public Cursor obtenerHabitos() {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.query(
                HabitoContract.HabitoEntry.TABLE_NAME,
                // Esta parte la he cogido de internet, me costo bastante entenderla , por eso comento cada linea para que sirve
                null, // Las columnas que se desean retornar; null retorna todas
                null, // La columna para la cláusula WHERE; null retorna todas las filas
                null, // Los valores para la cláusula WHERE
                null, // group by
                null, // having
                null  // order by
        );
    }

    /**
     * Actualiza un habito
     * @param id
     * @param titulo
     * @param descripcion
     * @param completado
     * @return
     */
    public int actualizarHabito(long id, String titulo, String descripcion, int completado) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HabitoContract.HabitoEntry.COLUMN_NAME_TITULO, titulo);
        values.put(HabitoContract.HabitoEntry.COLUMN_NAME_DESCRIPCION, descripcion);
        values.put(HabitoContract.HabitoEntry.COLUMN_NAME_COMPLETADO, completado);

        return db.update(
                HabitoContract.HabitoEntry.TABLE_NAME,
                values,
                HabitoContract.HabitoEntry._ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }

    /**
     * Persiste el cambio de estado del atributo completado en la bd
     * @param id
     * @param completado
     */
    public void actualizarEstadoCompletado(long id, int completado){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HabitoContract.HabitoEntry.COLUMN_NAME_COMPLETADO, completado);

        String selection = HabitoContract.HabitoEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        db.update(HabitoContract.HabitoEntry.TABLE_NAME, values, selection, selectionArgs);

    }

    /**
     * Elimina un habito
     * @param id
     */
    public void eliminarHabito(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(HabitoContract.HabitoEntry.TABLE_NAME, HabitoContract.HabitoEntry._ID + " = ?", new String[]{String.valueOf(id)});
    }
}

