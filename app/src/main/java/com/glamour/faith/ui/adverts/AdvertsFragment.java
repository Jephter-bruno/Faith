package com.glamour.faith.ui.adverts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.glamour.faith.AddPhotoActivity;
import com.glamour.faith.ClickPostActivity;
import com.glamour.faith.CommentsActivity;
import com.glamour.faith.Model.Member;
import com.glamour.faith.Model.PostPhoto;
import com.glamour.faith.R;
import com.glamour.faith.post.AdvertizementActivity;
import com.glamour.faith.post.PostAdvertTextActivity;
import com.glamour.faith.post.PostPhotoActivity;
import com.glamour.faith.post.PostScriptureActivity;
import com.glamour.faith.post.PostTestimonyPhotoActivity;
import com.glamour.faith.post.PostTestimonyTextActivity;
import com.glamour.faith.post.PostTextActivity;
import com.glamour.faith.post.PostVideoActivity;
import com.glamour.faith.post.TestimonyActivity;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.ponnamkarthik.richlinkpreview.RichLinkView;
import io.github.ponnamkarthik.richlinkpreview.RichLinkViewSkype;
import io.github.ponnamkarthik.richlinkpreview.ViewListener;

public class AdvertsFragment extends Fragment {
    String[] filter = {"filter", "Testimonies", "Adverts", "My Church"};
    private TextView add_video, add_text, videos, text, photos,add_scripture;
    private CircleImageView add_photo,profile;
    private RecyclerView posts;
    private FirebaseUser fuser;
    ImageSlider imageSlider;
    private DatabaseReference postrefTextPublic,reference,postphotoref, databaseReference,likesRef;
    private RecyclerView recycler_post ;
    private AdView mAdView;
    Boolean LikeChecker = false;
    String PostKey;
    ImageButton like;
    FirebaseAuth mAuth;
    String CurrentUserId;
    private boolean mGamePaused;
    private boolean mGameOver;
    public Button watch_video,retry;
    public String chur;
    private InterstitialAd interstitialAd;
    boolean  ad = false;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_adverts, container, false);


        watch_video = root.findViewById(R.id.watch_video);
        retry = root.findViewById(R.id.retry);
        retry.setVisibility(View.GONE);
        watch_video.setVisibility(View.GONE);
        posts = root.findViewById(R.id.posts);
        add_photo = root.findViewById(R.id.add_photo);
        add_video = root.findViewById(R.id.add_video);
        add_text = root.findViewById(R.id.add_text);
        add_scripture = root.findViewById(R.id.add_scripture);

        mAuth = FirebaseAuth.getInstance();
        CurrentUserId= mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Post_videos_public");
        likesRef = FirebaseDatabase.getInstance().getReference("Likes");
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Members").child(fuser.getUid());
        postrefTextPublic = FirebaseDatabase.getInstance().getReference().child("Post_Text_public");
        postphotoref = FirebaseDatabase.getInstance().getReference().child("Post_photos_public");

        text = root.findViewById(R.id.text);
        videos = root.findViewById(R.id.videos);
        photos = root.findViewById(R.id.photos);
        final CircleImageView profile = root.findViewById(R.id.profile);
        recycler_post = root.findViewById(R.id.recycler_post);
        recycler_post.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recycler_post.setLayoutManager(linearLayoutManager);

        mAdView = root.findViewById(R.id.adView);
        AdView adView = new AdView(getContext());
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-2023195679775199/5791427352");
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        // prepare a list of slides ..


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Member patients = dataSnapshot.getValue(Member.class);
                chur = dataSnapshot.child("church").getValue().toString();
                assert patients != null;
                if(patients.getProfileImage().equals("default"))
                {
                    profile.setImageResource(R.drawable.user);
                }
                else {
                    Picasso.get().load(patients.getProfileImage()).into(profile);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater1 = getLayoutInflater();
                View dialoglayout = inflater1.inflate(R.layout.dialog_testimony,null);
                builder.setView(dialoglayout);

                LinearLayout post_testimonys = dialoglayout.findViewById(R.id.post_testimonys);
                LinearLayout photo_testimony = dialoglayout.findViewById(R.id.photo_testimony);
                LinearLayout write_text = dialoglayout.findViewById(R.id.write_text);

                CircleImageView profile = dialoglayout.findViewById(R.id.profile);
                TextView name = dialoglayout.findViewById(R.id.name);
                post_testimonys.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getContext(), TestimonyActivity.class));
                    }
                });
                photo_testimony.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getContext(), PostTestimonyPhotoActivity.class));
                    }
                });
                write_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getContext(), PostTestimonyTextActivity.class));
                    }
                });
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Member patients = dataSnapshot.getValue(Member.class);
                        assert patients != null;
                        name.setText(patients.getName());
                        if(patients.getProfileImage().equals("default"))
                        {
                            profile.setImageResource(R.drawable.user);
                        }
                        else {
                            Picasso.get().load(patients.getProfileImage()).into(profile);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                builder.show();

            }
        });
        videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater1 = getLayoutInflater();
                View dialoglayout = inflater1.inflate(R.layout.dialog_adverts,null);
                builder.setView(dialoglayout);

                LinearLayout post_testimonys = dialoglayout.findViewById(R.id.post_testimonys);
                LinearLayout photo_testimony = dialoglayout.findViewById(R.id.photo_testimony);
                LinearLayout write_text = dialoglayout.findViewById(R.id.write_text);

                CircleImageView profile = dialoglayout.findViewById(R.id.profile);
                TextView name = dialoglayout.findViewById(R.id.name);
                post_testimonys.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getContext(), AdvertizementActivity.class));
                    }
                });
                photo_testimony.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getContext(), AdvertizementActivity.class));
                    }
                });
                write_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getContext(), PostAdvertTextActivity.class));
                    }
                });
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Member patients = dataSnapshot.getValue(Member.class);
                        assert patients != null;
                        name.setText(patients.getName());
                        if(patients.getProfileImage().equals("default"))
                        {
                            profile.setImageResource(R.drawable.user);
                        }
                        else {
                            Picasso.get().load(patients.getProfileImage()).into(profile);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                builder.show();

            }

        });
        add_scripture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), PostScriptureActivity.class));
            }
        });

     /*   text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DisplayTextInPublic();DisplayTextInPrivate();
            }
        });*/
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddPhotoActivity.class));
            }
        });
        add_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), PostTextActivity.class));
            }
        });
        add_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), PostVideoActivity.class));
            }
        });
        add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), PostPhotoActivity.class));
            }
        });
loadfbads();

        return root;
    }
    private void loadfbads() {

        interstitialAd = new InterstitialAd(getContext(), getString(R.string.fb_placement_Interstitial));
        InterstitialAdListener madlistner = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                Toast.makeText(getContext(), "Facebook Ads Loaded successfully", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {

                //// on Interstitial dismissed
//                Intent out = new Intent();
//                out.putExtra("SAVE", Boolean.TRUE);
//                setResult(RESULT_OK, out);
//                finish();
            }

            @Override
            public void onError(Ad ad, AdError adError) {



            }

            @Override
            public void onAdLoaded(Ad ad) {
                interstitialAd.show();
            }
            @Override
            public void onAdClicked(Ad ad) {
                //// on ad clicked
                /*Intent out = new Intent();
                out.putExtra("SAVE", Boolean.TRUE);
                setResult(RESULT_OK, out);
                finish();*/
            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        };
        interstitialAd.loadAd(interstitialAd.buildLoadAdConfig().withAdListener(madlistner).build());


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
                final String PostKey = getRef(position).getKey();

                if(model.getPostmode().equals("advertwithtextandimage")&& model.getConfidentiality().equals("private")){
                    if(model.getChurch().equals(chur)){
                        holder.setName(model.getName());
                        holder.setChurch(model.getChurch());
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

                else if(model.getPostmode().equals("advertwithphotoonly")&& model.getConfidentiality().equals("private")){
               if(model.getChurch().equals(chur)){
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
                else if(model.getPostmode().equals("advertwithtextonly")&& model.getConfidentiality().equals("private")){
                    if(model.getChurch().equals(chur)){
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


                else if(model.getPostmode().equals("advertwithtextandimage")&& model.getConfidentiality().equals("public")){
                    holder.setName(model.getName());
                    holder.setChurch(model.getChurch());
                    holder.setDate(model.getDate());
                    holder.setDescription(model.getDescription());
                    holder.videoview.setVisibility(View.GONE);
                    holder.scripturebk.setVisibility(View.GONE);
                    holder.scripturecnt.setVisibility(View.GONE);
                    holder.richLinkView.setVisibility(View.GONE);
                    holder.setTime(model.getTime());
                    holder.setPostImage(getContext(),model.getPostImage());
                    holder.setProfileImage(getContext(),model.getProfileImage());}

                else if(model.getPostmode().equals("advertwithphotoonly")&& model.getConfidentiality().equals("public")){
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
                    holder.setProfileImage(getContext(),model.getProfileImage());}
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
                holder.setLikeButtonStatus(PostKey);
                holder.setCommentStatus(PostKey);
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent clickPost = new Intent(getContext(), ClickPostActivity.class);
                        clickPost.putExtra("PostKey",PostKey);
                        clickPost.putExtra("DatabaseUserId",model.getUserid());
                        clickPost.putExtra("name",model.getName());
                        clickPost.putExtra("profile",model.getProfileImage());
                        clickPost.putExtra("postmode",model.getPostmode());
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
                        Intent commentIntent = new Intent(getContext(), CommentsActivity.class);
                        commentIntent.putExtra("PostKey",PostKey);
                        String post = PostKey;
                        commentIntent.putExtra("postmode",model.getPostmode());
                        if(model.getPostmode().equals("text")){
                            commentIntent.putExtra("description",model.getDescription());
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
                holder.like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LikeChecker =true;
                        likesRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
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
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout like,comment;
        View mView;
        public TextView scripturebk,scripturecnt,description,from,posted,church,date,time,name,displayNocomment;
        int countLikes;
        String CurrentUserId;
        DatabaseReference likesRef,postphotoref,reference, databaseReference;
        ImageView image_view,menudrop;
        PlayerView videoview;
        public CardView cardView;
        public CircleImageView profile;
        public LinearLayout likeds,com,coms,comss,comsss,comssss,comsssss,comssssss,comsssssss,comssssssss;
        RichLinkViewSkype richLinkView;
        public TextView displayNolikes;
        ImageButton liked;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            liked = mView.findViewById(R.id.liked);

            coms = mView.findViewById(R.id.coms);
            comss = mView.findViewById(R.id.comss);
            comsss = mView.findViewById(R.id.comsss);
            comssss= mView.findViewById(R.id.comssss);
            comsssss = mView.findViewById(R.id.comsssss);
            comssssss = mView.findViewById(R.id.comssssss);
            comsssssss = mView.findViewById(R.id.comsssssss);
            comssssssss = mView.findViewById(R.id.comssssssss);
            mView = itemView;
            like = mView.findViewById(R.id.like);
            comment = mView.findViewById(R.id.comment);
            displayNolikes = mView.findViewById(R.id.displayNolikes);
            displayNocomment = mView.findViewById(R.id.displayNocomment);
            richLinkView = mView.findViewById(R.id.richLinkView);
            scripturebk = mView.findViewById(R.id.scripturebk);
            scripturecnt = mView.findViewById(R.id.scripturecnt);
            image_view = mView.findViewById(R.id.image_view);
            description = mView.findViewById(R.id.description);
            likesRef = FirebaseDatabase.getInstance().getReference("Likes");
            CurrentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            postphotoref = FirebaseDatabase.getInstance().getReference().child("Post_photos_public");
            videoview = mView.findViewById(R.id.videoview);
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
            CurrentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            videoview = mView.findViewById(R.id.videoview);

        }

        public LinearLayout getLike() {
            return like;
        }

        public void setName(String name) {
            TextView username = mView.findViewById(R.id.name);
            username.setText("@"+name);
        }
        public void setScriptureBook(String scriptureBook) {
            TextView scripturebook = itemView.findViewById(R.id.scripturebk);
            scripturebook.setText(scriptureBook);
        }
        public void setScriptureContent(String scriptureContent) {
            TextView scripturecontent = itemView.findViewById(R.id.scripturecnt);
            scripturecontent.setText(scriptureContent);
        }

        public void setLink(String link) {
            RichLinkView richLinkView = itemView.findViewById(R.id.richLinkView);
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
            Picasso.get().load(postImage).into(image);

        }
        public void setProfileImage(Context ctxs,String profileImage) {
            CircleImageView images = mView.findViewById(R.id.profile);
            Picasso.get().load(profileImage).into(images);

        }


    }


    @Override
    public void onStart() {
        super.onStart();

        DisplayPhotoInPublic();
    }
}
