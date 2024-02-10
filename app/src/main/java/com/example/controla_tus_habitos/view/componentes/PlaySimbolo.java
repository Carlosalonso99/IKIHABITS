package com.example.controla_tus_habitos.view.componentes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;



public class PlaySimbolo  extends View {

    private Paint paint;
    private Path path;

    public PlaySimbolo(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //paint.setColor(ContextCompat.getColor(getContext(), R.color.white));
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.FILL);

        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Calcular el centro y el tamaño del triángulo
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        float size = Math.min(getWidth(), getHeight()) / 2f; // Ajusta el tamaño según tus necesidades

        // Definir los puntos del triángulo (play)
        path.moveTo(centerX - size / 2, centerY - size / 2); // Punto superior izquierdo
        path.lineTo(centerX - size / 2, centerY + size / 2); // Punto inferior izquierdo
        path.lineTo(centerX + size / 2, centerY); // Punto central derecho
        path.close(); // Cerrar el path para formar el triángulo

        canvas.drawPath(path, paint);
    }
}