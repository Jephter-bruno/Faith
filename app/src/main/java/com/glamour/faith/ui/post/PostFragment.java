package com.glamour.faith.ui.post;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.denzcoskun.imageslider.ImageSlider;
import com.glamour.faith.Model.Member;
import com.glamour.faith.R;
import com.glamour.faith.models.Slide;
import com.glamour.faith.post.CelebrateActivity;
import com.glamour.faith.post.PostPhotoActivity;
import com.glamour.faith.post.PostPrayerActivity;
import com.glamour.faith.post.PostScriptureActivity;
import com.glamour.faith.post.PostTestimonyPhotoActivity;
import com.glamour.faith.post.PostTestimonyTextActivity;
import com.glamour.faith.post.PostTextActivity;
import com.glamour.faith.post.PostVideoActivity;
import com.glamour.faith.post.TestimonyActivity;
import com.glamour.faith.post.UpcomingEventActivity;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostFragment extends Fragment {
private LinearLayout post_prayer,post_photo,post_video,celebrate, post_advert, post_testimony, post_scripture, post_text, post_event;
    private List<Slide> lstSlides ;
    private ViewPager sliderpager;
    private TabLayout indicator;
    private RecyclerView MoviesRV ;
    private AdView mAdView;
    private FirebaseUser fuser;
    private DatabaseReference userRef, postRef_private,postRef_public;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private DatabaseReference reference;
    private InterstitialAd interstitialAd;
    boolean  ad = false;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_post, container, false);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        mAuth =FirebaseAuth.getInstance();
        ImageSlider imageSlider = root.findViewById(R.id.image_slider);
        reference = FirebaseDatabase.getInstance().getReference().child("Members").child(fuser.getUid());
        post_photo = root.findViewById(R.id.post_photo);
        post_prayer = root.findViewById(R.id.post_prayer);
        post_text = root.findViewById(R.id.post_text);
        post_video = root.findViewById(R.id.post_video);
        celebrate = root.findViewById(R.id.celebrate);
        post_advert = root.findViewById(R.id.post_advert);
        post_testimony = root.findViewById(R.id.post_testimony);
        post_scripture = root.findViewById(R.id.post_scripture);
        post_event = root.findViewById(R.id.post_event);

        com.facebook.ads.AdView adView = new com.facebook.ads.AdView(getContext(), getString(R.string.fb_placement_banner), com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        LinearLayout bannerContainer = root.findViewById(R.id.banner_container);
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
        mAdView = root.findViewById(R.id.adView);
        AdView adViews = new AdView(getContext());
        adViews.setAdSize(AdSize.BANNER);
        adViews.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        post_prayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), PostPrayerActivity.class));
            }
        });
        post_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), PostPhotoActivity .class));
            }
        });
        post_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), PostTextActivity.class));
            }
        });
        post_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), UpcomingEventActivity.class));
            }
        });
        post_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), PostVideoActivity.class));
            }
        });

        celebrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), CelebrateActivity.class));
            }
        });

        post_advert.setOnClickListener(new View.OnClickListener() {
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

        post_testimony.setOnClickListener(new View.OnClickListener() {
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


        post_scripture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), PostScriptureActivity.class));
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

    @Override
    public void onStart() {
        super.onStart();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Member member = dataSnapshot.getValue(Member.class);
                assert member != null;

                if(member.getDesignation().equals("Member/Artist"))
                {
            post_event.setVisibility(View.GONE);
                }
                if(member.getDesignation().equals("Member"))
                {
               post_event.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

}