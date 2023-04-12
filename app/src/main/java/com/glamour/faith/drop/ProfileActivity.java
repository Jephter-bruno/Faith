package com.glamour.faith.drop;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.glamour.faith.AddPhotoActivity;
import com.glamour.faith.ClickPostActivity;
import com.glamour.faith.CommentsActivity;
import com.glamour.faith.Model.Member;
import com.glamour.faith.Model.PostPhoto;
import com.glamour.faith.OtherProfileActivity;
import com.glamour.faith.R;
import com.glamour.faith.SetUpActivity;
import com.glamour.faith.post.AdvertizementActivity;
import com.glamour.faith.post.PostPhotoActivity;
import com.glamour.faith.post.PostScriptureActivity;
import com.glamour.faith.post.PostTextActivity;
import com.glamour.faith.post.PostVideoActivity;
import com.glamour.faith.post.TestimonyActivity;
import com.glamour.faith.post.UpcomingEventActivity;
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
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.ponnamkarthik.richlinkpreview.MetaData;
import io.github.ponnamkarthik.richlinkpreview.ResponseListener;
import io.github.ponnamkarthik.richlinkpreview.RichLinkView;
import io.github.ponnamkarthik.richlinkpreview.RichLinkViewSkype;
import io.github.ponnamkarthik.richlinkpreview.RichPreview;
import io.github.ponnamkarthik.richlinkpreview.ViewListener;

public class ProfileActivity extends AppCompatActivity {
    private LinearLayout post_photo,post_text,post_scripture,post_video,post_testimony,postadvert,submitchurch,post_event,post_my_videos,view_members,addchurch,View_reportedPosts;
    private TextView gender,age,mar_status,phone,email,church,designation,name;
    private FloatingActionButton floatingActionButton2;
    private DatabaseReference databaseReference,postphotoref,likesRef,Reference,References,churches,mDatabaseRef;
    private FirebaseAuth mAuth;
    private String savecurrentDate, saveCurrentTime, saveRandomName, current_user_id;
    Spinner spinner, spinner2, spinner3, spinner4;
    private CircleImageView profile;
    String currentUserId;
    Boolean LikeChecker = false;
    ValueEventListener listener;
    public ImageView image_view;
    private RecyclerView recycler_post,recycler_members,recycler_youtube ;
    ArrayList<String> spinnerDataList;
    private WebView webView;
    public String PostKey;
    View youtubeview,memberview,articleview;
    LinearLayout viewchurd, youtube, members, articles,addchurchs;
    ArrayAdapter<String> adapter;
    Button updatechurc;
    ImageView updateprofile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        articles = findViewById(R.id.articles);
        updateprofile = findViewById(R.id.updateprofile);
        viewchurd = findViewById(R.id.viewchurd);
        spinner4 = findViewById(R.id.spinner4);
        updatechurc = findViewById(R.id.updatechurc);
        addchurchs  = findViewById(R.id.addchurchs);
        youtube = findViewById(R.id.youtube);
        members = findViewById(R.id.members);
        recycler_youtube = findViewById(R.id.recycler_youtube);
        youtubeview = findViewById(R.id.youtubeview);
        youtubeview.setVisibility(View.GONE);
        memberview = findViewById(R.id.memberview);
        memberview.setVisibility(View.GONE);
        articleview = findViewById(R.id.articleview);
        profile = findViewById(R.id.profile);
        post_photo = findViewById(R.id.post_photo);
        post_text = findViewById(R.id.post_text);
        post_scripture = findViewById(R.id.post_scripture);
        post_testimony = findViewById(R.id.post_testimony);
        postadvert = findViewById(R.id.postadvert);
        post_video = findViewById(R.id.post_video);
        post_event = findViewById(R.id.post_event);
        post_my_videos = findViewById(R.id.post_my_videos);
        view_members = findViewById(R.id.view_members);
        View_reportedPosts = findViewById(R.id.View_reportedPosts);
        gender = findViewById(R.id.gender);
       /* age = findViewById(R.id.age);
        mar_status = findViewById(R.id.mar_status);*/
        phone = findViewById(R.id.phone);
        addchurch = findViewById(R.id.addchurch);
        email = findViewById(R.id.email);
        church = findViewById(R.id.church);
        designation = findViewById(R.id.designation);
        name = findViewById(R.id.name);
        recycler_members = findViewById(R.id.recycler_members);
        submitchurch = findViewById(R.id.submitchurch);
        recycler_members.setLayoutManager(new GridLayoutManager(ProfileActivity.this,3));


        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        Reference = FirebaseDatabase.getInstance().getReference("Churches");
        churches = FirebaseDatabase.getInstance().getReference("Churches");
        References = FirebaseDatabase.getInstance().getReference("PendingChurches");
        databaseReference = FirebaseDatabase.getInstance().getReference("Members").child(currentUserId);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Members").child(currentUserId);

        postphotoref = FirebaseDatabase.getInstance().getReference().child("Post_photos_public");
        likesRef = FirebaseDatabase.getInstance().getReference("Likes");
        recycler_post = findViewById(R.id.recycler_post);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recycler_post.setLayoutManager(linearLayoutManager);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager1.setReverseLayout(true);
        linearLayoutManager1.setStackFromEnd(true);
        recycler_youtube.setLayoutManager(linearLayoutManager1);
        retrieveChurches();
        updatechurc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateChurch();
            }
        });
        submitchurch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                {


                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                    LayoutInflater inflater1 = getLayoutInflater();
                    View dialoglayout = inflater1.inflate(R.layout.dialog_add_church,null);
                    builder.setView(dialoglayout);

                    CircleImageView profile = dialoglayout.findViewById(R.id.profile);
                    TextView name = dialoglayout.findViewById(R.id.name);
                    Button post_churc = dialoglayout.findViewById(R.id.post_churc);
                    EditText chuched = dialoglayout.findViewById(R.id.chuched);
                    post_churc.setText("Submit Church");

                    post_churc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String church = chuched.getText().toString();
                            if(TextUtils.isEmpty(church)){
                                Toast.makeText(ProfileActivity.this, "Please Enter church to submit", Toast.LENGTH_SHORT).show();
                                chuched.setError("PLease Enter Church to submit");
                            }
                            else
                            {
                                ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this);
                                progressDialog.setMessage("Please Wait...");
                                progressDialog.show();
                                progressDialog.setCanceledOnTouchOutside(true);
                                References.push().setValue(church).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            chuched.setText("");
                                            Toast.makeText(ProfileActivity.this, "Church submitted successfully", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                        else
                                        {
                                            String mess = task.getException().getMessage();
                                            Toast.makeText(ProfileActivity.this, mess, Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                    }
                                });
                            }}

                    });

                    databaseReference.addValueEventListener(new ValueEventListener() {
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

            }
        });
        spinnerDataList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(ProfileActivity.this,android.R.layout.simple_spinner_dropdown_item,spinnerDataList);
        spinner4.setAdapter(adapter);
        com.facebook.ads.AdView adViews = new com.facebook.ads.AdView(ProfileActivity.this, getString(R.string.fb_placement_banner), com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        LinearLayout bannerContainer = findViewById(R.id.banner_container);
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
        AdView mAdView = findViewById(R.id.adView);
        AdView adView = new AdView(ProfileActivity.this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getString(R.string.admob_app_id));
        MobileAds.initialize(ProfileActivity.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if(datasnapshot.exists()){
                    String username = datasnapshot.child("name").getValue().toString();
                    String profiless = datasnapshot.child("profileImage").getValue().toString();
                    String churchss = datasnapshot.child("church").getValue().toString();
                    String gend = datasnapshot.child("gender").getValue().toString();
                    String marital = datasnapshot.child("status").getValue().toString();
                    String phones = datasnapshot.child("phone").getValue().toString();
                    String design = datasnapshot.child("designation").getValue().toString();
                    String userid = datasnapshot.child("userId").getValue().toString();
                    String dateofbirth = datasnapshot.child("dateOfBirth").getValue().toString();
                    Picasso.get().load(profiless).into(profile);
                    if(churchss.equals("default")){
                                addchurchs.setVisibility(View.VISIBLE);
                    }
                    name.setText(username);
                    church.setText(churchss);
                    /* email.setText();*/
                    gender.setText(gend);
                   /* mar_status.setText(marital);*/
                    phone.setText(phones);
                    /* age.setText(dateofbirth);*/
                    designation.setText(design);
                    if(design.equals("Member/Artist")){
                        post_event.setVisibility(View.GONE);
                        view_members.setVisibility(View.GONE);
                        viewchurd.setVisibility(View.GONE);
                    }
                    else if(design.equals("Member")){
                        viewchurd.setVisibility(View.GONE);
                        youtube.setVisibility(View.GONE);
                        post_event.setVisibility(View.GONE);
                        view_members.setVisibility(View.GONE);
                    }
                    else if(!design.equals("Member/Artist"))
                    {
                        post_my_videos.setVisibility(View.GONE);
                        youtube.setVisibility(View.GONE);
                    }
                    else if(design.equals("Pastor")){
                        viewchurd.setVisibility(View.GONE);
                        youtube.setVisibility(View.GONE);
                        view_members.setVisibility(View.VISIBLE);
                    }
                    else if(design.equals("Reverend")){
                        viewchurd.setVisibility(View.GONE);
                        youtube.setVisibility(View.GONE);
                        view_members.setVisibility(View.GONE);
                    }
                    else if(design.equals("Bishop")){
                        viewchurd.setVisibility(View.GONE);
                        youtube.setVisibility(View.GONE);
                        view_members.setVisibility(View.GONE);
                    }
                    else if(design.equals("Evangelist")){
                        viewchurd.setVisibility(View.GONE);
                        youtube.setVisibility(View.GONE);
                        view_members.setVisibility(View.GONE);
                    }
                    else if(design.equals("Developer")){
                        addchurch.setVisibility(View.VISIBLE);
                        View_reportedPosts.setVisibility(View.VISIBLE);
                    }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        updateprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, SetUpActivity.class));
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, AddPhotoActivity.class));
            }
        });
        articles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                articleview.setVisibility(View.VISIBLE);
                memberview.setVisibility(View.GONE);
                youtubeview.setVisibility(View.GONE);
                recycler_members.setVisibility(View.GONE);
                recycler_youtube.setVisibility(View.GONE);
                recycler_post.setVisibility(View.VISIBLE);
                DisplayPhotoInPublic();
            }
        });
        members.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memberview.setVisibility(View.VISIBLE);
                articleview.setVisibility(View.GONE);
                youtubeview.setVisibility(View.GONE);
                recycler_post.setVisibility(View.GONE);
                recycler_members.setVisibility(View.VISIBLE);
                recycler_youtube.setVisibility(View.GONE);
                DisplayMmembers();
            }
        });
        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                youtubeview.setVisibility(View.VISIBLE);
                articleview.setVisibility(View.GONE);
                recycler_post.setVisibility(View.GONE);
                recycler_members.setVisibility(View.GONE);
                memberview.setVisibility(View.GONE);
                recycler_youtube.setVisibility(View.VISIBLE);
                DisplayYoutube();
            }
        });

        view_members.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DisplayMmembers();
            }
        });
        post_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PostPhotoActivity.class));
            }
        });

        post_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PostTextActivity.class));
            }
        });
        post_scripture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PostScriptureActivity.class));
            }
        });       post_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PostVideoActivity.class));
            }
        });
        post_testimony.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), TestimonyActivity.class));
            }
        });
        postadvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AdvertizementActivity.class));
            }
        });       post_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UpcomingEventActivity.class));
            }
        });
        post_my_videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
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
                databaseReference.addValueEventListener(new ValueEventListener() {
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
                            Glide.with(getApplicationContext()).load(patients.getProfileImage()).into(profile);
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
                            Toast.makeText(ProfileActivity.this, "You can't send an empty link", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this);
                            progressDialog.setMessage("Adding youtube song to your playlist");
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
                            databaseReference.addValueEventListener(new ValueEventListener() {
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
                                        hashMap.put("postmode","youtubelink");
                                        hashMap.put("search",username.toLowerCase());

                                        postphotoref.child(saveRandomName).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(ProfileActivity.this, "Link saved Successfully", Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
                                                }
                                                else
                                                {

                                                    Toast.makeText(ProfileActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
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
        addchurch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                LayoutInflater inflater1 = getLayoutInflater();
                View dialoglayout = inflater1.inflate(R.layout.dialog_add_church,null);
                builder.setView(dialoglayout);

                CircleImageView profile = dialoglayout.findViewById(R.id.profile);
                TextView name = dialoglayout.findViewById(R.id.name);
                Button post_churc = dialoglayout.findViewById(R.id.post_churc);
                EditText chuched = dialoglayout.findViewById(R.id.chuched);

                post_churc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String church = chuched.getText().toString();
                        if(TextUtils.isEmpty(church)){
                            Toast.makeText(ProfileActivity.this, "Please Enter church to add", Toast.LENGTH_SHORT).show();
                            chuched.setError("PLease Enter Church to save");
                        }
                        else
                        {
                            ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this);
                            progressDialog.setMessage("Please Wait...");
                            progressDialog.show();
                            progressDialog.setCanceledOnTouchOutside(true);
                            Reference.push().setValue(church).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        chuched.setText("");
                                        Toast.makeText(ProfileActivity.this, "Church saved successfully", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                    else
                                    {
                                        String mess = task.getException().getMessage();
                                        Toast.makeText(ProfileActivity.this, mess, Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                        }}

                });

                databaseReference.addValueEventListener(new ValueEventListener() {
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


    }

    private void updateChurch() {
        Spinner spinnner4 =  findViewById(R.id.spinner4);
        String church = spinnner4.getSelectedItem().toString();

    if (church.equals("Select Your Church Branch")) {
        Toast.makeText(ProfileActivity.this, "Please Select your Church Branch", Toast.LENGTH_SHORT).show();
    }else
    {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait as we update your church...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(true);
        HashMap hashMap = new HashMap();
        hashMap.put("church", church);
        mDatabaseRef.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {

            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Church updated Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    String message = task.getException().getMessage();
                    Toast.makeText(ProfileActivity.this, "Something went wrong " + message, Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    }

    public void retrieveChurches(){
        listener = churches.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item: snapshot.getChildren()){
                    spinnerDataList.add(item.getValue().toString());

                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void DisplayYoutube() {

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
                if(model.getUserid().equals(currentUserId)){
                    if(model.getPostmode().equals("youtubelink")){
                        holder.setName(model.getName());
                        holder.setChurch(model.getChurch());
                        holder.setDate(model.getDate());
                        holder.description.setVisibility(View.GONE);
                        holder.videoview.setVisibility(View.GONE);
                        holder.scripturebk.setVisibility(View.GONE);
                        holder.scripturecnt.setVisibility(View.GONE);
                        holder.image_view.setVisibility(View.GONE);
                        holder.setTime(model.getTime());
                        holder.setProfileImage(ProfileActivity.this,model.getProfileImage());
                        holder.richLinkView.setLink(model.getLink(), new ViewListener() {
                            @Override
                            public void onSuccess(boolean status) {
                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });

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

                    holder.setLikeButtonStatus(PostKey);
                    holder.setCommentStatus(PostKey);
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

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent clickPost = new Intent(ProfileActivity.this, ClickPostActivity.class);
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
                        Intent commentIntent = new Intent(ProfileActivity.this, CommentsActivity.class);
                        commentIntent.putExtra("PostKey",PostKey);
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
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View viewsd = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo,parent,false);
                return new ViewHolder(viewsd);
            }
        };
        firebaseRecyclerAdapter.startListening();
        recycler_youtube.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();
    }

    private void DisplayMmembers() {

        String ch = church.getText().toString();
        Query query = FirebaseDatabase.getInstance().getReference("Members")
                .orderByChild("church")
                .equalTo(ch);

        FirebaseRecyclerOptions<PostPhoto> options = new FirebaseRecyclerOptions.Builder<PostPhoto>()
                .setQuery(query,PostPhoto.class)
                .build();

        FirebaseRecyclerAdapter<PostPhoto, MemberViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PostPhoto, MemberViewHolder>(
                options
        ) {
            @Override
            protected void onBindViewHolder(@NonNull MemberViewHolder holder, int position, @NonNull PostPhoto model) {
                final String usersID = getRef(position).getKey();
                holder.setName(model.getName());
                holder.setChurch(model.getChurch());
                holder.setProfileImage(ProfileActivity.this,model.getProfileImage());
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                        LayoutInflater inflater1 = getLayoutInflater();
                        View dialoglayout = inflater1.inflate(R.layout.dialog_profile,null);
                        builder.setView(dialoglayout);

                        LinearLayout post_testimonys = dialoglayout.findViewById(R.id.post_testimonys);
                        LinearLayout photo_testimony = dialoglayout.findViewById(R.id.photo_testimony);
                        LinearLayout write_text = dialoglayout.findViewById(R.id.write_text);

                        CircleImageView profile = dialoglayout.findViewById(R.id.profile);
                        TextView name = dialoglayout.findViewById(R.id.name);
                        Picasso.get().load(model.getProfileImage()).into(profile);
                        name.setText(model.getName());
                        write_text.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(usersID.equals(currentUserId)){
                                    Toast.makeText(ProfileActivity.this, "You can't remove yourself", Toast.LENGTH_SHORT).show();
                                }else
                                {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                                    builder.setTitle("Are you sure you want to remove this user?");

                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });
                                    Dialog dialog = builder.create();
                                    dialog.show();
                                }


                            }
                        });
                        post_testimonys.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(usersID.equals(currentUserId)){
                                    Toast.makeText(ProfileActivity.this, "You can't send  message to yourself", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Intent chatIntent = new Intent(ProfileActivity.this,UserMessageActivity.class);
                                    chatIntent.putExtra("userName",model.getName());
                                    chatIntent.putExtra("visit_user_id",usersID);
                                    chatIntent.putExtra("profile",model.getProfileImage());

                                    startActivity(chatIntent);}

                            }

                        });
                        photo_testimony.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(usersID.equals(currentUserId)){
                                    Intent profIntent = new Intent(ProfileActivity.this, ProfileActivity.class);
                                    startActivity(profIntent);
                                }
                                else {
                                    Intent profIntent = new Intent(ProfileActivity.this, OtherProfileActivity.class);
                                    profIntent.putExtra("visit_user_id", usersID);
                                    profIntent.putExtra("userName", model.getName());
                                    profIntent.putExtra("visit_user_id", usersID);
                                    profIntent.putExtra("userName", model.getName());
                                    startActivity(profIntent);
                                }

                            }
                        });

                        builder.show();

                    }
                });


            }
            @NonNull
            @Override
            public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View viewsd = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reverend,parent,false);
                return new MemberViewHolder(viewsd);
            }
        };
        firebaseRecyclerAdapter.startListening();
        recycler_members.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();
    }
    public static class MemberViewHolder extends RecyclerView.ViewHolder{
        public CircleImageView profile;
        public  TextView names,church;
        View mView;
        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            profile = mView.findViewById(R.id.profile);
            names = mView.findViewById(R.id.name);
            church = mView.findViewById(R.id.church);
        }
        public void setProfileImage(Context ctxs,String profileImage) {
            CircleImageView profiles = mView.findViewById(R.id.profile);
            Glide.with(ctxs).load(profileImage).into(profiles);
        }
        public void setName(String name) {
            TextView username = mView.findViewById(R.id.name);
            username.setText(name);
        }
        public void setChurch(String church) {
            TextView chu = mView.findViewById(R.id.church);
            chu.setText(church);

        }
        public void setDesignation(String designation) {

        }
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
                if(model.getUserid().equals(currentUserId)){
                    if(model.getPostmode().equals("link")){
                        holder.setName(model.getName());
                        holder.setChurch(model.getChurch());
                        holder.setDate(model.getDate());
                        holder.description.setVisibility(View.GONE);
                        holder.videoview.setVisibility(View.GONE);
                        holder.scripturebk.setVisibility(View.GONE);
                        holder.scripturecnt.setVisibility(View.GONE);
                        holder.image_view.setVisibility(View.GONE);
                        holder.setTime(model.getTime());
                        holder.setProfileImage(ProfileActivity.this,model.getProfileImage());
                        holder.richLinkView.setLink(model.getLink(), new ViewListener() {
                            @Override
                            public void onSuccess(boolean status) {
                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });

                    }

                    else if(model.getPostmode().equals("youtubelink")){
                        holder.setName(model.getName());
                        holder.setChurch(model.getChurch());
                        holder.setDate(model.getDate());
                        holder.description.setVisibility(View.GONE);
                        holder.videoview.setVisibility(View.GONE);
                        holder.scripturebk.setVisibility(View.GONE);
                        holder.scripturecnt.setVisibility(View.GONE);
                        holder.image_view.setVisibility(View.GONE);
                        holder.setTime(model.getTime());
                        holder.setProfileImage(ProfileActivity.this,model.getProfileImage());
                        holder.richLinkView.setLink(model.getLink(), new ViewListener() {
                            @Override
                            public void onSuccess(boolean status) {
                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });

                    }

                    else if(model.getPostmode().equals("photowithtext")){
                        holder.setName(model.getName());
                        holder.setChurch(model.getChurch());
                        holder.setDate(model.getDate());
                        holder.setDescription(model.getDescription());
                        holder.videoview.setVisibility(View.GONE);
                        holder.scripturebk.setVisibility(View.GONE);
                        holder.scripturecnt.setVisibility(View.GONE);
                        holder.richLinkView.setVisibility(View.GONE);
                        holder.setTime(model.getTime());
                        holder.setPostImage(getApplicationContext(),model.getPostImage());
                        holder.setProfileImage(getApplicationContext(),model.getProfileImage());}

                    else if(model.getPostmode().equals("text")){
                        holder.setName(model.getName());
                        holder.setChurch(model.getChurch());
                        holder.setDate(model.getDate());
                        holder.setDescription(model.getDescription());
                        holder.videoview.setVisibility(View.GONE);
                        holder.scripturebk.setVisibility(View.GONE);
                        holder.scripturecnt.setVisibility(View.GONE);
                        holder.image_view.setVisibility(View.GONE);
                        holder.richLinkView.setVisibility(View.GONE);
                        holder.setTime(model.getTime());
                        holder.setProfileImage(ProfileActivity.this,model.getProfileImage());}
                    else if(model.getPostmode().equals("testimonytext")){
                        holder.setName(model.getName());
                        holder.setChurch(model.getChurch());
                        holder.setDate(model.getDate());
                        holder.setDescription(model.getDescription());
                        holder.videoview.setVisibility(View.GONE);
                        holder.scripturebk.setVisibility(View.GONE);
                        holder.scripturecnt.setVisibility(View.GONE);
                        holder.richLinkView.setVisibility(View.GONE);
                        holder.image_view.setVisibility(View.GONE);
                        holder.setTime(model.getTime());
                        holder.setProfileImage(ProfileActivity.this,model.getProfileImage());}
                    else if(model.getPostmode().equals("testimonyphoto")){
                        holder.setName(model.getName());
                        holder.setChurch(model.getChurch());
                        holder.setDate(model.getDate());
                        holder.videoview.setVisibility(View.GONE);
                        holder.scripturebk.setVisibility(View.GONE);
                        holder.scripturecnt.setVisibility(View.GONE);
                        holder.description.setVisibility(View.GONE);
                        holder.richLinkView.setVisibility(View.GONE);
                        holder.setPostImage(ProfileActivity.this,model.getPostImage());
                        holder.setTime(model.getTime());
                        holder.setProfileImage(ProfileActivity.this,model.getProfileImage());}
                    else if(model.getPostmode().equals("testimonyphotoandtext")){
                        holder.setName(model.getName());
                        holder.setChurch(model.getChurch());
                        holder.setDate(model.getDate());
                        holder.setDescription(model.getDescription());
                        holder.videoview.setVisibility(View.GONE);
                        holder.scripturebk.setVisibility(View.GONE);
                        holder.scripturecnt.setVisibility(View.GONE);
                        holder.richLinkView.setVisibility(View.GONE);
                        holder.setTime(model.getTime());
                        holder.setPostImage(ProfileActivity.this,model.getPostImage());
                        holder.setProfileImage(ProfileActivity.this,model.getProfileImage());}
                   else if(model.getPostmode().equals("advertwithtextandimage")){
                        holder.setName(model.getName());
                        holder.setChurch(model.getChurch());
                        holder.setDate(model.getDate());
                        holder.setDescription(model.getDescription());
                        holder.videoview.setVisibility(View.GONE);
                        holder.scripturebk.setVisibility(View.GONE);
                        holder.scripturecnt.setVisibility(View.GONE);
                        holder.richLinkView.setVisibility(View.GONE);
                        holder.setTime(model.getTime());
                        holder.setPostImage(ProfileActivity.this,model.getPostImage());
                        holder.setProfileImage(ProfileActivity.this,model.getProfileImage());}

                    else if(model.getPostmode().equals("scripture")){
                        holder.setName(model.getName());
                        holder.setChurch(model.getChurch());
                        holder.setDate(model.getDate());
                        holder.description.setVisibility(View.GONE);
                        holder.videoview.setVisibility(View.GONE);
                        holder.richLinkView.setVisibility(View.GONE);
                        holder.setScriptureBook(model.getScriptureBook());
                        holder.setScriptureContent(model.getScriptureContent());
                        holder.setTime(model.getTime());
                        holder.image_view.setVisibility(View.GONE);
                        holder.setProfileImage(ProfileActivity.this,model.getProfileImage());}
                    else if(model.getPostmode().equals("advertwithphotoonly")){
                        holder.setName(model.getName());
                        holder.setChurch(model.getChurch());
                        holder.setDate(model.getDate());
                        holder.videoview.setVisibility(View.GONE);
                        holder.scripturebk.setVisibility(View.GONE);
                        holder.scripturecnt.setVisibility(View.GONE);
                        holder.description.setVisibility(View.GONE);
                        holder.richLinkView.setVisibility(View.GONE);
                        holder.setPostImage(ProfileActivity.this,model.getPostImage());
                        holder.setTime(model.getTime());
                        holder.setProfileImage(ProfileActivity.this,model.getProfileImage());}

                    else if (model.getPostmode().equals("retweet")) {
                        holder.eventprof.setVisibility(View.VISIBLE);
                        holder.setName(model.getName());
                        holder.setChurch(model.getChurch());
                        holder.setDate(model.getDate());
                        holder.celebrate.setText("Re-tweeted a post posted by");
                        holder.descriptions.setText("Re-tweeted a post posted by");
                        holder.celebrate.setVisibility(View.VISIBLE);
                        holder.cel.setVisibility(View.GONE);
                        holder.setPostedProfile(ProfileActivity.this,model.getPostedProfile());
                        holder.setPostedChurch(model.getPostedChurch());
                        holder.setPostedname(model.getPostedname());
                        holder.setTime(model.getTime());
                        holder.setProfileImage(ProfileActivity.this, model.getProfileImage());
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
                            holder.setPostVideo(ProfileActivity.this,model.getPostVideo());
                            holder.videoview.setVisibility(View.VISIBLE);
                            holder.setDescription(model.getDescription());
                            holder.richLinkView.setVisibility(View.GONE);
                        }
                        else if(model.getPostmode().equals("prayer")){
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
                            holder.setProfileImage(ProfileActivity.this,model.getProfileImage());}
                        else if(model.getType().equals("photo")){
                            holder.celebrate.setText("Re-tweeted a photo posted by");
                            holder.scripturebk.setVisibility(View.GONE);
                            holder.image_view.setVisibility(View.VISIBLE);
                            holder.scripturecnt.setVisibility(View.GONE);
                            holder.videoview.setVisibility(View.GONE);
                            holder.setPostImage(ProfileActivity.this,model.getPostImage());
                            holder.description.setVisibility(View.GONE);
                            holder.richLinkView.setVisibility(View.GONE);
                        }
                        else if(model.getType().equals("photowithtext")){
                            holder.celebrate.setText("Re-tweeted a this post posted by");
                            holder.image_view.setVisibility(View.VISIBLE);
                            holder.scripturebk.setVisibility(View.GONE);
                            holder.scripturecnt.setVisibility(View.GONE);
                            holder.videoview.setVisibility(View.GONE);
                            holder.setPostImage(ProfileActivity.this,model.getPostImage());
                            holder.setDescription(model.getDescription());
                            holder.richLinkView.setVisibility(View.GONE);
                        }
                        else if(model.getType().equals("scripture")){
                            holder.celebrate.setText("Re-tweeted a this post posted by");
                            holder.image_view.setVisibility(View.VISIBLE);
                            holder.setScriptureBook(model.getScriptureBook());
                            holder.setScriptureContent(model.getScriptureContent());
                            holder.videoview.setVisibility(View.GONE);
                            holder.setPostImage(ProfileActivity.this,model.getPostImage());
                            holder.description.setVisibility(View.GONE);
                            holder.richLinkView.setVisibility(View.GONE);
                        }


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
                        holder.setPostedProfile(ProfileActivity.this,model.getPostedProfile());
                        holder.setPostedChurch(model.getPostedChurch());
                        holder.setPostedname(model.getPostedname());
                        holder.setTime(model.getTime());
                        holder.setProfileImage(ProfileActivity.this, model.getProfileImage());


                    }
                    else if(model.getPostmode().equals("video")){
                            holder.setName(model.getName());
                            holder.setChurch(model.getChurch());
                            holder.setDate(model.getDate());
                            holder.setPostVideo(ProfileActivity.this,model.getPostVideo());
                            holder.videoview.setVisibility(View.VISIBLE);
                            holder.image_view.setVisibility(View.GONE);
                            holder.setDescription(model.getDescription());
                            holder.scripturebk.setVisibility(View.GONE);
                            holder.scripturecnt.setVisibility(View.GONE);
                            holder.description.setVisibility(View.GONE);
                            holder.richLinkView.setVisibility(View.GONE);
                            holder.setPostImage(ProfileActivity.this,model.getPostImage());
                            holder.setTime(model.getTime());
                            holder.setProfileImage(ProfileActivity.this,model.getProfileImage());

                    }

                    else if(model.getPostmode().equals("advertwithtextonly")){
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
                        holder.setProfileImage(ProfileActivity.this,model.getProfileImage());}
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
                holder.retweet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);

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
                                                        Toast.makeText(ProfileActivity.this, "Post Re-tweeted Successfully", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else
                                                    {

                                                        Toast.makeText(ProfileActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();

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

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent clickPost = new Intent(ProfileActivity.this, CommentsActivity.class);
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
                        Intent commentIntent = new Intent(ProfileActivity.this, CommentsActivity.class);
                        commentIntent.putExtra("PostKey",PostKey);
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
            cel = mView.findViewById(R.id.cel);
            eventprof = mView.findViewById(R.id.eventprof);
            prof = mView.findViewById(R.id.prof);
            churchs = mView.findViewById(R.id.churchs);
            namd = mView.findViewById(R.id.namd);
            liked = mView.findViewById(R.id.liked);
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

