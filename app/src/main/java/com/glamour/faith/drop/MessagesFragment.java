package com.glamour.faith.drop;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.glamour.faith.Model.Chat;
import com.glamour.faith.Model.Member;
import com.glamour.faith.R;
import com.glamour.faith.adapters.UsersAdapter;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessagesFragment extends Fragment {
private RecyclerView users_message;
private FirebaseUser fuser;
private DatabaseReference reference;
private UsersAdapter usersAdapter;
private List<Member> mUser;
private List<String> usersList;
TextView chatss;
    public MessagesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_messages, container, false);


        users_message = view.findViewById(R.id.users_message);
        chatss = view.findViewById(R.id.chatss);
        users_message.setLayoutManager(new LinearLayoutManager(getContext()));

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        usersList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                        usersList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Chat chat = dataSnapshot.getValue(Chat.class);
                            assert chat != null;
                            if(chat.getSender().equals(fuser.getUid())){
                                usersList.add(chat.getReceiver());
                            }
                            else if(chat.getReceiver().equals(fuser.getUid())){
                                usersList.add(chat.getSender());
                            }

                        }
                        readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        com.facebook.ads.AdView adViews = new com.facebook.ads.AdView(getContext(), getString(R.string.fb_placement_banner), com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        LinearLayout bannerContainer = view.findViewById(R.id.banner_container);
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
       AdView mAdView = view.findViewById(R.id.adView);
        AdView adView = new AdView(getContext());
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getString(R.string.admob_app_id));
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        return view;
    }

    private void readChats() {
        mUser = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Members");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUser.clear();
                chatss.setVisibility(View.GONE);
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Member member = dataSnapshot.getValue(Member.class);

                    for (String id : usersList){
                        assert member != null;
                        if(member.getUserId().equals(id)){
                            if(mUser.size() != 1){
                                mUser.add(member);

/*
                                for(Member member1:mUser){
                                    if(!member.getUserId().equals(member1.getUserId())){
                                        mUser.add(member);
                                    }
                                }
*/

                                //error is here.. if removed it works but it doesn't show one user per chat
                         }
                            else
                            {
                                mUser.add(member);
                            }
                        }
                    }

                }
                usersAdapter = new UsersAdapter(getContext(),mUser);
                users_message.setAdapter(usersAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}