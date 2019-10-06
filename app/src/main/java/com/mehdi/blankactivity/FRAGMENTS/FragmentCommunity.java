package com.mehdi.blankactivity.FRAGMENTS;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mehdi.blankactivity.ACTIVITYS.Addpost;
import com.mehdi.blankactivity.ACTIVITYS.PostDetail;
import com.mehdi.blankactivity.ADAPTERS.AdapterCommunity;
import com.mehdi.blankactivity.DATAS.POST;
import com.mehdi.blankactivity.R;
import com.mehdi.blankactivity.databinding.CommunityLayoutBinding;

import java.util.ArrayList;

public class FragmentCommunity extends Fragment implements AdapterCommunity.CLICK {

    private ArrayList<POST> comments;
    private DatabaseReference reference;
    private ValueEventListener listener;
    private AdapterCommunity adapterCommunity;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        CommunityLayoutBinding binding = DataBindingUtil.inflate(inflater, R.layout.community_layout, container, false);


        try {

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            reference = database.getReference();

            adapterCommunity = new AdapterCommunity(this);
            binding.recCommunity.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.recCommunity.setHasFixedSize(true);
            binding.recCommunity.setAdapter(adapterCommunity);

            comments = new ArrayList<>();

            listener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    comments.clear();
                    adapterCommunity.swapAdapter(comments);
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        POST post = snapshot.getValue(POST.class);
                        comments.add(post);
                    }
                    adapterCommunity.swapAdapter(comments);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };



            binding.addPost.setOnClickListener(view -> startActivity(new Intent(getContext(), Addpost.class)));

        }catch (Exception ignored){ }

        return binding.getRoot();

    }

    @Override
    public void onPause() {
        super.onPause();

        try {
            reference.child("COMMUNITY").removeEventListener(listener);
        }catch (Exception e){
            e.printStackTrace();
        }


    }


    @Override
    public void onResume() {
        super.onResume();
        try {
            reference.child("COMMUNITY").addValueEventListener(listener);
        }catch (Exception ignored){ }

    }

    @Override
    public void select(String uid, String sub) {
        try {
            String s = uid + "--" + sub;
            Intent i = new Intent(getContext(), PostDetail.class);
            i.putExtra("postId", s);
            startActivity(i);
        }catch (Exception ignored){

        }
    }
}
