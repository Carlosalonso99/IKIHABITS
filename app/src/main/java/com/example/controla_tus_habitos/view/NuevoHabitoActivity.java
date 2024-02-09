package com.example.controla_tus_habitos.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.controla_tus_habitos.R;
import com.example.controla_tus_habitos.model.entities_pojos.habito.CategoriaHabitoEnum;
import com.example.controla_tus_habitos.repository.HabitoRepository;
import com.example.controla_tus_habitos.utils.Color;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class NuevoHabitoActivity extends AppCompatActivity {

    HabitoRepository habitoRep = HabitoRepository.getInstance(this);

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean isRecording = false;
    private MediaRecorder mediaRecorder;
    private List<String> audioFilePath = new LinkedList<String>();

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(this, "Permiso de grabación de audio denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
        }

        setContentView(R.layout.activity_nuevo_habito);

        Spinner spinnerCategoria = findViewById(R.id.spinnerCategoria);
        // Asume que CategoriaHabito es tu enum y tiene los valores SALUD, TRABAJO, ALIMENTACION
        spinnerCategoria.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, CategoriaHabitoEnum.values()));


        Button grabarBtn = findViewById(R.id.btnGrabar);

        grabarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRecording) {
                    // Comenzar grabación
                    startRecording();
                } else {
                    // Detener grabación
                    stopRecording();
                }
                isRecording = !isRecording;
                updateUI();
            }
            private void startRecording() {
                mediaRecorder = new MediaRecorder();
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

                // Define el archivo de salida
                String path = getExternalFilesDir(null).getAbsolutePath() + "/audio_" + System.currentTimeMillis() + ".3gp";
                audioFilePath.add(path);
                mediaRecorder.setOutputFile(path);

                try {
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                    Toast.makeText(NuevoHabitoActivity.this, "Grabación iniciada", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(NuevoHabitoActivity.this, "No se pudo iniciar la grabación", Toast.LENGTH_SHORT).show();
                }
            }

            private void stopRecording() {
                if (mediaRecorder != null) {
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    mediaRecorder = null;
                    Toast.makeText(NuevoHabitoActivity.this, "Grabación detenida", Toast.LENGTH_SHORT).show();
                }
            }
            private void updateUI() {
                Button btnGrabar = findViewById(R.id.btnGrabar);
                TextView tvEstadoGrabacion = findViewById(R.id.tvEstadoGrabacion);

                if (isRecording) {
                    btnGrabar.setText("Detener");
                    tvEstadoGrabacion.setText("Estado: Grabando");
                } else {
                    btnGrabar.setText("Grabar");
                    tvEstadoGrabacion.setText("Estado: Inactivo");
                }
            }
        });


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
                long nuevoId = habitoRep.agregarHabito(tituloContent, descripcionContent, completadoValue, categoriaContent);
                if (nuevoId != -1) {
                    if(audioFilePath.size() != 0) {
                        for(String path : audioFilePath) {
                            habitoRep.agregarAudioAHabito(nuevoId, path);
                        }
                    }
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
