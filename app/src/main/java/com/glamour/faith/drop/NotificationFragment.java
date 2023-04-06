package com.glamour.faith.drop;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.glamour.faith.Model.PostPhoto;
import com.glamour.faith.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.ponnamkarthik.richlinkpreview.MetaData;
import io.github.ponnamkarthik.richlinkpreview.RichLinkViewSkype;
import io.github.ponnamkarthik.richlinkpreview.ViewListener;

public class NotificationFragment extends Fragment {
    String[] mode = { "All Church Members","Church Branch Only"};

    public String churs,DatabaseUserId,namess,profiless,postmodes;
    RecyclerView recycler_post;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notification, container, false);
        recycler_post = root.findViewById(R.id.recycler_post);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recycler_post.setLayoutManager(linearLayoutManager);
        AdView mAdView = root.findViewById(R.id.adView);
        AdView adView = new AdView(getContext());
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getString(R.string.admob_app_id));
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        DisplayPhotoInPublic();
        return root;
    }
    private void DisplayPhotoInPublic() {

        Query postphotore = FirebaseDatabase.getInstance().getReference().child("Post_photos_public").orderByChild("Counter");
        FirebaseRecyclerOptions<PostPhoto> options = new FirebaseRecyclerOptions.Builder<PostPhoto>()
                .setQuery(postphotore,PostPhoto.class)
                .build();

        FirebaseRecyclerAdapter<PostPhoto, ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PostPhoto, ViewHolder>(
                options
        ) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull PostPhoto model) {
                String PostKey = getRef(position).getKey();
                namess = model.getName();
                profiless = model.getProfileImage();
                postmodes = model.getPostmode();
                final MetaData[] data = new MetaData[1];
                DatabaseUserId = model.getUserid();


                /*else if (model.getPostmode().equals("retweet")) {
                    holder.eventprof.setVisibility(View.VISIBLE);
                    holder.setName(model.getName());
                    holder.setChurch(model.getChurch());
                    holder.setDate(model.getDate());
                    holder.celebrate.setText("Re-tweeted a post posted by");
                    holder.descriptions.setText("Re-tweeted a post posted by");
                    holder.celebrate.setVisibility(View.VISIBLE);
                    holder.cel.setVisibility(View.GONE);
                    holder.setPostedProfile(getContext(),model.getPostedProfile());
                    holder.setPostedChurch(model.getPostedChurch());
                    holder.setPostedname(model.getPostedname());
                    holder.setTime(model.getTime());
                    holder.setProfileImage(getContext(), model.getProfileImage());
                    if(model.getType().equals("text")){
                        holder.celebrate.setText("Re-tweeted a post posted by");
                        holder.scripturebk.setVisibility(View.GONE);
                        holder.scripturecnt.setVisibility(View.GONE);
                        holder.videoview.setVisibility(View.GONE);
                        holder.image_view.setVisibility(View.GONE);
                        holder.setDescription(model.getDescription());
                        holder.richLinkView.setVisibility(View.GONE);
                    }
                    else if(model.getType().equals("link")){
                        holder.celebrate.setText("Re-tweeted a link posted by");
                        holder.scripturebk.setVisibility(View.GONE);
                        holder.scripturecnt.setVisibility(View.GONE);
                        holder.videoview.setVisibility(View.GONE);
                        holder.image_view.setVisibility(View.GONE);
                        holder.description.setVisibility(View.GONE);
                        holder.richLinkView.setLink(model.getLink(), new ViewListener() {
                            @Override
                            public void onSuccess(boolean status) {
                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
                    }
                    else if(model.getType().equals("video")){
                        holder.celebrate.setText("Re-tweeted a video posted by");
                        holder.scripturebk.setVisibility(View.GONE);
                        holder.scripturecnt.setVisibility(View.GONE);
                        holder.image_view.setVisibility(View.GONE);
                        holder.setPostVideo(getContext(),model.getPostVideo());
                        holder.setDescription(model.getDescription());
                        holder.richLinkView.setVisibility(View.GONE);
                    }

                    else if(model.getType().equals("photo")){
                        holder.celebrate.setText("Re-tweeted a photo posted by");
                        holder.scripturebk.setVisibility(View.GONE);
                        holder.image_view.setVisibility(View.VISIBLE);
                        holder.scripturecnt.setVisibility(View.GONE);
                        holder.videoview.setVisibility(View.GONE);
                        holder.setPostImage(getContext(),model.getPostImage());
                        holder.description.setVisibility(View.GONE);
                        holder.richLinkView.setVisibility(View.GONE);
                    }
                    else if(model.getType().equals("photowithtext")){
                        holder.celebrate.setText("Re-tweeted a this post posted by");
                        holder.image_view.setVisibility(View.VISIBLE);
                        holder.scripturebk.setVisibility(View.GONE);
                        holder.scripturecnt.setVisibility(View.GONE);
                        holder.videoview.setVisibility(View.GONE);
                        holder.setPostImage(getContext(),model.getPostImage());
                        holder.setDescription(model.getDescription());
                        holder.richLinkView.setVisibility(View.GONE);
                    }


                }
                else if(model.getPostmode().equals("events") && model.getConfidentiality().equals("public")){
                    holder.setChurch(model.getName()+ " on behalf of the whole ministry, is welcoming you all to a "+model.getEvent()+" to be held on "+ model.getDate());
                    holder.lin1.setVisibility(View.VISIBLE);
                    holder.comssssss.setVisibility(View.GONE);
                    holder.setTime(model.getTime());
                    holder.setDate(model.getDate());
                    holder.setProfileImage(getContext(), model.getProfileImage());
                    holder.setName(model.getName());

                }
                else if (model.getPostmode().equals("celebrate")) {
                    holder.eventprof.setVisibility(View.VISIBLE);
                    holder.setName(model.getName());
                    holder.setChurch(model.getChurch());
                    holder.setDate(model.getDate());
                    holder.setDescription(model.getDescription());
                    holder.videoview.setVisibility(View.GONE);
                    holder.scripturebk.setVisibility(View.GONE);
                    holder.image_view.setVisibility(View.VISIBLE);
                    holder.scripturecnt.setVisibility(View.GONE);
                    holder.retweet.setVisibility(View.GONE);
                    holder.cel.setVisibility(View.VISIBLE);
                    holder.setPostedProfile(getContext(),model.getPostedProfile());
                    holder.setPostedChurch(model.getPostedChurch());
                    holder.setPostedname(model.getPostedname());
                    holder.setTime(model.getTime());
                    holder.setProfileImage(getContext(), model.getProfileImage());


                }

                else if (model.getPostmode().equals("youtubelink")) {
                    holder.setName(model.getName());
                    holder.setChurch(model.getChurch());
                    holder.setDate(model.getDate());
                    holder.description.setVisibility(View.GONE);
                    holder.videoview.setVisibility(View.GONE);
                    holder.scripturebk.setVisibility(View.GONE);
                    holder.scripturecnt.setVisibility(View.GONE);
                    holder.image_view.setVisibility(View.GONE);
                    holder.setTime(model.getTime());
                    holder.setProfileImage(getContext(), model.getProfileImage());
                    holder.richLinkView.setLink(model.getLink(), new ViewListener() {
                        @Override
                        public void onSuccess(boolean status) {

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });

                    RichPreview richPreview = new RichPreview(new ResponseListener() {
                        @Override
                        public void onData(MetaData metaData) {

                            data[0] = metaData;
                            metaData.getTitle();
                            metaData.getImageurl();
                            metaData.getDescription();
                            metaData.getSitename();
                            metaData.getUrl();
                            metaData.getFavicon();
                            metaData.getMediatype();
                            holder.richLinkView.setLinkFromMeta(metaData);

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });

                }

                else if(model.getPostmode().equals("video")&& model.getConfidentiality().equals("private")){
                    if(model.getChurch().equals(churs)){
                        holder.setName(model.getName());
                        holder.setChurch(model.getChurch());
                        holder.setDate(model.getDate());
                        holder.setPostVideo(getContext(),model.getPostVideo());
                        holder.videoview.setVisibility(View.VISIBLE);
                        holder.image_view.setVisibility(View.GONE);
                        holder.setDescription(model.getDescription());
                        holder.scripturebk.setVisibility(View.GONE);
                        holder.scripturecnt.setVisibility(View.GONE);
                        holder.description.setVisibility(View.GONE);
                        holder.richLinkView.setVisibility(View.GONE);
                        holder.setPostImage(getContext(),model.getPostImage());
                        holder.setTime(model.getTime());
                        holder.setProfileImage(getContext(),model.getProfileImage());
                    }
                    else{
                        holder.cardView.setVisibility(View.GONE);
                        holder.posted.setVisibility(View.GONE);
                        holder.from.setVisibility(View.GONE);
                        holder.description.setVisibility(View.GONE);
                        holder.image_view.setVisibility(View.GONE);
                        holder.church.setVisibility(View.GONE);
                        holder.scripturebk.setVisibility(View.GONE);
                        holder.scripturecnt.setVisibility(View.GONE);
                        holder.date.setVisibility(View.GONE);
                        holder.time.setVisibility(View.GONE);
                        holder.videoview.setVisibility(View.GONE);
                        holder.profile.setVisibility(View.GONE);
                        holder.name.setVisibility(View.GONE);
                        holder.like.setVisibility(View.GONE);
                        holder.displayNolikes.setVisibility(View.GONE);
                        holder.comment.setVisibility(View.GONE);
                        holder.displayNocomment.setVisibility(View.GONE);
                        holder.likeds.setVisibility(View.GONE);
                        holder.coms.setVisibility(View.GONE);
                        holder.comss.setVisibility(View.GONE);
                        holder.comsss.setVisibility(View.GONE);
                        holder.comssss.setVisibility(View.GONE);
                        holder.comsssss.setVisibility(View.GONE);
                        holder.comssssss.setVisibility(View.GONE);
                        holder.comsssssss.setVisibility(View.GONE);
                        holder.comssssssss.setVisibility(View.GONE);
                        holder.richLinkView.setVisibility(View.GONE);
                    }
                }

                else if(model.getPostmode().equals("advertwithtextandimage")&& model.getConfidentiality().equals("private")){
                    if(model.getChurch().equals(churs)){
                        holder.setName(model.getName());
                        holder.image_view.setVisibility(View.VISIBLE);
                        holder.setChurch(model.getChurch());
                        holder.setDate(model.getDate());
                        holder.videoview.setVisibility(View.GONE);
                        holder.scripturebk.setVisibility(View.GONE);
                        holder.scripturecnt.setVisibility(View.GONE);
                        holder.description.setVisibility(View.GONE);
                        holder.richLinkView.setVisibility(View.GONE);
                        holder.setPostImage(getContext(),model.getPostImage());
                        holder.setTime(model.getTime());
                        holder.setProfileImage(getContext(),model.getProfileImage());
                    }

                    else{
                        holder.cardView.setVisibility(View.GONE);
                        holder.posted.setVisibility(View.GONE);
                        holder.from.setVisibility(View.GONE);
                        holder.description.setVisibility(View.GONE);
                        holder.image_view.setVisibility(View.GONE);
                        holder.church.setVisibility(View.GONE);
                        holder.scripturebk.setVisibility(View.GONE);
                        holder.scripturecnt.setVisibility(View.GONE);
                        holder.date.setVisibility(View.GONE);
                        holder.time.setVisibility(View.GONE);
                        holder.videoview.setVisibility(View.GONE);
                        holder.profile.setVisibility(View.GONE);
                        holder.name.setVisibility(View.GONE);
                        holder.like.setVisibility(View.GONE);
                        holder.displayNolikes.setVisibility(View.GONE);
                        holder.comment.setVisibility(View.GONE);
                        holder.displayNocomment.setVisibility(View.GONE);
                        holder.likeds.setVisibility(View.GONE);
                        holder.coms.setVisibility(View.GONE);
                        holder.comss.setVisibility(View.GONE);
                        holder.comsss.setVisibility(View.GONE);
                        holder.comssss.setVisibility(View.GONE);
                        holder.comsssss.setVisibility(View.GONE);
                        holder.comssssss.setVisibility(View.GONE);
                        holder.comsssssss.setVisibility(View.GONE);
                        holder.comssssssss.setVisibility(View.GONE);
                        holder.richLinkView.setVisibility(View.GONE);
                    }
                }

                else if(model.getPostmode().equals("advertwithphotoonly")&& model.getConfidentiality().equals("private")){
                    if(model.getChurch().equals(churs)){
                        holder.setName(model.getName());
                        holder.setChurch(model.getChurch());
                        holder.setDate(model.getDate());
                        holder.videoview.setVisibility(View.GONE);
                        holder.image_view.setVisibility(View.VISIBLE);
                        holder.scripturebk.setVisibility(View.GONE);
                        holder.scripturecnt.setVisibility(View.GONE);
                        holder.description.setVisibility(View.GONE);
                        holder.richLinkView.setVisibility(View.GONE);
                        holder.setPostImage(getContext(),model.getPostImage());
                        holder.setTime(model.getTime());
                        holder.setProfileImage(getContext(),model.getProfileImage());
                    }
                    else{
                        holder.cardView.setVisibility(View.GONE);
                        holder.posted.setVisibility(View.GONE);
                        holder.from.setVisibility(View.GONE);
                        holder.description.setVisibility(View.GONE);
                        holder.image_view.setVisibility(View.GONE);
                        holder.church.setVisibility(View.GONE);
                        holder.scripturebk.setVisibility(View.GONE);
                        holder.scripturecnt.setVisibility(View.GONE);
                        holder.date.setVisibility(View.GONE);
                        holder.time.setVisibility(View.GONE);
                        holder.videoview.setVisibility(View.GONE);
                        holder.profile.setVisibility(View.GONE);
                        holder.name.setVisibility(View.GONE);
                        holder.like.setVisibility(View.GONE);
                        holder.displayNolikes.setVisibility(View.GONE);
                        holder.comment.setVisibility(View.GONE);
                        holder.displayNocomment.setVisibility(View.GONE);
                        holder.likeds.setVisibility(View.GONE);
                        holder.coms.setVisibility(View.GONE);
                        holder.comss.setVisibility(View.GONE);
                        holder.comsss.setVisibility(View.GONE);
                        holder.comssss.setVisibility(View.GONE);
                        holder.comsssss.setVisibility(View.GONE);
                        holder.comssssss.setVisibility(View.GONE);
                        holder.comsssssss.setVisibility(View.GONE);
                        holder.comssssssss.setVisibility(View.GONE);
                        holder.richLinkView.setVisibility(View.GONE);
                    }
                }
                else if(model.getPostmode().equals("advertwithtextonly")&& model.getConfidentiality().equals("private")){
                    if(model.getChurch().equals(churs)){
                        holder.setName(model.getName());
                        holder.setChurch(model.getChurch());
                        holder.setDate(model.getDate());
                        holder.videoview.setVisibility(View.GONE);
                        holder.scripturebk.setVisibility(View.GONE);
                        holder.scripturecnt.setVisibility(View.GONE);
                        holder.description.setVisibility(View.GONE);
                        holder.richLinkView.setVisibility(View.GONE);
                        holder.setPostImage(getContext(),model.getPostImage());
                        holder.setTime(model.getTime());
                        holder.setProfileImage(getContext(),model.getProfileImage());
                    }
                    else{
                        holder.cardView.setVisibility(View.GONE);
                        holder.posted.setVisibility(View.GONE);
                        holder.from.setVisibility(View.GONE);
                        holder.description.setVisibility(View.GONE);
                        holder.image_view.setVisibility(View.GONE);
                        holder.church.setVisibility(View.GONE);
                        holder.scripturebk.setVisibility(View.GONE);
                        holder.scripturecnt.setVisibility(View.GONE);
                        holder.date.setVisibility(View.GONE);
                        holder.time.setVisibility(View.GONE);
                        holder.videoview.setVisibility(View.GONE);
                        holder.profile.setVisibility(View.GONE);
                        holder.name.setVisibility(View.GONE);
                        holder.like.setVisibility(View.GONE);
                        holder.displayNolikes.setVisibility(View.GONE);
                        holder.comment.setVisibility(View.GONE);
                        holder.displayNocomment.setVisibility(View.GONE);
                        holder.likeds.setVisibility(View.GONE);
                        holder.coms.setVisibility(View.GONE);
                        holder.comss.setVisibility(View.GONE);
                        holder.comsss.setVisibility(View.GONE);
                        holder.comssss.setVisibility(View.GONE);
                        holder.comsssss.setVisibility(View.GONE);
                        holder.comssssss.setVisibility(View.GONE);
                        holder.comsssssss.setVisibility(View.GONE);
                        holder.comssssssss.setVisibility(View.GONE);
                        holder.richLinkView.setVisibility(View.GONE);
                    }
                }
                else if(model.getPostmode().equals("testimonyphoto")&& model.getConfidentiality().equals("private")){
                    if(model.getChurch().equals(churs)){
                        holder.setName(model.getName());
                        holder.setChurch(model.getChurch());
                        holder.setDate(model.getDate());
                        holder.videoview.setVisibility(View.GONE);
                        holder.image_view.setVisibility(View.VISIBLE);
                        holder.scripturebk.setVisibility(View.GONE);
                        holder.scripturecnt.setVisibility(View.GONE);
                        holder.description.setVisibility(View.GONE);
                        holder.richLinkView.setVisibility(View.GONE);
                        holder.setPostImage(getContext(),model.getPostImage());
                        holder.setTime(model.getTime());
                        holder.setProfileImage(getContext(),model.getProfileImage());
                    }
                    else{
                        holder.cardView.setVisibility(View.GONE);
                        holder.posted.setVisibility(View.GONE);
                        holder.from.setVisibility(View.GONE);
                        holder.description.setVisibility(View.GONE);
                        holder.image_view.setVisibility(View.GONE);
                        holder.church.setVisibility(View.GONE);
                        holder.scripturebk.setVisibility(View.GONE);
                        holder.scripturecnt.setVisibility(View.GONE);
                        holder.date.setVisibility(View.GONE);
                        holder.time.setVisibility(View.GONE);
                        holder.videoview.setVisibility(View.GONE);
                        holder.profile.setVisibility(View.GONE);
                        holder.name.setVisibility(View.GONE);
                        holder.like.setVisibility(View.GONE);
                        holder.displayNolikes.setVisibility(View.GONE);
                        holder.comment.setVisibility(View.GONE);
                        holder.displayNocomment.setVisibility(View.GONE);
                        holder.likeds.setVisibility(View.GONE);
                        holder.coms.setVisibility(View.GONE);
                        holder.comss.setVisibility(View.GONE);
                        holder.comsss.setVisibility(View.GONE);
                        holder.comssss.setVisibility(View.GONE);
                        holder.comsssss.setVisibility(View.GONE);
                        holder.comssssss.setVisibility(View.GONE);
                        holder.comsssssss.setVisibility(View.GONE);
                        holder.comssssssss.setVisibility(View.GONE);
                        holder.richLinkView.setVisibility(View.GONE);
                    }
                }

                else if(model.getPostmode().equals("testimonytext")&& model.getConfidentiality().equals("private")){
                    if(model.getChurch().equals(churs)){
                        holder.setName(model.getName());
                        holder.setChurch(model.getChurch());
                        holder.setDate(model.getDate());
                        holder.videoview.setVisibility(View.GONE);
                        holder.scripturebk.setVisibility(View.GONE);
                        holder.scripturecnt.setVisibility(View.GONE);
                        holder.setDescription(model.getDescription());
                        holder.richLinkView.setVisibility(View.GONE);
                        holder.image_view.setVisibility(View.GONE);
                        holder.setTime(model.getTime());
                        holder.setProfileImage(getContext(),model.getProfileImage());
                    }
                    else{
                        holder.cardView.setVisibility(View.GONE);
                        holder.posted.setVisibility(View.GONE);
                        holder.from.setVisibility(View.GONE);
                        holder.description.setVisibility(View.GONE);
                        holder.image_view.setVisibility(View.GONE);
                        holder.church.setVisibility(View.GONE);
                        holder.scripturebk.setVisibility(View.GONE);
                        holder.scripturecnt.setVisibility(View.GONE);
                        holder.date.setVisibility(View.GONE);
                        holder.time.setVisibility(View.GONE);
                        holder.videoview.setVisibility(View.GONE);
                        holder.profile.setVisibility(View.GONE);
                        holder.name.setVisibility(View.GONE);
                        holder.like.setVisibility(View.GONE);
                        holder.displayNolikes.setVisibility(View.GONE);
                        holder.comment.setVisibility(View.GONE);
                        holder.displayNocomment.setVisibility(View.GONE);
                        holder.likeds.setVisibility(View.GONE);
                        holder.coms.setVisibility(View.GONE);
                        holder.comss.setVisibility(View.GONE);
                        holder.comsss.setVisibility(View.GONE);
                        holder.comssss.setVisibility(View.GONE);
                        holder.comsssss.setVisibility(View.GONE);
                        holder.comssssss.setVisibility(View.GONE);
                        holder.comsssssss.setVisibility(View.GONE);
                        holder.comssssssss.setVisibility(View.GONE);
                        holder.richLinkView.setVisibility(View.GONE);
                    }
                }
                else if(model.getPostmode().equals("testimonyphotoandtext")&& model.getConfidentiality().equals("private")){
                    if(model.getChurch().equals(churs)){
                        holder.setName(model.getName());
                        holder.setChurch(model.getChurch());
                        holder.setDate(model.getDate());
                        holder.videoview.setVisibility(View.GONE);
                        holder.image_view.setVisibility(View.VISIBLE);
                        holder.scripturebk.setVisibility(View.GONE);
                        holder.scripturecnt.setVisibility(View.GONE);
                        holder.setDescription(model.getDescription());
                        holder.richLinkView.setVisibility(View.GONE);
                        holder.setPostImage(getContext(),model.getPostImage());
                        holder.setTime(model.getTime());
                        holder.setProfileImage(getContext(),model.getProfileImage());
                    }
                    else{
                        holder.cardView.setVisibility(View.GONE);
                        holder.posted.setVisibility(View.GONE);
                        holder.from.setVisibility(View.GONE);
                        holder.description.setVisibility(View.GONE);
                        holder.image_view.setVisibility(View.GONE);
                        holder.church.setVisibility(View.GONE);
                        holder.scripturebk.setVisibility(View.GONE);
                        holder.scripturecnt.setVisibility(View.GONE);
                        holder.date.setVisibility(View.GONE);
                        holder.time.setVisibility(View.GONE);
                        holder.videoview.setVisibility(View.GONE);
                        holder.profile.setVisibility(View.GONE);
                        holder.name.setVisibility(View.GONE);
                        holder.like.setVisibility(View.GONE);
                        holder.displayNolikes.setVisibility(View.GONE);
                        holder.comment.setVisibility(View.GONE);
                        holder.displayNocomment.setVisibility(View.GONE);
                        holder.likeds.setVisibility(View.GONE);
                        holder.coms.setVisibility(View.GONE);
                        holder.comss.setVisibility(View.GONE);
                        holder.comsss.setVisibility(View.GONE);
                        holder.comssss.setVisibility(View.GONE);
                        holder.comsssss.setVisibility(View.GONE);
                        holder.comssssss.setVisibility(View.GONE);
                        holder.comsssssss.setVisibility(View.GONE);
                        holder.comssssssss.setVisibility(View.GONE);
                        holder.richLinkView.setVisibility(View.GONE);
                    }
                }*/
                if(model.getPostmode().equals("text")&& model.getConfidentiality().equals("private")){
                    if(model.getChurch().equals(churs)){
                        holder.setName(model.getName());
                        holder.type.setText("Wrote this on your church's timeline");
                        holder.setChurch(model.getChurch());
                        holder.setDate(model.getDate());
                        holder.setDescription(model.getDescription());
                        holder.setTime(model.getTime());
                        holder.setProfileImage(getContext(),model.getProfileImage());
                        holder.photo.setVisibility(View.GONE);
                        holder.watch_video.setVisibility(View.GONE);
                        holder.view_more.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                holder.descriptions.setText(model.getDescription());
                                holder.description.setVisibility(View.GONE);
                                holder.view_more.setVisibility(View.GONE);
                            }
                        });
                    }
                    else{
                        holder.description.setVisibility(View.GONE);
                        holder.church.setVisibility(View.GONE);
                        holder.photo.setVisibility(View.GONE);
                        holder.view_more.setVisibility(View.GONE);
                        holder.date.setVisibility(View.GONE);
                        holder.time.setVisibility(View.GONE);
                        holder.watch_video.setVisibility(View.GONE);
                        holder.profile.setVisibility(View.GONE);
                        holder.name.setVisibility(View.GONE);
                        holder.type.setVisibility(View.GONE);
                        holder.from.setVisibility(View.GONE);
                        holder.incase.setVisibility(View.GONE);
                        holder.views.setVisibility(View.GONE);


                    }
                }
                if(model.getPostmode().equals("prayer")&& model.getConfidentiality().equals("private")){
                    if(model.getChurch().equals(churs)){
                        holder.setName(model.getName());
                        holder.type.setText("Is requesting for your prayers in  church's timeline saying ");
                        holder.setChurch(model.getChurch());
                        holder.setDate(model.getDate());
                        holder.setDescription(model.getDescription());
                        holder.setTime(model.getTime());
                        holder.setProfileImage(getContext(),model.getProfileImage());
                        holder.photo.setVisibility(View.GONE);
                        holder.watch_video.setVisibility(View.GONE);
                        holder.view_more.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                holder.descriptions.setText(model.getDescription());
                                holder.description.setVisibility(View.GONE);
                                holder.view_more.setVisibility(View.GONE);
                            }
                        });
                    }
                    else{
                        holder.description.setVisibility(View.GONE);
                        holder.church.setVisibility(View.GONE);
                        holder.photo.setVisibility(View.GONE);
                        holder.view_more.setVisibility(View.GONE);
                        holder.date.setVisibility(View.GONE);
                        holder.time.setVisibility(View.GONE);
                        holder.watch_video.setVisibility(View.GONE);
                        holder.profile.setVisibility(View.GONE);
                        holder.name.setVisibility(View.GONE);
                        holder.type.setVisibility(View.GONE);
                        holder.from.setVisibility(View.GONE);
                        holder.incase.setVisibility(View.GONE);
                        holder.views.setVisibility(View.GONE);


                    }
                }

                else if (model.getPostmode().equals("youtubelink")) {
                    holder.setName(model.getName());
                    holder.type.setText("shared this link on Faith Ministry's timeline");
                    holder.setChurch(model.getChurch());
                    holder.setDate(model.getDate());
                    holder.description.setText(model.getLink());
                    holder.description.setTextColor(Color.parseColor("#f1b814"));
                    holder.setTime(model.getTime());
                    holder.setProfileImage(getContext(),model.getProfileImage());
                    holder.photo.setVisibility(View.GONE);
                    holder.watch_video.setVisibility(View.GONE);
                    holder.view_more.setText("Preview Link");
                    holder.view_more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            holder.view_more.setVisibility(View.GONE);
                            holder.richLinkView.setLink(model.getLink(), new ViewListener() {
                                @Override
                                public void onSuccess(boolean status) {

                                }

                                @Override
                                public void onError(Exception e) {

                                }
                            });


                        }
                    });

                }

                else if (model.getPostmode().equals("link")) {
                    holder.setName(model.getName());
                    holder.type.setText("shared this link on Faith Ministry's timeline");
                    holder.setChurch(model.getChurch());
                    holder.setDate(model.getDate());
                    holder.description.setText(model.getLink());
                    holder.description.setTextColor(Color.parseColor("#f1b814"));
                    holder.setTime(model.getTime());
                    holder.setProfileImage(getContext(),model.getProfileImage());
                    holder.photo.setVisibility(View.GONE);
                    holder.watch_video.setVisibility(View.GONE);
                    holder.view_more.setText("Preview Link");
                    holder.view_more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            holder.view_more.setVisibility(View.GONE);
                            holder.richLinkView.setLink(model.getLink(), new ViewListener() {
                                @Override
                                public void onSuccess(boolean status) {

                                }

                                @Override
                                public void onError(Exception e) {

                                }
                            });


                        }
                    });

                }

/*
                else if(model.getPostmode().equals("photowithtext")&& model.getConfidentiality().equals("private")){
                    if(model.getChurch().equals(churs)){
                        holder.setName(model.getName());
                        holder.setChurch(model.getChurch());
                        holder.image_view.setVisibility(View.VISIBLE);
                        holder.setDate(model.getDate());
                        holder.videoview.setVisibility(View.GONE);
                        holder.scripturebk.setVisibility(View.GONE);
                        holder.scripturecnt.setVisibility(View.GONE);
                        holder.setDescription(model.getDescription());
                        holder.richLinkView.setVisibility(View.GONE);
                        holder.setPostImage(getContext(),model.getPostImage());
                        holder.setTime(model.getTime());
                        holder.setProfileImage(getContext(),model.getProfileImage());
                    }
                    else{
                        holder.cardView.setVisibility(View.GONE);
                        holder.posted.setVisibility(View.GONE);
                        holder.from.setVisibility(View.GONE);
                        holder.description.setVisibility(View.GONE);
                        holder.image_view.setVisibility(View.GONE);
                        holder.church.setVisibility(View.GONE);
                        holder.scripturebk.setVisibility(View.GONE);
                        holder.scripturecnt.setVisibility(View.GONE);
                        holder.date.setVisibility(View.GONE);
                        holder.time.setVisibility(View.GONE);
                        holder.videoview.setVisibility(View.GONE);
                        holder.profile.setVisibility(View.GONE);
                        holder.name.setVisibility(View.GONE);
                        holder.like.setVisibility(View.GONE);
                        holder.displayNolikes.setVisibility(View.GONE);
                        holder.comment.setVisibility(View.GONE);
                        holder.displayNocomment.setVisibility(View.GONE);
                        holder.likeds.setVisibility(View.GONE);
                        holder.coms.setVisibility(View.GONE);
                        holder.comss.setVisibility(View.GONE);
                        holder.comsss.setVisibility(View.GONE);
                        holder.comssss.setVisibility(View.GONE);
                        holder.comsssss.setVisibility(View.GONE);
                        holder.comssssss.setVisibility(View.GONE);
                        holder.comsssssss.setVisibility(View.GONE);
                        holder.comssssssss.setVisibility(View.GONE);
                        holder.richLinkView.setVisibility(View.GONE);
                    }
                }
*/
/*
                else if(model.getPostmode().equals("scripture")&& model.getConfidentiality().equals("private")){
                    if(model.getChurch().equals(churs)){
                        holder.setName(model.getName());
                        holder.setChurch(model.getChurch());
                        holder.setDate(model.getDate());
                        holder.description.setVisibility(View.GONE);
                        holder.videoview.setVisibility(View.GONE);
                        holder.richLinkView.setVisibility(View.GONE);
                        holder.setScriptureContent(model.getScriptureContent());
                        holder.setScriptureBook(model.getScriptureBook());
                        holder.image_view.setVisibility(View.GONE);
                        holder.setTime(model.getTime());
                        holder.setProfileImage(getContext(),model.getProfileImage());}


*/
/*
                    else{
                        holder.cardView.setVisibility(View.GONE);
                        holder.posted.setVisibility(View.GONE);
                        holder.from.setVisibility(View.GONE);
                        holder.description.setVisibility(View.GONE);
                        holder.image_view.setVisibility(View.GONE);
                        holder.church.setVisibility(View.GONE);
                        holder.scripturebk.setVisibility(View.GONE);
                        holder.scripturecnt.setVisibility(View.GONE);
                        holder.date.setVisibility(View.GONE);
                        holder.time.setVisibility(View.GONE);
                        holder.videoview.setVisibility(View.GONE);
                        holder.profile.setVisibility(View.GONE);
                        holder.name.setVisibility(View.GONE);
                        holder.like.setVisibility(View.GONE);
                        holder.displayNolikes.setVisibility(View.GONE);
                        holder.comment.setVisibility(View.GONE);
                        holder.displayNocomment.setVisibility(View.GONE);
                        holder.likeds.setVisibility(View.GONE);
                        holder.coms.setVisibility(View.GONE);
                        holder.comss.setVisibility(View.GONE);
                        holder.comsss.setVisibility(View.GONE);
                        holder.comssss.setVisibility(View.GONE);
                        holder.comsssss.setVisibility(View.GONE);
                        holder.comssssss.setVisibility(View.GONE);
                        holder.comsssssss.setVisibility(View.GONE);
                        holder.comssssssss.setVisibility(View.GONE);
                        holder.richLinkView.setVisibility(View.GONE);
                    }
*//*

                }
*/

/*
                else if(model.getPostmode().equals("advertwithtextandimage")&& model.getConfidentiality().equals("public")){
                    holder.setName(model.getName());
                    holder.setChurch(model.getChurch());
                    holder.setDate(model.getDate());
                    holder.setDescription(model.getDescription());
                    holder.image_view.setVisibility(View.VISIBLE);
                    holder.videoview.setVisibility(View.GONE);
                    holder.scripturebk.setVisibility(View.GONE);
                    holder.scripturecnt.setVisibility(View.GONE);
                    holder.richLinkView.setVisibility(View.GONE);
                    holder.setTime(model.getTime());
                    holder.setPostImage(getContext(),model.getPostImage());
                    holder.setProfileImage(getContext(),model.getProfileImage());}
*/

/*
                else if(model.getPostmode().equals("advertwithphotoonly")&& model.getConfidentiality().equals("public")){
                    holder.setName(model.getName());
                    holder.setChurch(model.getChurch());
                    holder.setDate(model.getDate());
                    holder.videoview.setVisibility(View.GONE);
                    holder.scripturebk.setVisibility(View.GONE);
                    holder.scripturecnt.setVisibility(View.GONE);
                    holder.image_view.setVisibility(View.VISIBLE);
                    holder.description.setVisibility(View.GONE);
                    holder.richLinkView.setVisibility(View.GONE);
                    holder.setPostImage(getContext(),model.getPostImage());
                    holder.setTime(model.getTime());
                    holder.setProfileImage(getContext(),model.getProfileImage());}
*/
/*
                else if(model.getPostmode().equals("advertwithtextonly")&& model.getConfidentiality().equals("public")){
                    holder.setName(model.getName());
                    holder.setChurch(model.getChurch());
                    holder.setDate(model.getDate());
                    holder.setDescription(model.getDescription());
                    holder.videoview.setVisibility(View.GONE);
                    holder.richLinkView.setVisibility(View.GONE);
                    holder.scripturebk.setVisibility(View.GONE);
                    holder.scripturecnt.setVisibility(View.GONE);
                    holder.image_view.setVisibility(View.GONE);
                    holder.setTime(model.getTime());
                    holder.setProfileImage(getContext(),model.getProfileImage());}
*/

/*
                else if(model.getPostmode().equals("testimonyphotoandtext")&& model.getConfidentiality().equals("public")){
                    holder.setName(model.getName());
                    holder.setChurch(model.getChurch());
                    holder.setDate(model.getDate());
                    holder.image_view.setVisibility(View.VISIBLE);
                    holder.setDescription(model.getDescription());
                    holder.videoview.setVisibility(View.GONE);
                    holder.scripturebk.setVisibility(View.GONE);
                    holder.scripturecnt.setVisibility(View.GONE);
                    holder.richLinkView.setVisibility(View.GONE);
                    holder.setTime(model.getTime());
                    holder.setPostImage(getContext(),model.getPostImage());
                    holder.setProfileImage(getContext(),model.getProfileImage());}
*/

/*
                else if(model.getPostmode().equals("testimonytext")&& model.getConfidentiality().equals("public")){
                    holder.setName(model.getName());
                    holder.setChurch(model.getChurch());
                    holder.setDate(model.getDate());
                    holder.videoview.setVisibility(View.GONE);
                    holder.scripturebk.setVisibility(View.GONE);
                    holder.scripturecnt.setVisibility(View.GONE);
                    holder.setDescription(model.getDescription());
                    holder.richLinkView.setVisibility(View.GONE);
                    holder.image_view.setVisibility(View.GONE);
                    holder.setTime(model.getTime());
                    holder.setProfileImage(getContext(),model.getProfileImage());}
*/
/*
                else if(model.getPostmode().equals("testimonyphoto")&& model.getConfidentiality().equals("public")){
                    holder.setName(model.getName());
                    holder.image_view.setVisibility(View.VISIBLE);
                    holder.setChurch(model.getChurch());
                    holder.setDate(model.getDate());
                    holder.setDescription(model.getDescription());
                    holder.videoview.setVisibility(View.GONE);
                    holder.richLinkView.setVisibility(View.GONE);
                    holder.scripturebk.setVisibility(View.GONE);
                    holder.scripturecnt.setVisibility(View.GONE);
                    holder.setPostImage(getContext(),model.getPostImage());
                    holder.setTime(model.getTime());
                    holder.setProfileImage(getContext(),model.getProfileImage());}
*/
                else if(model.getPostmode().equals("text")&& model.getConfidentiality().equals("public")){
                    holder.setName(model.getName());
                    holder.type.setText("wrote this message on Faith Ministry's timeline");
                    holder.setChurch(model.getChurch());
                    holder.setDate(model.getDate());
                    holder.setDescription(model.getDescription());
                    holder.setTime(model.getTime());
                    holder.setProfileImage(getContext(),model.getProfileImage());
                    holder.photo.setVisibility(View.GONE);
                    holder.watch_video.setVisibility(View.GONE);
                    holder.view_more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.descriptions.setText(model.getDescription());
                            holder.description.setVisibility(View.GONE);
                            holder.view_more.setVisibility(View.GONE);

                        }
                    });
                }
/*
                else if(model.getPostmode().equals("photowithtext")&& model.getConfidentiality().equals("public")){
                    holder.setName(model.getName());
                    holder.setChurch(model.getChurch());
                    holder.image_view.setVisibility(View.VISIBLE);
                    holder.setDate(model.getDate());
                    holder.setDescription(model.getDescription());
                    holder.videoview.setVisibility(View.GONE);
                    holder.richLinkView.setVisibility(View.GONE);
                    holder.scripturebk.setVisibility(View.GONE);
                    holder.scripturecnt.setVisibility(View.GONE);
                    holder.setPostImage(getContext(),model.getPostImage());
                    holder.setTime(model.getTime());
                    holder.setProfileImage(getContext(),model.getProfileImage());}
*/
/*
                else if(model.getPostmode().equals("video")&& model.getConfidentiality().equals("public")) {
                    holder.setName(model.getName());
                    holder.setChurch(model.getChurch());
                    holder.setDate(model.getDate());
                    holder.setPostVideo(getContext(),model.getPostVideo());
                    holder.videoview.setVisibility(View.VISIBLE);
                    holder.image_view.setVisibility(View.GONE);
                    holder.setDescription(model.getDescription());
                    holder.scripturebk.setVisibility(View.GONE);
                    holder.scripturecnt.setVisibility(View.GONE);
                    holder.richLinkView.setVisibility(View.GONE);
                    holder.setPostImage(getContext(), model.getPostImage());
                    holder.setTime(model.getTime());
                    holder.setProfileImage(getContext(), model.getProfileImage());
                }
*/

/*
                else if(model.getPostmode().equals("scripture")&& model.getConfidentiality().equals("public")){
                    holder.setName(model.getName());
                    holder.setChurch(model.getChurch());
                    holder.setDate(model.getDate());
                    holder.description.setVisibility(View.GONE);
                    holder.videoview.setVisibility(View.GONE);
                    holder.richLinkView.setVisibility(View.GONE);
                    holder.setScriptureContent(model.getScriptureContent());
                    holder.setScriptureBook(model.getScriptureBook());
                    holder.image_view.setVisibility(View.GONE);
                    holder.setTime(model.getTime());
                    holder.setProfileImage(getContext(),model.getProfileImage());}
*/


                else{


                    holder.description.setVisibility(View.GONE);
                    holder.church.setVisibility(View.GONE);
                    holder.photo.setVisibility(View.GONE);
                    holder.view_more.setVisibility(View.GONE);
                    holder.date.setVisibility(View.GONE);
                    holder.time.setVisibility(View.GONE);
                    holder.watch_video.setVisibility(View.GONE);
                    holder.profile.setVisibility(View.GONE);
                    holder.name.setVisibility(View.GONE);
                    holder.type.setVisibility(View.GONE);
                    holder.from.setVisibility(View.GONE);
                    holder.incase.setVisibility(View.GONE);
                    holder.views.setVisibility(View.GONE);
                    holder.lin1.setVisibility(View.GONE);


                }




            }
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View viewsd = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification,parent,false);
                return new ViewHolder(viewsd);
            }
        };
        firebaseRecyclerAdapter.startListening();
        recycler_post.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();

    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageButton like,comment;
        View mView;
        public TextView churchss, designations,names,dates, times, cel,scripturebk,scripturecnt,description,churchs,from,posted,church,date,time,name,displayNocomment,celebrate,descriptions,namd;
        int countLikes;
        String CurrentUserId;
        DatabaseReference likesRef,postphotoref;
        ImageView image_view,more;

        public LinearLayout cardView,lin;
        public CircleImageView profile,prof,profiles;
        public LinearLayout lin1,retweet, eventprof,likeds,com,coms,comss,comsss,comssss,comsssss,comssssss,comsssssss,comssssssss,lindrop;
        RichLinkViewSkype richLinkView;
        SimpleExoPlayer exoPlayer;
        PlayerView videoview;

        public TextView displayNolikes,type,watch_video,view_more,photo,  incase;
        public View views;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            watch_video = mView.findViewById(R.id.watch_video);
            photo = mView.findViewById(R.id.photo);
            view_more = mView.findViewById(R.id.view_more);
            type = mView.findViewById(R.id.type);
            times = mView.findViewById(R.id.times);
            dates = mView.findViewById(R.id.dates);
            designations = mView.findViewById(R.id.designations);
            incase = mView.findViewById(R.id.incase);
            lin = mView.findViewById(R.id.lin);
            views = mView.findViewById(R.id.view);
            cel = mView.findViewById(R.id.cel);
            eventprof = mView.findViewById(R.id.eventprof);
            prof = mView.findViewById(R.id.prof);
            churchs = mView.findViewById(R.id.churchs);
            namd = mView.findViewById(R.id.namd);
            celebrate = mView.findViewById(R.id.celebrate);
            descriptions = mView.findViewById(R.id.descriptions);
            lin1 = mView.findViewById(R.id.lin1);
            coms = mView.findViewById(R.id.coms);
            comss = mView.findViewById(R.id.comss);
            comsss = mView.findViewById(R.id.comsss);
            comssss= mView.findViewById(R.id.comssss);
            retweet= mView.findViewById(R.id.retweet);
            comsssss = mView.findViewById(R.id.comsssss);
            comssssss = mView.findViewById(R.id.comssssss);
            comsssssss = mView.findViewById(R.id.comsssssss);
            comssssssss = mView.findViewById(R.id.comssssssss);
            richLinkView = mView.findViewById(R.id.richLinkView);
            lindrop = mView.findViewById(R.id.lindrop);
            more = mView.findViewById(R.id.more);
            like = mView.findViewById(R.id.like);
            comment = mView.findViewById(R.id.comment);
            displayNocomment = mView.findViewById(R.id.displayNocomment);
            displayNolikes = mView.findViewById(R.id.displayNolikes);
            cardView = mView.findViewById(R.id.card);
            name = mView.findViewById(R.id.name);
            profile = mView.findViewById(R.id.profile);
            date = mView.findViewById(R.id.date);
            time = mView.findViewById(R.id.time);
            church = mView.findViewById(R.id.church);
            from = mView.findViewById(R.id.from);
            posted = mView.findViewById(R.id.posted);
            scripturebk = mView.findViewById(R.id.scripturebk);
            scripturecnt = mView.findViewById(R.id.scripturecnt);
            image_view = mView.findViewById(R.id.image_view);
            description = mView.findViewById(R.id.description);
            descriptions = mView.findViewById(R.id.descriptions);
            likesRef = FirebaseDatabase.getInstance().getReference("Likes");
            postphotoref = FirebaseDatabase.getInstance().getReference().child("Post_photos_public");
            CurrentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            videoview = mView.findViewById(R.id.videoview);

        }

        public void setDesignation(String designation) {
            designations.setText(designation);

        }

        public ImageButton getLike() {
            return like;
        }
        public void setPostedProfile(Context ctx,String postedProfile) {
            Glide.with(ctx).load(postedProfile).into(image_view);
            Glide.with(ctx).load(postedProfile).into(prof);
        }
        public void setPostedname(String postedname) {
            namd.setText(postedname);
        }
        public void setPostedChurch(String postedChurch) {
            churchs.setText(postedChurch);
        }
        public void setName(String name) {
            TextView username = mView.findViewById(R.id.name);
            username.setText(name);

        }
        public void setScriptureBook(String scriptureBook) {
            TextView scripturebook = itemView.findViewById(R.id.scripturebk);
            scripturebook.setText(scriptureBook);
        }

        public void setScriptureContent(String scriptureContent) {
            TextView scripturecontent = itemView.findViewById(R.id.scripturecnt);
            scripturecontent.setText(scriptureContent);
        }
        public void setPostVideo(Context cx, String postVideo) {
            videoview = mView.findViewById(R.id.videoview);
            try {
                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(cx).build();
                TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                exoPlayer = ExoPlayerFactory.newSimpleInstance(cx);
                Uri video = Uri.parse(postVideo);
                DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(cx,"video");
                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                MediaSource mediaSource = new ExtractorMediaSource(video,dataSourceFactory,extractorsFactory,null,null);
                videoview.setPlayer(exoPlayer);
                exoPlayer.prepare(mediaSource);
                exoPlayer.setPlayWhenReady(false);



            }
            catch (Exception e){
                Log.e("ViewHolder","exoplayer error" +e.toString());
            }

        }
        public void setLink(String link) {
            RichLinkViewSkype richLinkView = itemView.findViewById(R.id.richLinkView);
            richLinkView.setLink(link, new ViewListener() {
                @Override
                public void onSuccess(boolean status) {

                }

                @Override
                public void onError(Exception e) {

                }
            });
        }

        public void setCommentStatus(final String PostKey){
            postphotoref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child(PostKey).hasChild(CurrentUserId)){
                        countLikes = (int) snapshot.child(PostKey).child("comments").getChildrenCount();
                        displayNocomment.setText(countLikes + (" Comments"));
                    }
                    else
                    {
                        countLikes = (int) snapshot.child(PostKey).child("comments").getChildrenCount();
                        displayNocomment.setText(countLikes + (" Comments"));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            }) ;
        }
        public void setDescription(String description) {
            TextView describ = mView.findViewById(R.id.description);
            describ.setText(description);

        }
        public void setDate(String date) {
            TextView dat = mView.findViewById(R.id.date);
            dat.setText(date);

        }
        public void setTime(String time) {
            TextView tim = mView.findViewById(R.id.time);
            tim.setText(time);

        }
        public void setChurch(String church) {
            TextView chu = mView.findViewById(R.id.church);
            chu.setText(church);


        }
        public void setPostImage(Context ctx, String postImage) {
            ImageView image = mView.findViewById(R.id.image_view);
            Glide.with(ctx).load(postImage).into(image);

        }
        public void setProfileImage(Context ctxs, String profileImage) {
            CircleImageView images = mView.findViewById(R.id.profile);
            Glide.with(ctxs).load(profileImage).into(images);


        }

        public void setProfile(Context ctxs,String profileImage) {
            CircleImageView images = mView.findViewById(R.id.profile);
            Glide.with(ctxs).load(profileImage).into(images);

        }

    }

    @Override
    public void onStart() {
        super.onStart();
    }
}