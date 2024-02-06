
package com.example.controla_tus_habitos.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.controla_tus_habitos.R;
import com.example.controla_tus_habitos.model.CategoriaHabitoEnum;
import com.example.controla_tus_habitos.model.HabitoDbHelper;
import com.example.controla_tus_habitos.repository.HabitoRepository;
import com.example.controla_tus_habitos.utils.Color;

public class ActualizarHabitoActivity extends AppCompatActivity {

    HabitoRepository rep = HabitoRepository.getInstance(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_habito);

        Spinner spinnerCategoria = findViewById(R.id.spinnerCategoria);
        spinnerCategoria.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, CategoriaHabitoEnum.values()));
        Intent intent = getIntent();

        CategoriaHabitoEnum categoria = (CategoriaHabitoEnum) intent.getSerializableExtra("categoria");

        int categoriaInt = 0;
        switch (categoria){
            case GENERICO:
                categoriaInt = 0;
                break;
            case DEPORTE:
                categoriaInt = 1;
                break;
            case TRABAJO:
                categoriaInt = 2;
                break;
            case ALIMENTACION:
                categoriaInt = 3;
                break;
        }

        spinnerCategoria.setSelection(categoriaInt);

        EditText tituloHabito = findViewById(R.id.editTextTituloHabito);
        tituloHabito.setText(intent.getStringExtra("titulo"));

        EditText descripcionHabito = findViewById(R.id.editTextDescripcionHabito);
        descripcionHabito.setText(intent.getStringExtra("descripcion"));

        Switch completado = findViewById(R.id.switchCompletado);
        completado.setChecked(intent.getBooleanExtra("completado", false));

        Long idHabito = intent.getLongExtra("id", 0);


        Button nuevoHabitoBtn = findViewById(R.id.buttonGuardarHabito);
        nuevoHabitoBtn.setOnClickListener(view -> {
            String tituloContent = tituloHabito.getText().toString();
            String descripcionContent = descripcionHabito.getText().toString();
            CategoriaHabitoEnum categoriaSeleccionada = (CategoriaHabitoEnum) spinnerCategoria.getSelectedItem();
            String categoriaContent = categoriaSeleccionada.toString();
            int completadoValue = completado.isChecked() ? 1 : 0;

            if(tituloContent.isEmpty()){
                Toast.makeText(ActualizarHabitoActivity.this, "Título necesario", Toast.LENGTH_SHORT).show();
                Color.errorAnimation(this, tituloHabito);
            } else {
                long nuevoId = rep.actualizarHabito(idHabito, tituloContent, descripcionContent, completadoValue, categoriaContent);

                if (nuevoId != -1) {
                    Toast.makeText(ActualizarHabitoActivity.this, "Hábito agregado exitosamente", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK); // Indica que funciono y activa actualizarHabitos() en la mainAct
                    finish(); // Cierra la actividad y regresa a MainActivity
                } else {
                    Toast.makeText(ActualizarHabitoActivity.this, "Error al agregar hábito", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
