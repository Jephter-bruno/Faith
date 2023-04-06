package com.glamour.faith.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.glamour.faith.Model.Member;
import com.glamour.faith.R;
import com.glamour.faith.drop.UserMessageActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private final Context mContext;
    private final List<Member> mUsers;

    public UsersAdapter(Context mContext, List<Member> mUsers){
        this.mUsers = mUsers;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_members,parent,false);
        return new UsersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        Member user = mUsers.get(position);
        holder.userchurch.setText(user.getChurch());
        holder.username.setText(user.getName());
        if(user.getProfileImage().equals("default")){
            holder.profile.setImageResource(R.drawable.user);
        }
        else {
            Picasso.get().load(user.getProfileImage()).into(holder.profile);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatIntent = new Intent(mContext,UserMessageActivity.class);
                chatIntent.putExtra("userName",user.getName());
                chatIntent.putExtra("visit_user_id",user.getUserId());
                chatIntent.putExtra("profile", user.getProfileImage());
                mContext.startActivity(chatIntent);}

        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
  public TextView username, userchurch;
  public CircleImageView profile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            userchurch = itemView.findViewById(R.id.userchurch);
            profile = itemView.findViewById(R.id.profile);


        }
    }
}
