package com.glamour.faith;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.glamour.faith.Model.Member;
import com.glamour.faith.Model.PostPhoto;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.ponnamkarthik.richlinkpreview.MetaData;
import io.github.ponnamkarthik.richlinkpreview.ResponseListener;
import io.github.ponnamkarthik.richlinkpreview.RichLinkViewSkype;
import io.github.ponnamkarthik.richlinkpreview.RichPreview;
import io.github.ponnamkarthik.richlinkpreview.ViewListener;

public class OtherProfileActivity extends AppCompatActivity {
    private LinearLayout post_photo,post_text,post_scripture,post_video,post_testimony,postadvert,post_event,post_my_videos;
    private TextView gender,age,mar_status,phone,email,church,designation,name,my;
    private FloatingActionButton floatingActionButton2;
    private DatabaseReference databaseReference,postphotoref,likesRef,reference;
    private FirebaseAuth mAuth;
    private CircleImageView profile;
    private String savecurrentDate, saveCurrentTime, saveRandomName, current_user_id;
    String currentUserId;
    Boolean LikeChecker = false;
    public ImageView image_view;
    private RecyclerView recycler_post ;
    public String churs, names,prof;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);
        profile = findViewById(R.id.profile);
        gender = findViewById(R.id.gender);
        my = findViewById(R.id.my);
       /* age = findViewById(R.id.age);
        mar_status = findViewById(R.id.mar_status);*/
        phone = findViewById(R.id.phone);
        church = findViewById(R.id.church);
        designation = findViewById(R.id.designation);
        name = findViewById(R.id.name);


        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Members");
        reference = FirebaseDatabase.getInstance().getReference("Members").child(currentUserId);
        postphotoref = FirebaseDatabase.getInstance().getReference().child("Post_photos_public");
        likesRef = FirebaseDatabase.getInstance().getReference("Likes");
        recycler_post = findViewById(R.id.recycler_post);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recycler_post.setLayoutManager(linearLayoutManager);
        AdView mAdView = findViewById(R.id.adView);
        String user = getIntent().getExtras().get("visit_user_id").toString();
        com.facebook.ads.AdView adView = new com.facebook.ads.AdView(OtherProfileActivity.this, getString(R.string.fb_placement_banner), com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        LinearLayout bannerContainer = findViewById(R.id.banner_container);
        /// here is am getting the banner view by enabling databinding you can
        /// dobygetting the view like
        //  LinearLayout banner_container= findViewById(R.id.banner_container);
        bannerContainer.addView(adView);
        adView.loadAd(adView.buildLoadAdConfig().withAdListener(new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {


            }

            @Override
            public void onAdLoaded(Ad ad) {


            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {


            }
        }).build());

        adView.loadAd();
        AdView adViews = new AdView(OtherProfileActivity.this);
        adViews.setAdSize(AdSize.BANNER);
        adViews.setAdUnitId(getString(R.string.admob_app_id));
        MobileAds.initialize(OtherProfileActivity.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                churs = snapshot.child("church").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        DisplayUerInfo();

    }

    private void DisplayUerInfo() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String user = getIntent().getExtras().get("visit_user_id").toString();

                    Member member = dataSnapshot.getValue(Member.class);
                    if(member.getUserId().equals(user)){
                        Glide.with(getApplicationContext()).load(member.getProfileImage()).into(profile);
                        name.setText(member.getName());
                        church.setText(member.getChurch());
                        /* email.setText();*/
                        gender.setText(member.getGender());
                  /*      mar_status.setText(member.getStatus());*/
                        phone.setText(member.getPhone());
                        /*age.setText(member.getBirthDate());*/
                        designation.setText(member.getDesignation());
                        my.setText(member.getName());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void DisplayPhotoInPublic() {
        String user = getIntent().getExtras().get("visit_user_id").toString();
        Query postphotore = FirebaseDatabase.getInstance().getReference().child("Post_photos_public").orderByChild("userid").equalTo(user);
        FirebaseRecyclerOptions<PostPhoto> options = new FirebaseRecyclerOptions.Builder<PostPhoto>()
                .setQuery(postphotore,PostPhoto.class)
                .build();
        FirebaseRecyclerAdapter<PostPhoto, ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PostPhoto, ViewHolder>(
                options
        ) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull PostPhoto model) {
                final String PostKey = getRef(position).getKey();
                String user = getIntent().getExtras().get("visit_user_id").toString();
                final MetaData[] data = new MetaData[1];
                if(model.getUserid().equals(user)) {
                    if (model.getPostmode().equals("link")) {
                        holder.setName(model.getName());
                        holder.setChurch(model.getChurch());
                        holder.setDate(model.getDate());
                        holder.description.setVisibility(View.GONE);
                        holder.videoview.setVisibility(View.GONE);
                        holder.scripturebk.setVisibility(View.GONE);
                        holder.scripturecnt.setVisibility(View.GONE);
                        holder.image_view.setVisibility(View.GONE);
                        holder.setTime(model.getTime());
                        holder.setProfileImage(OtherProfileActivity.this, model.getProfileImage());
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

                    else if (model.getPostmode().equals("retweet")) {
                        holder.eventprof.setVisibility(View.VISIBLE);
                        holder.setName(model.getName());
                        holder.setChurch(model.getChurch());
                        holder.setDate(model.getDate());
                        holder.celebrate.setText("Re-tweeted a post posted by");
                        holder.descriptions.setText("Re-tweeted a post posted by");
                        holder.celebrate.setVisibility(View.VISIBLE);
                        holder.cel.setVisibility(View.GONE);
                        holder.setPostedProfile(OtherProfileActivity.this,model.getPostedProfile());
                        holder.setPostedChurch(model.getPostedChurch());
                        holder.setPostedname(model.getPostedname());
                        holder.setTime(model.getTime());
                        holder.setProfileImage(OtherProfileActivity.this, model.getProfileImage());
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
                            holder.richLinkView.setVisibility(View.VISIBLE);
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
                            holder.setPostVideo(OtherProfileActivity.this,model.getPostVideo());
                            holder.videoview.setVisibility(View.VISIBLE);
                            holder.setDescription(model.getDescription());
                            holder.richLinkView.setVisibility(View.GONE);
                        }

                        else if(model.getType().equals("photo")){
                            holder.celebrate.setText("Re-tweeted a photo posted by");
                            holder.scripturebk.setVisibility(View.GONE);
                            holder.image_view.setVisibility(View.VISIBLE);
                            holder.scripturecnt.setVisibility(View.GONE);
                            holder.videoview.setVisibility(View.GONE);
                            holder.setPostImage(OtherProfileActivity.this,model.getPostImage());
                            holder.description.setVisibility(View.GONE);
                            holder.richLinkView.setVisibility(View.GONE);
                        }
                        else if(model.getType().equals("photowithtext")){
                            holder.celebrate.setText("Re-tweeted a this post posted by");
                            holder.image_view.setVisibility(View.VISIBLE);
                            holder.scripturebk.setVisibility(View.GONE);
                            holder.scripturecnt.setVisibility(View.GONE);
                            holder.videoview.setVisibility(View.GONE);
                            holder.setPostImage(OtherProfileActivity.this,model.getPostImage());
                            holder.setDescription(model.getDescription());
                            holder.richLinkView.setVisibility(View.GONE);
                        }
                        else if(model.getType().equals("scripture")){
                            holder.celebrate.setText("Re-tweeted a this post posted by");
                            holder.image_view.setVisibility(View.VISIBLE);
                            holder.setScriptureBook(model.getScriptureBook());
                            holder.setScriptureContent(model.getScriptureContent());
                            holder.videoview.setVisibility(View.GONE);
                            holder.setPostImage(OtherProfileActivity.this,model.getPostImage());
                            holder.description.setVisibility(View.GONE);
                            holder.richLinkView.setVisibility(View.GONE);
                        }


                    }
                    else if(model.getPostmode().equals("events") && model.getConfidentiality().equals("public")){
                        holder.setChurch(model.getName()+ " on behalf of the whole ministry, is welcoming you all to a "+model.getEvent()+" to be held on "+ model.getDate());
                        holder.lin1.setVisibility(View.VISIBLE);
                        holder.comssssss.setVisibility(View.GONE);
                        holder.setTime(model.getTime());
                        holder.setDate(model.getDate());
                        holder.setProfileImage(OtherProfileActivity.this, model.getProfileImage());
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
                        holder.setPostedProfile(OtherProfileActivity.this,model.getPostedProfile());
                        holder.setPostedChurch(model.getPostedChurch());
                        holder.setPostedname(model.getPostedname());
                        holder.setTime(model.getTime());
                        holder.setProfileImage(OtherProfileActivity.this, model.getProfileImage());


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
                        holder.setProfileImage(OtherProfileActivity.this, model.getProfileImage());
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
                            holder.setPostVideo(OtherProfileActivity.this,model.getPostVideo());
                            holder.videoview.setVisibility(View.VISIBLE);
                            holder.image_view.setVisibility(View.GONE);
                            holder.setDescription(model.getDescription());
                            holder.scripturebk.setVisibility(View.GONE);
                            holder.scripturecnt.setVisibility(View.GONE);
                            holder.description.setVisibility(View.GONE);
                            holder.richLinkView.setVisibility(View.GONE);
                            holder.setPostImage(OtherProfileActivity.this,model.getPostImage());
                            holder.setTime(model.getTime());
                            holder.setProfileImage(OtherProfileActivity.this,model.getProfileImage());
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
                            holder.setPostImage(OtherProfileActivity.this,model.getPostImage());
                            holder.setTime(model.getTime());
                            holder.setProfileImage(OtherProfileActivity.this,model.getProfileImage());
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
                            holder.setPostImage(OtherProfileActivity.this,model.getPostImage());
                            holder.setTime(model.getTime());
                            holder.setProfileImage(OtherProfileActivity.this,model.getProfileImage());
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
                            holder.setPostImage(OtherProfileActivity.this,model.getPostImage());
                            holder.setTime(model.getTime());
                            holder.setProfileImage(OtherProfileActivity.this,model.getProfileImage());
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
                            holder.setPostImage(OtherProfileActivity.this,model.getPostImage());
                            holder.setTime(model.getTime());
                            holder.setProfileImage(OtherProfileActivity.this,model.getProfileImage());
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
                            holder.setProfileImage(OtherProfileActivity.this,model.getProfileImage());
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
                            holder.setPostImage(OtherProfileActivity.this,model.getPostImage());
                            holder.setTime(model.getTime());
                            holder.setProfileImage(OtherProfileActivity.this,model.getProfileImage());
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
                    else if(model.getPostmode().equals("text")&& model.getConfidentiality().equals("private")){
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
                            holder.setProfileImage(OtherProfileActivity.this,model.getProfileImage());
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
                    else if(model.getPostmode().equals("prayer")&& model.getConfidentiality().equals("public")){
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
                        holder.setProfileImage(OtherProfileActivity.this,model.getProfileImage());}

                    else if(model.getPostmode().equals("prayer")&& model.getConfidentiality().equals("private")){
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
                            holder.setProfileImage(OtherProfileActivity.this,model.getProfileImage());
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
                            holder.setPostImage(OtherProfileActivity.this,model.getPostImage());
                            holder.setTime(model.getTime());
                            holder.setProfileImage(OtherProfileActivity.this,model.getProfileImage());
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
                            holder.setProfileImage(OtherProfileActivity.this,model.getProfileImage());}


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
                        holder.setPostImage(OtherProfileActivity.this,model.getPostImage());
                        holder.setProfileImage(OtherProfileActivity.this,model.getProfileImage());}

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
                        holder.setPostImage(OtherProfileActivity.this,model.getPostImage());
                        holder.setTime(model.getTime());
                        holder.setProfileImage(OtherProfileActivity.this,model.getProfileImage());}
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
                        holder.setProfileImage(OtherProfileActivity.this,model.getProfileImage());}

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
                        holder.setPostImage(OtherProfileActivity.this,model.getPostImage());
                        holder.setProfileImage(OtherProfileActivity.this,model.getProfileImage());}

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
                        holder.setProfileImage(OtherProfileActivity.this,model.getProfileImage());}
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
                        holder.setPostImage(OtherProfileActivity.this,model.getPostImage());
                        holder.setTime(model.getTime());
                        holder.setProfileImage(OtherProfileActivity.this,model.getProfileImage());}
                    else if(model.getPostmode().equals("text")&& model.getConfidentiality().equals("public")){
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
                        holder.setProfileImage(OtherProfileActivity.this,model.getProfileImage());}
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
                        holder.setPostImage(OtherProfileActivity.this,model.getPostImage());
                        holder.setTime(model.getTime());
                        holder.setProfileImage(OtherProfileActivity.this,model.getProfileImage());}
                    else if(model.getPostmode().equals("video")&& model.getConfidentiality().equals("public")) {
                        holder.setName(model.getName());
                        holder.setChurch(model.getChurch());
                        holder.setDate(model.getDate());
                        holder.setPostVideo(OtherProfileActivity.this,model.getPostVideo());
                        holder.videoview.setVisibility(View.VISIBLE);
                        holder.image_view.setVisibility(View.GONE);
                        holder.setDescription(model.getDescription());
                        holder.scripturebk.setVisibility(View.GONE);
                        holder.scripturecnt.setVisibility(View.GONE);
                        holder.richLinkView.setVisibility(View.GONE);
                        holder.setPostImage(OtherProfileActivity.this, model.getPostImage());
                        holder.setTime(model.getTime());
                        holder.setProfileImage(OtherProfileActivity.this, model.getProfileImage());
                    }

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
                        holder.setProfileImage(OtherProfileActivity.this,model.getProfileImage());}

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
                    holder.setLikeButtonStatus(PostKey);
                    holder.setCommentStatus(PostKey);
                    holder.scripturecnt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent clickPost = new Intent(OtherProfileActivity.this, CommentsActivity.class);
                            clickPost.putExtra("PostKey",PostKey);
                            clickPost.putExtra("DatabaseUserId",model.getUserid());
                            clickPost.putExtra("name",model.getName());
                            clickPost.putExtra("profile",model.getProfileImage());
                            clickPost.putExtra("postmode",model.getPostmode());
                            clickPost.putExtra("postmode",model.getPostmode());
                            clickPost.putExtra("postphoto",model.getPostImage());
                            if(model.getPostmode().equals("text")){
                                clickPost.putExtra("description",model.getDescription());
                            }
                            if(model.getPostmode().equals("link")){
                                clickPost.putExtra("link",model.getLink());
                            }
                            if(model.getPostmode().equals("scripture")){
                                clickPost.putExtra("scriptbk",model.getScriptureBook());
                                clickPost.putExtra("scriptcnt",model.getScriptureContent());
                            }
                            if(model.getPostmode().equals("advertwithtextandimage")){
                                clickPost.putExtra("description",model.getDescription());
                                clickPost.putExtra("postImage",model.getPostImage());
                            }
                            if(model.getPostmode().equals("advertwithphotoonly")){
                                clickPost.putExtra("postImage",model.getPostImage());
                            }
                            if(model.getPostmode().equals("advertwithtextonly")){
                                clickPost.putExtra("description",model.getDescription());
                            }
                            if(model.getPostmode().equals("photowithtext")){
                                clickPost.putExtra("description",model.getDescription());
                                clickPost.putExtra("postImage",model.getPostImage());
                            }
                            if(model.getPostmode().equals("text")){
                                clickPost.putExtra("testimonytext",model.getDescription());
                            }
                            if(model.getPostmode().equals("testimonyphoto")){
                                clickPost.putExtra("postImage",model.getPostImage());
                            }
                            if(model.getPostmode().equals("testimonyphotoandtext")){
                                clickPost.putExtra("description",model.getDescription());
                                clickPost.putExtra("postImage",model.getPostImage());
                            }
                            startActivity(clickPost);
                        }
                    });



                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent clickPost = new Intent(OtherProfileActivity.this, CommentsActivity.class);
                            startActivity(clickPost);
                        }
                    });

                    holder.image_view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent clickPost = new Intent(OtherProfileActivity.this, CommentsActivity.class);
                            clickPost.putExtra("PostKey",PostKey);
                            clickPost.putExtra("DatabaseUserId",model.getUserid());
                            clickPost.putExtra("name",model.getName());
                            clickPost.putExtra("profile",model.getProfileImage());
                            clickPost.putExtra("postmode",model.getPostmode());
                            clickPost.putExtra("postmode",model.getPostmode());
                            clickPost.putExtra("postphoto",model.getPostImage());
                            if(model.getPostmode().equals("text")){
                                clickPost.putExtra("description",model.getDescription());
                            }
                            if(model.getPostmode().equals("link")){
                                clickPost.putExtra("link",model.getLink());
                            }
                            if(model.getPostmode().equals("scripture")){
                                clickPost.putExtra("scriptbk",model.getScriptureBook());
                                clickPost.putExtra("scriptcnt",model.getScriptureContent());
                            }
                            if(model.getPostmode().equals("advertwithtextandimage")){
                                clickPost.putExtra("description",model.getDescription());
                                clickPost.putExtra("postImage",model.getPostImage());
                            }
                            if(model.getPostmode().equals("advertwithphotoonly")){
                                clickPost.putExtra("postImage",model.getPostImage());
                            }
                            if(model.getPostmode().equals("advertwithtextonly")){
                                clickPost.putExtra("description",model.getDescription());
                            }
                            if(model.getPostmode().equals("photowithtext")){
                                clickPost.putExtra("description",model.getDescription());
                                clickPost.putExtra("postImage",model.getPostImage());
                            }
                            if(model.getPostmode().equals("text")){
                                clickPost.putExtra("testimonytext",model.getDescription());
                            }
                            if(model.getPostmode().equals("testimonyphoto")){
                                clickPost.putExtra("postImage",model.getPostImage());
                            }
                            if(model.getPostmode().equals("testimonyphotoandtext")){
                                clickPost.putExtra("description",model.getDescription());
                                clickPost.putExtra("postImage",model.getPostImage());
                            }
                            startActivity(clickPost);
                        }
                    });
                    holder.comment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent commentIntent = new Intent(OtherProfileActivity.this, CommentsActivity.class);
                            commentIntent.putExtra("PostKey",PostKey);


                            String post = PostKey;
                            commentIntent.putExtra("postmode",model.getPostmode());
                            if(model.getPostmode().equals("text")){
                                commentIntent.putExtra("description",model.getDescription());
                            }
                            if(model.getPostmode().equals("video")){
                                commentIntent.putExtra("description",model.getDescription());
                                commentIntent.putExtra("vid",model.getPostVideo());
                            }
                            if(model.getPostmode().equals("scripture")){
                                commentIntent.putExtra("scriptbk",model.getScriptureBook());
                                commentIntent.putExtra("scriptcnt",model.getScriptureContent());
                            }
                            if(model.getPostmode().equals("advertwithtextandimage")){
                                commentIntent.putExtra("description",model.getDescription());
                                commentIntent.putExtra("postImage",model.getPostImage());
                            }
                            if(model.getPostmode().equals("advertwithphotoonly")){
                                commentIntent.putExtra("postImage",model.getPostImage());
                            }
                            if(model.getPostmode().equals("advertwithtextonly")){
                                commentIntent.putExtra("description",model.getDescription());
                            }
                            if(model.getPostmode().equals("photowithtext")){
                                commentIntent.putExtra("description",model.getDescription());
                                commentIntent.putExtra("postImage",model.getPostImage());
                            }
                            if(model.getPostmode().equals("text")){
                                commentIntent.putExtra("testimonytext",model.getDescription());
                            }
                            if(model.getPostmode().equals("testimonyphoto")){
                                commentIntent.putExtra("postImage",model.getPostImage());
                            }
                            if(model.getPostmode().equals("testimonyphotoandtext")){
                                commentIntent.putExtra("description",model.getDescription());
                                commentIntent.putExtra("postImage",model.getPostImage());
                            }
                            startActivity(commentIntent);

                        }
                    });


                    holder.retweet.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(OtherProfileActivity.this);

                            builder.setTitle("Are you sure you want to Re-tweet this Post posted by "+ model.getName()+"?");

                            builder.setPositiveButton("Re- tweet", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Members");
                                    DatabaseReference postRef_public = FirebaseDatabase.getInstance().getReference().child("Post_photos_public");
                                    Calendar callFordate = Calendar.getInstance();
                                    SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
                                    savecurrentDate = currentDate.format(callFordate.getTime());


                                    Calendar callForTIME = Calendar.getInstance();
                                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
                                    saveCurrentTime = currentTime.format(callForTIME.getTime());

                                    userRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.exists()){
                                                String username = snapshot.child("name").getValue().toString().trim();
                                                String profile = snapshot.child("profileImage").getValue().toString().trim();
                                                String church = snapshot.child("church").getValue().toString().trim();
                                                String conf = model.getConfidentiality();
                                                String postedprofile = model.getProfileImage();
                                                String postedchurch = model.getChurch();
                                                String postedname = model.getName();

                                                String desc = model.getDescription();
                                                String imag = model.getPostImage();
                                                String video = model.getPostVideo();
                                                String urlsd = model.getLink();
                                                String scrip = model.getScriptureBook();
                                                String script = model.getScriptureContent();

                                                String count = savecurrentDate + saveCurrentTime;
                                                String saveRandomName = currentUserId + savecurrentDate + saveCurrentTime;
                                                HashMap hashMap = new HashMap();
                                                hashMap.put("userid",currentUserId);
                                                hashMap.put("name",username);
                                                hashMap.put("profileImage",profile);
                                                hashMap.put("date",savecurrentDate);
                                                hashMap.put("time",saveCurrentTime);
                                                hashMap.put("church",church);
                                                hashMap.put("Counter",count);
                                                hashMap.put("postmode","retweet");
                                                if(model.getPostmode().equals("text")){
                                                    hashMap.put("description",desc);
                                                    hashMap.put("postImage","default");
                                                    hashMap.put("postVideo","default");
                                                    hashMap.put("link","default");
                                                    hashMap.put("type","text");
                                                    hashMap.put("scriptureContent","default");
                                                    hashMap.put("scriptureBook","default");
                                                }
                                                else if(model.getPostmode().equals("advertwithtextonly")){
                                                    hashMap.put("description",desc);
                                                    hashMap.put("postImage","default");
                                                    hashMap.put("postVideo","default");
                                                    hashMap.put("link","default");
                                                    hashMap.put("type","text");
                                                    hashMap.put("scriptureContent","default");
                                                    hashMap.put("scriptureBook","default");
                                                }
                                                else if(model.getPostmode().equals("testimonytext")){
                                                    hashMap.put("description",desc);
                                                    hashMap.put("postImage","default");
                                                    hashMap.put("postVideo","default");
                                                    hashMap.put("link","default");
                                                    hashMap.put("type","text");
                                                    hashMap.put("scriptureContent","default");
                                                    hashMap.put("scriptureBook","default");
                                                }


                                                else if(model.getPostmode().equals("link")){
                                                    hashMap.put("description","default");
                                                    hashMap.put("postImage","default");
                                                    hashMap.put("postVideo","default");
                                                    hashMap.put("link",model.getLink());
                                                    hashMap.put("type","link");
                                                    hashMap.put("scriptureContent","default");
                                                    hashMap.put("scriptureBook","default");
                                                }
                                                else if(model.getPostmode().equals("youtubelink")){
                                                    hashMap.put("description","default");
                                                    hashMap.put("postImage","default");
                                                    hashMap.put("postVideo","default");
                                                    hashMap.put("link",model.getLink());
                                                    hashMap.put("type","link");
                                                    hashMap.put("scriptureContent","default");
                                                    hashMap.put("scriptureBook","default");
                                                }
                                                else if(model.getPostmode().equals("photowithtext")){
                                                    hashMap.put("description",desc);
                                                    hashMap.put("postImage",model.getPostImage());
                                                    hashMap.put("postVideo","default");
                                                    hashMap.put("link","default");
                                                    hashMap.put("type","photowithtext");
                                                    hashMap.put("scriptureContent","default");
                                                    hashMap.put("scriptureBook","default");
                                                }
                                                else if(model.getPostmode().equals("advertwithimageandtext")){
                                                    hashMap.put("description",desc);
                                                    hashMap.put("postImage",model.getPostImage());
                                                    hashMap.put("postVideo","default");
                                                    hashMap.put("link","default");
                                                    hashMap.put("type","photowithtext");
                                                    hashMap.put("scriptureContent","default");
                                                    hashMap.put("scriptureBook","default");
                                                }
                                                else if(model.getPostmode().equals("testimonyphotoandtext")){
                                                    hashMap.put("description",desc);
                                                    hashMap.put("postImage",model.getPostImage());
                                                    hashMap.put("postVideo","default");
                                                    hashMap.put("link","default");
                                                    hashMap.put("type","photowithtext");
                                                    hashMap.put("scriptureContent","default");
                                                    hashMap.put("scriptureBook","default");
                                                }

                                                else if(model.getPostmode().equals("advertwithphotoonly")){
                                                    hashMap.put("description","default");
                                                    hashMap.put("postImage",model.getPostImage());
                                                    hashMap.put("postVideo","default");
                                                    hashMap.put("link","default");
                                                    hashMap.put("type","photo");
                                                    hashMap.put("scriptureContent","default");
                                                    hashMap.put("scriptureBook","default");
                                                }
                                                else if(model.getPostmode().equals("testimonyphoto")){
                                                    hashMap.put("description","default");
                                                    hashMap.put("postImage",model.getPostImage());
                                                    hashMap.put("postVideo","default");
                                                    hashMap.put("link","default");
                                                    hashMap.put("type","photo");
                                                    hashMap.put("scriptureContent","default");
                                                    hashMap.put("scriptureBook","default");
                                                }
                                                else if(model.getPostmode().equals("video")){
                                                    hashMap.put("description",model.getDescription());
                                                    hashMap.put("postImage","default");
                                                    hashMap.put("postVideo",model.getPostVideo());
                                                    hashMap.put("link","default");
                                                    hashMap.put("type","video");
                                                    hashMap.put("scriptureContent","default");
                                                    hashMap.put("scriptureBook","default");
                                                }
                                                else if(model.getPostmode().equals("scripture")){
                                                    hashMap.put("description","default");
                                                    hashMap.put("postImage","default");
                                                    hashMap.put("postVideo","default");
                                                    hashMap.put("link","default");
                                                    hashMap.put("type","scripture");
                                                    hashMap.put("scriptureContent",model.getScriptureContent());
                                                    hashMap.put("scriptureBook",model.getScriptureBook());
                                                }

                                                hashMap.put("confidentiality",conf);
                                                hashMap.put("postedChurch",postedchurch);
                                                hashMap.put("postedname",postedname);
                                                hashMap.put("postedProfile",postedprofile);
                                                hashMap.put("search",username.toLowerCase());




                                                postRef_public.child(saveRandomName).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                                    @Override
                                                    public void onComplete(@NonNull Task task) {
                                                        if(task.isSuccessful()){
                                                            Toast.makeText(OtherProfileActivity.this, "Post Re-tweeted Successfully", Toast.LENGTH_SHORT).show();
                                                        }
                                                        else
                                                        {

                                                            Toast.makeText(OtherProfileActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();

                                                        }
                                                    }
                                                });
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }
                            });
                            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                            Dialog dialog = builder.create();
                            dialog.show();
                        }
                    });
                    holder.like.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            LikeChecker =true;
                            likesRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(LikeChecker.equals(true))
                                    {
                                        if(snapshot.child(PostKey).hasChild(currentUserId)){
                                            likesRef.child(PostKey).child(currentUserId).removeValue();
                                            LikeChecker = false;
                                        }else
                                        {
                                            likesRef.child(PostKey).child(currentUserId).setValue(true);
                                            LikeChecker=false;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }

                            });

                        }

                    });
                }


            }
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View viewsd = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo,parent,false);
                return new ViewHolder(viewsd);
            }
        };
        firebaseRecyclerAdapter.startListening();
        recycler_post.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();
    }
    public static class ProfileViewHolder extends RecyclerView.ViewHolder{
        View view;
        private final TextView gender;
        private final TextView phone;
        private TextView email;
        private final TextView church;
        private final TextView designation;
        private final TextView name;
        private CircleImageView profile;
        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            profile = view.findViewById(R.id.profile);
            gender = view.findViewById(R.id.gender);
            /*age = view.findViewById(R.id.age);
            mar_status = view.findViewById(R.id.mar_status);*/
            phone = view.findViewById(R.id.phone);
            church = view.findViewById(R.id.church);
            designation = view.findViewById(R.id.designation);
            name = view.findViewById(R.id.name);
        }
        public void setProfileImage(Context cxt,String profileImage) {
            profile = view.findViewById(R.id.profile);
            Glide.with(cxt).load(profileImage).into(profile);
        }
        public void setDesignation(String designation) {
            TextView desig = view.findViewById(R.id.designation);
            desig.setText(designation);
        }
        public void setGender(String gender) {
            TextView gend = view.findViewById(R.id.gender);
            gend.setText(gender);
        }
        public void setMaritalStatus(String maritalStatus) {
            /*TextView mar_status = view.findViewById(R.id.mar_status);
            mar_status.setText(maritalStatus);*/
        }
        public void setPhone(String phone) {
            TextView phon = view.findViewById(R.id.phone);
            phon.setText(phone);
        }
        public void setName(String name) {
            TextView nam = view.findViewById(R.id.name);
            nam.setText(name);
        }




    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout like,comment;
        View mView;
        public TextView churchss, designations,names,dates, times, cel,scripturebk,scripturecnt,description,churchs,from,posted,church,date,time,name,displayNocomment,celebrate,descriptions,namd;
        int countLikes;
        String CurrentUserId;
        DatabaseReference likesRef,postphotoref;
        ImageView image_view,more;
        ImageButton liked;

        public LinearLayout cardView;
        public CircleImageView profile,prof,profiles;
        public LinearLayout lin1,retweet, eventprof,likeds,com,coms,comss,comsss,comssss,comsssss,comssssss,comsssssss,comssssssss,lindrop;
        RichLinkViewSkype richLinkView;
        SimpleExoPlayer exoPlayer;
        PlayerView videoview;

        public TextView displayNolikes;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            times = mView.findViewById(R.id.times);
            dates = mView.findViewById(R.id.dates);
            designations = mView.findViewById(R.id.designations);
            retweet = mView.findViewById(R.id.retweet);
            lin1 = mView.findViewById(R.id.lin1);
            liked = mView.findViewById(R.id.liked);
            cel = mView.findViewById(R.id.cel);
            eventprof = mView.findViewById(R.id.eventprof);
            prof = mView.findViewById(R.id.prof);
            churchs = mView.findViewById(R.id.churchs);
            namd = mView.findViewById(R.id.namd);
            celebrate = mView.findViewById(R.id.celebrate);
            descriptions = mView.findViewById(R.id.descriptions);

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
            likesRef = FirebaseDatabase.getInstance().getReference("Likes");
            postphotoref = FirebaseDatabase.getInstance().getReference().child("Post_photos_public");
            CurrentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            videoview = mView.findViewById(R.id.videoview);

        }

        public void setDesignation(String designation) {
            designations.setText(designation);

        }

        public LinearLayout getLike() {
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
            TextView namess = mView.findViewById(R.id.names);
            username.setText(name);
            namess.setText(name);
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
        public void setLikeButtonStatus(final String PostKey){
            likesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child(PostKey).hasChild(CurrentUserId)){
                        countLikes = (int) snapshot.child(PostKey).getChildrenCount();
                        liked.setImageResource(R.drawable.liked);
                        /*    displayNolikes.setTextColor(Color.parseColor("#FF0000"));*/
                        displayNolikes.setText(countLikes + (" Likes"));
                    }
                    else
                    {
                        countLikes = (int) snapshot.child(PostKey).getChildrenCount();
                        liked.setImageResource(R.drawable.like);
                        displayNolikes.setText(countLikes + (" Likes"));
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
            dates.setText(date);

        }
        public void setTime(String time) {
            TextView tim = mView.findViewById(R.id.time);
            tim.setText(time);
            times.setText(time);

        }
        public void setChurch(String church) {
            TextView chu = mView.findViewById(R.id.church);
            churchss = mView.findViewById(R.id.churchss);
            chu.setText("@"+church);
            churchss.setText(church);

        }
        public void setPostImage(Context ctx, String postImage) {
            ImageView image = mView.findViewById(R.id.image_view);
            Glide.with(ctx).load(postImage).into(image);

        }
        public void setProfileImage(Context ctxs,String profileImage) {
            CircleImageView images = mView.findViewById(R.id.profile);
            profiles = mView.findViewById(R.id.profiles);
            Glide.with(ctxs).load(profileImage).into(images);
            Glide.with(ctxs).load(profileImage).into(profiles);

        }

        public void setProfile(Context ctxs,String profileImage) {
            CircleImageView images = mView.findViewById(R.id.profile);
            Glide.with(ctxs).load(profileImage).into(images);

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        DisplayPhotoInPublic();
    }
}
