package com.glamour.faith;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class CelebrateFinalActivity extends AppCompatActivity {
    private String savecurrentDate, saveCurrentTime, saveRandomName;
    private FirebaseAuth mAuth;
    private InterstitialAd interstitialAd;
    public String name, profile,churc, postedname, postedprofile, postedchurch,tim,dat, CurrentUserId,description;
    EditText says;
    TextView nam;
    CircleImageView prof;
    DatabaseReference postRef_public,reference;
    Button post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celebrate_final);
        nam = findViewById(R.id.name);
        post = findViewById(R.id.post);
        says = findViewById(R.id.says);
        prof = findViewById(R.id.profile);
        postRef_public = FirebaseDatabase.getInstance().getReference().child("Post_photos_public");
        mAuth = FirebaseAuth.getInstance();
        CurrentUserId = mAuth.getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Members").child(CurrentUserId);
        postedname = getIntent().getExtras().get("userName").toString();
        postedprofile = getIntent().getExtras().get("profile").toString();
        postedchurch = getIntent().getExtras().get("church").toString();

        nam.setText(postedname);
        Picasso.get().load(postedprofile).into(prof);
        com.facebook.ads.AdView adView = new com.facebook.ads.AdView(CelebrateFinalActivity.this, getString(R.string.fb_placement_banner), AdSize.BANNER_HEIGHT_50);
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
        post.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         description = says.getText().toString();
         if(TextUtils.isEmpty(description)){
             says.setError("Please say something about "+postedname);
             Toast.makeText(CelebrateFinalActivity.this, "Please say something about " + postedname, Toast.LENGTH_SHORT).show();
         }
         else{
             reference.addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                     if(datasnapshot.exists()){
                         name = datasnapshot.child("name").getValue().toString();
                         profile = datasnapshot.child("profileImage").getValue().toString();
                         churc = datasnapshot.child("church").getValue().toString();
                         postedname = getIntent().getExtras().get("userName").toString();
                         postedprofile = getIntent().getExtras().get("profile").toString();
                         postedchurch = getIntent().getExtras().get("church").toString();
                         ProgressDialog progressDialog = new ProgressDialog(CelebrateFinalActivity.this);
                         progressDialog.setMessage("Celebrating..");
                         progressDialog.setCanceledOnTouchOutside(true);
                         progressDialog.show();
                         Calendar callFordate = Calendar.getInstance();
                         SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
                         savecurrentDate = currentDate.format(callFordate.getTime());
                         Calendar callForTIME = Calendar.getInstance();
                         SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
                         saveCurrentTime = currentTime.format(callForTIME.getTime());
                         saveRandomName = CurrentUserId + savecurrentDate + saveCurrentTime;
                         String count = savecurrentDate + saveCurrentTime;
                         HashMap<String, Object> hashMap = new HashMap<>();
                         hashMap.put("userid",CurrentUserId);
                         hashMap.put("name",name);
                         hashMap.put("profileImage",profile);
                         hashMap.put("church",churc);
                         hashMap.put("postedname",postedname);
                         hashMap.put("postedProfile",postedprofile);
                         hashMap.put("postedChurch",postedchurch);
                         hashMap.put("description",description);
                         hashMap.put("Counter",count);
                         hashMap.put("postmode","celebrate");
                         hashMap.put("notification"," is celebrating ");
                         hashMap.put("confidentiality","public");
                         hashMap.put("time",saveCurrentTime);
                         hashMap.put("date",savecurrentDate);

                         postRef_public.child(saveRandomName).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                             @Override
                             public void onComplete(@NonNull Task task) {
                                 if(task.isSuccessful()){
                                     Toast.makeText(CelebrateFinalActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                     progressDialog.dismiss();
                                     finish();
                                 }
                                 else
                                 {
                                     String mess = task.getException().getMessage();
                                     Toast.makeText(CelebrateFinalActivity.this, "Error ocured "+mess, Toast.LENGTH_SHORT).show();
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

    }
}