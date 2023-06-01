package com.glamour.faith.ui.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.glamour.faith.AddPhotoActivity;
import com.glamour.faith.ChatRoomActivity;
import com.glamour.faith.ClickPostActivity;
import com.glamour.faith.CommentsActivity;
import com.glamour.faith.Model.ChatRoom;
import com.glamour.faith.Model.Member;
import com.glamour.faith.Model.PostPhoto;
import com.glamour.faith.R;
import com.glamour.faith.post.AdvertizementActivity;
import com.glamour.faith.post.PostPhotoActivity;
import com.glamour.faith.post.PostPrayerActivity;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.ponnamkarthik.richlinkpreview.MetaData;
import io.github.ponnamkarthik.richlinkpreview.ResponseListener;
import io.github.ponnamkarthik.richlinkpreview.RichLinkView;
import io.github.ponnamkarthik.richlinkpreview.RichLinkViewSkype;
import io.github.ponnamkarthik.richlinkpreview.RichPreview;
import io.github.ponnamkarthik.richlinkpreview.ViewListener;

public class HomeFragment extends Fragment {
    private InterstitialAd interstitialAd;
    boolean  ad = false;
    private String savecurrentDate, saveCurrentTime, saveRandomName, current_user_id;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private DatabaseReference reference;
    String[] filter = {"filter", "Testimonies", "Adverts", "My Church"};
    private TextView add_video, add_text, videos, text, photos,add_scripture,addurl,available;
    private CircleImageView profile;
private RecyclerView posts;
    private FirebaseUser fuser;
    ImageSlider imageSlider;
    LinearLayout add_photo;
private DatabaseReference postphotoref, databaseReference,likesRef,postRef_public;

    private RecyclerView recycler_post,recycler_posts ;
    private AdView mAdView;
    Boolean LikeChecker = false;
    String PostKey;
    private boolean isLoading = false;
    String CurrentUserId;
    private ImageView ratingBar;

    private boolean mGameOver;
    public Button watch_video,retry;
    public String churs,DatabaseUserId,namess,profiless,postmodes;
    public ProgressBar progressBar;
    EditText search;
    TextView load;
    CardView card;
    LinearLayout linhom;
    ImageView gif;
    String names, churc,youth,gend, past;
    SwipeRefreshLayout swipeRefreshLayout;
    ShimmerFrameLayout shimmerFramelayout;
    NestedScrollView nestedScrollView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        watch_video = root.findViewById(R.id.watch_video);
        retry = root.findViewById(R.id.retry);
        linhom = root.findViewById(R.id.linhome);
        shimmerFramelayout = root.findViewById(R.id.shimmer);
        shimmerFramelayout.startShimmer();
        nestedScrollView = root.findViewById(R.id.nestedscroll);
        gif = root.findViewById(R.id.gif);
        available = root.findViewById(R.id.available);
        ratingBar = root.findViewById(R.id.ratingBar);
        available.setVisibility(View.GONE);
        card = root.findViewById(R.id.card);
        card.setVisibility(View.GONE);
        linhom.setVisibility(View.GONE);
        retry.setVisibility(View.GONE);
        load = root.findViewById(R.id.load);
        load.setText("Loading....");
        watch_video.setVisibility(View.GONE);
        posts = root.findViewById(R.id.posts);
        add_photo = root.findViewById(R.id.add_photo);
        add_video = root.findViewById(R.id.add_video);
        add_text = root.findViewById(R.id.add_text);
        search = root.findViewById(R.id.search);
        add_scripture = root.findViewById(R.id.add_scripture);
        addurl = root.findViewById(R.id.addurl);
        progressBar = root.findViewById(R.id.progressBars);
        progressBar.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();
        CurrentUserId= mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Post_videos_public");
        postRef_public = FirebaseDatabase.getInstance().getReference().child("Post_photos_public");
        likesRef = FirebaseDatabase.getInstance().getReference("Likes");
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Members").child(fuser.getUid());
        postphotoref = FirebaseDatabase.getInstance().getReference().child("Post_photos_public");
        imageSlider = root.findViewById(R.id.image_slider);
        text = root.findViewById(R.id.text);
        videos = root.findViewById(R.id.videos);
        photos = root.findViewById(R.id.photos);
        final CircleImageView profile = root.findViewById(R.id.profile);
        recycler_posts = root.findViewById(R.id.recycler_posts);
        recycler_post = root.findViewById(R.id.recycler_post);
        recycler_post.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recycler_post.setLayoutManager(linearLayoutManager);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManager1.setReverseLayout(true);
        linearLayoutManager1.setStackFromEnd(true);
        recycler_posts.setLayoutManager(linearLayoutManager1);



        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               if(dataSnapshot.exists()){
                   Member patients = dataSnapshot.getValue(Member.class);

                   churc= patients.getChurch();
                   youth = patients.getStatus();
                   gend =patients.getGender();
                   past = patients.getDesignation();
               }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        loadfbads();
        displayChatRooms();
        Glide.with(getContext()).asGif().load(R.drawable.loading).diskCacheStrategy(DiskCacheStrategy.ALL).into(gif);
        com.facebook.ads.AdView adViews = new com.facebook.ads.AdView(getContext(), getString(R.string.fb_placement_banner), com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        LinearLayout bannerContainer = root.findViewById(R.id.banner_container);
        /// here is am getting the banner view by enabling databinding you can
        /// dobygetting the view like
        //  LinearLayout banner_container= findViewById(R.id.banner_container);
        bannerContainer.addView(adViews);
        adViews.loadAd(adViews.buildLoadAdConfig().withAdListener(new com.facebook.ads.AdListener() {
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

        adViews.loadAd();
/*
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                DisplayPhotoInPublic();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
*/
        search.addTextChangedListener(new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        SearchPost(charSequence.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
});
        Spinner spin = root.findViewById(R.id.spinner2);

        ArrayAdapter dd = new ArrayAdapter(getContext(), R.layout.spinner_text_color, filter);
        dd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spin.setAdapter(dd);
        mAdView = root.findViewById(R.id.adView);
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
        // prepare a list of slides ..


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              if(dataSnapshot.exists()){
                  Member patients = dataSnapshot.getValue(Member.class);
                  churs = patients.getChurch();
                  if(patients.getProfileImage().equals("default"))

                  {
                      profile.setImageResource(R.drawable.user);
                  }
                  else {
                      Picasso.get().load(patients.getProfileImage()).into(profile);
                  }
              }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        addurl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater1 = getLayoutInflater();
                View dialoglayout = inflater1.inflate(R.layout.dialog_add_url,null);
                builder.setView(dialoglayout);

                RichLinkView richLinkViews = dialoglayout.findViewById(R.id.richLinkViews);
                CircleImageView profile = dialoglayout.findViewById(R.id.profile);
                TextView name = dialoglayout.findViewById(R.id.name);
                Button post_url = dialoglayout.findViewById(R.id.post_url);
                EditText urls = dialoglayout.findViewById(R.id.urls);
                String url = urls.getText().toString();
                if(TextUtils.isEmpty(url)){}
else{
             richLinkViews.setLink("https://stackoverflow.com", new ViewListener() {
                 @Override
                 public void onSuccess(boolean status) {
                     final MetaData[] data = new MetaData[1];
                     RichPreview richPreview = new RichPreview(new ResponseListener() {
                         @Override
                         public void onData(MetaData metaData) {
                             data[0] = metaData;
                             richLinkViews.setLinkFromMeta(metaData);
                             //Implement your Layout
                         }

                         @Override
                         public void onError(Exception e) {
                             //handle error
                         }
                     });
                 }

                 @Override
                 public void onError(Exception e) {

                 }
             });

}
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
                post_url.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String link = urls.getText().toString();
                        if(TextUtils.isEmpty(link)){
                            Toast.makeText(getContext(), "You can't send an empty link", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            ProgressDialog progressDialog = new ProgressDialog(getContext());
                            progressDialog.setMessage("Saving your link..");
                            progressDialog.setCanceledOnTouchOutside(true);
                            progressDialog.show();
                            FirebaseAuth mAuth = FirebaseAuth.getInstance();
                            String CurrenTuserID = mAuth.getCurrentUser().getUid();
                            String savecurrentDate,saveCurrentTime,saveRandomName;
                            Calendar callFordate = Calendar.getInstance();
                            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                            savecurrentDate = currentDate.format(callFordate.getTime());


                            Calendar callForTIME = Calendar.getInstance();
                            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
                            saveCurrentTime = currentTime.format(callForTIME.getTime());
                            saveRandomName = CurrenTuserID + savecurrentDate + saveCurrentTime;
                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        String username = snapshot.child("name").getValue().toString().trim();
                                        String profile = snapshot.child("profileImage").getValue().toString().trim();
                                        String chuc = snapshot.child("church").getValue().toString().trim();
                                        String designation = snapshot.child("designation").getValue().toString().trim();
                                        String date = savecurrentDate +" at " +saveCurrentTime;
                                        HashMap hashMap = new HashMap();
                                        hashMap.put("name",username);
                                        hashMap.put("profileImage",profile);
                                        hashMap.put("userid",CurrenTuserID);
                                        hashMap.put("date",date);
                                        hashMap.put("time",saveCurrentTime);
                                        hashMap.put("church",chuc);
                                        hashMap.put("Counter",date);
                                        hashMap.put("link",link);
                                        hashMap.put("designation",designation);
                                        hashMap.put("postmode","link");
                                        hashMap.put("search",username.toLowerCase());

                                        postphotoref.child(saveRandomName).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(getContext(), "Link saved Successfully", Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
                                                }
                                                else
                                                {

                                                    Toast.makeText(getContext(), "Error Occurred", Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
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
                    }
                });



                builder.show();

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
                startActivity(new Intent(getContext(),PostPhotoActivity.class));
                }
        });


        add_scripture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(getContext(), PostScriptureActivity.class));
            }
        });

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

                startActivity(new Intent(getContext(), PostPrayerActivity.class));
            }
        });

final List<SlideModel> remoteImages = new ArrayList<>();
/*
FirebaseDatabase.getInstance().getReference().child("Post_Scripture").addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot datasnapshot) {
        for(DataSnapshot data:datasnapshot.getChildren()){
            String image = data.child("profileImage").getValue().toString();
            String bk = data.child("scriptureBook").getValue().toString();
            String cnt = data.child("scriptureContent").getValue().toString();

            String title = bk+" "+ cnt;
            remoteImages.add(new SlideModel(image,title, ScaleTypes.CENTER_CROP));
            imageSlider.setImageList(remoteImages,ScaleTypes.CENTER_CROP);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});
*/
        return root;
    }

    private void animateHeartbeat() {
        ratingBar.animate()
                .scaleX(1.2f) // Scale up horizontally
                .scaleY(1.2f) // Scale up vertically
                .alpha(0.5f) // Reduce opacity
                .setDuration(500) // Animation duration in milliseconds
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        ratingBar.animate()
                                .scaleX(1.0f) // Scale back to original size horizontally
                                .scaleY(1.0f) // Scale back to original size vertically
                                .alpha(1.0f) // Restore opacity
                                .setDuration(500) // Animation duration in milliseconds
                                .withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        animateHeartbeat(); // Repeat the animation
                                    }
                                })
                                .start();
                    }
                })
                .start();
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


                /// on error ad loading

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

    private void displayChatRooms() {
        Query query = FirebaseDatabase.getInstance().getReference("ChatRooms");

        FirebaseRecyclerOptions<ChatRoom> options = new FirebaseRecyclerOptions.Builder<ChatRoom>()
                .setQuery(query,ChatRoom.class)
                .build();

        FirebaseRecyclerAdapter<ChatRoom, ViewHolders> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ChatRoom, ViewHolders>(
                options
        ) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolders holder, int position, @NonNull ChatRoom model) {
                final String PostKey = getRef(position).getKey();
                holder.join.setVisibility(View.GONE);
                available.setVisibility(View.VISIBLE);
                holder.lin9.setVisibility(View.GONE);

                if(model.getWhoisInvited().equals("Everyone")){
                    holder.setProfileImage(getContext(),model.getProfileImage());
                    holder.setWhoisInvited(model.getWhoisInvited());


                    holder.profile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent chatIntent = new Intent(getContext(), ChatRoomActivity.class);
                            chatIntent.putExtra("PostKey",PostKey);
                            chatIntent.putExtra("userName",model.getName());
                            chatIntent.putExtra("profile",model.getProfileImage());
                            chatIntent.putExtra("topics",model.getTopic());
                            startActivity(chatIntent);
                        }
                    });
                }

                else if(model.getWhoisInvited().equals("My Church's Chat room")){

                    if(model.getChurch().equals(churc)){

                        holder.setProfileImage(getContext(),model.getProfileImage());
                        holder.setWhoisInvited(model.getWhoisInvited());


                        holder.profile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent chatIntent = new Intent(getContext(), ChatRoomActivity.class);
                                chatIntent.putExtra("PostKey",PostKey);
                                chatIntent.putExtra("userName",model.getName());
                                chatIntent.putExtra("profile",model.getProfileImage());
                                chatIntent.putExtra("topics",model.getTopic());
                                startActivity(chatIntent);
                            }
                        });

                    }
                    else{
                        holder.chat.setVisibility(View.GONE);
                        holder.lin1.setVisibility(View.GONE);
                        holder.lin7.setVisibility(View.GONE);
                        holder.lin8.setVisibility(View.GONE);
                        holder.lin9.setVisibility(View.GONE);
                        holder.mView.setVisibility(View.GONE);
                        holder.profile.setVisibility(View.GONE);
                        holder.names.setVisibility(View.GONE);
                        holder.invitees.setVisibility(View.GONE);
                        holder.topics.setVisibility(View.GONE);
                        holder.join.setVisibility(View.GONE);
                        holder.btn.setVisibility(View.GONE);
                        holder.btn1.setVisibility(View.GONE);
                       }
                }

                else if(model.getWhoisInvited().equals("Youth's Chat room")){

                    if(youth.equals("Single")){

                        holder.setProfileImage(getContext(),model.getProfileImage());
                        holder.setWhoisInvited(model.getWhoisInvited());


                        holder.profile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent chatIntent = new Intent(getContext(), ChatRoomActivity.class);
                                chatIntent.putExtra("PostKey",PostKey);
                                chatIntent.putExtra("userName",model.getName());
                                chatIntent.putExtra("profile",model.getProfileImage());
                                chatIntent.putExtra("topics",model.getTopic());
                                startActivity(chatIntent);
                            }
                        });

                    }
                    else{
                        holder.chat.setVisibility(View.GONE);
                        holder.lin1.setVisibility(View.GONE);
                        holder.lin7.setVisibility(View.GONE);
                        holder.lin8.setVisibility(View.GONE);
                        holder.lin9.setVisibility(View.GONE);
                        holder.mView.setVisibility(View.GONE);
                        holder.profile.setVisibility(View.GONE);
                        holder.names.setVisibility(View.GONE);
                        holder.invitees.setVisibility(View.GONE);
                        holder.topics.setVisibility(View.GONE);
                        holder.join.setVisibility(View.GONE);
                        holder.btn.setVisibility(View.GONE);
                        holder.btn1.setVisibility(View.GONE);
                        }
                }


                else if(model.getWhoisInvited().equals("Pastors")){

                    if(past.equals("Pastor")){

                        holder.setProfileImage(getContext(),model.getProfileImage());

                        holder.setWhoisInvited(model.getWhoisInvited());



                        holder.profile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent chatIntent = new Intent(getContext(), ChatRoomActivity.class);
                                chatIntent.putExtra("PostKey",PostKey);
                                chatIntent.putExtra("userName",model.getName());
                                chatIntent.putExtra("profile",model.getProfileImage());
                                chatIntent.putExtra("topics",model.getTopic());
                                startActivity(chatIntent);
                            }
                        });

                    }
                    else{
                        holder.chat.setVisibility(View.GONE);
                        holder.lin1.setVisibility(View.GONE);
                        holder.lin7.setVisibility(View.GONE);
                        holder.lin8.setVisibility(View.GONE);
                        holder.lin9.setVisibility(View.GONE);
                        holder.mView.setVisibility(View.GONE);
                        holder.profile.setVisibility(View.GONE);
                        holder.names.setVisibility(View.GONE);
                        holder.invitees.setVisibility(View.GONE);
                        holder.topics.setVisibility(View.GONE);
                        holder.join.setVisibility(View.GONE);
                        holder.btn.setVisibility(View.GONE);
                        holder.btn1.setVisibility(View.GONE);
                       }
                }
                else if(model.getWhoisInvited().equals("Artist's Chat room")){

                    if(model.getDesignation().equals("Member/Artist")){
                        holder.setProfileImage(getContext(),model.getProfileImage());

                        holder.setWhoisInvited(model.getWhoisInvited());



                        holder.profile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent chatIntent = new Intent(getContext(), ChatRoomActivity.class);
                                chatIntent.putExtra("PostKey",PostKey);
                                chatIntent.putExtra("userName",model.getName());
                                chatIntent.putExtra("profile",model.getProfileImage());
                                chatIntent.putExtra("topics",model.getTopic());
                                startActivity(chatIntent);
                            }
                        });

                    }
                    else{
                        holder.chat.setVisibility(View.GONE);
                        holder.lin1.setVisibility(View.GONE);
                        holder.lin7.setVisibility(View.GONE);
                        holder.lin8.setVisibility(View.GONE);
                        holder.lin9.setVisibility(View.GONE);
                        holder.mView.setVisibility(View.GONE);
                        holder.profile.setVisibility(View.GONE);
                        holder.names.setVisibility(View.GONE);
                        holder.invitees.setVisibility(View.GONE);
                        holder.topics.setVisibility(View.GONE);
                        holder.join.setVisibility(View.GONE);
                        holder.btn.setVisibility(View.GONE);
                        holder.btn1.setVisibility(View.GONE);
                      }
                }
                else if(model.getWhoisInvited().equals("Bishops")){

                    if(past.equals("Bishop")){

                        holder.setProfileImage(getContext(),model.getProfileImage());

                        holder.setWhoisInvited(model.getWhoisInvited());



                        holder.profile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent chatIntent = new Intent(getContext(), ChatRoomActivity.class);
                                chatIntent.putExtra("PostKey",PostKey);
                                chatIntent.putExtra("userName",model.getName());
                                chatIntent.putExtra("profile",model.getProfileImage());
                                chatIntent.putExtra("topics",model.getTopic());
                                startActivity(chatIntent);
                            }
                        });

                    }
                    else{
                        holder.chat.setVisibility(View.GONE);
                        holder.lin1.setVisibility(View.GONE);
                        holder.lin7.setVisibility(View.GONE);
                        holder.lin8.setVisibility(View.GONE);
                        holder.lin9.setVisibility(View.GONE);
                        holder.mView.setVisibility(View.GONE);
                        holder.profile.setVisibility(View.GONE);
                        holder.names.setVisibility(View.GONE);
                        holder.invitees.setVisibility(View.GONE);
                        holder.topics.setVisibility(View.GONE);
                        holder.join.setVisibility(View.GONE);
                        holder.btn.setVisibility(View.GONE);
                        holder.btn1.setVisibility(View.GONE);
                        }
                }
                else if(model.getWhoisInvited().equals("Reverends")){

                    if(past.equals("Reverend")){

                        holder.setProfileImage(getContext(),model.getProfileImage());

                        holder.setWhoisInvited(model.getWhoisInvited());


                        holder.profile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent chatIntent = new Intent(getContext(), ChatRoomActivity.class);
                                chatIntent.putExtra("PostKey",PostKey);
                                chatIntent.putExtra("userName",model.getName());
                                chatIntent.putExtra("profile",model.getProfileImage());
                                chatIntent.putExtra("topics",model.getTopic());
                                startActivity(chatIntent);
                            }
                        });

                    }
                    else{
                        holder.chat.setVisibility(View.GONE);
                        holder.lin1.setVisibility(View.GONE);
                        holder.lin7.setVisibility(View.GONE);
                        holder.lin8.setVisibility(View.GONE);
                        holder.lin9.setVisibility(View.GONE);
                        holder.mView.setVisibility(View.GONE);
                        holder.profile.setVisibility(View.GONE);
                        holder.names.setVisibility(View.GONE);
                        holder.invitees.setVisibility(View.GONE);
                        holder.topics.setVisibility(View.GONE);
                        holder.join.setVisibility(View.GONE);
                        holder.btn.setVisibility(View.GONE);
                        holder.btn1.setVisibility(View.GONE);
                        }
                }
                else if(model.getWhoisInvited().equals("Evangelists")){

                    if(past.equals("Evangelist")){
                        holder.setProfileImage(getContext(),model.getProfileImage());

                        holder.setWhoisInvited(model.getWhoisInvited());



                        holder.profile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent chatIntent = new Intent(getContext(), ChatRoomActivity.class);
                                chatIntent.putExtra("PostKey",PostKey);
                                chatIntent.putExtra("userName",model.getName());
                                chatIntent.putExtra("profile",model.getProfileImage());
                                chatIntent.putExtra("topics",model.getTopic());
                                startActivity(chatIntent);
                            }
                        });

                    }
                    else{
                        holder.chat.setVisibility(View.GONE);
                        holder.lin1.setVisibility(View.GONE);
                        holder.lin7.setVisibility(View.GONE);
                        holder.lin8.setVisibility(View.GONE);
                        holder.lin9.setVisibility(View.GONE);
                        holder.mView.setVisibility(View.GONE);
                        holder.profile.setVisibility(View.GONE);
                        holder.names.setVisibility(View.GONE);
                        holder.invitees.setVisibility(View.GONE);
                        holder.topics.setVisibility(View.GONE);
                        holder.join.setVisibility(View.GONE);
                        holder.btn.setVisibility(View.GONE);
                        holder.btn1.setVisibility(View.GONE);
                        }
                }
                else if(model.getWhoisInvited().equals("Women's Chat room")){

                    if(youth.equals("Married") && gend.equals("Female")){


                        holder.setProfileImage(getContext(),model.getProfileImage());

                        holder.setWhoisInvited(model.getWhoisInvited());



                        holder.profile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent chatIntent = new Intent(getContext(), ChatRoomActivity.class);
                                chatIntent.putExtra("PostKey",PostKey);
                                chatIntent.putExtra("userName",model.getName());
                                chatIntent.putExtra("profile",model.getProfileImage());
                                chatIntent.putExtra("topics",model.getTopic());
                                startActivity(chatIntent);
                            }
                        });

                    }
                    else{
                        holder.chat.setVisibility(View.GONE);
                        holder.lin1.setVisibility(View.GONE);
                        holder.lin7.setVisibility(View.GONE);
                        holder.lin8.setVisibility(View.GONE);
                        holder.lin9.setVisibility(View.GONE);
                        holder.mView.setVisibility(View.GONE);
                        holder.profile.setVisibility(View.GONE);
                        holder.names.setVisibility(View.GONE);
                        holder.invitees.setVisibility(View.GONE);
                        holder.topics.setVisibility(View.GONE);
                        holder.join.setVisibility(View.GONE);
                        holder.btn.setVisibility(View.GONE);
                        holder.btn1.setVisibility(View.GONE);
                      }
                }
                else if(model.getWhoisInvited().equals("Men's Chat room")){

                    if(youth.equals("Married") && gend.equals("Male")&&youth.equals("Widower")){


                        holder.setProfileImage(getContext(),model.getProfileImage());

                        holder.setWhoisInvited(model.getWhoisInvited());


                        holder.profile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent chatIntent = new Intent(getContext(), ChatRoomActivity.class);
                                chatIntent.putExtra("PostKey",PostKey);
                                chatIntent.putExtra("userName",model.getName());
                                chatIntent.putExtra("profile",model.getProfileImage());
                                chatIntent.putExtra("topics",model.getTopic());
                                startActivity(chatIntent);
                            }
                        });
                    }
                    else{
                        holder.chat.setVisibility(View.GONE);
                        holder.lin1.setVisibility(View.GONE);
                        holder.lin7.setVisibility(View.GONE);
                        holder.lin8.setVisibility(View.GONE);
                        holder.lin9.setVisibility(View.GONE);
                        holder.mView.setVisibility(View.GONE);
                        holder.profile.setVisibility(View.GONE);
                        holder.names.setVisibility(View.GONE);
                        holder.invitees.setVisibility(View.GONE);
                        holder.topics.setVisibility(View.GONE);
                        holder.join.setVisibility(View.GONE);
                        holder.btn.setVisibility(View.GONE);
                        holder.btn1.setVisibility(View.GONE);
                        }
                }
                else if(model.getWhoisInvited().equals("Bishops/Reverends/Pastors/Evangelist")){
                    if(model.getDesignation().equals("Bishop")){


                        holder.setProfileImage(getContext(),model.getProfileImage());

                        holder.setWhoisInvited(model.getWhoisInvited());



                        holder.profile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent chatIntent = new Intent(getContext(), ChatRoomActivity.class);
                                chatIntent.putExtra("PostKey",PostKey);
                                chatIntent.putExtra("userName",model.getName());
                                chatIntent.putExtra("profile",model.getProfileImage());
                                chatIntent.putExtra("topics",model.getTopic());
                                startActivity(chatIntent);
                            }
                        });

                    }
                    else if(past.equals("Reverend")){

                        holder.setProfileImage(getContext(),model.getProfileImage());

                        holder.setWhoisInvited(model.getWhoisInvited());


                        holder.profile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent chatIntent = new Intent(getContext(), ChatRoomActivity.class);
                                chatIntent.putExtra("PostKey",PostKey);
                                chatIntent.putExtra("userName",model.getName());
                                chatIntent.putExtra("profile",model.getProfileImage());
                                chatIntent.putExtra("topics",model.getTopic());
                                startActivity(chatIntent);
                            }
                        });

                    }
                    else if(past.equals("Pastor")){

                        holder.setProfileImage(getContext(),model.getProfileImage());
                        holder.setWhoisInvited(model.getWhoisInvited());



                        holder.profile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent chatIntent = new Intent(getContext(), ChatRoomActivity.class);
                                chatIntent.putExtra("PostKey",PostKey);
                                chatIntent.putExtra("userName",model.getName());
                                chatIntent.putExtra("profile",model.getProfileImage());
                                chatIntent.putExtra("topics",model.getTopic());
                                startActivity(chatIntent);
                            }
                        });

                    }
                    else if(past.equals("Evangelist")){

                        holder.setProfileImage(getContext(),model.getProfileImage());
                        holder.setWhoisInvited(model.getWhoisInvited());



                        holder.profile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent chatIntent = new Intent(getContext(), ChatRoomActivity.class);
                                chatIntent.putExtra("PostKey",PostKey);
                                chatIntent.putExtra("userName",model.getName());
                                chatIntent.putExtra("profile",model.getProfileImage());
                                chatIntent.putExtra("topics",model.getTopic());
                                startActivity(chatIntent);
                            }
                        });
                    }

                    else{
                        holder.chat.setVisibility(View.GONE);
                        holder.lin1.setVisibility(View.GONE);
                        holder.lin7.setVisibility(View.GONE);
                        holder.lin8.setVisibility(View.GONE);
                        holder.lin9.setVisibility(View.GONE);
                        holder.mView.setVisibility(View.GONE);
                        holder.profile.setVisibility(View.GONE);
                        holder.names.setVisibility(View.GONE);
                        holder.invitees.setVisibility(View.GONE);
                        holder.topics.setVisibility(View.GONE);
                        holder.join.setVisibility(View.GONE);
                        holder.btn.setVisibility(View.GONE);
                        holder.btn1.setVisibility(View.GONE);
                        }

                }

                else{
                    holder.chat.setVisibility(View.GONE);
                    holder.lin1.setVisibility(View.GONE);
                    holder.lin7.setVisibility(View.GONE);
                    holder.lin8.setVisibility(View.GONE);
                    holder.lin9.setVisibility(View.GONE);
                    holder.mView.setVisibility(View.GONE);
                    holder.profile.setVisibility(View.GONE);
                    holder.names.setVisibility(View.GONE);
                    holder.invitees.setVisibility(View.GONE);
                    holder.topics.setVisibility(View.GONE);
                    holder.join.setVisibility(View.GONE);
                    holder.btn.setVisibility(View.GONE);
                    holder.btn1.setVisibility(View.GONE);
                    }

            }
            @NonNull
            @Override
            public ViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View viewsd = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_room,parent,false);
                return new ViewHolders(viewsd);
            }
        };
        firebaseRecyclerAdapter.startListening();
        recycler_posts.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();
    }

    private void SearchPost(String s) {

        Query postphotore = FirebaseDatabase.getInstance().getReference().child("Post_photos_public").orderByChild("search").startAt(s).endAt(s+"\uf8ff");
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

                else if (model.getPostmode().equals("retweet")) {
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
                        holder.setPostVideo(getContext(),model.getPostVideo());
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
                    else if(model.getType().equals("scripture")){
                        holder.celebrate.setText("Re-tweeted a this post posted by");
                        holder.image_view.setVisibility(View.VISIBLE);
                        holder.setScriptureBook(model.getScriptureBook());
                        holder.setScriptureContent(model.getScriptureContent());
                        holder.videoview.setVisibility(View.GONE);
                        holder.setPostImage(getContext(),model.getPostImage());
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
                    holder.setProfileImage(getContext(),model.getProfileImage());}

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
                    holder.setPostImage(getContext(),model.getPostImage());
                    holder.setProfileImage(getContext(),model.getProfileImage());}

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
                    holder.setProfileImage(getContext(),model.getProfileImage());}
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

                        Intent clickPost = new Intent(getContext(), CommentsActivity.class);
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

                holder.scripturebk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent clickPost = new Intent(getContext(), CommentsActivity.class);
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


                holder.description.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent clickPost = new Intent(getContext(), CommentsActivity.class);
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

                holder.image_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent clickPost = new Intent(getContext(), CommentsActivity.class);
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
                        Intent commentIntent = new Intent(getContext(), CommentsActivity.class);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

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

                                userRef.child(CurrentUserId).addValueEventListener(new ValueEventListener() {
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
                                            String saveRandomName = CurrentUserId + savecurrentDate + saveCurrentTime;
                                            HashMap hashMap = new HashMap();
                                            hashMap.put("userid",CurrentUserId);
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
                                                        Toast.makeText(getContext(), "Post Re-tweeted Successfully", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else
                                                    {

                                                        Toast.makeText(getContext(), "Error Occurred", Toast.LENGTH_SHORT).show();

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
        progressBar.setVisibility(View.GONE);
        shimmerFramelayout.stopShimmer();
        shimmerFramelayout.setVisibility(View.GONE);
        nestedScrollView.setVisibility(View.VISIBLE);
        recycler_post.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();

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
                load.setText("Loading more posts for you....");
                linhom.setVisibility(View.VISIBLE);
                card.setVisibility(View.VISIBLE);
                namess = model.getName();
                profiless = model.getProfileImage();
                postmodes = model.getPostmode();
                final MetaData[] data = new MetaData[1];
                DatabaseUserId = model.getUserid();

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

                else if (model.getPostmode().equals("retweet")) {
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
                        holder.setPostVideo(getContext(),model.getPostVideo());
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
                    else if(model.getType().equals("scripture")){
                        holder.celebrate.setText("Re-tweeted a this post posted by");
                        holder.image_view.setVisibility(View.VISIBLE);
                        holder.setScriptureBook(model.getScriptureBook());
                        holder.setScriptureContent(model.getScriptureContent());
                        holder.videoview.setVisibility(View.GONE);
                        holder.setPostImage(getContext(),model.getPostImage());
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
                    holder.setProfileImage(getContext(),model.getProfileImage());}

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
                    holder.setPostImage(getContext(),model.getPostImage());
                    holder.setProfileImage(getContext(),model.getProfileImage());}

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
                    holder.setProfileImage(getContext(),model.getProfileImage());}
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

                        Intent clickPost = new Intent(getContext(), CommentsActivity.class);
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

                holder.scripturebk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent clickPost = new Intent(getContext(), CommentsActivity.class);
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

/*
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent clickPost = new Intent(getContext(), ClickPostActivity.class);
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
*/

                holder.description.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent clickPost = new Intent(getContext(), CommentsActivity.class);
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

                holder.image_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent clickPost = new Intent(getContext(), CommentsActivity.class);
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

                        Intent clickPost = new Intent(getContext(), CommentsActivity.class);
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
                        Intent commentIntent = new Intent(getContext(), CommentsActivity.class);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

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

                                userRef.child(CurrentUserId).addValueEventListener(new ValueEventListener() {
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
                                            String saveRandomName = CurrentUserId + savecurrentDate + saveCurrentTime;
                                            HashMap hashMap = new HashMap();
                                            hashMap.put("userid",CurrentUserId);
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
                                                        Toast.makeText(getContext(), "Post Re-tweeted Successfully", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else
                                                    {

                                                        Toast.makeText(getContext(), "Error Occurred", Toast.LENGTH_SHORT).show();

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
        progressBar.setVisibility(View.GONE);
        shimmerFramelayout.stopShimmer();
        shimmerFramelayout.setVisibility(View.GONE);
        nestedScrollView.setVisibility(View.VISIBLE);
        recycler_post.setAdapter(firebaseRecyclerAdapter);

    }


    private void menuClicked() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater1 = getLayoutInflater();
        View dialoglayout = inflater1.inflate(R.layout.dialog_more,null);
        builder.setView(dialoglayout);

        LinearLayout post_testimonys = dialoglayout.findViewById(R.id.post_testimonys);
        LinearLayout photo_testimony = dialoglayout.findViewById(R.id.photo_testimony);
        LinearLayout write_text = dialoglayout.findViewById(R.id.write_text);

        CircleImageView profile = dialoglayout.findViewById(R.id.profile);
        TextView name = dialoglayout.findViewById(R.id.name);
        post_testimonys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent clickPost = new Intent(getContext(), ClickPostActivity.class);
                clickPost.putExtra("PostKey",PostKey);
                clickPost.putExtra("DatabaseUserId",DatabaseUserId);
                clickPost.putExtra("name",namess);
                clickPost.putExtra("profile",profiless);
                clickPost.putExtra("postmode",postmodes);

                startActivity(clickPost);
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
                startActivity(new Intent(getContext(), PostTextActivity.class));
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
        Toast.makeText(getContext(), "More Clicked!", Toast.LENGTH_SHORT).show();
    }
    public static class ViewHolders extends RecyclerView.ViewHolder{
        public CircleImageView profile;
        public  TextView names,church,design,invitees,topics,date,chat;
        public Button join;
        View mView;
        public LinearLayout lin1,lin7,lin8,lin9;
        private  ImageView btn;
        private  ImageView btn1;
        public ViewHolders(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            profile = mView.findViewById(R.id.profile);
            names = mView.findViewById(R.id.namesd);
            invitees = mView.findViewById(R.id.invitees);
            topics = mView.findViewById(R.id.topics);
            join = mView.findViewById(R.id.join);


            lin1 = mView.findViewById(R.id.lin1);
            btn1 = mView.findViewById(R.id.btn1);
            lin8 = mView.findViewById(R.id.lin8);
            lin7 = mView.findViewById(R.id.lin7);
            lin9 = mView.findViewById(R.id.lin9);
            chat = mView.findViewById(R.id.chat);
            btn = mView.findViewById(R.id.btn);

        }
        public void setProfileImage(Context ctxs, String profileImage) {
            CircleImageView profiles = mView.findViewById(R.id.profile);
            Glide.with(ctxs).load(profileImage).into(profiles);
        }
        public void setName(String name) {
            TextView username = mView.findViewById(R.id.namesd);
            username.setText(name +"'s ");
        }
        public void setWhoisInvited(String whoisInvited) {
            TextView who = mView.findViewById(R.id.invitees);
            who.setText(whoisInvited);
        }
        public void setTopic(String topic) {
            TextView tops = mView.findViewById(R.id.topics);
            tops.setText(topic);
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

     public LinearLayout cardView;
     public CircleImageView profile,prof,profiles;
     public LinearLayout lin1,retweet, eventprof,likeds,com,coms,comss,comsss,comssss,comsssss,comssssss,comsssssss,comssssssss,lindrop;
     RichLinkViewSkype richLinkView;
     SimpleExoPlayer exoPlayer;
     PlayerView videoview;
        ImageButton liked;
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
    public void onStart() {
        super.onStart();

        DisplayPhotoInPublic();
    }
}
