package com.glamour.faith.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.glamour.faith.Model.PostEvent;
import com.glamour.faith.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostEventAdapter extends RecyclerView.Adapter<PostEventAdapter.ViewHolder> {

    private final Context mContext;
    private final List<PostEvent> mUsers;

    public PostEventAdapter(Context mContext, List<PostEvent> mUsers){
        this.mUsers = mUsers;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_link,parent,false);
        return new PostEventAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        PostEvent user = mUsers.get(position);
        holder.church.setText(user.getChurch());
        holder.name.setText(user.getUsername());
        holder.date.setText(user.getDate());
        holder.time.setText(user.getTime());
        holder.venue.setText(user.getVenue());
        holder.event.setText(user.getEvent());

        if(user.getProfile().equals("default")){
            holder.profile.setImageResource(R.drawable.user);
        }
        else {
            Picasso.get().load(user.getProfile()).into(holder.profile);
        }
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
  public TextView name, church, date, time, venue, event;
  public CircleImageView profile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            church = itemView.findViewById(R.id.church);
            profile = itemView.findViewById(R.id.profile);

            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            venue = itemView.findViewById(R.id.venue);
            event = itemView.findViewById(R.id.event);

        }
    }
}
