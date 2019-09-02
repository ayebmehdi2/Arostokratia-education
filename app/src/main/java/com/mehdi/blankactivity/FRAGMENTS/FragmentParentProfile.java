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
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mehdi.blankactivity.ACTIVITYS.DetailKids;
import com.mehdi.blankactivity.ACTIVITYS.EditProfile;
import com.mehdi.blankactivity.ADAPTERS.AdapterKids;
import com.mehdi.blankactivity.DATAS.CHILD;
import com.mehdi.blankactivity.DATAS.PARENT;
import com.mehdi.blankactivity.R;
import com.mehdi.blankactivity.ShowImage;
import com.mehdi.blankactivity.databinding.LayoutProfileBinding;

import java.util.ArrayList;
import java.util.Objects;

public class FragmentParentProfile extends Fragment implements AdapterKids.clickPrson {


    private String uid;
    private LayoutProfileBinding binding;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private ValueEventListener listener;
    private ValueEventListener listener2;
    private ArrayList<CHILD> children;
    private AdapterKids kids;
    private String image = "";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_profile, container, false);

        SharedPreferences prf = PreferenceManager.getDefaultSharedPreferences(getContext());
        uid = prf.getString("uid", "e");

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
        reference.child("PARENTS").child(uid).addValueEventListener(listener);

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

        kids = new AdapterKids(this);
        GridLayoutManager manager = new GridLayoutManager(getContext(),2);
        binding.recKids.setHasFixedSize(true);
        binding.recKids.setLayoutManager(manager);
        binding.recKids.setAdapter(kids);

        children = new ArrayList<>();

        listener2 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             children.clear();
             for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                 CHILD child = snapshot.getValue(CHILD.class);
                 children.add(child);
             }
             kids.swapAdapter(children);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        reference.child("PARENTS").child(uid).child("CHILDREN").addValueEventListener(listener2);


        return binding.getRoot();
    }

    @Override
    public void select(String nameKid) {
        Intent i = new Intent(getContext(), DetailKids.class);
        i.putExtra("nameKid", nameKid);
        startActivity(i);
    }
}
