package com.example.musicaadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class MainActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    FirebaseStorage storage;

    Uri musicPath;
    Uri coverPath;

    String musicName;
    String artist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseDatabase = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

    }

    public Uri getMusicPath() {
        return musicPath;
    }

    public Uri getCoverPath() {
        return coverPath;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }


    public void upload() {
        MusicInfo musicInfo = new MusicInfo();

        musicInfo.artist = artist;
        musicInfo.musicName = musicName;

        musicInfo.musicURL = "https://firebasestorage.googleapis.com/v0/b/mymusic-c9487.appspot.com/o/mp3%2Fmp3_" + musicName + "_" + artist + "?alt=media&token=dabd806a-cf10-4811-ad41-c0e563aaf368";
        musicInfo.coverURL = "https://firebasestorage.googleapis.com/v0/b/mymusic-c9487.appspot.com/o/covers%2Fcover_" + musicName + "_" + artist + "?alt=media&token=05636bf7-2aa9-4a9b-b869-8999af3399c0";

        firebaseDatabase.getReference("tracksInfo").child(musicName + "_" + artist).setValue(musicInfo);

        StorageReference refImage = storage.getReference().child("covers").child("cover" + "_" + musicName + "_" + artist);
        refImage.putFile(coverPath);

        StorageReference refMusic = storage.getReference().child("mp3").child("mp3" + "_" + musicName + "_" + artist);
        refMusic.putFile(musicPath);



        musicPath = null;
        coverPath = null;
    }

    public void makeToast(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
    }

    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 71);
    }

    public void chooseAudio() {
        Intent intent = new Intent();
        intent.setType("audio/mp3");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Music File"), 72);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 71 && resultCode == RESULT_OK && data != null && data.getData() != null ) {
            coverPath = data.getData();
        }
        if(requestCode == 72 && resultCode == RESULT_OK && data != null && data.getData() != null ) {
            musicPath = data.getData();
        }
    }
}