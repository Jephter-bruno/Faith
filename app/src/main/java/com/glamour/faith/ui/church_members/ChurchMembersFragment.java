package com.glamour.faith.ui.church_members;

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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.glamour.faith.Model.Member;
import com.glamour.faith.OtherProfileActivity;
import com.glamour.faith.R;
import com.glamour.faith.drop.ProfileActivity;
import com.glamour.faith.drop.UserMessageActivity;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChurchMembersFragment extends Fragment {

    private RecyclerView MoviesRV,recycle_pastor,recycle_reverend,recycle_bishop,artist;

    private DatabaseReference userRef;
    private String  current_user_id;
    private FirebaseAuth mAuth;

    DatabaseReference dbArtists;
    private InterstitialAd interstitialAd;
    boolean  ad = false;
    EditText searchbishop,searchreverend, searchpastor, searchmember, searchartist;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_church_members, container, false);
        Spinner spinner = root.findViewById(R.id.spinner);
        searchbishop = root.findViewById(R.id.searchbishop);
        artist = root.findViewById(R.id.Rv_movies);
        recycle_bishop =root.findViewById(R.id.recycle_bishop);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recycle_bishop.setLayoutManager(new GridLayoutManager(getContext(),3));
        mAuth =FirebaseAuth.getInstance();
        current_user_id =mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Members");

        AdView mAdView = root.findViewById(R.id.adView);
        AdView adView = new AdView(getContext());
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getString(R.string.banner_ad_unit_id));

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

        DisplayBishops();
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        String [] values =
                { "All Church Members","Church Branch Only"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this.requireActivity(), R.layout.spinner_text_color, values);
        adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter1);
       String spinne = spinner.getSelectedItem().toString().trim();
loadfbads();
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

    private void searchBishop(String s) {
        Query query = FirebaseDatabase.getInstance().getReference("Members")
                .orderByChild("search")
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
                    holder.setProfileImage(getContext(),model.getProfileImage());

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                                    Toast.makeText(getContext(), "You can't remove yourself", Toast.LENGTH_SHORT).show();
                                }else
                                {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                                    Toast.makeText(getContext(), "You can't send  message to yourself", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Intent chatIntent = new Intent(getContext(),UserMessageActivity.class);
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
                                    Intent profIntent = new Intent(getContext(), ProfileActivity.class);
                                    startActivity(profIntent);
                                }
                                else {
                                    Intent profIntent = new Intent(getContext(), OtherProfileActivity.class);
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



    private void DisplayBishops() {
        Query query = FirebaseDatabase.getInstance().getReference("Members");

        FirebaseRecyclerOptions<Member> options = new FirebaseRecyclerOptions.Builder<Member>()
                .setQuery(query,Member.class)
                .build();

        FirebaseRecyclerAdapter<Member, ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Member, ViewHolder>(
                options
        ) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Member model) {
                final String usersID = getRef(position).getKey();
                holder.setName(model.getName());
                holder.setChurch(model.getChurch());
                holder.setProfileImage(getContext(),model.getProfileImage());

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                                    Toast.makeText(getContext(), "You can't remove yourself", Toast.LENGTH_SHORT).show();
                                }else
                                {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                                    Toast.makeText(getContext(), "You can't send  message to yourself", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Intent chatIntent = new Intent(getContext(),UserMessageActivity.class);
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
                                    Intent profIntent = new Intent(getContext(), ProfileActivity.class);
                                    startActivity(profIntent);
                                }
                                else {
                                    Intent profIntent = new Intent(getContext(), OtherProfileActivity.class);
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


    public static class ViewHolder extends RecyclerView.ViewHolder{
       public CircleImageView profile;
       public  TextView names,church;
       View mView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            profile = mView.findViewById(R.id.profile);
            names = mView.findViewById(R.id.name);
            church = mView.findViewById(R.id.church);
        }
        public void setProfileImage(Context ctxs,String profileImage) {
            CircleImageView profiles = mView.findViewById(R.id.profile);
           Glide.with(ctxs).load(profileImage).into(profiles);
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