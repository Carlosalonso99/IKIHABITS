package com.example.controla_tus_habitos.view.componentes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.controla_tus_habitos.R;
import com.example.controla_tus_habitos.model.entities_pojos.habito.CategoriaHabitoEnum;
import com.example.controla_tus_habitos.repository.HabitoRepository;

public class SimpleBarChartView extends View {

    private Paint paint;
    private Paint paintValores;
    private float[] dataValues;
    private HabitoRepository habitoRep;

    public SimpleBarChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setColor(ContextCompat.getColor(getContext(), R.color.theme1)); // Color de las barras

        paint.setTextSize(40); // Tamaño del texto para las etiquetas

        paintValores = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintValores.setColor(ContextCompat.getColor(getContext(), R.color.black));
        paintValores.setTextSize(50);

        habitoRep = HabitoRepository.getInstance(getContext());


        dataValues = habitoRep.obtenerConteoHabitosCategoria(); // Ejemplo de valores
        Log.d("", dataValues.toString() );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Dibuja el gráfico de barras
        float barWidth = getWidth() / (dataValues.length * 2); // Ancho de cada barra
        float spacing = barWidth ; // Espacio entre las barras
        for (int i = 0; i < dataValues.length; i++) {
            // Calcula la posición y tamaño de la barra
            float left = spacing + ((barWidth + spacing) * i);
            float top = getHeight() - (dataValues[i] / 50) * getHeight(); // Ejemplo de cálculo de altura
            float right = left + barWidth;
            float bottom = getHeight();

            // Dibuja la barra
            canvas.drawRect(left, top, right, bottom, paint);

            // Dibuja el valor numérico de la barra
            canvas.drawText(String.valueOf(dataValues[i]), left , top - 30 , paintValores);

            // Dibuja la etiqueta de la categoría
            String categoria = CategoriaHabitoEnum.values()[i].name();
            // Ajusta el texto para que se centre bajo la barra
            paint.setTextAlign(Paint.Align.CENTER);

        }

    }


}

