package com.example.controla_tus_habitos.utils;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.example.controla_tus_habitos.R;

/**
 * Msg: La verdad que no se si esta clase tiene mucho sentido ponerla en utils, me gustaria saber tu opinion
 */
public class Color {

    public static void errorAnimation(Context context, View view){
        // Definir los colores de inicio y fin
        int colorInicio = ContextCompat.getColor(context, R.color.red_warning);
        int colorFin = ContextCompat.getColor(context, R.color.white);

        // Crear el ValueAnimator
        ValueAnimator colorAnimacion = ValueAnimator.ofArgb(colorInicio, colorFin);
        colorAnimacion.setDuration(3000); // Duración en milisegundos

        // Agregar un listener para actualizar el color de fondo en cada frame de la animación
        colorAnimacion.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view.setBackgroundColor((int) animator.getAnimatedValue());
            }
        });

        // Iniciar la animación
        colorAnimacion.start();

    }
}
