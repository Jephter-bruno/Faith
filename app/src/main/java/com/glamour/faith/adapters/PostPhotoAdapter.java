package com.glamour.faith.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.glamour.faith.Model.PostPhoto;
import com.glamour.faith.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostPhotoAdapter extends RecyclerView.Adapter<PostPhotoAdapter.ViewHolder> {

    private final Context mContext;
    private final List<PostPhoto> mUsers;
    Boolean likeCheker = false;

    public PostPhotoAdapter(Context mContext, List<PostPhoto> mUsers){
        this.mUsers = mUsers;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_photo,parent,false);
        return new PostPhotoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        PostPhoto user = mUsers.get(position);
        holder.church.setText(user.getChurch());
        holder.name.setText(user.getName());
        holder.date.setText(user.getDate());
        holder.time.setText(user.getTime());
        Picasso.get().load(user.getPostImage()).into(holder.imageView);
        holder.description.setText(user.getDescription());
        if(user.getProfileImage().equals("default")){
            holder.profile.setImageResource(R.drawable.user);
        }
        else {
            Picasso.get().load(user.getProfileImage()).into(holder.profile);
        }
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeCheker = true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
  public TextView name, church, date, time, description,scripture_content,scripture_book;
  public CircleImageView profile;
        public ImageView imageView;
        public ImageButton like;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            church = itemView.findViewById(R.id.church);
            profile = itemView.findViewById(R.id.profile);

            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            description = itemView.findViewById(R.id.description);
            imageView = itemView.findViewById(R.id.image_view);
            like = itemView.findViewById(R.id.like);
        /*    scripture_content = itemView.findViewById(R.id.scripture_content);
            scripture_book = itemView.findViewById(R.id.scripture_book);*/
        }
    }
}
