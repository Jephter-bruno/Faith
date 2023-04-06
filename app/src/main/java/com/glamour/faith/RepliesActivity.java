package com.glamour.faith;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.ponnamkarthik.richlinkpreview.RichLinkView;

public class RepliesActivity extends AppCompatActivity {
    private ImageView image_view;
    private TextView name, texts,click_text_script_cont,click_text_script;
    private CircleImageView profiles;
    private Button edit_post, delete_post,report_post;
    private String  CurrentUserId,DatabaseUserId,usernames,profiless,description1,postimages,scriptbk;
    private DatabaseReference clickpostRef;
    private FirebaseAuth mAuth;
    private RichLinkView richLinkView;
    public String PostKey;

    private String scriptcont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replies);
        PostKey = getIntent().getExtras().get("PostKey").toString();
        clickpostRef  = FirebaseDatabase.getInstance().getReference().child("Post_photos_public").child(PostKey);
        DatabaseUserId = getIntent().getExtras().getString("DatabaseUserId");
        profiless = getIntent().getExtras().getString("profile");
        usernames = getIntent().getExtras().getString("name");

        AdView mAdView = findViewById(R.id.adView);
        AdView adView = new AdView(RepliesActivity.this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getString(R.string.admob_app_id));
        MobileAds.initialize(RepliesActivity.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }


    @Override
    protected void onStart() {
        super.onStart();
    }
}