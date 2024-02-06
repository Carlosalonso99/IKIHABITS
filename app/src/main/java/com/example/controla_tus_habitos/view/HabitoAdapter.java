package com.example.controla_tus_habitos.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.controla_tus_habitos.R;
import com.example.controla_tus_habitos.model.Habito;
import com.example.controla_tus_habitos.repository.HabitoRepository;

import java.util.List;

public class HabitoAdapter extends RecyclerView.Adapter<HabitoAdapter.HabitoViewHolder> {

    private static List<Habito> listaHabitos; // Asume que tienes una clase Habito
    private LayoutInflater inflater;
    private static HabitoRepository habitoRepository;
    private OnHabitoClickListener listener;

    // Constructor
    public HabitoAdapter(Context context, List<Habito> listaHabitos, HabitoRepository habitoRepository, OnHabitoClickListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.listaHabitos = listaHabitos;
        this.habitoRepository = habitoRepository;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HabitoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_habito, parent, false);
        return new HabitoViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitoViewHolder holder, int position) {
        Habito habitoActual = listaHabitos.get(position);
        holder.titulo.setText(habitoActual.getTitulo());
        holder.descripcion.setText(habitoActual.getDescripcion());
        holder.checkBoxCompletado.setChecked(habitoActual.isCompletado());
        switch (habitoActual.getCategoria()) {
            case ALIMENTACION:
                holder.iconoCategoria.setImageResource(R.drawable.icono_alimentacion);
                break;
            case DEPORTE:
                holder.iconoCategoria.setImageResource(R.drawable.icono_deporte);
                break;
            case TRABAJO:
                holder.iconoCategoria.setImageResource(R.drawable.icono_trabajo);
                break;
            case GENERICO:
                holder.iconoCategoria.setImageResource(R.drawable.icono_generico);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return listaHabitos.size();
    }


    /**
     * ViewHolder personalizado(patron para trabajar mas comodo)
     */

    public static class HabitoViewHolder extends RecyclerView.ViewHolder {
        public TextView titulo;
        public TextView descripcion;
        public CheckBox checkBoxCompletado;
        public ImageView iconoCategoria;


        public HabitoViewHolder(View itemView, OnHabitoClickListener listener) {
            super(itemView);

            titulo = itemView.findViewById(R.id.tvHabitoTitulo);
            descripcion = itemView.findViewById(R.id.tvHabitoDescripcion);
            checkBoxCompletado = itemView.findViewById(R.id.checkBoxCompletado);
            iconoCategoria = itemView.findViewById(R.id.iconoCategoria);
            /**
             * TODO: gestionar color por categoria
             */





            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onHabitoClick(listaHabitos.get(position));
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onHabitoLongClick(listaHabitos.get(position));
                    }
                    return false;
                }
            });

            checkBoxCompletado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Habito habito = listaHabitos.get(position);
                        habitoRepository.cambiarCompletado(habito.getId(), isChecked);
                    }
                }
            });

        }

    }
}

