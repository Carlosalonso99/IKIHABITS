package com.example.controla_tus_habitos.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.controla_tus_habitos.R;
import com.example.controla_tus_habitos.model.CategoriaHabito;
import com.example.controla_tus_habitos.repository.HabitoDbHelper;

public class NuevoHabitoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_habito);
        HabitoDbHelper dbHelper = HabitoDbHelper.getInstance(this);
        Spinner spinnerCategoria = findViewById(R.id.spinnerCategoria);
        // Asume que CategoriaHabito es tu enum y tiene los valores SALUD, TRABAJO, ALIMENTACION
        spinnerCategoria.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, CategoriaHabito.values()));



        Button nuevoHabitoBtn = findViewById(R.id.buttonGuardarHabito);
        nuevoHabitoBtn.setOnClickListener(view -> {
            EditText tituloHabito = findViewById(R.id.editTextTituloHabito);
            String tituloContent = tituloHabito.getText().toString();
            EditText descripcionHabito = findViewById(R.id.editTextDescripcionHabito);
            String descripcionContent = descripcionHabito.getText().toString();
            Switch completado = findViewById(R.id.switchCompletado);
            CategoriaHabito categoriaSeleccionada = (CategoriaHabito) spinnerCategoria.getSelectedItem();
            String categoriaContent = categoriaSeleccionada.toString();

            int completadoValue = completado.isChecked() ? 1 : 0;

            if(tituloContent.isEmpty()){
                Toast.makeText(NuevoHabitoActivity.this, "Título necesario", Toast.LENGTH_SHORT).show();
            } else {
                long nuevoId = dbHelper.agregarHabito(tituloContent, descripcionContent, completadoValue, categoriaContent);
                if (nuevoId != -1) {
                    Toast.makeText(NuevoHabitoActivity.this, "Hábito agregado exitosamente", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK); // Indica que funciono y activa actualizarHabitos() en la mainAct
                    finish(); // Cierra la actividad y regresa a MainActivity
                } else {
                    Toast.makeText(NuevoHabitoActivity.this, "Error al agregar hábito", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
