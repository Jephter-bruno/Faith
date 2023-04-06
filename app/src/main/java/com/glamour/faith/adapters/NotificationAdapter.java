package com.glamour.faith.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.glamour.faith.Model.Notification;
import com.glamour.faith.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private final Context mContext;
    private final List<Notification> mUsers;
    Boolean likeCheker = false;

    public NotificationAdapter(Context mContext, List<Notification> mUsers){
        this.mUsers = mUsers;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_notification,parent,false);
        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Notification user = mUsers.get(position);
        holder.church.setText(user.getChurch());
        holder.name.setText(user.getName());
        holder.date.setText(user.getDate());
        holder.time.setText(user.getTime());
        holder.description.setText(user.getDescription());
        if(user.getProfileImage().equals("default")){
            holder.profile.setImageResource(R.drawable.user);
        }
        else {
            Picasso.get().load(user.getProfileImage()).into(holder.profile);
        }

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
  public TextView name, church, date, time, description;
  public CircleImageView profile;
        public ImageView image_view;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            church = itemView.findViewById(R.id.church);
            profile = itemView.findViewById(R.id.profile);

            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            description = itemView.findViewById(R.id.description);
            image_view = itemView.findViewById(R.id.image_view);

        }
    }
}
