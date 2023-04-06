package com.glamour.faith.ui.testimony;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.glamour.faith.ChatRoomActivity;
import com.glamour.faith.Model.ChatRoom;
import com.glamour.faith.Model.Member;
import com.glamour.faith.R;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TestimonyFragment extends Fragment {
    String[] gender = {"Select people", "Everyone", "My Church's Chat room", "My Church Youth's Chat room","My Church men's Chat room","My Church Women's Chat room","Youth's Chat room","Women's Chat room","Men's Chat room","Artist's Chat room","Bishops/Reverends/Pastors/Evangelist","Reverends","Bishops","Pastors","Evangelist"};
    private LinearLayout start_create;
    private FirebaseUser fuser;
    ImageSlider imageSlider;
    private DatabaseReference reference,postref;
    private TextView names,chur;
    private long countPosts = 0;
    private RecyclerView recycler_post;
    public String churc,youth,past,gend;
    private InterstitialAd interstitialAd;
    boolean  ad = false;

private List<String> chatlist;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_testimony, container, false);

        start_create = root.findViewById(R.id.start_create);
        AdView mAdView = root.findViewById(R.id.adView);
        names = root.findViewById(R.id.names);
        chur =root.findViewById(R.id.chur);
        recycler_post =root.findViewById(R.id.recycler_post);
        recycler_post.setLayoutManager(new GridLayoutManager(getContext(),1));

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Members").child(fuser.getUid());
        postref = FirebaseDatabase.getInstance().getReference("ChatRooms");
        chatlist = new ArrayList<>();

        com.facebook.ads.AdView adView = new com.facebook.ads.AdView(getContext(), getString(R.string.fb_placement_banner), com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        LinearLayout bannerContainer = root.findViewById(R.id.banner_container);
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
        AdView adViews = new AdView(getContext());
        adViews.setAdSize(AdSize.BANNER);
        adViews.setAdUnitId("ca-app-pub-2023195679775199/5791427352");
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        displayChatRooms();
        loadfbads();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Member patients = dataSnapshot.getValue(Member.class);
                assert patients != null;
                names.setText("Create");
                churc= patients.getChurch();
                youth = patients.getStatus();
                gend =patients.getGender();
                past = patients.getDesignation();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        start_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater1 = getLayoutInflater();
                View dialoglayout = inflater1.inflate(R.layout.dialog_create_chat_room,null);
                builder.setView(dialoglayout);
                ImageButton add = dialoglayout.findViewById(R.id.add);
                CircleImageView profile = dialoglayout.findViewById(R.id.profile);
                TextView name = dialoglayout.findViewById(R.id.name);
                Button btn_create = dialoglayout.findViewById(R.id.btn_create);
                Spinner spinnner =  dialoglayout.findViewById(R.id.spinner);
                ArrayAdapter aa = new ArrayAdapter(getContext(), R.layout.spinner_text_color, gender);
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //Setting the ArrayAdapter data on the Spinner
                spinnner.setAdapter(aa);
                EditText topic = dialoglayout.findViewById(R.id.topic);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        topic.setVisibility(View.VISIBLE);
                    }
                });
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Member patients = dataSnapshot.getValue(Member.class);
                        assert patients != null;
                        name.setText(patients.getName());

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

                btn_create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String top = topic.getText().toString();
                        String spin = spinnner.getSelectedItem().toString();
                        if(TextUtils.isEmpty(top)){
                            Toast.makeText(getContext(), "Please Write the Topic to proceed", Toast.LENGTH_SHORT).show();
                            topic.setError("Please Write the Topic to proceed");
                        }
                        else if(spin.equals("Select people")){
                            Toast.makeText(getContext(), "Please Who is invited", Toast.LENGTH_SHORT).show();

                        }
                        else {
                            ProgressDialog  progressDialog = new ProgressDialog(getContext());
                            progressDialog.setMessage("Creating Chat Room");
                            progressDialog.setCanceledOnTouchOutside(true);
                            progressDialog.show();
                            FirebaseAuth mAuth = FirebaseAuth.getInstance();
                            String CurrenTuserID = mAuth.getCurrentUser().getUid();
                            String savecurrentDate,saveCurrentTime,saveRandomName;
                            Calendar callFordate = Calendar.getInstance();
                            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                            savecurrentDate = currentDate.format(callFordate.getTime());


                            Calendar callForTIME = Calendar.getInstance();
                            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
                            saveCurrentTime = currentTime.format(callForTIME.getTime());
                            saveRandomName = CurrenTuserID + savecurrentDate + saveCurrentTime;
                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        String username = snapshot.child("name").getValue().toString().trim();
                                        String profile = snapshot.child("profileImage").getValue().toString().trim();
                                        String chuc = snapshot.child("church").getValue().toString().trim();
                                        String designation = snapshot.child("designation").getValue().toString().trim();
                                        String marriage = snapshot.child("status").getValue().toString().trim();
                                        String date = savecurrentDate +" at " +saveCurrentTime;
                                        HashMap hashMap = new HashMap();
                                        hashMap.put("name",username);
                                        hashMap.put("profileImage",profile);
                                        hashMap.put("useriD",CurrenTuserID);
                                        hashMap.put("chatID",saveRandomName);
                                        hashMap.put("date",date);
                                        hashMap.put("time",saveCurrentTime);
                                        hashMap.put("church",chuc);
                                        hashMap.put("Counter",countPosts);
                                        hashMap.put("topic",top);
                                        hashMap.put("whoisInvited",spin);
                                        hashMap.put("designation",designation);
                                        hashMap.put("maritalstatus",marriage);

                                        postref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(snapshot.exists()){

                                                    countPosts = snapshot.getChildrenCount();

                                                }
                                                else{
                                                    countPosts = 0;
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                        postref.child(saveRandomName).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(getContext(), "Chat Room created Successfully", Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
                                                }
                                                else
                                                {

                                                    Toast.makeText(getContext(), "Error Occurred", Toast.LENGTH_SHORT).show();
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

                builder.show();
            }
        });

        return root;
    }
    private void loadfbads() {

        interstitialAd = new InterstitialAd(getContext(), getString(R.string.fb_placement_Interstitial));
        InterstitialAdListener madlistner = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                Toast.makeText(getContext(), "Facebook Ads Loaded successfully", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {

                //// on Interstitial dismissed
//                Intent out = new Intent();
//                out.putExtra("SAVE", Boolean.TRUE);
//                setResult(RESULT_OK, out);
//                finish();
            }

            @Override
            public void onError(Ad ad, AdError adError) {

                /// on error ad loading

            }

            @Override
            public void onAdLoaded(Ad ad) {
                interstitialAd.show();
            }
            @Override
            public void onAdClicked(Ad ad) {
                //// on ad clicked
                /*Intent out = new Intent();
                out.putExtra("SAVE", Boolean.TRUE);
                setResult(RESULT_OK, out);
                finish();*/
            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        };
        interstitialAd.loadAd(interstitialAd.buildLoadAdConfig().withAdListener(madlistner).build());


    }

    private void displayChatRooms() {
        Query query = FirebaseDatabase.getInstance().getReference("ChatRooms");

        FirebaseRecyclerOptions<ChatRoom> options = new FirebaseRecyclerOptions.Builder<ChatRoom>()
                .setQuery(query,ChatRoom.class)
                .build();

        FirebaseRecyclerAdapter<ChatRoom, ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ChatRoom, ViewHolder>(
                options
        ) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull ChatRoom model) {
                final String PostKey = getRef(position).getKey();

          if(model.getWhoisInvited().equals("Everyone")){
                   holder.setName(model.getName());
                   holder.setProfileImage(getContext(),model.getProfileImage());
                   holder.setWhoisInvited(model.getWhoisInvited());
                   holder.setTopic(model.getTopic());


                   holder.join.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           Intent chatIntent = new Intent(getContext(), ChatRoomActivity.class);
                           chatIntent.putExtra("PostKey",PostKey);
                           chatIntent.putExtra("userName",model.getName());
                           chatIntent.putExtra("profile",model.getProfileImage());
                           chatIntent.putExtra("topics",model.getTopic());
                           startActivity(chatIntent);
                       }
                   });
        }

               else if(model.getWhoisInvited().equals("My Church's Chat room")){

                   if(model.getChurch().equals(churc)){

                       holder.setName(model.getName());
                       holder.setProfileImage(getContext(),model.getProfileImage());
                       holder.setWhoisInvited(model.getWhoisInvited());
                       holder.setTopic(model.getTopic());

                       holder.join.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View view) {
                               Intent chatIntent = new Intent(getContext(), ChatRoomActivity.class);
                               chatIntent.putExtra("PostKey",PostKey);
                               chatIntent.putExtra("userName",model.getName());
                               chatIntent.putExtra("profile",model.getProfileImage());
                               chatIntent.putExtra("topics",model.getTopic());
                               startActivity(chatIntent);
                           }
                       });

                   }
                   else{
                       holder.chat.setVisibility(View.GONE);
                       holder.lin1.setVisibility(View.GONE);
                       holder.lin7.setVisibility(View.GONE);
                       holder.lin8.setVisibility(View.GONE);
                       holder.lin9.setVisibility(View.GONE);
                       holder.mView.setVisibility(View.GONE);
                       holder.profile.setVisibility(View.GONE);
                       holder.names.setVisibility(View.GONE);
                       holder.invitees.setVisibility(View.GONE);
                       holder.topics.setVisibility(View.GONE);
                       holder.join.setVisibility(View.GONE);
                       holder.btn.setVisibility(View.GONE);
                       holder.btn1.setVisibility(View.GONE);
                       chatlist.clear();}
               }

          else if(model.getWhoisInvited().equals("Youth's Chat room")){

              if(youth.equals("Single")){
                  holder.setName(model.getName());
                  holder.setProfileImage(getContext(),model.getProfileImage());
                  holder.setWhoisInvited(model.getWhoisInvited());
                  holder.setTopic(model.getTopic());


                  holder.join.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          Intent chatIntent = new Intent(getContext(), ChatRoomActivity.class);
                          chatIntent.putExtra("PostKey",PostKey);
                          chatIntent.putExtra("userName",model.getName());
                          chatIntent.putExtra("profile",model.getProfileImage());
                          chatIntent.putExtra("topics",model.getTopic());
                          startActivity(chatIntent);
                      }
                  });

              }
              else{
                  holder.chat.setVisibility(View.GONE);
                  holder.lin1.setVisibility(View.GONE);
                  holder.lin7.setVisibility(View.GONE);
                  holder.lin8.setVisibility(View.GONE);
                  holder.lin9.setVisibility(View.GONE);
                  holder.mView.setVisibility(View.GONE);
                  holder.profile.setVisibility(View.GONE);
                  holder.names.setVisibility(View.GONE);
                  holder.invitees.setVisibility(View.GONE);
                  holder.topics.setVisibility(View.GONE);
                  holder.join.setVisibility(View.GONE);
                  holder.btn.setVisibility(View.GONE);
                  holder.btn1.setVisibility(View.GONE);
                  chatlist.clear();}
          }


          else if(model.getWhoisInvited().equals("Pastors")){

              if(past.equals("Pastor")){
                  holder.setName(model.getName());
                  holder.setProfileImage(getContext(),model.getProfileImage());

                  holder.setWhoisInvited(model.getWhoisInvited());
                  holder.setTopic(model.getTopic());

                  holder.join.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          Intent chatIntent = new Intent(getContext(), ChatRoomActivity.class);
                          chatIntent.putExtra("PostKey",PostKey);
                          chatIntent.putExtra("userName",model.getName());
                          chatIntent.putExtra("profile",model.getProfileImage());
                          chatIntent.putExtra("topics",model.getTopic());
                          startActivity(chatIntent);
                      }
                  });

              }
              else{
                  holder.chat.setVisibility(View.GONE);
                  holder.lin1.setVisibility(View.GONE);
                  holder.lin7.setVisibility(View.GONE);
                  holder.lin8.setVisibility(View.GONE);
                  holder.lin9.setVisibility(View.GONE);
                  holder.mView.setVisibility(View.GONE);
                  holder.profile.setVisibility(View.GONE);
                  holder.names.setVisibility(View.GONE);
                  holder.invitees.setVisibility(View.GONE);
                  holder.topics.setVisibility(View.GONE);
                  holder.join.setVisibility(View.GONE);
                  holder.btn.setVisibility(View.GONE);
                  holder.btn1.setVisibility(View.GONE);
                  chatlist.clear();}
          }
          else if(model.getWhoisInvited().equals("Artist's Chat room")){

              if(model.getDesignation().equals("Member/Artist")){
                  holder.setName(model.getName());
                  holder.setProfileImage(getContext(),model.getProfileImage());

                  holder.setWhoisInvited(model.getWhoisInvited());
                  holder.setTopic(model.getTopic());


                  holder.join.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          Intent chatIntent = new Intent(getContext(), ChatRoomActivity.class);
                          chatIntent.putExtra("PostKey",PostKey);
                          chatIntent.putExtra("userName",model.getName());
                          chatIntent.putExtra("profile",model.getProfileImage());
                          chatIntent.putExtra("topics",model.getTopic());
                          startActivity(chatIntent);
                      }
                  });

              }
              else{
                  holder.chat.setVisibility(View.GONE);
                  holder.lin1.setVisibility(View.GONE);
                  holder.lin7.setVisibility(View.GONE);
                  holder.lin8.setVisibility(View.GONE);
                  holder.lin9.setVisibility(View.GONE);
                  holder.mView.setVisibility(View.GONE);
                  holder.profile.setVisibility(View.GONE);
                  holder.names.setVisibility(View.GONE);
                  holder.invitees.setVisibility(View.GONE);
                  holder.topics.setVisibility(View.GONE);
                  holder.join.setVisibility(View.GONE);
                  holder.btn.setVisibility(View.GONE);
                  holder.btn1.setVisibility(View.GONE);
                  chatlist.clear();}
          }
          else if(model.getWhoisInvited().equals("Bishops")){

              if(past.equals("Bishop")){
                  holder.setName(model.getName());
                  holder.setProfileImage(getContext(),model.getProfileImage());

                  holder.setWhoisInvited(model.getWhoisInvited());
                  holder.setTopic(model.getTopic());


                  holder.join.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          Intent chatIntent = new Intent(getContext(), ChatRoomActivity.class);
                          chatIntent.putExtra("PostKey",PostKey);
                          chatIntent.putExtra("userName",model.getName());
                          chatIntent.putExtra("profile",model.getProfileImage());
                          chatIntent.putExtra("topics",model.getTopic());
                          startActivity(chatIntent);
                      }
                  });

              }
              else{
                  holder.chat.setVisibility(View.GONE);
                  holder.lin1.setVisibility(View.GONE);
                  holder.lin7.setVisibility(View.GONE);
                  holder.lin8.setVisibility(View.GONE);
                  holder.lin9.setVisibility(View.GONE);
                  holder.mView.setVisibility(View.GONE);
                  holder.profile.setVisibility(View.GONE);
                  holder.names.setVisibility(View.GONE);
                  holder.invitees.setVisibility(View.GONE);
                  holder.topics.setVisibility(View.GONE);
                  holder.join.setVisibility(View.GONE);
                  holder.btn.setVisibility(View.GONE);
                  holder.btn1.setVisibility(View.GONE);
                  chatlist.clear();}
          }
          else if(model.getWhoisInvited().equals("Reverends")){

              if(past.equals("Reverend")){
                  holder.setName(model.getName());
                  holder.setProfileImage(getContext(),model.getProfileImage());

                  holder.setWhoisInvited(model.getWhoisInvited());
                  holder.setTopic(model.getTopic());

                  holder.join.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          Intent chatIntent = new Intent(getContext(), ChatRoomActivity.class);
                          chatIntent.putExtra("PostKey",PostKey);
                          chatIntent.putExtra("userName",model.getName());
                          chatIntent.putExtra("profile",model.getProfileImage());
                          chatIntent.putExtra("topics",model.getTopic());
                          startActivity(chatIntent);
                      }
                  });

              }
              else{
                  holder.chat.setVisibility(View.GONE);
                  holder.lin1.setVisibility(View.GONE);
                  holder.lin7.setVisibility(View.GONE);
                  holder.lin8.setVisibility(View.GONE);
                  holder.lin9.setVisibility(View.GONE);
                  holder.mView.setVisibility(View.GONE);
                  holder.profile.setVisibility(View.GONE);
                  holder.names.setVisibility(View.GONE);
                  holder.invitees.setVisibility(View.GONE);
                  holder.topics.setVisibility(View.GONE);
                  holder.join.setVisibility(View.GONE);
                  holder.btn.setVisibility(View.GONE);
                  holder.btn1.setVisibility(View.GONE);
                  chatlist.clear();}
          }
          else if(model.getWhoisInvited().equals("Evangelists")){

              if(past.equals("Evangelist")){
                  holder.setName(model.getName());
                  holder.setProfileImage(getContext(),model.getProfileImage());

                  holder.setWhoisInvited(model.getWhoisInvited());
                  holder.setTopic(model.getTopic());


                  holder.join.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          Intent chatIntent = new Intent(getContext(), ChatRoomActivity.class);
                          chatIntent.putExtra("PostKey",PostKey);
                          chatIntent.putExtra("userName",model.getName());
                          chatIntent.putExtra("profile",model.getProfileImage());
                          chatIntent.putExtra("topics",model.getTopic());
                          startActivity(chatIntent);
                      }
                  });

              }
              else{
                  holder.chat.setVisibility(View.GONE);
                  holder.lin1.setVisibility(View.GONE);
                  holder.lin7.setVisibility(View.GONE);
                  holder.lin8.setVisibility(View.GONE);
                  holder.lin9.setVisibility(View.GONE);
                  holder.mView.setVisibility(View.GONE);
                  holder.profile.setVisibility(View.GONE);
                  holder.names.setVisibility(View.GONE);
                  holder.invitees.setVisibility(View.GONE);
                  holder.topics.setVisibility(View.GONE);
                  holder.join.setVisibility(View.GONE);
                  holder.btn.setVisibility(View.GONE);
                  holder.btn1.setVisibility(View.GONE);
                  chatlist.clear();}
          }
          else if(model.getWhoisInvited().equals("Women's Chat room")){

              if(youth.equals("Married") && gend.equals("Female")){

                  holder.setName(model.getName());
                  holder.setProfileImage(getContext(),model.getProfileImage());

                  holder.setWhoisInvited(model.getWhoisInvited());
                  holder.setTopic(model.getTopic());


                  holder.join.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          Intent chatIntent = new Intent(getContext(), ChatRoomActivity.class);
                          chatIntent.putExtra("PostKey",PostKey);
                          chatIntent.putExtra("userName",model.getName());
                          chatIntent.putExtra("profile",model.getProfileImage());
                          chatIntent.putExtra("topics",model.getTopic());
                          startActivity(chatIntent);
                      }
                  });

              }
              else{
                  holder.chat.setVisibility(View.GONE);
                  holder.lin1.setVisibility(View.GONE);
                  holder.lin7.setVisibility(View.GONE);
                  holder.lin8.setVisibility(View.GONE);
                  holder.lin9.setVisibility(View.GONE);
                  holder.mView.setVisibility(View.GONE);
                  holder.profile.setVisibility(View.GONE);
                  holder.names.setVisibility(View.GONE);
                  holder.invitees.setVisibility(View.GONE);
                  holder.topics.setVisibility(View.GONE);
                  holder.join.setVisibility(View.GONE);
                  holder.btn.setVisibility(View.GONE);
                  holder.btn1.setVisibility(View.GONE);
                  chatlist.clear();}
          }
          else if(model.getWhoisInvited().equals("Men's Chat room")){

              if(youth.equals("Married") && gend.equals("Male")&&youth.equals("Widower")){

                  holder.setName(model.getName());
                  holder.setProfileImage(getContext(),model.getProfileImage());

                  holder.setWhoisInvited(model.getWhoisInvited());
                  holder.setTopic(model.getTopic());


                  holder.join.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          Intent chatIntent = new Intent(getContext(), ChatRoomActivity.class);
                          chatIntent.putExtra("PostKey",PostKey);
                          chatIntent.putExtra("userName",model.getName());
                          chatIntent.putExtra("profile",model.getProfileImage());
                          chatIntent.putExtra("topics",model.getTopic());
                          startActivity(chatIntent);
                      }
                  });
              }
              else{
                  holder.chat.setVisibility(View.GONE);
                  holder.lin1.setVisibility(View.GONE);
                  holder.lin7.setVisibility(View.GONE);
                  holder.lin8.setVisibility(View.GONE);
                  holder.lin9.setVisibility(View.GONE);
                  holder.mView.setVisibility(View.GONE);
                  holder.profile.setVisibility(View.GONE);
                  holder.names.setVisibility(View.GONE);
                  holder.invitees.setVisibility(View.GONE);
                  holder.topics.setVisibility(View.GONE);
                  holder.join.setVisibility(View.GONE);
                  holder.btn.setVisibility(View.GONE);
                  holder.btn1.setVisibility(View.GONE);
                  chatlist.clear();}
          }
           else if(model.getWhoisInvited().equals("Bishops/Reverends/Pastors/Evangelist")){
                    if(model.getDesignation().equals("Bishop")){

                        holder.setName(model.getName());

                        holder.setProfileImage(getContext(),model.getProfileImage());

                        holder.setWhoisInvited(model.getWhoisInvited());
                        holder.setTopic(model.getTopic());


                        holder.join.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent chatIntent = new Intent(getContext(), ChatRoomActivity.class);
                                chatIntent.putExtra("PostKey",PostKey);
                                chatIntent.putExtra("userName",model.getName());
                                chatIntent.putExtra("profile",model.getProfileImage());
                                chatIntent.putExtra("topics",model.getTopic());
                                startActivity(chatIntent);
                            }
                        });

                    }
                  else if(past.equals("Reverend")){
                        holder.setName(model.getName());
                        holder.setProfileImage(getContext(),model.getProfileImage());

                        holder.setWhoisInvited(model.getWhoisInvited());
                        holder.setTopic(model.getTopic());


                        holder.join.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent chatIntent = new Intent(getContext(), ChatRoomActivity.class);
                                chatIntent.putExtra("PostKey",PostKey);
                                chatIntent.putExtra("userName",model.getName());
                                chatIntent.putExtra("profile",model.getProfileImage());
                                chatIntent.putExtra("topics",model.getTopic());
                                startActivity(chatIntent);
                            }
                        });

                    }
                    else if(past.equals("Pastor")){
                        holder.setName(model.getName());
                        holder.setProfileImage(getContext(),model.getProfileImage());
                        holder.setWhoisInvited(model.getWhoisInvited());
                        holder.setTopic(model.getTopic());


                        holder.join.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent chatIntent = new Intent(getContext(), ChatRoomActivity.class);
                                chatIntent.putExtra("PostKey",PostKey);
                                chatIntent.putExtra("userName",model.getName());
                                chatIntent.putExtra("profile",model.getProfileImage());
                                chatIntent.putExtra("topics",model.getTopic());
                                startActivity(chatIntent);
                            }
                        });

                    }
                    else if(past.equals("Evangelist")){
                        holder.setName(model.getName());
                        holder.setProfileImage(getContext(),model.getProfileImage());
                        holder.setWhoisInvited(model.getWhoisInvited());
                        holder.setTopic(model.getTopic());


                        holder.join.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent chatIntent = new Intent(getContext(), ChatRoomActivity.class);
                                chatIntent.putExtra("PostKey",PostKey);
                                chatIntent.putExtra("userName",model.getName());
                                chatIntent.putExtra("profile",model.getProfileImage());
                                chatIntent.putExtra("topics",model.getTopic());
                                startActivity(chatIntent);
                            }
                        });
                    }

                    else{
                        holder.chat.setVisibility(View.GONE);
                        holder.lin1.setVisibility(View.GONE);
                        holder.lin7.setVisibility(View.GONE);
                        holder.lin8.setVisibility(View.GONE);
                        holder.lin9.setVisibility(View.GONE);
                        holder.mView.setVisibility(View.GONE);
                        holder.profile.setVisibility(View.GONE);
                        holder.names.setVisibility(View.GONE);
                        holder.invitees.setVisibility(View.GONE);
                        holder.topics.setVisibility(View.GONE);
                        holder.join.setVisibility(View.GONE);
                        holder.btn.setVisibility(View.GONE);
                        holder.btn1.setVisibility(View.GONE);
                        chatlist.clear();}

                }

          else{
              holder.chat.setVisibility(View.GONE);
              holder.lin1.setVisibility(View.GONE);
              holder.lin7.setVisibility(View.GONE);
              holder.lin8.setVisibility(View.GONE);
              holder.lin9.setVisibility(View.GONE);
              holder.mView.setVisibility(View.GONE);
              holder.profile.setVisibility(View.GONE);
              holder.names.setVisibility(View.GONE);
              holder.invitees.setVisibility(View.GONE);
              holder.topics.setVisibility(View.GONE);
              holder.join.setVisibility(View.GONE);
              holder.btn.setVisibility(View.GONE);
              holder.btn1.setVisibility(View.GONE);
                chatlist.clear();}

            }
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View viewsd = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_room,parent,false);
                return new ViewHolder(viewsd);
            }
        };
        firebaseRecyclerAdapter.startListening();
        recycler_post.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public CircleImageView profile;
        public  TextView names,church,design,invitees,topics,date,chat;
        public Button join;
        View mView;
        public LinearLayout lin1,lin7,lin8,lin9;
        private final ImageView btn;
        private final ImageView btn1;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            profile = mView.findViewById(R.id.profile);
            names = mView.findViewById(R.id.namesd);
            invitees = mView.findViewById(R.id.invitees);
            topics = mView.findViewById(R.id.topics);
            join = mView.findViewById(R.id.join);


            lin1 = mView.findViewById(R.id.lin1);
            btn1 = mView.findViewById(R.id.btn1);
            lin8 = mView.findViewById(R.id.lin8);
            lin7 = mView.findViewById(R.id.lin7);
            lin9 = mView.findViewById(R.id.lin9);
            chat = mView.findViewById(R.id.chat);
            btn = mView.findViewById(R.id.btn);

        }
        public void setProfileImage(Context ctxs, String profileImage) {
            CircleImageView profiles = mView.findViewById(R.id.profile);
            Glide.with(ctxs).load(profileImage).into(profiles);
        }
        public void setName(String name) {
            TextView username = mView.findViewById(R.id.namesd);
            username.setText(name +"'s ");
        }
        public void setWhoisInvited(String whoisInvited) {
            TextView who = mView.findViewById(R.id.invitees);
            who.setText(whoisInvited);
        }
        public void setTopic(String topic) {
            TextView tops = mView.findViewById(R.id.topics);
            tops.setText(topic);
        }
    }


}