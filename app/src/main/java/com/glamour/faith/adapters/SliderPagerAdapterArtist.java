package com.glamour.faith.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.glamour.faith.Model.Member;
import com.glamour.faith.R;

import com.squareup.picasso.Picasso;

import java.util.List;

public class SliderPagerAdapterArtist extends PagerAdapter {

    private final Context mContext ;
    private final List<Member> mUsers;


    public SliderPagerAdapterArtist(Context mContext, List<Member> mUsers) {
        this.mContext = mContext;
        this.mUsers = mUsers;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View slideLayout = inflater.inflate(R.layout.slide_item,null);
        Member user = mUsers.get(position);

        ImageView slideImg = slideLayout.findViewById(R.id.slide_img);
        TextView slideText = slideLayout.findViewById(R.id.slide_title);

        if(user.getProfileImage().equals("default")){
            slideImg.setImageResource(R.drawable.user);
        }
        else{
            Picasso.get().load(user.getProfileImage()).into(slideImg);
        }
        slideText.setText(user.getName());
        container.addView(slideLayout);

        return slideLayout;

    }


    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}

