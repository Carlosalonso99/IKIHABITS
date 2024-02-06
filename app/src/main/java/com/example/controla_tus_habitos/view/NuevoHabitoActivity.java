package com.example.controla_tus_habitos.view;

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

public class NuevoHabitoActivity extends AppCompatActivity {

    HabitoRepository rep = HabitoRepository.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_habito);

        Spinner spinnerCategoria = findViewById(R.id.spinnerCategoria);
        // Asume que CategoriaHabito es tu enum y tiene los valores SALUD, TRABAJO, ALIMENTACION
        spinnerCategoria.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, CategoriaHabitoEnum.values()));



        Button nuevoHabitoBtn = findViewById(R.id.buttonGuardarHabito);
        nuevoHabitoBtn.setOnClickListener(view -> {
            EditText tituloHabito = findViewById(R.id.editTextTituloHabito);
            String tituloContent = tituloHabito.getText().toString();
            EditText descripcionHabito = findViewById(R.id.editTextDescripcionHabito);
            String descripcionContent = descripcionHabito.getText().toString();
            Switch completado = findViewById(R.id.switchCompletado);
            CategoriaHabitoEnum categoriaSeleccionada = (CategoriaHabitoEnum) spinnerCategoria.getSelectedItem();
            String categoriaContent = categoriaSeleccionada.toString();

            int completadoValue = completado.isChecked() ? 1 : 0;



            if(tituloContent.isEmpty()){
                Toast.makeText(NuevoHabitoActivity.this, "Título necesario", Toast.LENGTH_SHORT).show();
                Color.errorAnimation(this, tituloHabito);
            } else {
                long nuevoId = rep.agregarHabito(tituloContent, descripcionContent, completadoValue, categoriaContent);
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
