package com.glamour.faith;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.glamour.faith.drop.ProfileActivity;
import com.glamour.faith.models.Slide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetUpActivity extends AppCompatActivity  implements
        AdapterView.OnItemSelectedListener {
    private static final int PICK_IMAGE_REQUEST = 1;
    private CircleImageView chooseImageBtn;
    private Button uploadBtn, submitchurch;
    String[] gender = {"Select Gender", "Male", "Female", "Other"};
    String[] marital_status = {"Select Marital Status", "Married", "Single", "Widow", "Widower"};
    String[] designation = {"Select Your Designation", "Bishop", "Reverend", "Pastor", "Evangelist","Member/Artist", "Member"};
    String[] church = {"Select Your Church Branch", "Meswondo", "Bosto", "Satiet"};
    private DatePicker datePicker;
    private TextView selected_date,dateofbirth;

    private TextInputEditText name;
    private TextInputEditText phone;
    private ImageView chosenImageView;
    private ProgressBar uploadProgressBar;
    private Spinner spinner, spinner2, spinner3, spinner4;

    private Uri ImageUri;
    private FirebaseUser fuser;
    private StorageReference userProfileImageRef;
    private DatabaseReference mDatabaseRef, churches,Reference;
    private StorageTask mUploadTask;
    private FirebaseAuth mAuth;
    String currentUser_id;
    ValueEventListener listener;
    EditText churchs;
    ArrayAdapter<String> adapter;
    ArrayList<String> spinnerDataList;
    private List<Slide> lstSlides ;
    private ViewPager sliderpager;
    private TabLayout indicator;
    private RecyclerView MoviesRV ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);
        spinner4 = findViewById(R.id.spinner4);
        chooseImageBtn = findViewById(R.id.button_choose_image);
        churchs = findViewById(R.id.church);
        submitchurch = findViewById(R.id.submitchurch);
        uploadBtn = findViewById(R.id.uploadBtn);
        dateofbirth = findViewById(R.id.dateofbirth);
        sliderpager = findViewById(R.id.slider_pager);

        mAuth = FirebaseAuth.getInstance();
        currentUser_id = mAuth.getCurrentUser().getUid();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Members").child(currentUser_id);
        churches = FirebaseDatabase.getInstance().getReference("Churches");
        Reference = FirebaseDatabase.getInstance().getReference("Churches");
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
        submitchurch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String church = churchs.getText().toString();
                if(TextUtils.isEmpty(church)){
                    Toast.makeText(SetUpActivity.this, "Please Enter church to submit", Toast.LENGTH_SHORT).show();
                    churchs.setError("PLease Enter Church to submit");
                }
                else
                {
                    ProgressDialog progressDialog = new ProgressDialog(SetUpActivity.this);
                    progressDialog.setMessage("Please Wait...");
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(true);
                    Reference.push().setValue(church).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                Toast.makeText(SetUpActivity.this, "Church submitted successfully", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                            else
                            {
                                String mess = task.getException().getMessage();
                                Toast.makeText(SetUpActivity.this, mess, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
                }}

        });

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

        spinnerDataList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(SetUpActivity.this,android.R.layout.simple_spinner_dropdown_item,spinnerDataList);
        spinner4.setAdapter(adapter);

       /* chooseImageBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });*/
/*
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                // TODO Auto-generated method stub

                dateofbirth.setText( "" + dayOfMonth +" / " + (month+1) + " / " + year);

            }
        });
*/
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    saveUserInfo();
            }
        });

        retrieveChurches();

        Spinner spin = findViewById(R.id.spinner2);
        Spinner spinnner = findViewById(R.id.spinner);
        Spinner spinnner3 = findViewById(R.id.spinner3);
        Spinner spinnner4 = findViewById(R.id.spinner4);

        spin.setOnItemSelectedListener(this);
        spinnner.setOnItemSelectedListener(this);
        spinnner3.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinner_text_color, gender);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        ArrayAdapter bb = new ArrayAdapter(this, R.layout.spinner_text_color, marital_status);
        bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter cc = new ArrayAdapter(this, R.layout.spinner_text_color, designation);
        cc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter dd = new ArrayAdapter(this, R.layout.spinner_text_color, church);
        dd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spin.setAdapter(aa);
        spinnner.setAdapter(bb);
        spinnner3.setAdapter(cc);
      /*  spinnner4.setAdapter(dd);*/

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

    private void saveUserInfo() {
        Spinner spin = findViewById(R.id.spinner2);
        Spinner spinnner =  findViewById(R.id.spinner);
        Spinner spinnner3 =  findViewById(R.id.spinner3);
        Spinner spinnner4 =  findViewById(R.id.spinner4);
        EditText dateofbirth =  findViewById(R.id.dateofbirth);
        TextInputEditText nam = findViewById(R.id.name);
        TextInputEditText phon =  findViewById(R.id.phone);

        String day = Objects.requireNonNull(dateofbirth.getText()).toString();
        String name = Objects.requireNonNull(nam.getText()).toString();
        String phone = Objects.requireNonNull(phon.getText()).toString();
        String gender = spin.getSelectedItem().toString();
        String designation = spinnner3.getSelectedItem().toString();
        String status = spinnner.getSelectedItem().toString();
        String church = spinnner4.getSelectedItem().toString();

        if (gender.equals("Select Gender")) {
            Toast.makeText(SetUpActivity.this, "Please Select your gender", Toast.LENGTH_SHORT).show();
        } else if (designation.equals("Select Your Designation")) {
            Toast.makeText(SetUpActivity.this, "Please Select your designation", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(day)) {
            Toast.makeText(SetUpActivity.this, "Please Enter Date of birth", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(name)) {
            Toast.makeText(SetUpActivity.this, "Please Enter Your Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(SetUpActivity.this, "Please Enter Your Phone Number", Toast.LENGTH_SHORT).show();
        } else if (status.equals("Select Marital Status")) {
            Toast.makeText(SetUpActivity.this, "Please Select your Marital Status", Toast.LENGTH_SHORT).show();
        } else if (church.equals("Select Your Church Branch")) {
            Toast.makeText(SetUpActivity.this, "Please Select your Church Branch", Toast.LENGTH_SHORT).show();
        } else {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(true);
            HashMap hashMap = new HashMap();
            hashMap.put("userId", currentUser_id);
            hashMap.put("gender", gender);
            hashMap.put("designation", designation);
            hashMap.put("status", status);
            hashMap.put("church", church);
            hashMap.put("name", name);
            hashMap.put("phone", phone);
            hashMap.put("dateOfBirth", day);
            hashMap.put("profileImage", "default");
            hashMap.put("search", name.toLowerCase());

            mDatabaseRef.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {

                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(SetUpActivity.this, AddPhotoActivity.class));
                        finish();
                    } else {
                        String message = task.getException().getMessage();
                        Toast.makeText(SetUpActivity.this, "Something went wrong " + message, Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
