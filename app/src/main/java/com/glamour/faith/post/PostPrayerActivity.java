package com.glamour.faith.post;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.glamour.faith.Model.Member;
import com.glamour.faith.R;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
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
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class PostPrayerActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {
    String[] church = { "Who sees your Post?,","Everyone","My Church Members Only"};
    private FirebaseUser fuser;
    private DatabaseReference userRef,postRef_public,postnotification;

    private EditText add_text, co;
    private Button post;
    private String savecurrentDate, saveCurrentTime, saveRandomName, current_user_id;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private DatabaseReference reference;

    private final long countPosts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_text);
        final TextView name =  findViewById(R.id.name);
        final ImageView profile = findViewById(R.id.profile);
        final Spinner spinnner =  findViewById(R.id.spinner);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        mAuth =FirebaseAuth.getInstance();
        current_user_id =mAuth.getCurrentUser().getUid();
        loadingBar = new ProgressDialog(this);
        userRef = FirebaseDatabase.getInstance().getReference().child("Members");
        postRef_public = FirebaseDatabase.getInstance().getReference().child("Post_photos_public");
        postnotification = FirebaseDatabase.getInstance().getReference().child("notifications");

        reference = FirebaseDatabase.getInstance().getReference().child("Members").child(fuser.getUid());
        add_text = findViewById(R.id.add_text);
        add_text.setHint("What's your prayer request?");

        post = findViewById(R.id.post);
        spinnner.setOnItemSelectedListener(this);


        com.facebook.ads.AdView adViews = new com.facebook.ads.AdView(PostPrayerActivity.this, getString(R.string.fb_placement_banner), com.facebook.ads.AdSize.BANNER_HEIGHT_50);
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
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getString(R.string.admob_app_id));
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinner_text_color, church);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ImageSlider imageSlider = findViewById(R.id.image_slider);
        final List<SlideModel> remoteImages = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("Post_Scripture").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                for(DataSnapshot data:datasnapshot.getChildren()){
                    String image = data.child("profileImage").getValue().toString();
                    String bk = data.child("scriptureBook").getValue().toString();
                    String cnt = data.child("scriptureContent").getValue().toString();

                    String title = bk+" "+ cnt;
                    remoteImages.add(new SlideModel(image,title, ScaleTypes.FIT));
                    imageSlider.setImageList(remoteImages,ScaleTypes.FIT);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        spinnner.setAdapter(aa);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mode = spinnner.getSelectedItem().toString();if(mode.equals("Who sees your Post?")){
                } if(mode.equals("My Church Members Only")){

                    ValidatePostInfo();
                }
                else if(mode.equals("Everyone")){

                    ValidatePostInfoPublic();
                }
                else {
                    Toast.makeText(PostPrayerActivity.this, "Please select who sees your post", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }



    private void ValidatePostInfoPublic() {
        add_text = findViewById(R.id.add_text);
        String text = add_text.getText().toString();
        if(TextUtils.isEmpty(text)){
            add_text.setError("Please say something");
        }
        else{
            loadingBar.setMessage("Please wait....");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);
            savingPostInformationPublic();
        }

    }
    private void savingPostInformationPublic() {
        Calendar callFordate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
        savecurrentDate = currentDate.format(callFordate.getTime());


        Calendar callForTIME = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(callForTIME.getTime());
        saveRandomName = current_user_id + savecurrentDate + saveCurrentTime;
        add_text = findViewById(R.id.add_text);
        final String text = add_text.getText().toString();
        userRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String username = snapshot.child("name").getValue().toString().trim();
                    String profile = snapshot.child("profileImage").getValue().toString().trim();
                    String church = snapshot.child("church").getValue().toString().trim();
                    String count = savecurrentDate + saveCurrentTime;
                    HashMap hashMap = new HashMap();
                    hashMap.put("userid",current_user_id);
                    hashMap.put("name",username);
                    hashMap.put("profileImage",profile);
                    hashMap.put("description",text);
                    hashMap.put("date",savecurrentDate);
                    hashMap.put("time",saveCurrentTime);
                    hashMap.put("church",church);
                    hashMap.put("Counter",count);
                    hashMap.put("postmode","prayer");
                    hashMap.put("confidentiality","public");
                    hashMap.put("search",username.toLowerCase());

                    postRef_public.child(saveRandomName).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
finish();
Toast.makeText(PostPrayerActivity.this, "New Post updated Successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                            else
                            {

                                Toast.makeText(PostPrayerActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
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

    private void ValidatePostInfo() {
        add_text = findViewById(R.id.add_text);
        String text = add_text.getText().toString();
        if(TextUtils.isEmpty(text)){
            add_text.setError("Please say something");
        }

        else{
            loadingBar.setMessage("Please wait....");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);
            savingPostInformation();
        }

    }




    private void savingPostInformation() {
        Calendar callFordate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
        savecurrentDate = currentDate.format(callFordate.getTime());
        Calendar callForTIME = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(callForTIME.getTime());
        saveRandomName = savecurrentDate + saveCurrentTime;

        add_text = findViewById(R.id.add_text);
        final String text = add_text.getText().toString();
        userRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String username = snapshot.child("name").getValue().toString().trim();
                    String profile = snapshot.child("profileImage").getValue().toString().trim();
                    String church = snapshot.child("church").getValue().toString().trim();
                    String count = savecurrentDate + saveCurrentTime;
                    HashMap hashMap = new HashMap();
                    hashMap.put("userid",current_user_id);
                    hashMap.put("name",username);
                    hashMap.put("profileImage",profile);
                    hashMap.put("description",text);
                    hashMap.put("date",savecurrentDate);
                    hashMap.put("time",saveCurrentTime);
                    hashMap.put("church",church);
                    hashMap.put("Counter",count);
                    hashMap.put("postmode","prayer");
                    hashMap.put("confidentiality","private");
                    hashMap.put("search",username.toLowerCase());

                    postRef_public.child(saveRandomName).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                finish();
                                Toast.makeText(PostPrayerActivity.this, "New Post updated Successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                            else
                            {

                                Toast.makeText(PostPrayerActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
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

    @Override
    protected void onStart() {
        super.onStart();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Member patients = dataSnapshot.getValue(Member.class);
                assert patients != null;
                TextView name = findViewById(R.id.name);
                name.setText(patients.getName());
                ImageView profile = findViewById(R.id.profile);
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

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}