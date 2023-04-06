package com.glamour.faith.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.glamour.faith.Model.Chat;
import com.glamour.faith.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.ponnamkarthik.richlinkpreview.RichLinkViewTwitter;
import io.github.ponnamkarthik.richlinkpreview.ViewListener;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {
    private  static  final int MSG_TYPE_LEFT=0;
    private  static  final int MSG_TYPE_RIGHT=1;
    private final Context mContext;
    private final List<Chat> mChat;
    private final String imageUrl;
    FirebaseUser fuser;

    public MessagesAdapter (List<Chat> mChat,Context mcontext,String imageUrl){
     this.mContext = mcontext;
     this.mChat = mChat;
     this.imageUrl=imageUrl;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView show_message,show_time,show_times;
        public CircleImageView message_profile_image;
        public ImageView show_Image;
        RichLinkViewTwitter richLinkView;
        LinearLayout mess;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            message_profile_image = itemView.findViewById(R.id.message_profile_image);
            show_message = itemView.findViewById(R.id.show_Message);
            show_Image = itemView.findViewById(R.id.show_image);
            show_time = itemView.findViewById(R.id.show_time);
            mess = itemView.findViewById(R.id.mess);
            show_times = itemView.findViewById(R.id.show_times);
            richLinkView = itemView.findViewById(R.id.richLinkView);




        }
    }

    @Override
    public int getItemViewType(int position) {
       fuser = FirebaseAuth.getInstance().getCurrentUser();
       if(mChat.get(position).getSender().equals(fuser.getUid())){
           return MSG_TYPE_RIGHT;
       }
       else{
           return MSG_TYPE_LEFT;
       }
    }

    @NonNull
    @Override
    public MessagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType ==MSG_TYPE_RIGHT){
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right,parent,false);

              return new MessagesAdapter.ViewHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left,parent,false);

            return new MessagesAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat = mChat.get(position);

if(chat.getType().equals("text")){
             if(URLUtil.isValidUrl(chat.getMessage())){
        holder.richLinkView.setLink(chat.getMessage(), new ViewListener() {
            @Override
            public void onSuccess(boolean status) {

            }

            @Override
            public void onError(Exception e) {

            }
        });
        Picasso.get().load(imageUrl).into(holder.message_profile_image);
        holder.show_Image.setVisibility(View.GONE);
        holder.show_message.setVisibility(View.GONE);
                 holder.show_time.setVisibility(View.GONE);
        holder.show_times.setText(chat.getTime());

    }

   else{
                 Picasso.get().load(imageUrl).into(holder.message_profile_image);
                 holder.show_Image.setVisibility(View.GONE);
                 holder.show_message.setText(chat.getMessage());
                 holder.show_times.setText(chat.getTime());
                 holder.show_time.setVisibility(View.GONE);
             }
}
else if(chat.getType().equals("image")){
            Picasso.get().load(imageUrl).into(holder.message_profile_image);
            holder.show_message.setVisibility(View.GONE);
            Picasso.get().load(chat.getMessage()).into(holder.show_Image);
            holder.show_time.setText(chat.getTime());
            holder.mess.setVisibility(View.GONE);
            holder.show_times.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount()
    {
        return mChat.size();
    }
}
