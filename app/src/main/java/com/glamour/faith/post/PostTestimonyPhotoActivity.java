package com.glamour.faith.post;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.glamour.faith.Model.Member;
import com.glamour.faith.R;
import com.glamour.faith.models.Slide;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class PostTestimonyPhotoActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {
    String[] church = { "Who sees your Post?","Everyone","My Church Members Only"};
    private FirebaseUser fuser;
    private DatabaseReference userRef, postRef_private,postRef_public,postnotification;

    private StorageReference postImagereference;
    private static final int IMAGE_REQUEST = 1;
    private EditText add_text;
    private TextView add_photo;
    private Button post;
    private StorageTask uploadTask;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri ImageUri;
    private ImageView image_view;
    private String savecurrentDate;
    private String saveCurrentTime;
    private String saveRandomName;
    private String downloadUrl;
    private String current_user_id;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private DatabaseReference reference;
    private List<Slide> lstSlides ;
    private ViewPager sliderpager;
    private TabLayout indicator;
    private RecyclerView MoviesRV ;
    private StorageReference userProfileImageRef;
    private final long countPosts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_photo_advert);

        final TextView name = findViewById(R.id.name);
        final ImageView profile = findViewById(R.id.profile);
        final Spinner spinnner = findViewById(R.id.spinner);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        mAuth =FirebaseAuth.getInstance();
        current_user_id =mAuth.getCurrentUser().getUid();
        loadingBar = new ProgressDialog(this);
        userProfileImageRef = FirebaseStorage.getInstance().getReference("Profile Images");
        userRef = FirebaseDatabase.getInstance().getReference().child("Members").child(current_user_id);
        postRef_private = FirebaseDatabase.getInstance().getReference().child("Post_photos_private");
        postRef_public = FirebaseDatabase.getInstance().getReference().child("Post_photos_public");
        postnotification = FirebaseDatabase.getInstance().getReference().child("notifications");

        reference = FirebaseDatabase.getInstance().getReference().child("Members").child(current_user_id);
        postImagereference = FirebaseStorage.getInstance().getReference();
        add_photo = findViewById(R.id.add_photo);
        image_view = findViewById(R.id.image_view);
        post = findViewById(R.id.post);
        userProfileImageRef = FirebaseStorage.getInstance().getReference("Profile Images");


        com.facebook.ads.AdView adViews = new com.facebook.ads.AdView(PostTestimonyPhotoActivity.this, getString(R.string.fb_placement_banner), com.facebook.ads.AdSize.BANNER_HEIGHT_50);
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
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mode = spinnner.getSelectedItem().toString();if(mode.equals("Who sees your Post?")){
                } if(mode.equals("My Church Members Only")){
                    ValidatePostInfoPrivate();
                }

                else if(mode.equals("Everyone")){

                    ValidatePostInfoPublic();
                }
                else {
                    Toast.makeText(PostTestimonyPhotoActivity.this, "Please select who sees your post", Toast.LENGTH_SHORT).show();

                }
        }
        });
        add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
        spinnner.setOnItemSelectedListener(this);

        ArrayAdapter<String> aa = new ArrayAdapter<>(this, R.layout.spinner_text_color, church);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner

        spinnner.setAdapter(aa);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Member patients = dataSnapshot.getValue(Member.class);
                assert patients != null;
                name.setText(patients.getName());
                if (patients.getProfileImage().equals("default")) {
                    profile.setImageResource(R.drawable.user);
                } else {
                    Picasso.get().load(patients.getProfileImage()).into(profile);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void ValidatePostInfoPrivate() {
    if(ImageUri == null){
            Toast.makeText(this, "You haven't attached a file yet", Toast.LENGTH_SHORT).show();
        }
        else{
            final ProgressDialog progressDialog =new ProgressDialog(PostTestimonyPhotoActivity.this);
            progressDialog.setMessage("Uploading..");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();
            StoringImageToPrivate();
        }
    }

    private void StoringImageToPrivate() {
        Calendar callFordate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
        savecurrentDate = currentDate.format(callFordate.getTime());


        Calendar callForTIME = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(callForTIME.getTime());
        saveRandomName = current_user_id + savecurrentDate + saveCurrentTime;
        final StorageReference filepath =postImagereference.child("Post Images").child(ImageUri.getLastPathSegment() + saveRandomName + ".jpg");
        final UploadTask uploadTask = filepath.putFile(ImageUri);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()){
                    throw task.getException();
                }
                return filepath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                final Uri downloadUri = task.getResult();
                final String mUri = downloadUri.toString();
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String username = snapshot.child("name").getValue().toString().trim();
                            String profile = snapshot.child("profileImage").getValue().toString().trim();
                            String chuc = snapshot.child("church").getValue().toString().trim();
                            String count = savecurrentDate + saveCurrentTime;
                            HashMap hashMap = new HashMap();
                            hashMap.put("userid",current_user_id);
                            hashMap.put("name",username);
                            hashMap.put("profileImage",profile);
                            hashMap.put("postImage",mUri);
                            hashMap.put("date",savecurrentDate);
                            hashMap.put("time",saveCurrentTime);
                            hashMap.put("church",chuc);
                            hashMap.put("Counter",count);
                            hashMap.put("postmode","photowithtext");
                            hashMap.put("confidentiality","private");
                            hashMap.put("search",username.toLowerCase());

                            postRef_public.child(saveRandomName).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if(task.isSuccessful()){
                                      finish();
                                        Toast.makeText(PostTestimonyPhotoActivity.this, "Photo Posted Successfully", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                    else
                                    {

                                        Toast.makeText(PostTestimonyPhotoActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
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
        });
    }

    private void ValidatePostInfoPublic() {

        if(ImageUri == null){
            Toast.makeText(this, "You haven't attached a file yet", Toast.LENGTH_SHORT).show();
        }
        else{
            final ProgressDialog progressDialog =new ProgressDialog(PostTestimonyPhotoActivity.this);
            progressDialog.setMessage("Uploading..");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();
         StoringImageToPublic();
        }

    }

    private void StoringImageToPublic() {
        Calendar callFordate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
        savecurrentDate = currentDate.format(callFordate.getTime());


        Calendar callForTIME = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(callForTIME.getTime());
        saveRandomName = current_user_id + savecurrentDate + saveCurrentTime;
        final StorageReference filepath =postImagereference.child("Post Images").child(ImageUri.getLastPathSegment() + saveRandomName + ".jpg");
        final UploadTask uploadTask = filepath.putFile(ImageUri);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()){
                    throw task.getException();
                }
                return filepath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                final Uri downloadUri = task.getResult();
                final String mUri = downloadUri.toString();
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                  if(snapshot.exists()){
                      String username = snapshot.child("name").getValue().toString().trim();
                      String profile = snapshot.child("profileImage").getValue().toString().trim();
                      String chuc = snapshot.child("church").getValue().toString().trim();
                      String count = savecurrentDate + saveCurrentTime;
                      HashMap hashMap = new HashMap();
                      hashMap.put("userid",current_user_id);
                      hashMap.put("name",username);
                      hashMap.put("profileImage",profile);
                      hashMap.put("postImage",mUri);
                      hashMap.put("date",savecurrentDate);
                      hashMap.put("time",saveCurrentTime);
                      hashMap.put("church",chuc);
                      hashMap.put("Counter",count);
                      hashMap.put("postmode","photowithtext");
                      hashMap.put("confidentiality","public");
                      hashMap.put("search",username.toLowerCase());

                      postRef_public.child(saveRandomName).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                          @Override
                          public void onComplete(@NonNull Task task) {
                              if(task.isSuccessful()){
                                finish();
                                  Toast.makeText(PostTestimonyPhotoActivity.this, "Photo Posted Successfully", Toast.LENGTH_SHORT).show();
                                  loadingBar.dismiss();
                              }
                              else
                              {

                                  Toast.makeText(PostTestimonyPhotoActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
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
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            ImageUri = data.getData();
            image_view.setImageURI(ImageUri);


        }

    }


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}