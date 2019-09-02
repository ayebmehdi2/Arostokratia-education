package com.mehdi.blankactivity.FRAGMENTS;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mehdi.blankactivity.ACTIVITYS.EditProfile;
import com.mehdi.blankactivity.DATAS.PARENT;
import com.mehdi.blankactivity.R;
import com.mehdi.blankactivity.ShowImage;
import com.mehdi.blankactivity.databinding.LayoutProfileBinding;

import java.util.Objects;

public class FragmentSchoolProfil extends Fragment {

    private LayoutProfileBinding binding;
    private String uid;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private String image;
    private ValueEventListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_profile, container, false);

        SharedPreferences prf = PreferenceManager.getDefaultSharedPreferences(getContext());
        uid = prf.getString("uid", "e");

        binding.kidsTex.setVisibility(View.GONE);
        binding.recKids.setVisibility(View.GONE);

        binding.tof.setImageDrawable(getResources().getDrawable(R.drawable.ic_parents));

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();


        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PARENT parent = dataSnapshot.getValue(PARENT.class);
                if (parent == null) return;
                binding.nameProf.setText(parent.getName());
                binding.emailProf.setText(parent.getEmail());
                if (parent.getImage().length() > 0){
                    image = parent.getImage();
                    try {
                        Glide.with(Objects.requireNonNull(getActivity())).load(image).into(binding.tof);
                    }catch (Exception e){ }
                }else {
                    binding.tof.setImageDrawable(getResources().getDrawable(R.drawable.ic_parents));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        };
        reference.child("SCHOOLS").child(uid).addValueEventListener(listener);


        binding.tof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (image.length() > 0){
                    Intent i = new Intent(getContext(), ShowImage.class);
                    i.putExtra("url", image);
                    startActivity(i);
                }
            }
        });

        binding.editProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), EditProfile.class));
            }
        });

        return binding.getRoot();
    }
}
