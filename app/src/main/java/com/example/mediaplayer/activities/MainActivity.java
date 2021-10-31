package com.example.mediaplayer.activities;

import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mediaplayer.R;
import com.example.mediaplayer.databinding.ActivityMainBinding;
import com.example.mediaplayer.model.MediaPlayerViewModel;
import com.example.mediaplayer.model.SongEntry;
import com.example.mediaplayer.services.MediaStoreAudioObserverService;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String SELECTED_SONG_KEY = "selected";
    private static final int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 0;

    private SongAdapter mAdapter;
    private ActivityMainBinding mActivityMainBinding;
    private View mView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate()");
        // Initialize main view
        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        mView = mActivityMainBinding.getRoot();
        setContentView(mView);
        initializeRecyclerView();
        requestReadExternalStoragePermission();
        startService(new Intent(this, MediaStoreAudioObserverService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume()");

    }

    private void initializeRecyclerView() {
        Log.d(LOG_TAG, "initializeRecyclerView()");
        
        // Set the adapter for the recyclerView.
        mActivityMainBinding.rvSongs.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SongAdapter(this, new RecyclerViewPlayButtonListener() {
            @Override
            public void onClick(View view, int position) {
                playSong(position);
            }
        });
        mActivityMainBinding.rvSongs.setAdapter(mAdapter);

        // Set divider between recyclerView rows.
        DividerItemDecoration decoration =
                new DividerItemDecoration(getApplicationContext(), VERTICAL);
        mActivityMainBinding.rvSongs.addItemDecoration(decoration);
    }

    private void requestReadExternalStoragePermission() {
        Log.d(LOG_TAG, "requestReadExternalStoragePermission()");
        if (!(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            Snackbar.make(mView, R.string.read_external_storage_required,
                    Snackbar.LENGTH_SHORT).show();

            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            setupViewModel();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(LOG_TAG, "onRequestPermissionsResult()");
        if (requestCode == PERMISSION_REQUEST_READ_EXTERNAL_STORAGE) {
            // Check for read external permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been already granted.
                Snackbar.make(mView, R.string.read_external_storage_permission_granted,
                        Snackbar.LENGTH_SHORT)
                        .show();
                setupViewModel();
            } else {
                // Permission request was denied.
                Snackbar.make(mView, R.string.read_external_storage_failed,
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void setupViewModel() {
        Log.d(LOG_TAG, "setupViewModel()");
        MediaPlayerViewModel mediaPlayerViewModel =
                ViewModelProviders.of(this).get(MediaPlayerViewModel.class);
        mediaPlayerViewModel.getSongs().observe(this, new Observer<List<SongEntry>>() {
            @Override
            public void onChanged(@Nullable List<SongEntry> songEntries) {
                Log.d(LOG_TAG, "Updating list of songs from LiveData in ViewModel");
                mAdapter.setSongs(songEntries);
            }
        });
    }

    private void playSong(int selectedSong) {
        Intent myIntent = new Intent(MainActivity.this, PlaySongActivity.class);
        myIntent.putExtra(SELECTED_SONG_KEY, selectedSong);
        MainActivity.this.startActivity(myIntent);
    }
}