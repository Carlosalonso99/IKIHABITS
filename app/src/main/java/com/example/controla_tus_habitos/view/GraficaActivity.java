package com.example.controla_tus_habitos.view;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import com.example.controla_tus_habitos.R;
import com.example.controla_tus_habitos.repository.HabitoRepository;

public class GraficaActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafica);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }
}