package com.glamour.faith;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.glamour.faith.Model.Member;
import com.glamour.faith.Model.PostPhoto;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.ponnamkarthik.richlinkpreview.MetaData;
import io.github.ponnamkarthik.richlinkpreview.ResponseListener;
import io.github.ponnamkarthik.richlinkpreview.RichLinkView;
import io.github.ponnamkarthik.richlinkpreview.RichLinkViewSkype;
import io.github.ponnamkarthik.richlinkpreview.RichPreview;
import io.github.ponnamkarthik.richlinkpreview.ViewListener;

public class YoutubeActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    TextView name,church;
    CircleImageView profile;
    RecyclerView recycler_post;
    Boolean LikeChecker = false;
    DatabaseReference likesRef;
    FirebaseAuth mAuth;
    String currentUserId;
    TextView load;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Members");
        likesRef = FirebaseDatabase.getInstance().getReference("Likes");
        recycler_post = findViewById(R.id.recycler_post);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recycler_post.setLayoutManager(linearLayoutManager);
        church = findViewById(R.id.church);
        name = findViewById(R.id.name);
        profile = findViewById(R.id.profile);
        load = findViewById(R.id.load);

        DisplayUserInfo();
        DisplayPhotoInPublic();
    }

    private void DisplayUserInfo() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String user = getIntent().getExtras().get("visit_user_id").toString();
                    String userName = getIntent().getExtras().get("userName").toString();
                    String profiles = getIntent().getExtras().get("profile").toString();
                    String churchs = getIntent().getExtras().get("church").toString();
                    Member member = dataSnapshot.getValue(Member.class);
                    if(member.getUserId().equals(user)){
                        Picasso.get().load(profiles).into(profile);
                        name.setText(userName +"'s " +"Youtube Songs");
                        church.setText(churchs);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void DisplayPhotoInPublic() {
        String user = getIntent().getExtras().get("visit_user_id").toString();
        Query postphotore = FirebaseDatabase.getInstance().getReference().child("Post_photos_public").orderByChild("postmode").equalTo("youtubelink");
        FirebaseRecyclerOptions<PostPhoto> options = new FirebaseRecyclerOptions.Builder<PostPhoto>()
                .setQuery(postphotore,PostPhoto.class)
                .build();
        FirebaseRecyclerAdapter<PostPhoto, ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PostPhoto, ViewHolder>(
                options
        ) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull PostPhoto model) {
                final String PostKey = getRef(position).getKey();
                String user = getIntent().getExtras().get("visit_user_id").toString();
                final MetaData[] data = new MetaData[1];

                if(model.getUserid().equals(user)) {
                    load.setVisibility(View.GONE);
                        holder.setName(model.getName());
                        holder.setChurch(model.getChurch());
                        holder.setDate(model.getDate());
                        holder.description.setVisibility(View.GONE);
                        holder.videoview.setVisibility(View.GONE);
                        holder.scripturebk.setVisibility(View.GONE);
                        holder.scripturecnt.setVisibility(View.GONE);
                        holder.image_view.setVisibility(View.GONE);
                        holder.setTime(model.getTime());
                        holder.setProfileImage(YoutubeActivity.this, model.getProfileImage());
                        holder.richLinkView.setLink(model.getLink(), new ViewListener() {
                            @Override
                            public void onSuccess(boolean status) {
                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
                        RichPreview richPreview = new RichPreview(new ResponseListener() {
                            @Override
                            public void onData(MetaData metaData) {
                                data[0] = metaData;
                                holder.richLinkView.setLinkFromMeta(metaData);

                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });






                    holder.setLikeButtonStatus(PostKey);
                    holder.setCommentStatus(PostKey);
                    holder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent clickPost = new Intent(YoutubeActivity.this, ClickPostActivity.class);
                            clickPost.putExtra("PostKey",PostKey);
                            clickPost.putExtra("DatabaseUserId",model.getUserid());
                            clickPost.putExtra("name",model.getName());
                            clickPost.putExtra("profile",model.getProfileImage());
                            clickPost.putExtra("postmode",model.getPostmode());
                            if(model.getPostmode().equals("youtubelink")){
                                clickPost.putExtra("link",model.getLink());
                            }
                            startActivity(clickPost);
                        }
                    });
                    holder.comment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent commentIntent = new Intent(YoutubeActivity.this, CommentsActivity.class);
                            commentIntent.putExtra("PostKey",PostKey);
                            String post = PostKey;
                            commentIntent.putExtra("postmode",model.getPostmode());
                            if(model.getPostmode().equals("youtubelink")){
                                commentIntent.putExtra("link",model.getLink());
                            }

                            startActivity(commentIntent);

                        }
                    });
                    holder.like.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            LikeChecker =true;
                            likesRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(LikeChecker.equals(true))
                                    {
                                        if(snapshot.child(PostKey).hasChild(currentUserId)){
                                            likesRef.child(PostKey).child(currentUserId).removeValue();
                                            LikeChecker = false;
                                        }else
                                        {
                                            likesRef.child(PostKey).child(currentUserId).setValue(true);
                                            LikeChecker=false;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }

                            });

                        }

                    });

                }
                else{
                    holder.cardView.setVisibility(View.GONE);
                    holder.posted.setVisibility(View.GONE);
                    holder.from.setVisibility(View.GONE);
                    holder.description.setVisibility(View.GONE);
                    holder.image_view.setVisibility(View.GONE);
                    holder.church.setVisibility(View.GONE);
                    holder.scripturebk.setVisibility(View.GONE);
                    holder.scripturecnt.setVisibility(View.GONE);
                    holder.date.setVisibility(View.GONE);
                    holder.time.setVisibility(View.GONE);
                    holder.videoview.setVisibility(View.GONE);
                    holder.profile.setVisibility(View.GONE);
                    holder.name.setVisibility(View.GONE);
                    holder.like.setVisibility(View.GONE);
                    holder.displayNolikes.setVisibility(View.GONE);
                    holder.comment.setVisibility(View.GONE);
                    holder.displayNocomment.setVisibility(View.GONE);
                    holder.coms.setVisibility(View.GONE);
                    holder.comss.setVisibility(View.GONE);
                    holder.comsss.setVisibility(View.GONE);
                    holder.comssss.setVisibility(View.GONE);
                    holder.comsssss.setVisibility(View.GONE);
                    holder.comssssss.setVisibility(View.GONE);
                    holder.comsssssss.setVisibility(View.GONE);
                    holder.comssssssss.setVisibility(View.GONE);
                    holder.richLinkView.setVisibility(View.GONE);
                }

            }
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View viewsd = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo,parent,false);
                return new ViewHolder(viewsd);
            }
        };
        firebaseRecyclerAdapter.startListening();
        recycler_post.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout like,comment;
        View mView;
        public TextView scripturebk,scripturecnt,description,from,posted,church,date,time,name,displayNocomment;
        int countLikes;
        ImageButton liked;
        String CurrentUserId;
        DatabaseReference likesRef,postphotoref;
        ImageView image_view,menudrop;
        PlayerView videoview;
        public LinearLayout cardView;
        public CircleImageView profile;
        public LinearLayout likeds,com,coms,comss,comsss,comssss,comsssss,comssssss,comsssssss,comssssssss;
        RichLinkViewSkype richLinkView;
        SimpleExoPlayer exoPlayer;

        public TextView displayNolikes;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            liked = mView.findViewById(R.id.liked);
            coms = mView.findViewById(R.id.coms);
            comss = mView.findViewById(R.id.comss);
            comsss = mView.findViewById(R.id.comsss);
            comssss= mView.findViewById(R.id.comssss);
            comsssss = mView.findViewById(R.id.comsssss);
            comssssss = mView.findViewById(R.id.comssssss);
            comsssssss = mView.findViewById(R.id.comsssssss);
            comssssssss = mView.findViewById(R.id.comssssssss);
            richLinkView = mView.findViewById(R.id.richLinkView);
            like = mView.findViewById(R.id.like);
            liked = mView.findViewById(R.id.liked);
            comment = mView.findViewById(R.id.comment);
            displayNocomment = mView.findViewById(R.id.displayNocomment);
            displayNolikes = mView.findViewById(R.id.displayNolikes);
            cardView = mView.findViewById(R.id.card);
            name = mView.findViewById(R.id.name);
            profile = mView.findViewById(R.id.profile);
            date = mView.findViewById(R.id.date);
            time = mView.findViewById(R.id.time);
            church = mView.findViewById(R.id.church);
            from = mView.findViewById(R.id.from);
            posted = mView.findViewById(R.id.posted);
            scripturebk = mView.findViewById(R.id.scripturebk);
            scripturecnt = mView.findViewById(R.id.scripturecnt);
            image_view = mView.findViewById(R.id.image_view);
            description = mView.findViewById(R.id.description);
            likesRef = FirebaseDatabase.getInstance().getReference("Likes");
            postphotoref = FirebaseDatabase.getInstance().getReference().child("Post_photos_public");
            CurrentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            videoview = mView.findViewById(R.id.videoview);
        }

        public LinearLayout getLike() {
            return like;
        }

        public void setName(String name) {
            TextView username = mView.findViewById(R.id.name);
            username.setText("@"+name);
        }
        public void setScriptureBook(String scriptureBook) {
            TextView scripturebook = itemView.findViewById(R.id.scripturebk);
            scripturebook.setText(scriptureBook);
        }
        public void setScriptureContent(String scriptureContent) {
            TextView scripturecontent = itemView.findViewById(R.id.scripturecnt);
            scripturecontent.setText(scriptureContent);
        }
        public void setLink(String link) {
            RichLinkView richLinkView = itemView.findViewById(R.id.richLinkView);
            richLinkView.setLink(link, new ViewListener() {
                @Override
                public void onSuccess(boolean status) {

                }

                @Override
                public void onError(Exception e) {

                }
            });
        }

        public void setCommentStatus(final String PostKey){
            postphotoref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child(PostKey).hasChild(CurrentUserId)){
                        countLikes = (int) snapshot.child(PostKey).child("comments").getChildrenCount();
                        displayNocomment.setText(countLikes + (" Comments"));
                    }
                    else
                    {
                        countLikes = (int) snapshot.child(PostKey).child("comments").getChildrenCount();
                        displayNocomment.setText(countLikes + (" Comments"));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            }) ;
        }
        public void setLikeButtonStatus(final String PostKey){
            likesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child(PostKey).hasChild(CurrentUserId)){
                        countLikes = (int) snapshot.child(PostKey).getChildrenCount();
                        liked.setImageResource(R.drawable.liked);
                        displayNolikes.setText(countLikes + (" Likes"));
                    }
                    else
                    {
                        countLikes = (int) snapshot.child(PostKey).getChildrenCount();
                        liked.setImageResource(R.drawable.like);
                        displayNolikes.setText(countLikes + (" Likes"));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            }) ;
        }
        public void setPostVideo(Context cx, String postVideo) {
            videoview = mView.findViewById(R.id.videoview);
            try {
                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(cx).build();
                TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                exoPlayer = ExoPlayerFactory.newSimpleInstance(cx);
                Uri video = Uri.parse(postVideo);
                DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(cx,"video");
                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                MediaSource mediaSource = new ExtractorMediaSource(video,dataSourceFactory,extractorsFactory,null,null);
                videoview.setPlayer(exoPlayer);
                exoPlayer.prepare(mediaSource);
                exoPlayer.setPlayWhenReady(false);



            }
            catch (Exception e){
                Log.e("ViewHolder","exoplayer error" +e.toString());
            }

        }

        public void setDescription(String description) {
            TextView describ = mView.findViewById(R.id.description);
            describ.setText(description);

        }
        public void setDate(String date) {
            TextView dat = mView.findViewById(R.id.date);
            dat.setText(date);
        }
        public void setTime(String time) {
            TextView tim = mView.findViewById(R.id.time);
            tim.setText(time);

        }
        public void setChurch(String church) {
            TextView chu = mView.findViewById(R.id.church);
            chu.setText(church);

        }
        public void setPostImage(Context ctx, String postImage) {
            ImageView image = mView.findViewById(R.id.image_view);
            Picasso.get().load(postImage).into(image);

        }
        public void setProfileImage(Context ctxs,String profileImage) {
            CircleImageView images = mView.findViewById(R.id.profile);
            Picasso.get().load(profileImage).into(images);

        }


    }


}