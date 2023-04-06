package com.glamour.faith;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.glamour.faith.Model.ChatRoomChats;
import com.glamour.faith.Model.Messages;
import com.glamour.faith.adapters.MessagesAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.ponnamkarthik.richlinkpreview.RichLinkViewSkype;
import io.github.ponnamkarthik.richlinkpreview.ViewListener;

public class ChatRoomActivity extends AppCompatActivity {
    private ImageButton send_image_file_button,send_button;
    private EditText comment_et;
    private RecyclerView message_list_users;
    private String messageReceiverID, messageRecieverName,topic, messageSenderID, savecurrentDate,saveCurrentTime,saveRandomName,messageReceiverProfile;
    private TextView receiverUserName;
    private CircleImageView receiverProfileImage;
    private DatabaseReference DatabaseRef;
    private FirebaseAuth mAuth;
    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessagesAdapter messagesAdapter;
    private static final int PICK_IMAGE_REQUEST = 1;
    private StorageReference userProfileImageRef;

    private StorageTask uploadTask;
    String currentUser_id;
    private Uri ImageUri;
    private String post_key;
    DatabaseReference UsersRef, commentRef;

    private RecyclerView recyclerView_comments;
    private ImageButton post_comments,add;
    private EditText editText_comment_input;

    private TextView names, churchss, dates, timess,description,scripturecnt,scripturebk;
    private ImageView image_view;
    private TextView custom_profile_name,custom_profile_topic;
    private CircleImageView custom_profile_image;
    public String churc, prof, name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        post_key = getIntent().getExtras().getString("PostKey");
        mAuth = FirebaseAuth.getInstance();
        currentUser_id = mAuth.getCurrentUser().getUid();
        userProfileImageRef = FirebaseStorage.getInstance().getReference("Images");

        UsersRef   = FirebaseDatabase.getInstance().getReference().child("Members").child(currentUser_id);

        DatabaseRef = FirebaseDatabase.getInstance().getReference("ChatRooms").child(post_key).child("chats");
        InitilizeFields();
        AdView mAdView = findViewById(R.id.adView);
        AdView adView = new AdView(ChatRoomActivity.this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getString(R.string.admob_app_id));
        MobileAds.initialize(ChatRoomActivity.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                churc = snapshot.child("church").getValue().toString();
                 prof = snapshot.child("profileImage").getValue().toString();
                 name = snapshot.child("name").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendMessage();
            }
        });
        post_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UsersRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String usename = snapshot.child("name").getValue().toString();
                            String profils = snapshot.child("profileImage").getValue().toString();
                            String churcss = snapshot.child("church").getValue().toString();
                            ValidateChat(usename,profils,churcss);
                            editText_comment_input.setText("");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }

    private void SendMessage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            ImageUri = data.getData();
            if(uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(this, "Upload in Progress", Toast.LENGTH_SHORT).show();
            }
            else
            {
                uploadImage();
            }
        }

    }
    private void uploadImage(){
        final ProgressDialog progressDialog =new ProgressDialog(ChatRoomActivity.this);
        progressDialog.setMessage("Uploading..");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();

        if(ImageUri != null){
            Calendar callFordate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
            savecurrentDate = currentDate.format(callFordate.getTime());

            Calendar callForTIME = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
            saveCurrentTime = currentTime.format(callForTIME.getTime());
            saveRandomName = savecurrentDate+saveCurrentTime;
            final StorageReference filereference = userProfileImageRef.child(ImageUri.getLastPathSegment() + saveRandomName + ".jpg");
            uploadTask = filereference.putFile(ImageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return filereference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();
                        Calendar callFordate = Calendar.getInstance();
                        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
                        savecurrentDate = currentDate.format(callFordate.getTime());


                        Calendar callForTIME = Calendar.getInstance();
                        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
                        saveCurrentTime = currentTime.format(callForTIME.getTime());
                        final String RandomKey = currentUser_id + savecurrentDate + saveCurrentTime;
                        String dates = savecurrentDate+" "+" "+" "+saveCurrentTime;
                        HashMap chatMap = new HashMap();
                        chatMap.put("uid",currentUser_id);
                        chatMap.put("chatimage",mUri);
                        chatMap.put("dates",dates);
                        chatMap.put("date",savecurrentDate);
                        chatMap.put("time",saveCurrentTime);
                        chatMap.put("profile",prof);
                        chatMap.put("username",name);
                        chatMap.put("church",churc);
                        chatMap.put("type","image");

                        DatabaseRef.child(RandomKey).setValue(chatMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(ChatRoomActivity.this, "Chat sent Successfully", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                                else
                                {
                                    String mess = task.getException().getMessage();
                                    Toast.makeText(ChatRoomActivity.this, mess, Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            }
                        });

                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ChatRoomActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }
        else
        {
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Query mDatabaseRef = FirebaseDatabase.getInstance().getReference("ChatRooms").child(post_key).child("chats").orderByChild("dates");
        FirebaseRecyclerOptions<ChatRoomChats> options =
                new FirebaseRecyclerOptions.Builder<ChatRoomChats>()
                        .setQuery(mDatabaseRef,ChatRoomChats.class)
                        .build();

        FirebaseRecyclerAdapter<ChatRoomChats, ChatsViewHolder> firebaseRecyclerAdapter =new FirebaseRecyclerAdapter<ChatRoomChats, ChatsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ChatsViewHolder holder, int position, @NonNull ChatRoomChats model) {
                if(model.getType().equals("text")){
                    if(model.getUid().equals(currentUser_id)){
                        if(URLUtil.isValidUrl(model.getChat())){
                            holder.setDates(model.getDates());
                            holder.nam.setVisibility(View.GONE);
                            holder.churchssss.setVisibility(View.GONE);
                            holder.profile.setVisibility(View.GONE);
                            holder.receivebuble.setVisibility(View.GONE);
                            holder.tim.setVisibility(View.GONE);
                            holder.cht.setVisibility(View.GONE);
                            holder.timet.setVisibility(View.GONE);
                            holder.from.setVisibility(View.GONE);
                            holder.reply.setVisibility(View.GONE);
                            holder.cardreceiver.setVisibility(View.GONE);
                            holder.cardsender.setVisibility(View.GONE);
                            holder.lin01.setVisibility(View.GONE);
                            holder.sender_message_text.setVisibility(View.GONE);
                            holder.richLinkViewSkype.setLink(model.getChat(), new ViewListener() {
                                @Override
                                public void onSuccess(boolean status) {

                                }

                                @Override
                                public void onError(Exception e) {

                                }
                            });
                        }

                       else{
                            holder.tim.setVisibility(View.GONE);
                            holder.nam.setVisibility(View.GONE);
                            holder.churchssss.setVisibility(View.GONE);
                            holder.profile.setVisibility(View.GONE);
                            holder.receivebuble.setVisibility(View.GONE);
                            holder.cht.setVisibility(View.GONE);
                            holder.timet.setVisibility(View.GONE);
                            holder.from.setVisibility(View.GONE);
                            holder.reply.setVisibility(View.GONE);
                            holder.setChat(model.getChat());
                            holder.setDates(model.getDates());
                            holder.cardreceiver.setVisibility(View.GONE);
                            holder.cardsender.setVisibility(View.GONE);
                        }
                    }
                    else{
                        if(URLUtil.isValidUrl(model.getChat())){
                            holder.setDates(model.getDates());
                            holder.nam.setVisibility(View.GONE);
                            holder.churchssss.setVisibility(View.GONE);
                            holder.profile.setVisibility(View.GONE);
                            holder.receivebuble.setVisibility(View.GONE);
                            holder.tim.setVisibility(View.GONE);
                            holder.cht.setVisibility(View.GONE);
                            holder.timet.setVisibility(View.GONE);
                            holder.from.setVisibility(View.GONE);
                            holder.reply.setVisibility(View.GONE);
                            holder.cardreceiver.setVisibility(View.GONE);
                            holder.cardsender.setVisibility(View.GONE);
                            holder.sender_message_text.setVisibility(View.GONE);
                            holder.richLinkViewSkypes.setLink(model.getChat(), new ViewListener() {
                                @Override
                                public void onSuccess(boolean status) {

                                }

                                @Override
                                public void onError(Exception e) {

                                }
                            });
                        }

                     else{
                            holder.tim.setVisibility(View.GONE);
                            holder.setProfile(getApplicationContext(),model.getProfile());
                            holder.setChat(model.getChat());
                            holder.setChurch(model.getChurch());
                            holder.setUsername(model.getUsername());
                            holder.setDates(model.getDates());
                            holder.sender_message_text.setVisibility(View.GONE);
                            holder.sentbubble.setVisibility(View.GONE);
                            holder.timest.setVisibility(View.GONE);
                            holder.cardsender.setVisibility(View.GONE);
                            holder.cardreceiver.setVisibility(View.GONE);
                        }
                    }
                }

                else if(model.getType().equals("image")){
                    if(model.getUid().equals(currentUser_id)){
                        holder.sender_message_text.setVisibility(View.GONE);
                        holder.nam.setVisibility(View.GONE);
                        holder.churchssss.setVisibility(View.GONE);
                        holder.profile.setVisibility(View.GONE);
                        holder.receivebuble.setVisibility(View.GONE);
                        holder.cht.setVisibility(View.GONE);
                        holder.timet.setVisibility(View.GONE);
                        holder.from.setVisibility(View.GONE);
                        holder.reply.setVisibility(View.GONE);
                        holder.setChat(model.getChat());
                        holder.setDates(model.getDates());
                        holder.cardreceiver.setVisibility(View.GONE);
                        holder.setChatimage(getApplicationContext(),model.getChatimage());
                        holder.tim.setVisibility(View.GONE);
                        holder.lin01.setVisibility(View.GONE);
                    }
                    else{
                        holder.cht.setVisibility(View.GONE);
                        holder.linearLayout.setVisibility(View.GONE);
                        holder.setDates(model.getDates());
                        holder.setProfile(getApplicationContext(),model.getProfile());
                        holder.setChat(model.getChat());
                        holder.setChurch(model.getChurch());
                        holder.setUsername(model.getUsername());
                        holder.setDates(model.getDates());
                        holder.sender_message_text.setVisibility(View.GONE);
                        holder.sentbubble.setVisibility(View.GONE);
                        holder.timest.setVisibility(View.GONE);
                        holder.cardsender.setVisibility(View.GONE);
                        holder.setChatimage(getApplicationContext(),model.getChatimage());
                    }
                }
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(ChatRoomActivity.this, "What!!!!!!!!!!!!!!!", Toast.LENGTH_SHORT).show();
                    }
                });
                holder.reply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String namd = holder.nam.getText().toString();
                        editText_comment_input.setText(namd);
                    }
                });

            }

            @NonNull
            @Override
            public ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_chat,parent,false);
                return new ChatsViewHolder(view);
            }
        }
;

        firebaseRecyclerAdapter.startListening();
        recyclerView_comments.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();
    }

    public static class ChatsViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public TextView from,cht,timet,nam,churchssss,sender_message_text,timest,reply,tim,date;
        public ImageView receivebuble,sentbubble,sender_message_image,chatimages;
        public CircleImageView profile;
        public CardView cardreceiver,cardsender;
        private final LinearLayout linearLayout,lin01;
        RichLinkViewSkype richLinkViewSkype,richLinkViewSkypes;
        public ChatsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            tim = mView.findViewById(R.id.tim);
            richLinkViewSkype = mView.findViewById(R.id.richLinkView);
            richLinkViewSkypes = mView.findViewById(R.id.richLinkViews);
            reply = mView.findViewById(R.id.reply);
            cht = mView.findViewById(R.id.chats);
            lin01 = mView.findViewById(R.id.lin01);
            from = mView.findViewById(R.id.from);
            timet = mView.findViewById(R.id.time);
            nam = mView.findViewById(R.id.name);
            churchssss = mView.findViewById(R.id.churchsss);
            sender_message_text = mView.findViewById(R.id.sender_message_text);
            timest = mView.findViewById(R.id.times);
            sentbubble = mView.findViewById(R.id.sentbubble);
            receivebuble = mView.findViewById(R.id.receivebuble);
            profile = mView.findViewById(R.id.profile);
            cardreceiver = mView.findViewById(R.id.cardreceiver);
            cardsender = mView.findViewById(R.id.cardsender);
            sender_message_image = mView.findViewById(R.id.sender_message_image);
            chatimages = mView.findViewById(R.id.chatimage);
            linearLayout = mView.findViewById(R.id.lin);

        }
        public void setChatimage(Context cx,String chatimage) {
            sender_message_image = mView.findViewById(R.id.sender_message_image);
            Glide.with(cx).load(chatimage).into(sender_message_image);
            Glide.with(cx).load(chatimage).into(chatimages);
        }

        public void setChat(String chat) {
            TextView cht = mView.findViewById(R.id.chats);
            TextView sender_message_text = mView.findViewById(R.id.sender_message_text);
            cht.setText(chat);
            sender_message_text.setText(chat);
        }
        public void setDates(String dates) {
             timest = mView.findViewById(R.id.times);
             date = mView.findViewById(R.id.time);
            tim = mView.findViewById(R.id.tim);
            date.setText(dates);
            timest.setText(dates);
            tim.setText(dates);
        }
        public void setProfile(Context cx, String profile) {
            CircleImageView prof = mView.findViewById(R.id.profile);
           Glide.with(cx).load(profile).into(prof);
        }
        public void setUsername(String username) {
            TextView nam = mView.findViewById(R.id.name);
            nam.setText("@"+username);
        }
        public void setChurch(String church) {
            TextView chu = mView.findViewById(R.id.churchsss);
            chu.setText(church);
        }
    }

    private void ValidateChat( String usename, String profils, String churcss) {
        String chat = editText_comment_input.getText().toString();
        if(TextUtils.isEmpty(chat)){
            Toast.makeText(this, "You can't send an empty chat", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Calendar callFordate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
            savecurrentDate = currentDate.format(callFordate.getTime());


            Calendar callForTIME = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
            saveCurrentTime = currentTime.format(callForTIME.getTime());
           final String RandomKey = currentUser_id + savecurrentDate + saveCurrentTime;
            String dates = savecurrentDate+" "+" "+" "+saveCurrentTime;
           HashMap chatMap = new HashMap();
           chatMap.put("uid",currentUser_id);
            chatMap.put("chat",chat);
            chatMap.put("dates",dates);
            chatMap.put("date",savecurrentDate);
            chatMap.put("time",saveCurrentTime);
            chatMap.put("profile",profils);
            chatMap.put("username",usename);
            chatMap.put("church",churcss);
            chatMap.put("type","text");

            DatabaseRef.child(RandomKey).setValue(chatMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ChatRoomActivity.this, "Chat sent Successfully", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String mess = task.getException().getMessage();
                        Toast.makeText(ChatRoomActivity.this, mess, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    private void InitilizeFields() {
        messageRecieverName = getIntent().getExtras().get("userName").toString();
        messageReceiverProfile = getIntent().getExtras().get("profile").toString();
        topic = getIntent().getExtras().get("topics").toString();


        custom_profile_topic = findViewById(R.id.custom_profile_topic);
        custom_profile_name = findViewById(R.id.custom_profile_name);
        custom_profile_image = findViewById(R.id.custom_profile_image);
        editText_comment_input = findViewById(R.id.comment_et);
        post_comments = findViewById(R.id.comment_btn_post);
        add = findViewById(R.id.add);
        recyclerView_comments = findViewById(R.id.recyclerview_comments);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
      /*  linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);*/
        recyclerView_comments.setLayoutManager(linearLayoutManager);

        custom_profile_name.setText(messageRecieverName +"'s" + " Chat Room");
        Glide.with(getApplicationContext()).load(messageReceiverProfile).into(custom_profile_image);
        custom_profile_topic.setText(topic);


    }
}