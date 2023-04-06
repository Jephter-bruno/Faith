package com.glamour.faith;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.ponnamkarthik.richlinkpreview.RichLinkView;
import io.github.ponnamkarthik.richlinkpreview.ViewListener;

public class ClickPostActivity extends AppCompatActivity {
private ImageView image_view;
private TextView name, texts,click_text_script_cont,click_text_script;
private CircleImageView profiles;
private Button edit_post, delete_post,report_post;
private String  CurrentUserId,DatabaseUserId,usernames,profiless,description1,postimages,scriptbk;
private DatabaseReference clickpostRef;
private FirebaseAuth mAuth;
private RichLinkView richLinkView;
 public String PostKey;

    private String mode,usename,profils,churcss,datse,timese;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_post);
        mAuth= FirebaseAuth.getInstance();
        CurrentUserId = mAuth.getCurrentUser().getUid();
        PostKey = getIntent().getExtras().get("PostKey").toString();
        clickpostRef  = FirebaseDatabase.getInstance().getReference().child("Post_photos_public").child(PostKey);
        DatabaseUserId = getIntent().getExtras().getString("DatabaseUserId");
        profiless = getIntent().getExtras().getString("profile");
        usernames = getIntent().getExtras().getString("name");
        String image = getIntent().getExtras().getString("postphoto");

        click_text_script_cont = findViewById(R.id.click_text_script_cont);
        click_text_script = findViewById(R.id.click_text_script);
        image_view =findViewById(R.id.click_image_view);
        richLinkView = findViewById(R.id.richLinkView);
        texts = findViewById(R.id.click_text);
        edit_post = findViewById(R.id.edit_post);
        delete_post = findViewById(R.id.delete_post);
        report_post = findViewById(R.id.report_post);
        profiles = findViewById(R.id.click_profile);
        name = findViewById(R.id.click_name);

        name.setText(usernames);
        Glide.with(getApplicationContext()).load(profiless).into(profiles);
        delete_post.setVisibility(View.GONE);
        edit_post.setVisibility(View.GONE);
        image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent commentIntent = new Intent(ClickPostActivity.this, ImageActivity.class);
                commentIntent.putExtra("PostKey",PostKey);
                commentIntent.putExtra("postimage",image);
                startActivity(commentIntent);
            }
        });
        clickpostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 mode = getIntent().getExtras().get("postmode").toString();
                 usename = snapshot.child("name").getValue().toString();
                 profils = snapshot.child("profileImage").getValue().toString();
                 churcss = snapshot.child("church").getValue().toString();
                 datse = snapshot.child("date").getValue().toString();
                 timese = snapshot.child("time").getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        report_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent clickPost = new Intent(ClickPostActivity.this, RepliesActivity.class);
                clickPost.putExtra("PostKey",PostKey);

                startActivity(clickPost);
            }

        });
        clickpostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {


                    if (CurrentUserId.equals(DatabaseUserId)) {
                        delete_post.setVisibility(View.VISIBLE);
                        edit_post.setVisibility(View.VISIBLE);
                        report_post.setVisibility(View.GONE);
                    }
                    edit_post.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            EditCurrentPost(description1);
                        }
                    });
                    name.setText(usernames);
                    String mode = getIntent().getExtras().get("postmode").toString();
                    if (mode.equals("advertwithtextonly")) {
                        String descriptionsse = snapshot.child("description").getValue().toString();
                        click_text_script_cont.setVisibility(View.GONE);
                        click_text_script.setVisibility(View.GONE);
                        image_view.setVisibility(View.GONE);
                        texts.setText(descriptionsse);
                    }
                    if (mode.equals("link")) {
                        String descriptionsse = snapshot.child("link").getValue().toString();
                        click_text_script_cont.setVisibility(View.GONE);
                        click_text_script.setVisibility(View.GONE);
                        image_view.setVisibility(View.GONE);
                        texts.setVisibility(View.GONE);
                        richLinkView.setLink(descriptionsse, new ViewListener() {
                            @Override
                            public void onSuccess(boolean status) {

                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });

                    }

                    if (mode.equals("text")) {
                        String descriptionsse = snapshot.child("description").getValue().toString();
                        click_text_script_cont.setVisibility(View.GONE);
                        click_text_script.setVisibility(View.GONE);
                        image_view.setVisibility(View.GONE);
                        texts.setText(descriptionsse);
                    }
                    if (mode.equals("testimonytext")) {
                        String descriptionsse = snapshot.child("description").getValue().toString();
                        click_text_script_cont.setVisibility(View.GONE);
                        click_text_script.setVisibility(View.GONE);
                        image_view.setVisibility(View.GONE);
                        texts.setText(descriptionsse);
                    }

                    if (mode.equals("scripture")) {
                        String scripture = snapshot.child("scriptureContent").getValue().toString();
                        String scripturebook = snapshot.child("scriptureBook").getValue().toString();
                        click_text_script_cont.setText(scripture);
                        click_text_script.setText(scripturebook);
                        image_view.setVisibility(View.GONE);
                        texts.setVisibility(View.GONE);
                    }

                    if (mode.equals("advertwithtextandimage")) {
                        click_text_script_cont.setVisibility(View.GONE);
                        click_text_script.setVisibility(View.GONE);
                        String postimage = getIntent().getExtras().get("postImage").toString();
                        String des = getIntent().getExtras().get("description").toString();

                        texts.setText(des);
                        Glide.with(getApplicationContext()).load(postimage).into(image_view);
                    }
                    if (mode.equals("photowithtext")) {
                        click_text_script.setVisibility(View.GONE);
                        click_text_script_cont.setVisibility(View.GONE);
                        String postimage = getIntent().getExtras().get("postImage").toString();
                        String des = getIntent().getExtras().get("description").toString();

                        texts.setText(des);
                        Glide.with(getApplicationContext()).load(postimage).into(image_view);
                    }

                    if (mode.equals("advertwithphotoonly")) {
                        click_text_script_cont.setVisibility(View.GONE);
                        click_text_script.setVisibility(View.GONE);
                        String postimages = getIntent().getExtras().get("postImage").toString();

                        texts.setVisibility(View.GONE);
                        Glide.with(getApplicationContext()).load(postimages).into(image_view);
                    }
                    if (mode.equals("testimonyphoto")) {
                        click_text_script_cont.setVisibility(View.GONE);
                        click_text_script.setVisibility(View.GONE);
                        String postimages = getIntent().getExtras().get("postImage").toString();

                        texts.setVisibility(View.GONE);
                        Glide.with(getApplicationContext()).load(postimages).into(image_view);
                    }
                    if (mode.equals("testimonyphotoandtext")) {
                        click_text_script_cont.setVisibility(View.GONE);
                        click_text_script.setVisibility(View.GONE);
                        String postimage = getIntent().getExtras().get("postImage").toString();
                        String des = getIntent().getExtras().get("description").toString();

                        texts.setText(des);
                        Glide.with(getApplicationContext()).load(postimage).into(image_view);
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
delete_post.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        DeleteCurrentPost();
    }
});
    }

    private void EditCurrentPost(String description1) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Post");

        final EditText inputField = new EditText(this);
        inputField.setText(description1);
        builder.setView(inputField);
        builder.setPositiveButton("update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                clickpostRef.child("description").setValue(inputField.getText().toString());
                Toast.makeText(ClickPostActivity.this, "Post Updated Successfully", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
dialogInterface.cancel();
            }
        });
        Dialog  dialog = builder.create();
        dialog.show();
    }

    private void DeleteCurrentPost() {
        clickpostRef.removeValue();
       finish();
        Toast.makeText(this, "Post has been deleted Successfully", Toast.LENGTH_SHORT).show();
    }
   private void SendUserToMainActivity()
   {
       Intent mainIntent = new Intent(ClickPostActivity.this, MainActivity.class);
       mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
       startActivity(mainIntent);
       finish();
   }
}