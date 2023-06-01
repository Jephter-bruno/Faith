package com.glamour.faith;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.EditText;
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
import com.glamour.faith.Model.Comments;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.ponnamkarthik.richlinkpreview.RichLinkViewSkype;
import io.github.ponnamkarthik.richlinkpreview.ViewListener;

public class CommentsActivity extends AppCompatActivity {
    private RecyclerView recyclerView_comments;
    private ImageButton post_comments;
    private EditText editText_comment_input;
    private DatabaseReference postref,commentRef,postphotoref, databaseReference,likesRef;
    private String post_key;
    Comments comments;
    private TextView names, churchss, dates, timess,description,scripturecnt,scripturebk,celebrate, celname, celchurch;
    private ImageView image_view;
    private RichLinkViewSkype richLinkView;
    private ImageButton addt;
    Boolean LikeChecker = false;
    SimpleExoPlayer exoPlayer;
    private CircleImageView celprofile;
    private LinearLayout lin;

    PlayerView videoview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        post_key = getIntent().getExtras().getString("PostKey");
        addt = findViewById(R.id.addt);
        videoview = findViewById(R.id.videoview);

        comments = new Comments();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        likesRef = FirebaseDatabase.getInstance().getReference("ReplyLikes");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Members");
        commentRef = FirebaseDatabase.getInstance().getReference().child("Post_photos_public").child(post_key);

        postref = FirebaseDatabase.getInstance().getReference().child("Post_photos_public").child(post_key).child("comments");
        com.facebook.ads.AdView adView = new com.facebook.ads.AdView(CommentsActivity.this, getString(R.string.fb_placement_banner), com.facebook.ads.AdSize.BANNER_HEIGHT_50);
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

        recyclerView_comments = findViewById(R.id.recyclerview_comments);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setStackFromEnd(false);
        recyclerView_comments.setLayoutManager(linearLayoutManager);
        lin = findViewById(R.id.lin);
        celprofile = findViewById(R.id.celprofile);
        celebrate = findViewById(R.id.celebrate);
        celname = findViewById(R.id.celname);
        celchurch = findViewById(R.id.celchurch);
        celprofile = findViewById(R.id.celprofile);

        richLinkView = findViewById(R.id.richLinkView);
        scripturecnt = findViewById(R.id.scripturecnt);
        description = findViewById(R.id.description);
        post_comments = findViewById(R.id.comment_btn_post);
        editText_comment_input = findViewById(R.id.comment_et);
        names = findViewById(R.id.namess);
        churchss = findViewById(R.id.churchsss);
        dates = findViewById(R.id.datess);
        timess = findViewById(R.id.timess);
        scripturebk = findViewById(R.id.scripturebk);
        image_view = findViewById(R.id.image_view);
       CircleImageView profilesss = findViewById(R.id.profilesss);
        AdView mAdView = findViewById(R.id.adView);
        AdView adViews = new AdView(CommentsActivity.this);
        adViews.setAdSize(AdSize.BANNER);
        adViews.setAdUnitId(getString(R.string.admob_app_id));
        MobileAds.initialize(CommentsActivity.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String mode = getIntent().getExtras().get("postmode").toString();
                    String usename = snapshot.child("name").getValue().toString();
                    String profils = snapshot.child("profileImage").getValue().toString();
                    String churcss = snapshot.child("church").getValue().toString();
                    String datse = snapshot.child("date").getValue().toString();
                    String timese = snapshot.child("time").getValue().toString();


                    String DatabaseUserId = snapshot.child("userid").getValue().toString();
                    names.setText("@"+usename);
                    Glide.with(getApplicationContext()).load(profils).into(profilesss);
                    churchss.setText(churcss);
                    dates.setText(datse);
                    timess.setText(timese);
                    if(mode.equals("advertwithtextonly")){
                        String descriptionsse = snapshot.child("description").getValue().toString();
                        scripturecnt.setVisibility(View.GONE);
                        scripturebk.setVisibility(View.GONE);
                        image_view.setVisibility(View.GONE);
                        description.setText(descriptionsse);
                    }
                    else if (mode.equals("retweet")){
                        celebrate.setVisibility(View.VISIBLE);
                        celebrate.setText("Re-tweeted this post");
                        lin.setVisibility(View.VISIBLE);
                        String postedprofil = snapshot.child("postedProfile").getValue().toString();
                        String postedchu = snapshot.child("postedChurch").getValue().toString();
                        String postednam = snapshot.child("postedname").getValue().toString();
                        String typ = snapshot.child("type").getValue().toString();
                        celchurch.setText(postedchu);
                        celname.setText(postednam);
                        Glide.with(getApplicationContext()).load(postedprofil).into(celprofile);
                        if(typ.equals("text")){
                            String descriptionsse = snapshot.child("description").getValue().toString();
                            scripturecnt.setVisibility(View.GONE);
                            scripturebk.setVisibility(View.GONE);
                            image_view.setVisibility(View.GONE);
                            description.setText(descriptionsse);

                        }
                        else if(typ.equals("photo")){
                            scripturebk.setVisibility(View.GONE);
                            scripturecnt.setVisibility(View.GONE);
                            String postimages = snapshot.child("postImage").getValue().toString();

                            description.setVisibility(View.GONE);
                            Glide.with(getApplicationContext()).load(postimages).into(image_view);
                        }
                        else if(typ.equals("photowithtext")){
                            String descriptionsse = snapshot.child("description").getValue().toString();
                            scripturebk.setVisibility(View.GONE);
                            scripturecnt.setVisibility(View.GONE);
                            String postimages = snapshot.child("postImage").getValue().toString();

                            description.setText(descriptionsse);
                            Glide.with(getApplicationContext()).load(postimages).into(image_view);
                        }
                        else if(typ.equals("scripture")){
                            String scripture = snapshot.child("scriptureContent").getValue().toString();
                            String scripturebook = snapshot.child("scriptureBook").getValue().toString();
                            scripturecnt.setText(scripture);
                            scripturebk.setText(scripturebook);
                            image_view.setVisibility(View.GONE);
                            description.setVisibility(View.GONE);
                        }
                        else if(typ.equals("link")){
                            String link = snapshot.child("link").getValue().toString();
                            scripturecnt.setVisibility(View.GONE);
                            scripturebk.setVisibility(View.GONE);
                            image_view.setVisibility(View.GONE);
                            description.setVisibility(View.GONE);
                            richLinkView.setLink(link, new ViewListener() {
                                @Override
                                public void onSuccess(boolean status) {

                                }

                                @Override
                                public void onError(Exception e) {

                                }
                            });

                        }

                        else if(typ.equals("video")){

                            String postimages = snapshot.child("postVideo").getValue().toString();
                            String des = snapshot.child("description").getValue().toString();

                            try {
                                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(CommentsActivity.this).build();
                                TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                                exoPlayer = ExoPlayerFactory.newSimpleInstance(CommentsActivity.this);
                                Uri video = Uri.parse(postimages);
                                DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(CommentsActivity.this,"video");
                                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                                MediaSource mediaSource = new ExtractorMediaSource(video,dataSourceFactory,extractorsFactory,null,null);
                                videoview.setPlayer(exoPlayer);
                                exoPlayer.prepare(mediaSource);
                                exoPlayer.setPlayWhenReady(false);


                            }
                            catch (Exception e){
                                Log.e("ViewHolder","exoplayer error" +e.toString());
                            }


                            videoview.setVisibility(View.VISIBLE);
                            scripturebk.setVisibility(View.GONE);
                            scripturecnt.setVisibility(View.GONE);
                            description.setText(des);
                            image_view.setVisibility(View.GONE);

                        }
                    }
                    else if(mode.equals("celebrate")){
                        String postedprofil = snapshot.child("postedProfile").getValue().toString();
                        String postedchu = snapshot.child("postedChurch").getValue().toString();
                        String postednam = snapshot.child("postedname").getValue().toString();
                        lin.setVisibility(View.VISIBLE);
                        celebrate.setVisibility(View.VISIBLE);
                        scripturecnt.setVisibility(View.GONE);
                        scripturebk.setVisibility(View.GONE);
                        description.setVisibility(View.GONE);
                        celchurch.setText(postedchu);
                        celname.setText(postednam);
                        Glide.with(getApplicationContext()).load(postedprofil).into(image_view);
                        Glide.with(getApplicationContext()).load(postedprofil).into(celprofile);

                    }
                    else if(mode.equals("youtubelink")){
                        String link = snapshot.child("link").getValue().toString();
                        scripturecnt.setVisibility(View.GONE);
                        scripturebk.setVisibility(View.GONE);
                        image_view.setVisibility(View.GONE);
                        description.setVisibility(View.GONE);
                        richLinkView.setLink(link, new ViewListener() {
                            @Override
                            public void onSuccess(boolean status) {

                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });

                    }

                    else if(mode.equals("link")){
                        String link = snapshot.child("link").getValue().toString();
                        scripturecnt.setVisibility(View.GONE);
                        scripturebk.setVisibility(View.GONE);
                        image_view.setVisibility(View.GONE);
                        description.setVisibility(View.GONE);
                        richLinkView.setLink(link, new ViewListener() {
                            @Override
                            public void onSuccess(boolean status) {

                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });

                    }
                    else if(mode.equals("text")){
                        String descriptionsse = snapshot.child("description").getValue().toString();
                        scripturecnt.setVisibility(View.GONE);
                        scripturebk.setVisibility(View.GONE);
                        image_view.setVisibility(View.GONE);
                        description.setText(descriptionsse);
                    }
                   else if(mode.equals("testimonytext")){
                        String descriptionsse = snapshot.child("description").getValue().toString();
                        scripturecnt.setVisibility(View.GONE);
                        scripturebk.setVisibility(View.GONE);
                        image_view.setVisibility(View.GONE);
                        description.setText(descriptionsse);
                    }

                    else if(mode.equals("scripture")){
                        String scripture = snapshot.child("scriptureContent").getValue().toString();
                        String scripturebook = snapshot.child("scriptureBook").getValue().toString();
                        scripturecnt.setText(scripture);
                        scripturebk.setText(scripturebook);
                        image_view.setVisibility(View.GONE);
                        description.setVisibility(View.GONE);
                    }

                    else if(mode.equals("advertwithtextandimage")){
                        scripturebk.setVisibility(View.GONE);
                        scripturecnt.setVisibility(View.GONE);
                        String postimage = getIntent().getExtras().get("postImage").toString();
                        String des = getIntent().getExtras().get("description").toString();

                        description.setText(des);
                      Glide.with(getApplicationContext()).load(postimage).into(image_view);
                    }
                   else if(mode.equals("photowithtext")){
                        scripturebk.setVisibility(View.GONE);
                        scripturecnt.setVisibility(View.GONE);
                        String postimage = getIntent().getExtras().get("postImage").toString();
                        String des = getIntent().getExtras().get("description").toString();
                        description.setText(des);
                        Glide.with(getApplicationContext()).load(postimage).into(image_view);
                    }
                    else if(mode.equals("postVideo")){
                        String postimages = getIntent().getExtras().get("video").toString();
                        String des = getIntent().getExtras().get("description").toString();

                            try {
                                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(CommentsActivity.this).build();
                                TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                                exoPlayer = ExoPlayerFactory.newSimpleInstance(CommentsActivity.this);
                                Uri video = Uri.parse(postimages);
                                DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(CommentsActivity.this,"video");
                                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                                MediaSource mediaSource = new ExtractorMediaSource(video,dataSourceFactory,extractorsFactory,null,null);
                                videoview.setPlayer(exoPlayer);
                                exoPlayer.prepare(mediaSource);
                                exoPlayer.setPlayWhenReady(false);


                            }
                            catch (Exception e){
                                Log.e("ViewHolder","exoplayer error" +e.toString());
                            }


                       videoview.setVisibility(View.VISIBLE);
                        scripturebk.setVisibility(View.GONE);
                        scripturecnt.setVisibility(View.GONE);
                        description.setText(des);
                        image_view.setVisibility(View.GONE);

                    }else if(mode.equals("celebrate")){

                    }
                   else if(mode.equals("advertwithphotoonly")){
                        scripturebk.setVisibility(View.GONE);
                        scripturecnt.setVisibility(View.GONE);
                        String postimages = getIntent().getExtras().get("postImage").toString();

                        description.setVisibility(View.GONE);
                       Glide.with(getApplicationContext()).load(postimages).into(image_view);
                    }
                   else if(mode.equals("testimonyphoto")){
                        scripturebk.setVisibility(View.GONE);
                        scripturecnt.setVisibility(View.GONE);
                        String postimages = getIntent().getExtras().get("postImage").toString();

                        description.setVisibility(View.GONE);
                        Glide.with(getApplicationContext()).load(postimages).into(image_view);

                    }
                    else if(mode.equals("testimonyphotoandtext")){
                        scripturebk.setVisibility(View.GONE);
                        scripturecnt.setVisibility(View.GONE);
                        String postimage = getIntent().getExtras().get("postImage").toString();
                        String des = getIntent().getExtras().get("description").toString();

                        description.setText(des);
                        Glide.with(getApplicationContext()).load(postimage).into(image_view);

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        post_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()){
                            String username = dataSnapshot.child("name").getValue().toString();
                            String profiless = dataSnapshot.child("profileImage").getValue().toString();
                            String churchss = dataSnapshot.child("church").getValue().toString();

                            Commentfeature(username,profiless,churchss);

                            editText_comment_input.setText("");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            private void Commentfeature(String username, String profiless, String churchss) {

                String comment_post = editText_comment_input.getText().toString();
                if (comment_post.isEmpty()){
                    Toast.makeText(CommentsActivity.this, "please write a comment", Toast.LENGTH_SHORT).show();
                }else {

                    Calendar callfordate = Calendar.getInstance();
                    SimpleDateFormat currentdate = new SimpleDateFormat("dd-MMMM-yyyy");
                    final String savecurrentdate = currentdate.format(callfordate.getTime());

                    Calendar callfortime = Calendar.getInstance();
                    SimpleDateFormat currenttime  = new SimpleDateFormat("HH:mm:ss");
                    final  String savecurrenttime = currenttime.format(callfortime.getTime());

                    final  String randomkey = userId + savecurrentdate + savecurrenttime;


                    HashMap commentMap = new HashMap();
                    commentMap.put("uid",userId);
                    commentMap.put("commment",comment_post);
                    commentMap.put("date",savecurrentdate);
                    commentMap.put("time",savecurrenttime);
                    commentMap.put("username",username);
                    commentMap.put("profile",profiless);
                    commentMap.put("church",churchss);

                    postref.child(randomkey).updateChildren(commentMap)
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {

                                    if (!task.isSuccessful()){
                                        Toast.makeText(CommentsActivity.this, "Not Sent", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });


                }
            }
        });


    }
public static class CommentsViewHolder extends RecyclerView.ViewHolder
{
    int countLikes;
    String CurrentUserId;
    DatabaseReference likesRef;
    public TextView reply,like;
    RichLinkViewSkype richLinkViewSkype;
    View mView;
    public TextView displayNolikes,displayNolikess;


    public CommentsViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        displayNolikess = mView.findViewById(R.id.displayNolikess);
        reply = mView.findViewById(R.id.reply);
        richLinkViewSkype = mView.findViewById(R.id.richLinkView);
        like = mView.findViewById(R.id.like);
        likesRef = FirebaseDatabase.getInstance().getReference("ReplyLikes");
        CurrentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();


    }
    public void setCommment(String commment) {
        TextView com = itemView.findViewById(R.id.comments);
        com.setText(commment);

    }
    public void setLink(String commment) {
        richLinkViewSkype = mView.findViewById(R.id.richLinkView);
        richLinkViewSkype.setLink(commment, new ViewListener() {
            @Override
            public void onSuccess(boolean status) {

            }

            @Override
            public void onError(Exception e) {

            }
        });

    }

    public void setDate(String date) {
        TextView dat = itemView.findViewById(R.id.dates);
        dat.setText(date);
}
    public void setTime(String time) {
        TextView tim = itemView.findViewById(R.id.times);
        tim.setText(time);
    }
    public void setUsername(String username) {
        TextView usernames = itemView.findViewById(R.id.name);
        usernames.setText("@"+username);
    }
    public void setProfile(Context applicationContext, String profile) {
        CircleImageView images = mView.findViewById(R.id.profiles);
       Glide.with(applicationContext).load(profile).into(images);
    }
    public void setChurch(String church) {
        TextView chu = itemView.findViewById(R.id.churchs);
        chu.setText(church);
    }
    public void setLikeButtonStatus(final String PostKey){
        likesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(PostKey).hasChild(CurrentUserId)){
                    countLikes = (int) snapshot.child(PostKey).getChildrenCount();
                    like.setText("liked");
                    like.setTextColor(Color.parseColor("#f1b814"));
                    displayNolikess.setText(countLikes + (""));
                }
                else
                {
                    countLikes = (int) snapshot.child(PostKey).getChildrenCount();
                    like.setText("like");
                    like.setTextColor(Color.BLACK);
                    displayNolikess.setText(countLikes + (""));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }) ;
    }


}
  @Override
    protected void onStart() {
        super.onStart();
      DisplayComments();

    }

    private void DisplayComments() {
        FirebaseRecyclerOptions<Comments> options =
                new FirebaseRecyclerOptions.Builder<Comments>()
                        .setQuery(postref,Comments.class)
                        .build();

        FirebaseRecyclerAdapter<Comments,CommentsViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Comments, CommentsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CommentsViewHolder holder, int position, @NonNull Comments model) {
                        String PostKey = getRef(position).getKey();
                        holder.setTime(model.getTime());
                        holder.setLikeButtonStatus(PostKey);
                        holder.setProfile(getApplicationContext(),model.getProfile());
                        holder.setChurch(model.getChurch());
                        holder.setUsername(model.getUsername());
                        holder.setDate(model.getDate());
                   if(URLUtil.isValidUrl(model.getCommment())){

                    holder.setLink(model.getCommment());
                   }else{
                       holder.setCommment(model.getCommment());
                       }
                        holder.like.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                LikeChecker =true;
                                likesRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                        String CurrentUserId= mAuth.getCurrentUser().getUid();
                                        if(LikeChecker.equals(true))
                                        {
                                            if(snapshot.child(PostKey).hasChild(CurrentUserId)){
                                                likesRef.child(PostKey).child(CurrentUserId).removeValue();
                                                LikeChecker = false;
                                            }else
                                            {
                                                likesRef.child(PostKey).child(CurrentUserId).setValue(true);
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

                        holder.reply.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                editText_comment_input.setText("@"+model.getUsername());
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.item_comments,parent,false);

                        return new CommentsViewHolder(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();
        recyclerView_comments.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();
    }

}