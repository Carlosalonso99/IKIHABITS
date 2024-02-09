package com.example.controla_tus_habitos.view.adapters;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.controla_tus_habitos.R;
import com.example.controla_tus_habitos.model.entities_pojos.audio.Audio;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.AudioViewHolder> {

    private Context context;
    private List<Audio> listaAudios;
    private LayoutInflater inflater;

    // Constructor
    public AudioAdapter(Context context, List<Audio> listaAudios) {
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
        if (audio.getAudioPath() != null) {
            String audioName = new File(audio.getAudioPath()).getName();
            holder.tvAudioName.setText(audioName);

            holder.btnPlayAudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Llama a un método para reproducir el audio
                    playAudio(audio.getAudioPath());
                }
            });
        } else {
            holder.tvAudioName.setText("Audio no disponible");
            holder.btnPlayAudio.setEnabled(false); // Deshabilita el botón si no hay audio
        }
    }
    private void playAudio(String audioPath) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioPath);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.d("MediaPlayer", "Prepared and starting playback");
                    mp.start();
                    Log.d("MediaPlayer", "Playing");
                }
            });


            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Log.d("MediaPlayer", "Playback completed");
                    mediaPlayer.release();
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Toast.makeText(context, "no se pudo reproducir" + what, Toast.LENGTH_SHORT).show();;
                    return true;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    @Override
    public int getItemCount() {
        return listaAudios.size();
    }

    // ViewHolder
    public static class AudioViewHolder extends RecyclerView.ViewHolder {
        public TextView tvAudioName;
        public Button btnPlayAudio;

        public AudioViewHolder(View itemView) {
            super(itemView);
            tvAudioName = itemView.findViewById(R.id.tvAudioName);
            btnPlayAudio = itemView.findViewById(R.id.btnPlayAudio);
        }
    }
}
