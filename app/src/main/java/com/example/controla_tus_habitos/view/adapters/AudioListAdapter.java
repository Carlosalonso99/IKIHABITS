package com.example.controla_tus_habitos.view.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.controla_tus_habitos.R;
import com.example.controla_tus_habitos.model.entities_pojos.audio.Audio;
import com.example.controla_tus_habitos.repository.HabitoRepository;

import java.io.File;
import java.util.List;

public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.AudioViewHolder> {

    private Context context;
    private List<Audio> listaAudios;
    private LayoutInflater inflater;

    // Constructor
    public AudioListAdapter(Context context, List<Audio> listaAudios) {
        this.context = context;
        this.listaAudios = listaAudios;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_audio, parent, false);
        return new AudioViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, int position) {
        Audio audio = listaAudios.get(position);
        String audioName = new File(audio.getAudioPath()).getName();
        holder.tvAudioName.setText(audioName);
    }

    @Override
    public int getItemCount() {
        return listaAudios.size();
    }

    // ViewHolder
    public static class AudioViewHolder extends RecyclerView.ViewHolder {
        public TextView tvAudioName;

        public AudioViewHolder(View itemView) {
            super(itemView);
            tvAudioName = itemView.findViewById(R.id.tvAudioName);
        }
    }
}
