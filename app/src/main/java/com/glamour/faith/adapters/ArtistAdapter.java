package com.glamour.faith.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.glamour.faith.Model.Member;
import com.glamour.faith.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {

    private final Context mContext;
    private final List<Member> mUsers;

    public ArtistAdapter(Context mContext, List<Member> mUsers){
        this.mUsers = mUsers;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_members,parent,false);
        return new ArtistAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Member user = mUsers.get(position);
        holder.userchurch.setText(user.getChurch());
        holder.username.setText(user.getName());
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
  public TextView username, userchurch;
  public ImageView profile;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            userchurch = itemView.findViewById(R.id.userchurch);
            profile = itemView.findViewById(R.id.profile);

        }
    }
}
