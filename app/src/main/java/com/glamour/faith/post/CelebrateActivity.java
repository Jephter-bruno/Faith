package com.glamour.faith.post;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.glamour.faith.CelebrateFinalActivity;
import com.glamour.faith.Model.Member;
import com.glamour.faith.Model.PostPhoto;
import com.glamour.faith.OtherProfileActivity;
import com.glamour.faith.R;
import com.glamour.faith.drop.ProfileActivity;
import com.glamour.faith.drop.UserMessageActivity;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class CelebrateActivity extends AppCompatActivity implements
    AdapterView.OnItemSelectedListener {
        String[] church = { "Who sees your Post?","Everyone", "My Church Members Only"};

    private RecyclerView MoviesRV,recycle_pastor,recycle_reverend,recycle_bishop,artist;
    private DatabaseReference userRef, reference,postRef_public;
    private String savecurrentDate, saveCurrentTime, saveRandomName,current_user_id;
    private FirebaseAuth mAuth;
    EditText searchbishop;

    public String name, profile,churc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celebrate);

        recycle_bishop =findViewById(R.id.recycle_bishop);
        searchbishop = findViewById(R.id.searchbishop);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);


        postRef_public = FirebaseDatabase.getInstance().getReference().child("Post_photos_public");
        recycle_bishop.setLayoutManager(new GridLayoutManager(CelebrateActivity.this,3));

        mAuth =FirebaseAuth.getInstance();
        current_user_id =mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Members");
        reference = FirebaseDatabase.getInstance().getReference().child("Members").child(current_user_id);

        searchbishop.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                searchBishop(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        com.facebook.ads.AdView adViews = new com.facebook.ads.AdView(CelebrateActivity.this, getString(R.string.fb_placement_banner), com.facebook.ads.AdSize.BANNER_HEIGHT_50);
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

    reference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
       name = snapshot.child("name").getValue().toString();
        profile = snapshot.child("profileImage").getValue().toString();
       churc= snapshot.child("church").getValue().toString();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});
        DisplayArtists();

    }
    private void DisplayArtists() {
        Query query = FirebaseDatabase.getInstance().getReference("Members");

        FirebaseRecyclerOptions<PostPhoto> options = new FirebaseRecyclerOptions.Builder<PostPhoto>()
                .setQuery(query,PostPhoto.class)
                .build();

        FirebaseRecyclerAdapter<PostPhoto, ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PostPhoto, ViewHolder>(
                options
        ) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull PostPhoto model) {
                final String usersID = getRef(position).getKey();
                final String PostKey = getRef(position).getKey();
                holder.setName(model.getName());
                holder.setChurch(model.getChurch());
                holder.setProfileImage(getApplicationContext(),model.getProfileImage());
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(usersID.equals(current_user_id)){

                            Toast.makeText(CelebrateActivity.this, "You can't celebrate yourself", Toast.LENGTH_SHORT).show();

                        }else{
                            Intent profIntent = new Intent(CelebrateActivity.this, CelebrateFinalActivity.class);
                            profIntent.putExtra("visit_user_id", usersID);
                            profIntent.putExtra("userName", model.getName());
                            profIntent.putExtra("profile", model.getProfileImage());
                            profIntent.putExtra("church", model.getChurch());

                            startActivity(profIntent);
                        }

                    }
                });


            }
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View viewsd = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reverend,parent,false);
                return new ViewHolder(viewsd);
            }
        };
        firebaseRecyclerAdapter.startListening();
        recycle_bishop.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();
    }
    private void searchBishop(String s) {
        Query query = FirebaseDatabase.getInstance().getReference("Members")
                .orderByChild("name")
                .startAt(s).endAt(s+"\uf8ff");

        FirebaseRecyclerOptions<Member> options = new FirebaseRecyclerOptions.Builder<Member>()
                .setQuery(query, Member.class)
                .build();

        FirebaseRecyclerAdapter<Member, ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Member, ViewHolder>(
                options
        ) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Member model) {
                final String usersID = getRef(position).getKey();

                holder.setName(model.getName());
                holder.setChurch(model.getChurch());
                holder.setProfileImage(CelebrateActivity.this,model.getProfileImage());

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(CelebrateActivity.this);
                        LayoutInflater inflater1 = getLayoutInflater();
                        View dialoglayout = inflater1.inflate(R.layout.dialog_profile,null);
                        builder.setView(dialoglayout);

                        LinearLayout post_testimonys = dialoglayout.findViewById(R.id.post_testimonys);
                        LinearLayout photo_testimony = dialoglayout.findViewById(R.id.photo_testimony);
                        LinearLayout write_text = dialoglayout.findViewById(R.id.write_text);

                        CircleImageView profile = dialoglayout.findViewById(R.id.profile);
                        TextView name = dialoglayout.findViewById(R.id.name);
                        write_text.setVisibility(View.GONE);
                        name.setText(model.getName());
                        if(model.getProfileImage().equals("default"))
                        {
                            profile.setImageResource(R.drawable.user);
                        }
                        else {
                            Picasso.get().load(model.getProfileImage()).into(profile);
                        }
                        write_text.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if(usersID.equals(current_user_id)){
                                    Toast.makeText(CelebrateActivity.this, "You can't remove yourself", Toast.LENGTH_SHORT).show();
                                }else
                                {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(CelebrateActivity.this);
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
                                if(usersID.equals(current_user_id)){
                                    Toast.makeText(CelebrateActivity.this, "You can't send  message to yourself", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Intent chatIntent = new Intent(CelebrateActivity.this, UserMessageActivity.class);
                                    chatIntent.putExtra("userName",model.getName());
                                    chatIntent.putExtra("visit_user_id",usersID);
                                    chatIntent.putExtra("profile",model.getProfileImage());

                                    startActivity(chatIntent);}

                            }

                        });
                        photo_testimony.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(usersID.equals(current_user_id)){
                                    Intent profIntent = new Intent(CelebrateActivity.this, ProfileActivity.class);
                                    startActivity(profIntent);
                                }
                                else {
                                    Intent profIntent = new Intent(CelebrateActivity.this, OtherProfileActivity.class);
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
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View viewsd = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reverend,parent,false);
                return new ViewHolder(viewsd);
            }
        };
        firebaseRecyclerAdapter.startListening();
        recycle_bishop.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public CircleImageView profile;
        public TextView names,church;
        View mView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            profile = mView.findViewById(R.id.profile);
            names = mView.findViewById(R.id.name);
            church = mView.findViewById(R.id.church);
        }
        public void setProfileImage(Context ctxs, String profileImage) {
            CircleImageView profiles = mView.findViewById(R.id.profile);
            Picasso.get().load(profileImage).into(profiles);
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
}