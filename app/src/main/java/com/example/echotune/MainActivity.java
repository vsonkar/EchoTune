package com.example.echotune;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.echotune.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SongAdapter.OnItemClickListener {
    private ActivityMainBinding binding;
    private List<Song> songList;
    private RecyclerView.Adapter adapter;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if(isGranted){
                    loadSongs();
                }else{
                    Toast.makeText(this, "Permission denied to read storage", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.recyclerViewSongs.setLayoutManager(new LinearLayoutManager(this));

        checkPermissionAndLoadSongs();
    }

    private void checkPermissionAndLoadSongs() {
        String permission;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
            permission= Manifest.permission.READ_MEDIA_AUDIO;
        } else{
            permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        }

        if(ContextCompat.checkSelfPermission(this, permission)== PackageManager.PERMISSION_GRANTED){
            loadSongs();
        } else{
            requestPermissionLauncher.launch(permission);
        }
    }

    private List<Song> getSongs(){
        List<Song> songs = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selecion = MediaStore.Audio.Media.IS_MUSIC + "!=0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        try(Cursor cursor = getContentResolver().query(uri, null, selecion, null, sortOrder)){
            if(cursor!=null){
                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
                int titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
                int artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
                int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                int albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID);

                while(cursor.moveToNext()){
                    long id = cursor.getLong(idColumn);
                    String title = cursor.getString(titleColumn);
                    String artist = cursor.getString(artistColumn);
                    String data = cursor.getString(dataColumn);
                    long albumId = cursor.getLong(albumIdColumn);

                    songs.add(new Song(id, title, artist, data, albumId));
                }
            }
        }
        return songs;
    }

    private void loadSongs(){
        songList = getSongs();
        if(songList.isEmpty()) {
            Toast.makeText(this, "No songs found!", Toast.LENGTH_SHORT).show();
        }
        adapter = new SongAdapter(songList, this);
        binding.recyclerViewSongs.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putParcelableArrayListExtra("songList", new ArrayList<>(songList));
        intent.putExtra("position", position);
        startActivity(intent);

    }
}