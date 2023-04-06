package com.glamour.faith.drop;

import android.os.Bundle;

import com.glamour.faith.Model.PostPhoto;
import com.glamour.faith.R;
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

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.GONE;

public class UpcomingEventsActivity extends AppCompatActivity {

    private AdView mAdView;
    private RecyclerView event;
    FirebaseAuth mAuth;
    String currentUser_id,chur;
    DatabaseReference mDatabaseRef,likesRef;
    CircleImageView profile;
    EditText searchbishop;
    TextView name,oops,privateevents,publicevent;
    int countLikes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_events);
        mAuth = FirebaseAuth.getInstance();
        currentUser_id = mAuth.getCurrentUser().getUid();
        likesRef = FirebaseDatabase.getInstance().getReference("events");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Members").child(currentUser_id);
        profile = findViewById(R.id.profile);
        privateevents = findViewById(R.id.privateevents);
        publicevent = findViewById(R.id.publicevent);
        name = findViewById(R.id.name);
        mAdView = findViewById(R.id.adView);
        oops = findViewById(R.id.oops);
        searchbishop = findViewById(R.id.searchbishop);
        event = findViewById(R.id.event);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UpcomingEventsActivity.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        event.setLayoutManager(linearLayoutManager);


        com.facebook.ads.AdView adViews = new com.facebook.ads.AdView(UpcomingEventsActivity.this, getString(R.string.fb_placement_banner), com.facebook.ads.AdSize.BANNER_HEIGHT_50);
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
        AdView adView = new AdView(UpcomingEventsActivity.this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getString(R.string.admob_app_id));
        displayEvents();
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

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
        likesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("confidentiality").equals("public")){
                    countLikes = (int) snapshot.getChildrenCount();
                    publicevent.setText(countLikes + (" Events around you"));
                }
                else if(snapshot.child("confidentiality").equals("private")){
                    countLikes = (int) snapshot.getChildrenCount();
                    privateevents.setText("You have " +countLikes +("Events around you"));
                }
                else
                {
                    publicevent.setText("0 Events around you");
                    privateevents.setText("You have 0 Events from your church");
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }) ;
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        chur = snapshot.child("church").getValue().toString();
        String username = snapshot.child("name").getValue().toString();
        String profiless = snapshot.child("profileImage").getValue().toString();
        String churchss = snapshot.child("church").getValue().toString();
        String gend = snapshot.child("gender").getValue().toString();
        String marital = snapshot.child("status").getValue().toString();
        String phones = snapshot.child("phone").getValue().toString();
        String design = snapshot.child("designation").getValue().toString();
        String userid = snapshot.child("userId").getValue().toString();
        Picasso.get().load(profiless).into(profile);
        name.setText(username);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



    }


    private void searchBishop(String s) {
        Query query = FirebaseDatabase.getInstance().getReference("events")
                .orderByChild("search")
                .startAt(s).endAt(s+"\uf8ff");

        FirebaseRecyclerOptions<PostPhoto> options = new FirebaseRecyclerOptions.Builder<PostPhoto>()
                .setQuery(query, PostPhoto.class)
                .build();

        FirebaseRecyclerAdapter<PostPhoto, ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PostPhoto,ViewHolder>(
                options
        ) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull PostPhoto model) {
                if(model.getPostmode().equals("events")){
                    holder.lin1.setVisibility(View.VISIBLE);
                    oops.setVisibility(GONE);
                    if(model.getConfidentiality().equals("public")){
                        holder.setChurch(model.getName()+ " on behalf of the whole ministry is welcoming you all to a "+model.getEvent()+" to be held on "+ model.getDate());
                        holder.setProfile(model.getProfileImage());
                        holder.setTime(model.getTime());
                        holder.setUsername(model.getName());
                        holder.setDate(model.getDate());
                        holder.setVenue(model.getVenue());
                        holder.setEvent(model.getEvent());}

                    else if(model.getConfidentiality().equals("private")){
                        if(chur.equals(model.getChurch())){
                            holder.setChurch(model.getName()+ " on behalf of the " +model.getChurch()+ " is welcoming you all to a "+model.getEvent()+" to be held on "+ model.getDate());
                            holder.setProfile(model.getProfileImage());
                            holder.setTime(model.getTime());
                            holder.setUsername(model.getName());
                            holder.setDate(model.getDate());
                            holder.setVenue(model.getVenue());
                            holder.setEvent(model.getEvent());
                        }
                        else {

                            holder.profiles.setVisibility(GONE);
                            holder.times.setVisibility(GONE);
                            holder.name.setVisibility(GONE);
                            holder.dates.setVisibility(GONE);
                            holder.venues.setVisibility(GONE);
                            holder.eventtxt.setVisibility(GONE);
                            holder.lin1.setVisibility(GONE);
                            holder.lin7.setVisibility(GONE);
                            holder.lin2.setVisibility(GONE);
                            holder.lin8.setVisibility(GONE);
                            holder.lindate.setVisibility(GONE);
                            holder.linvenue.setVisibility(GONE);
                            holder.linprof.setVisibility(GONE);
                            holder.linname.setVisibility(GONE);


                        }

                    }
                    else {

                        holder.profiles.setVisibility(GONE);
                        holder.times.setVisibility(GONE);
                        holder.name.setVisibility(GONE);
                        holder.dates.setVisibility(GONE);
                        holder.venues.setVisibility(GONE);
                        holder.eventtxt.setVisibility(GONE);
                        holder.lin1.setVisibility(GONE);
                        holder.lin7.setVisibility(GONE);
                        holder.lin2.setVisibility(GONE);
                        holder.lin8.setVisibility(GONE);
                        holder.lindate.setVisibility(GONE);
                        holder.linvenue.setVisibility(GONE);
                        holder.linprof.setVisibility(GONE);
                        holder.linname.setVisibility(GONE);


                    }
                }
                else {

                    holder.profiles.setVisibility(GONE);
                    holder.times.setVisibility(GONE);
                    holder.name.setVisibility(GONE);
                    holder.dates.setVisibility(GONE);
                    holder.venues.setVisibility(GONE);
                    holder.eventtxt.setVisibility(GONE);
                    holder.lin1.setVisibility(GONE);
                    holder.lin7.setVisibility(GONE);
                    holder.lin2.setVisibility(GONE);
                    holder.lin8.setVisibility(GONE);
                    holder.lindate.setVisibility(GONE);
                    holder.linvenue.setVisibility(GONE);
                    holder.linprof.setVisibility(GONE);
                    holder.linname.setVisibility(GONE);


                }

            }
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View viewsd = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_even,parent,false);
                return new ViewHolder(viewsd);
            }
        };
        firebaseRecyclerAdapter.startListening();
        event.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();
    }

    private void displayEvents() {

        Query postphotore = FirebaseDatabase.getInstance().getReference().child("events");
        FirebaseRecyclerOptions<PostPhoto> options = new FirebaseRecyclerOptions.Builder<PostPhoto>()
                .setQuery(postphotore, PostPhoto.class)
                .build();

        FirebaseRecyclerAdapter<PostPhoto, ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PostPhoto,ViewHolder>(
                options
        ) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull PostPhoto model) {
             if(model.getPostmode().equals("events")){
                 holder.lin1.setVisibility(View.VISIBLE);
                 oops.setVisibility(GONE);
                 if(model.getConfidentiality().equals("public")){
                     holder.setChurch(model.getName()+ " on behalf of the whole ministry is welcoming you all to a "+model.getEvent()+" to be held on "+ model.getDate());
                     holder.setProfile(model.getProfileImage());
                     holder.setTime(model.getTime());
                     holder.setUsername(model.getName());
                     holder.setDate(model.getDate());
                     holder.setVenue(model.getVenue());
                     holder.setEvent(model.getEvent());}

                 else if(model.getConfidentiality().equals("private")){
                    if(chur.equals(model.getChurch())){
                        holder.setChurch(model.getName()+ " on behalf of the " +model.getChurch()+ " is welcoming you all to a "+model.getEvent()+" to be held on "+ model.getDate());
                        holder.setProfile(model.getProfileImage());
                        holder.setTime(model.getTime());
                        holder.setUsername(model.getName());
                        holder.setDate(model.getDate());
                        holder.setVenue(model.getVenue());
                        holder.setEvent(model.getEvent());
                    }
                    else {

                        holder.profiles.setVisibility(GONE);
                        holder.times.setVisibility(GONE);
                        holder.name.setVisibility(GONE);
                        holder.dates.setVisibility(GONE);
                        holder.venues.setVisibility(GONE);
                        holder.eventtxt.setVisibility(GONE);
                        holder.lin1.setVisibility(GONE);
                        holder.lin7.setVisibility(GONE);
                        holder.lin2.setVisibility(GONE);
                        holder.lin8.setVisibility(GONE);
                        holder.lindate.setVisibility(GONE);
                        holder.linvenue.setVisibility(GONE);
                        holder.linprof.setVisibility(GONE);
                        holder.linname.setVisibility(GONE);


                    }

                 }
                 else {

                     holder.profiles.setVisibility(GONE);
                     holder.times.setVisibility(GONE);
                     holder.name.setVisibility(GONE);
                     holder.dates.setVisibility(GONE);
                     holder.venues.setVisibility(GONE);
                     holder.eventtxt.setVisibility(GONE);
                     holder.lin1.setVisibility(GONE);
                     holder.lin7.setVisibility(GONE);
                     holder.lin2.setVisibility(GONE);
                     holder.lin8.setVisibility(GONE);
                     holder.lindate.setVisibility(GONE);
                     holder.linvenue.setVisibility(GONE);
                     holder.linprof.setVisibility(GONE);
                     holder.linname.setVisibility(GONE);


                 }
             }
             else {

                 holder.profiles.setVisibility(GONE);
                 holder.times.setVisibility(GONE);
                 holder.name.setVisibility(GONE);
                 holder.dates.setVisibility(GONE);
                 holder.venues.setVisibility(GONE);
                 holder.eventtxt.setVisibility(GONE);
                 holder.lin1.setVisibility(GONE);
                 holder.lin7.setVisibility(GONE);
                 holder.lin2.setVisibility(GONE);
                 holder.lin8.setVisibility(GONE);
                 holder.lindate.setVisibility(GONE);
                 holder.linvenue.setVisibility(GONE);
                 holder.linprof.setVisibility(GONE);
                 holder.linname.setVisibility(GONE);


             }

            }
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View viewsd = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_even,parent,false);
                return new ViewHolder(viewsd);
            }
        };
        firebaseRecyclerAdapter.startListening();
        event.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
       View mView;
       public CircleImageView profiles;
        public TextView name,eventtxt,dates,times,venues,churchss,eventname,datetext,tim,venue,from;
        public CardView cardView;
        public LinearLayout lin1,lin2,lin3,lin4,lin5,lin6,lin7,lin8,lin9, lindate, linvenue, linprof, linname;
        public View view,view1,view2,view3,view4;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
             name = mView.findViewById(R.id.name);
            eventtxt = mView.findViewById(R.id.eventtxt);
            dates = mView.findViewById(R.id.dates);
            times = mView.findViewById(R.id.times);
            venues = mView.findViewById(R.id.venues);
            churchss = mView.findViewById(R.id.churchss);
            profiles = mView.findViewById(R.id.profiles);
            cardView = mView.findViewById(R.id.card);


            tim = mView.findViewById(R.id.tim);
            venue = mView.findViewById(R.id.venue);
            from = mView.findViewById(R.id.from);

            lindate = mView.findViewById(R.id.lindate);
            linvenue = mView.findViewById(R.id.linvenue);
            linprof = mView.findViewById(R.id.linprof);
            linname = mView.findViewById(R.id.linname);



            lin1 = mView.findViewById(R.id.lin1);

            lin7 = mView.findViewById(R.id.lin7);
            lin2 = mView.findViewById(R.id.lin2);
            lin8 = mView.findViewById(R.id.lin8);

        }
        public void setEvent(String event) {
            eventtxt = mView.findViewById(R.id.eventtxt);
            eventtxt.setText(event);
        }
        public void setTime(String time) {
            times = mView.findViewById(R.id.times);
            times.setText(time);
        }
        public void setDate(String date) {
            dates = mView.findViewById(R.id.dates);
            dates.setText(date);
        }
        public void setVenue(String venue) {
            venues = mView.findViewById(R.id.venues);
            venues.setText(venue);
        }
        public void setUsername(String username) {
            name = mView.findViewById(R.id.name);
            name.setText(username);
        }
        public void setProfile(String profile) {
            profiles = mView.findViewById(R.id.profiles);
            Picasso.get().load(profile).into(profiles);
        }
        public void setChurch(String church) {
            churchss = mView.findViewById(R.id.churchss);
            churchss.setText(church);
        }
    }

}