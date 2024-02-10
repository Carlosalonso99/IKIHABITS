package com.example.controla_tus_habitos.view.adapters;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
    private boolean reproduciendo = false;
    private MediaPlayer mediaPlayer;

    AudioViewHolder anteriorHolder;

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

            Drawable playDrawable = ContextCompat.getDrawable(context, R.drawable.ic_play_arrow);
            holder.btnPlayAudio.setCompoundDrawablesWithIntrinsicBounds(playDrawable, null, null, null);
            holder.btnPlayAudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Llama a un método para reproducir el audio

                    if(anteriorHolder != null && !holder.equals(anteriorHolder)){
                        pauseAudio(anteriorHolder);
                    }
                    if(!reproduciendo){
                        playAudio(audio.getAudioPath(), holder);

                    }else{
                        pauseAudio(holder);
                    }
                    anteriorHolder = holder;

                }
            });
        } else {
            holder.tvAudioName.setText("Audio no disponible");
            holder.btnPlayAudio.setEnabled(false); // Deshabilita el botón si no hay audio
        }
    }
    public void pauseAudio(AudioViewHolder holder) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            reproduciendo = false;
            Drawable playDrawable = ContextCompat.getDrawable(context, R.drawable.ic_play_arrow);
            holder.btnPlayAudio.setCompoundDrawablesWithIntrinsicBounds(playDrawable, null, null, null);
            holder.btnPlayAudio.setText("Play");
            Log.d("MediaPlayer", "Playback paused");

        }
    }
    private void playAudio(String audioPath, AudioViewHolder holder) {
        if (mediaPlayer != null) {
            if(mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
        }
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioPath);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.d("MediaPlayer", "Prepared and starting playback");
                    mp.start();
                    reproduciendo = true;
                    Drawable playDrawable = ContextCompat.getDrawable(context, R.drawable.ic_pause);
                    holder.btnPlayAudio.setCompoundDrawablesWithIntrinsicBounds(playDrawable, null, null, null);
                    holder.btnPlayAudio.setText("Pause");
                    Log.d("MediaPlayer", "Playing");
                }
            });


            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Log.d("MediaPlayer", "Playback completed");
                    mp.release();
                    mediaPlayer = null;
                    reproduciendo = false;
                    Drawable playDrawable = ContextCompat.getDrawable(context, R.drawable.ic_play_arrow);
                    holder.btnPlayAudio.setCompoundDrawablesWithIntrinsicBounds(playDrawable, null, null, null);
                    holder.btnPlayAudio.setText("Play");;
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Toast.makeText(context, "No se pudo reproducir. Error: " + what, Toast.LENGTH_SHORT).show();
                    mp.release(); // Liberar el reproductor si ocurre un error
                    mediaPlayer = null;
                    reproduciendo = false;
                    Drawable playDrawable = ContextCompat.getDrawable(context, R.drawable.ic_play_arrow);
                    holder.btnPlayAudio.setCompoundDrawablesWithIntrinsicBounds(playDrawable, null, null, null);
                    holder.btnPlayAudio.setText("Play");
                    return true;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            if (mediaPlayer != null) {
                mediaPlayer.release(); // Asegúrate de liberar el reproductor si ocurre una excepción
                mediaPlayer = null;
            }
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
