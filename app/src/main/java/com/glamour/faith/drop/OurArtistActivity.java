package com.glamour.faith.drop;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.glamour.faith.Model.Member;
import com.glamour.faith.Model.PostPhoto;
import com.glamour.faith.R;
import com.glamour.faith.YoutubeActivity;
import com.glamour.faith.adapters.ArtistAdapter;
import com.glamour.faith.adapters.MovieItemClickListener;
import com.glamour.faith.models.Movie;
import com.glamour.faith.ui.MovieDetailActivity;
import com.glamour.faith.ui.church_members.ChurchMembersFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class OurArtistActivity extends AppCompatActivity implements MovieItemClickListener {
    private RecyclerView MoviesRV ;
    private List<Member> artistList;
    private ArtistAdapter adapter;
    String image, bk,cnt;
TextView aa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_our_artist);
        MoviesRV = findViewById(R.id.Rv_movies);
        ImageSlider imageSlider = findViewById(R.id.image_slider);
        aa = findViewById(R.id.aa);

        artistList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        MoviesRV.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));

        //2. SELECT * FROM Artists WHERE id = "-LAJ7xKNj4UdBjaYr8Ju"
        MoviesRV = findViewById(R.id.Rv_movies);
        final List<SlideModel> remoteImages = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("Members").orderByChild("designation")
                .equalTo("Member/Artist").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                for(DataSnapshot data:datasnapshot.getChildren()){
                     image = data.child("profileImage").getValue().toString();
                     bk = data.child("name").getValue().toString();
                     cnt = data.child("church").getValue().toString();

                    String title = bk+" from "+ cnt;
                    remoteImages.add(new SlideModel(image,title, ScaleTypes.CENTER_INSIDE));
                    imageSlider.setImageList(remoteImages,ScaleTypes.CENTER_INSIDE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        AdView mAdView =findViewById(R.id.adView);
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
        // Recyclerview Setup


    }
    @Override
    public void onMovieClick(Movie movie, ImageView movieImageView) {
        // here we send movie information to detail activity
        // also we ll create the transition animation between the two activity
        Intent intent = new Intent(this,MovieDetailActivity.class);
        // send movie information to deatilActivity
        intent.putExtra("title",movie.getTitle());
        intent.putExtra("imgURL",movie.getThumbnail());
        intent.putExtra("imgCover",movie.getCoverPhoto());
        // lets crezte the animation
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(OurArtistActivity.this,
                movieImageView,"sharedName");

        startActivity(intent,options.toBundle());
        // i l make a simple test to see if the click works

        Toast.makeText(this,"item clicked : " + movie.getTitle(),Toast.LENGTH_LONG).show();
        // it works great
    }




    @Override
    protected void onStart() {
        super.onStart();
       DisplayArtists();
    }

    private void DisplayArtists() {
        Query query = FirebaseDatabase.getInstance().getReference("Members")
                .orderByChild("designation")
                .equalTo("Member/Artist");

        FirebaseRecyclerOptions<PostPhoto> options = new FirebaseRecyclerOptions.Builder<PostPhoto>()
                .setQuery(query,PostPhoto.class)
                .build();

        FirebaseRecyclerAdapter<PostPhoto, ChurchMembersFragment.ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PostPhoto, ChurchMembersFragment.ViewHolder>(
                options
        ) {
            @Override
            protected void onBindViewHolder(@NonNull ChurchMembersFragment.ViewHolder holder, int position, @NonNull PostPhoto model) {
                final String usersID = getRef(position).getKey();
                holder.setName(model.getName());
                holder.setChurch(model.getChurch());
                holder.setProfileImage(getApplicationContext(),model.getProfileImage());
                aa.setVisibility(View.GONE);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent profIntent = new Intent(OurArtistActivity.this, YoutubeActivity.class);
                        profIntent.putExtra("visit_user_id", usersID);
                        profIntent.putExtra("userName", model.getName());
                        profIntent.putExtra("profile", model.getProfileImage());
                        profIntent.putExtra("church", model.getChurch());

                        startActivity(profIntent);
                    }
                });

            }
            @NonNull
            @Override
            public ChurchMembersFragment.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View viewsd = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reverend,parent,false);
                return new ChurchMembersFragment.ViewHolder(viewsd);
            }
        };
        firebaseRecyclerAdapter.startListening();
        MoviesRV.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();
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
    }
}
