package com.glamour.faith.post;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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

public class UpcomingEventActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {
    String[] church = {"Who sees your Post?,", "Everyone", "My Church Members Only"};
    String[] types = {"Please Select Event type", "Wedding", "Video/ Album Launch","Fundraiser","Visit","Evangelism/Crusade", "Conference"};
    private DatabaseReference userRef, postRef_private, postRef_public;

    private Button post;
    private String savecurrentDate, saveCurrentTime, saveRandomName, current_user_id;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private DatabaseReference reference;
    private TimePicker timePicker1;
    private TextView time, datePicked;
    private Calendar calendar;
    private CalendarView calendarView;
    private String format = "";

    Spinner spinnner, spinners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_event);
        final TextView name = findViewById(R.id.name);
        final ImageView profile = findViewById(R.id.profile);
          spinnner = findViewById(R.id.spinner);
          spinners = findViewById(R.id.spinners);
        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();
        loadingBar = new ProgressDialog(this);
        timePicker1 = findViewById(R.id.timePicker1);
        time = findViewById(R.id.time);
        calendarView = findViewById(R.id.calendarView);
        datePicked = findViewById(R.id.datePicked);
        calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        showTime(hour, min);

       userRef = FirebaseDatabase.getInstance().getReference().child("Members").child(current_user_id);
        postRef_private = FirebaseDatabase.getInstance().getReference().child("events ");
        postRef_public = FirebaseDatabase.getInstance().getReference().child("events");
        reference = FirebaseDatabase.getInstance().getReference().child("Members").child(current_user_id);
        post = findViewById(R.id.post);
        ImageSlider imageSlider = findViewById(R.id.image_slider);
        com.facebook.ads.AdView adViews = new com.facebook.ads.AdView(UpcomingEventActivity.this, getString(R.string.fb_placement_banner), com.facebook.ads.AdSize.BANNER_HEIGHT_50);
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
        userRef.addValueEventListener(new ValueEventListener() {
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
                    /*     String dateofbirth = datasnapshot.child("dateOfBirth").getValue().toString();*/
                    Picasso.get().load(profiless).into(profile);
                    name.setText(username);




                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
        //Setting the ArrayAdapter data on the Spinner
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
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                // TODO Auto-generated method stub

                datePicked.setText( "" + dayOfMonth +" / " + (month+1) + " / " + year);
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mode = spinnner.getSelectedItem().toString();
                String typ = spinners.getSelectedItem().toString();

                if(typ.equals("Please Select Event type")){
                    Toast.makeText(UpcomingEventActivity.this, "Please select the type of event", Toast.LENGTH_SHORT).show();

                }
               else if (mode.equals("My Church Members Only")) {

                      ValidateEventInfoPrivate();
                } else if (mode.equals("Everyone")) {

                   ValidateEventInfoPublic();
                } else {
                    Toast.makeText(UpcomingEventActivity.this, "Please select who sees your post", Toast.LENGTH_SHORT).show();

                }
            }
        });

        spinnner.setOnItemSelectedListener(this);

        ArrayAdapter<String> aa = new ArrayAdapter<>(this,R.layout.spinner_text_color, church);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner

        spinnner.setAdapter(aa);
        spinners.setOnItemSelectedListener(this);
        ArrayAdapter<String> ab = new ArrayAdapter<>(this,R.layout.spinner_text_color, types);
        ab.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner

        spinners.setAdapter(ab);

    }

    private void ValidateEventInfoPublic() {
        EditText event = findViewById(R.id.event);
        EditText venue = findViewById(R.id.venue);
        TextView time = findViewById(R.id.time);
        TextView date = findViewById(R.id.datePicked);

        String eventname = event.getText().toString();
        String venuename = venue.getText().toString();
        String timename  = time.getText().toString();
        String datename  = date.getText().toString();

        if(TextUtils.isEmpty(eventname)){
            Toast.makeText(this, "Please Enter the event name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(venuename)){
            Toast.makeText(this, "Please enter the venue of the event", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(timename)){
            Toast.makeText(this, "At what time will the event take place?", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(datename)){
            Toast.makeText(this, "When will the event take place?", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setMessage("Please wait....");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);
            saveEventInfoPublic();
        }
    }

    private void saveEventInfoPublic() {

        EditText event = findViewById(R.id.event);
        EditText venue = findViewById(R.id.venue);
        TextView time = findViewById(R.id.time);
        TextView date = findViewById(R.id.datePicked);

        final String eventname = event.getText().toString();
        final String venuename = venue.getText().toString();
        final String timename  = time.getText().toString();
        final String datename  = date.getText().toString();

        String typd = spinners.getSelectedItem().toString();

        if(TextUtils.isEmpty(eventname)){
            Toast.makeText(this, "Please Enter the event name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(venuename)){
            Toast.makeText(this, "Please enter the venue of the event", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(timename)){
            Toast.makeText(this, "At what time will the event take place?", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(datename)){
            Toast.makeText(this, "When will the event take place?", Toast.LENGTH_SHORT).show();
        }
        else{

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Calendar callFordate = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
                    savecurrentDate = currentDate.format(callFordate.getTime());

                    Calendar callForTIME = Calendar.getInstance();
                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
                    saveCurrentTime = currentTime.format(callForTIME.getTime());
                    saveRandomName = current_user_id + savecurrentDate + saveCurrentTime;
                    String username = snapshot.child("name").getValue().toString();
                    String profile = snapshot.child("profileImage").getValue().toString();
                    String church = snapshot.child("church").getValue().toString();
                     String design  = snapshot.child("designation").getValue().toString();
                    String date = savecurrentDate +" " +saveCurrentTime;
                    HashMap<String, Object> hashMap = new HashMap<String, Object>();
                    hashMap.put("event",eventname);
                    hashMap.put("search",eventname.toLowerCase());
                    hashMap.put("userid",current_user_id);
                    hashMap.put("time",timename);
                    hashMap.put("date",datename);
                    hashMap.put("venue",venuename);
                    hashMap.put("name",username);
                    hashMap.put("postmode","events");
                    hashMap.put("profileImage",profile);
                    hashMap.put("church",church);
                    hashMap.put("designation",design);
                    hashMap.put("confidentiality","public");
                    hashMap.put("day",date);
                    hashMap.put("eventType",typd);


                    postRef_public.child(saveRandomName).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){

                                Toast.makeText(UpcomingEventActivity.this, "Event Posted Successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                finish();
                            }
                            else
                            {

                                Toast.makeText(UpcomingEventActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
                }



                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    private void ValidateEventInfoPrivate() {

        final EditText event = findViewById(R.id.event);
        EditText venue = findViewById(R.id.venue);
        TextView time = findViewById(R.id.time);
        TextView date = findViewById(R.id.datePicked);

        final String eventname = event.getText().toString();
        final String venuename = venue.getText().toString();
        final String timename  = time.getText().toString();
        final String datename  = date.getText().toString();
        String typd = spinners.getSelectedItem().toString();

        if(TextUtils.isEmpty(eventname)){
            Toast.makeText(this, "Please Enter the event name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(venuename)){
            Toast.makeText(this, "Please enter the venue of the event", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(timename)){
            Toast.makeText(this, "At what time will the event take place?", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(datename)){
            Toast.makeText(this, "When will the event take place?", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setMessage("Please wait....");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String username = snapshot.child("name").getValue().toString();
                    String church = snapshot.child("church").getValue().toString();
                    String profile = snapshot.child("profileImage").getValue().toString();
                    Calendar callFordate = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
                    savecurrentDate = currentDate.format(callFordate.getTime());

                    Calendar callForTIME = Calendar.getInstance();
                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");

                    saveCurrentTime = currentTime.format(callForTIME.getTime());
                    saveRandomName = savecurrentDate + saveCurrentTime;
                    String date = savecurrentDate+saveCurrentTime;
                    String design  = snapshot.child("designation").getValue().toString();

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("event",eventname);
                    hashMap.put("search",eventname.toLowerCase());
                    hashMap.put("userid",current_user_id);
                    hashMap.put("time",timename);
                    hashMap.put("date",datename);
                    hashMap.put("venue",venuename);
                    hashMap.put("username",username);
                    hashMap.put("postmode","events");
                    hashMap.put("profile",profile);
                    hashMap.put("church",church);
                    hashMap.put("designation",design);
                    hashMap.put("confidentiality","private");
                    hashMap.put("day",date);
                    hashMap.put("eventType",typd);
                    postRef_public.child(saveRandomName).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){

                                Toast.makeText(UpcomingEventActivity.this, "Event Posted Successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                finish();
                            }
                            else
                            {

                                Toast.makeText(UpcomingEventActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
                }



                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }



    public void setTime(View view) {
        int hour = timePicker1.getCurrentHour();
        int min = timePicker1.getCurrentMinute();
        showTime(hour, min);
    }
    public void showTime(int hour, int min) {
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }

        time.setText(new StringBuilder().append(hour).append(" : ").append(min)
                .append(" ").append(format));
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