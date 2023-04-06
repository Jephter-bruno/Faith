package com.glamour.faith.drop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.glamour.faith.Model.Chat;
import com.glamour.faith.Model.Messages;
import com.glamour.faith.R;
import com.glamour.faith.adapters.MessagesAdapter;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserMessageActivity extends AppCompatActivity {
    private ImageButton send_image_file_button,send_button;
    private EditText comment_et;
    private RecyclerView message_list_users;
    private String messageReceiverID, messageRecieverName, messageSenderID, savecurrentDate,saveCurrentTime,saveRandomName;
    private TextView receiverUserName, appear;
    private CircleImageView receiverProfileImage;
    private DatabaseReference RootRef;
    private FirebaseAuth mAuth;
    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
     MessagesAdapter messagesAdapter;
    private static final int PICK_IMAGE_REQUEST = 1;
    private StorageReference userProfileImageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask uploadTask;
    String currentUser_id;
    private Uri ImageUri;
    List<Chat> mChat;
    public String imageUrl;
    AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_message);
        mAuth = FirebaseAuth.getInstance();
        currentUser_id = mAuth.getCurrentUser().getUid();
        userProfileImageRef = FirebaseStorage.getInstance().getReference("Images");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Members").child(currentUser_id);
        messageSenderID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();
        messageReceiverID = getIntent().getExtras().get("visit_user_id").toString();
        imageUrl = getIntent().getExtras().get("profile").toString();
        messageRecieverName = getIntent().getExtras().get("userName").toString();

        mAdView = findViewById(R.id.adView);
        AdView adView = new AdView(UserMessageActivity.this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getString(R.string.admob_app_id));
        MobileAds.initialize(UserMessageActivity.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        InitilizeFields();
        DisplayReceiverInfo();
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  SendMessage();*/
                String msg = comment_et.getText().toString();
                if(!msg.equals("")){
                    sendMessage(currentUser_id,messageReceiverID,msg);
                }
             else{
                    Toast.makeText(UserMessageActivity.this, "You can't send an empty text", Toast.LENGTH_SHORT).show();
                }
             comment_et.setText("");
            }
        });
        send_image_file_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendImageMessage();
            }
        });
     /*   FetchMessages();*/
      mDatabaseRef.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {


              readMessages(currentUser_id,messageReceiverID,imageUrl);
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
      });
    }

    private void SendImageMessage() {
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
    private void uploadImage(){
        final ProgressDialog progressDialog =new ProgressDialog(UserMessageActivity.this);
        progressDialog.setMessage("Uploading..");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();

        if(ImageUri != null){
            Calendar callFordate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
            savecurrentDate = currentDate.format(callFordate.getTime());

            Calendar callForTIME = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm aa");
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
                        progressDialog.dismiss();
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();
                        Calendar callFordate = Calendar.getInstance();
                        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                        savecurrentDate = currentDate.format(callFordate.getTime());


                        Calendar callForTIME = Calendar.getInstance();
                        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm aa");
                        saveCurrentTime = currentTime.format(callForTIME.getTime());
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("sender",currentUser_id);
                        hashMap.put("receiver",messageReceiverID);
                        hashMap.put("message",mUri);
                        hashMap.put("time",savecurrentDate +" "+ saveCurrentTime);
                        hashMap.put("type","image");


                        reference.child("Chats").push().setValue(hashMap);


                    }
                    else
                    {
                        Toast.makeText(UserMessageActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UserMessageActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void FetchMessages() {
        RootRef.child("Messages").child(messageSenderID).child(messageReceiverID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){
                    Messages messages = snapshot.getValue(Messages.class);
                    messagesList.add(messages);
                    messagesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

private void sendMessage(String sender,String receiver, String message ){
    Calendar callFordate = Calendar.getInstance();
    SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
    savecurrentDate = currentDate.format(callFordate.getTime());


    Calendar callForTIME = Calendar.getInstance();
    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm ss");
    saveCurrentTime = currentTime.format(callForTIME.getTime());
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    HashMap<String, Object> hashMap = new HashMap<>();
    hashMap.put("sender",sender);
    hashMap.put("receiver",receiver);
    hashMap.put("message",message);
    hashMap.put("time",savecurrentDate +" "+ saveCurrentTime);
    hashMap.put("type","text");


    reference.child("Chats").push().setValue(hashMap);
}
/*
    private void SendMessage()
    {
        String messagetext = comment_et.getText().toString();
        if(TextUtils.isEmpty(messagetext)){
            Toast.makeText(this, "Please Type your message  First", Toast.LENGTH_SHORT).show();
        }
        else
        {
        String message_sender_ref ="Messages/" + messageSenderID + "/" + messageReceiverID;
        String message_receiver_ref ="Messages/" + messageReceiverID + "/" + messageSenderID;

        DatabaseReference user_message_key = RootRef.child("Messages").child(messageSenderID).child(messageReceiverID).push();
        String message_push_id = user_message_key.getKey();
            Calendar callFordate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
            savecurrentDate = currentDate.format(callFordate.getTime());


            Calendar callForTIME = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm aa");
            saveCurrentTime = currentTime.format(callForTIME.getTime());

            Map messageTextBody = new HashMap();
            messageTextBody.put("message",messagetext);
            messageTextBody.put("time",saveCurrentTime);
            messageTextBody.put("date",savecurrentDate);
            messageTextBody.put("type","text");
            messageTextBody.put("from",messageSenderID);

            Map messageBodyDetails = new HashMap();
            messageBodyDetails.put(message_sender_ref + "/" + message_push_id , messageTextBody);
            messageBodyDetails.put(message_receiver_ref + "/" + message_push_id , messageTextBody);

            RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Toast.makeText(UserMessageActivity.this, "Message Sent Successfully", Toast.LENGTH_SHORT).show();
                        comment_et.setText("");
                    }
                    else{
                        String mess = task.getException().getMessage();
                        Toast.makeText(UserMessageActivity.this, mess, Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }
    }
*/

    private void DisplayReceiverInfo() {
        appear.setText("Your chat with "+messageRecieverName+ " will be displayed here");
        receiverUserName.setText(messageRecieverName);
        RootRef.child("Members").child(messageReceiverID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    final String profileImage = snapshot.child("profileImage").getValue().toString();
                    Picasso.get().load(profileImage).into(receiverProfileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
private void readMessages(String myid,String userid, String imageurl){
        mChat = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 mChat.clear();
                 for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                     Chat chat = dataSnapshot.getValue(Chat.class);
                     if(chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                     chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
                         mChat.add(chat);
                     }
                     messagesAdapter = new MessagesAdapter(mChat, UserMessageActivity.this,imageurl);
                     message_list_users.setAdapter(messagesAdapter);
                 }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
}
    private void InitilizeFields() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actiob_bar_view = layoutInflater.inflate(R.layout.chat_custom_bar,null);
        actionBar.setCustomView(actiob_bar_view);

        send_button = findViewById(R.id.send_button);
        send_image_file_button = findViewById(R.id.send_image_file_button);
        appear = findViewById(R.id.appear);
        comment_et = findViewById(R.id.comment_et);
        message_list_users = findViewById(R.id.message_list_users);

        receiverUserName = findViewById(R.id.custom_profile_name);
        receiverProfileImage = findViewById(R.id.custom_profile_image);

/*        messagesAdapter = new MessagesAdapter(messagesList);*/
      linearLayoutManager = new LinearLayoutManager(this);
      message_list_users.setLayoutManager(linearLayoutManager);
      linearLayoutManager.setStackFromEnd(true);

    }
}