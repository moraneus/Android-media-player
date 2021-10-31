package com.example.mediaplayer.activities;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediaplayer.R;
import com.example.mediaplayer.databinding.SongItemBinding;
import com.example.mediaplayer.model.SongEntry;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewAdapter> {


    private static final String LOG_TAG = SongAdapter.class.getSimpleName();
    private List<SongEntry> mSongEntries;
    private final Context mContext;
    private final RecyclerViewPlayButtonListener mPlayClicklistener;

    /**
     * Constructor for the SongAdapter that initializes the Context.
     *
     * @param context  the current Context
     */
    public SongAdapter(Context context, RecyclerViewPlayButtonListener playClicklistener) {
        mContext = context;
        mPlayClicklistener = playClicklistener;
    }

    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new SongViewHolder that holds the view for each song
     */
    @NonNull
    @Override
    public SongViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(LOG_TAG, "onCreateViewHolder()");
        SongItemBinding viewBinding = SongItemBinding.
                inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SongViewAdapter(viewBinding, mPlayClicklistener);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewAdapter holder, int position) {
        Log.d(LOG_TAG, String.format("onBindViewHolder(position = {})", position));
        holder.getBinding().tvSongTitle.setText(mSongEntries.get(position).getTitle());
        holder.getBinding().tvSongArtist.setText(mSongEntries.get(position).getArtist());

        // Load album image if valid, otherwise set to default.
        int IMAGE_HEIGHT = 80;
        int IMAGE_WIDTH = 80;
        Picasso.get().load(mSongEntries.get(position).getImageLocation())
                .placeholder(R.drawable.ic_android_black)
                .resize(IMAGE_WIDTH, IMAGE_HEIGHT)
                .into(holder.getBinding().ivSongImage);
    }

    @Override
    public int getItemCount() {
        Log.d(LOG_TAG, "getItemCount()");
        if (mSongEntries == null) {
            return 0;
        }
        return mSongEntries.size();
    }

    /**
     * When data changes, this method updates the list of taskEntries
     * and notifies the adapter to use the new values on it
     */
    public void setSongs(List<SongEntry> songEntries) {
        Log.d(LOG_TAG, "setSongs()");
        mSongEntries = songEntries;
        notifyItemInserted(mSongEntries.size());
    }

    public static class SongViewAdapter extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private final SongItemBinding mBindingView;
        private final RecyclerViewPlayButtonListener mPlayClicklistener;


        public SongViewAdapter(@NonNull SongItemBinding bindingView,
                               RecyclerViewPlayButtonListener playClicklistener) {
            super(bindingView.getRoot());
            mBindingView = bindingView;
            mPlayClicklistener = playClicklistener;
            mBindingView.fabPlaySong.setOnClickListener(this);
        }

        public SongItemBinding getBinding() {
            return mBindingView;
        }

        @Override
        public void onClick(View view) {
            mPlayClicklistener.onClick(view, getAdapterPosition());
        }
    }
}
