package com.glamour.faith.drop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.glamour.faith.R;

public class BibleFragment extends Fragment {
    String[] mode = { "All Church Members","Church Branch Only"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bible, container, false);

        return root;
    }
}