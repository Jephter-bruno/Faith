package com.glamour.faith.adapters;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.media2.exoplayer.external.ExoPlayerFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.glamour.faith.R;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;

import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;


import de.hdodenhof.circleimageview.CircleImageView;

public class ViewHolder extends RecyclerView.ViewHolder {
   SimpleExoPlayer exoPlayer;
   PlayerView playerView;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
    }
    public void setExoplayer(FragmentActivity application , String name, String profileImage , String description, String postVideo, String date, String time, String church)
    {
        CircleImageView profile = itemView.findViewById(R.id.profile);
        TextView nameuser = itemView.findViewById(R.id.name);
        TextView chur = itemView.findViewById(R.id.church);
        TextView dates = itemView.findViewById(R.id.date);
        TextView times = itemView.findViewById(R.id.time);
        TextView describe = itemView.findViewById(R.id.description);
        playerView = itemView.findViewById(R.id.videoview);

        nameuser.setText(name);
        chur.setText(church);
        dates.setText(date);
        times.setText(time);
        describe.setText(description);
        /*Picasso.get().load(profileImage).into((Target) profile);*/
/*
        try {
            Uri videoUri = Uri.parse(postVideo);
            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(context);
            ProgressiveMediaSource.Factory mediaSourceFactory = new ProgressiveMediaSource.Factory(dataSourceFactory);
            MediaSource mediaSource = mediaSourceFactory.createMediaSource(MediaItem.fromUri(videoUri));
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(false);
        } catch (Exception e) {
            Log.e("ViewHolder", "ExoPlayer error: " + e.toString());
        }
*/

    }
}
