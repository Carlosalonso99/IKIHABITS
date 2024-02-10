
package com.example.controla_tus_habitos.view;

import android.Manifest;
import android.content.Intent;
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
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.controla_tus_habitos.R;
import com.example.controla_tus_habitos.model.entities_pojos.audio.Audio;
import com.example.controla_tus_habitos.model.entities_pojos.habito.CategoriaHabitoEnum;
import com.example.controla_tus_habitos.repository.HabitoRepository;
import com.example.controla_tus_habitos.utils.Color;
import com.example.controla_tus_habitos.view.adapters.AudioAdapter;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class ActualizarHabitoActivity extends ActivityBase {

    HabitoRepository habitoRep = HabitoRepository.getInstance(this);
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean isRecording = false; // Estado de la grabación
    private MediaRecorder mediaRecorder;
    private List<String> audioFilePath = new LinkedList<String>();
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerViewAudios;
    private AudioAdapter audioListAdapter;

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
        setContentView(R.layout.activity_nuevo_habito);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
        }


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

        recyclerViewAudios = findViewById(R.id.recyclerViewAudios);
        layoutManager = new LinearLayoutManager(this);
        recyclerViewAudios.setLayoutManager(layoutManager);

        List<Audio> listaAudios = obtenerListaAudios(idHabito);
        audioListAdapter = new AudioAdapter(this, listaAudios);
        recyclerViewAudios.setAdapter(audioListAdapter);



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
                    Toast.makeText(ActualizarHabitoActivity.this, "Grabación iniciada", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(ActualizarHabitoActivity.this, "No se pudo iniciar la grabación", Toast.LENGTH_SHORT).show();
                }
            }

            private void stopRecording() {
                if (mediaRecorder != null) {
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    mediaRecorder = null;
                    Toast.makeText(ActualizarHabitoActivity.this, "Grabación detenida", Toast.LENGTH_SHORT).show();
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
            String tituloContent = tituloHabito.getText().toString();
            String descripcionContent = descripcionHabito.getText().toString();
            CategoriaHabitoEnum categoriaSeleccionada = (CategoriaHabitoEnum) spinnerCategoria.getSelectedItem();
            String categoriaContent = categoriaSeleccionada.toString();
            int completadoValue = completado.isChecked() ? 1 : 0;

            if(tituloContent.isEmpty()){
                Toast.makeText(ActualizarHabitoActivity.this, "Título necesario", Toast.LENGTH_SHORT).show();
                Color.errorAnimation(this, tituloHabito);
            } else {
                long habitoId = habitoRep.actualizarHabito(idHabito, tituloContent, descripcionContent, completadoValue, categoriaContent);

                if (habitoId != -1) {
                    if(audioFilePath.size() != 0) {
                        for(String path : audioFilePath) {
                            habitoRep.agregarAudioAHabito(habitoId, path);
                        }
                    }
                    Toast.makeText(ActualizarHabitoActivity.this, "Hábito agregado exitosamente", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK); //Indica que funciono y activa actualizarHabitos() en la mainAct
                    finish(); //Cierra la actividad y regresa a MainActivity
                } else {
                    Toast.makeText(ActualizarHabitoActivity.this, "Error al agregar hábito", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private List<Audio> obtenerListaAudios(Long idHabito) {

        return habitoRep.obtenerAudiosDeHabitoId(idHabito);
    }
}
