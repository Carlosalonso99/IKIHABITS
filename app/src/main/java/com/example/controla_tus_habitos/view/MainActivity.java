package com.example.controla_tus_habitos.view;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.controla_tus_habitos.R;
import com.example.controla_tus_habitos.model.Habito;
import com.example.controla_tus_habitos.repository.HabitoRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Infla el menú; esto añade ítems a la barra de acción si está presente.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.itemHome) {
            // Maneja el clic en el ítem de configuración aquí
            Toast.makeText(this, "Hola", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            Intent intent = new Intent(MainActivity.this, NuevoHabitoActivity.class);
            nuevoHabitoLauncher.launch(intent);
        });


    }



    /**
     * Recargar la lista de hábitos desde la base de datos
     */
    private void actualizarListaHabitos() {
        List<Habito> habitos = habitoRep.obtenerTodosLosHabitos();
        adapter = new HabitoAdapter(this, habitos, habitoRep);
        recyclerView.setAdapter(adapter);
    }
}

