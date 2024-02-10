package com.example.controla_tus_habitos.view;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.controla_tus_habitos.R;
import com.example.controla_tus_habitos.model.entities_pojos.habito.Habito;
import com.example.controla_tus_habitos.repository.HabitoRepository;
import com.example.controla_tus_habitos.utils.OnHabitoClickListener;
import com.example.controla_tus_habitos.view.adapters.HabitoAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class HomeActivity extends BaseActivity implements OnHabitoClickListener {

    private RecyclerView recyclerView;
    private HabitoAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private HabitoRepository habitoRep;

    /**
     * Es una manera de gestionar la activiti para que se actualice bajo la llamada de NuevoHabitoActivity si todo sale bien
     */


    private final ActivityResultLauncher<Intent> nuevoHabitoLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    actualizarListaHabitos();
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        habitoRep = HabitoRepository.getInstance(this);

        recyclerView = findViewById(R.id.rvHabitos);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        actualizarListaHabitos();
        
        



        FloatingActionButton fab = findViewById(R.id.btnAgregarHabito);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, NuevoHabitoActivity.class);
            nuevoHabitoLauncher.launch(intent);
        });


    }



    /**
     * Recargar la lista de hábitos desde la base de datos
     */
    private void actualizarListaHabitos() {
        List<Habito> habitos = habitoRep.obtenerTodosLosHabitos();
        adapter = new HabitoAdapter(this, habitos, habitoRep, this);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onHabitoClick(Habito habito) {
        Intent intent = new Intent(HomeActivity.this, ActualizarHabitoActivity.class);
        intent.putExtra("id", habito.getId());
        intent.putExtra("titulo", habito.getTitulo());
        intent.putExtra("descripcion", habito.getDescripcion());
        intent.putExtra("completado", habito.isCompletado());
        intent.putExtra("categoria", habito.getCategoria());
        nuevoHabitoLauncher.launch(intent);

    }

    public void onHabitoLongClick(Habito habito) {
        confirmarBorrarHaito(habito);
    }

    public void confirmarBorrarHaito(Habito habito) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirmar acción");
        builder.setMessage("¿Estás seguro de que quieres borrar este hábito?");

        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(habitoRep.eliminarHabito(habito.getId())) {
                    actualizarListaHabitos();
                    Toast.makeText(HomeActivity.this, "Habito borrado correctamente", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(HomeActivity.this, "No se borro correctamente", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}

