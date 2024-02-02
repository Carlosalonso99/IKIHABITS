package com.example.controla_tus_habitos.view;

import com.example.controla_tus_habitos.model.Habito;

/**
 * Interfaz para manejar clics en ítems del RecyclerView
 * Esta pracrtica se hace para seguir manteniendo la separacion d los componentes de la app
 */
public interface OnHabitoClickListener {
    void onHabitoClick(Habito habito);
}
