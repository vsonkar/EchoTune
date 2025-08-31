package com.example.echotune;

import android.content.ContentUris;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.echotune.databinding.ItemSongBinding;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private final List<Song> songs;
    private final OnItemClickListener listener;

    public SongAdapter(List<Song> songs, OnItemClickListener listener) {
        this.songs = songs;
        this.listener = listener;
    }



    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public class SongViewHolder extends RecyclerView.ViewHolder {
        final ItemSongBinding binding;
        final OnItemClickListener listener;
        public SongViewHolder(ItemSongBinding binding,  OnItemClickListener listener) {
            super(binding.getRoot());
            this.binding = binding;
            this.listener = listener;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int pos = getAdapterPosition();
                        if(pos!=RecyclerView.NO_POSITION){
                            listener.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
    @NonNull
    @Override
    public SongAdapter.SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSongBinding binding = ItemSongBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new SongViewHolder(binding, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull SongAdapter.SongViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.binding.textTitle.setText(song.title);
        holder.binding.textArtist.setText(song.artist);

        Uri albumArtUri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), song.albumId);
        Glide.with(holder.binding.getRoot().getContext())
                .load(albumArtUri)
                .circleCrop()
                .placeholder(R.drawable.musicnote)
                .error(R.drawable.musicnote)
                .into(holder.binding.imageAlbumArt);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }
}
