package com.mehdi.blankactivity.FRAGMENTS;

import android.annotation.SuppressLint;
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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mehdi.blankactivity.ACTIVITYS.DetailKids;
import com.mehdi.blankactivity.ADAPTERS.AdapterKids;
import com.mehdi.blankactivity.ADAPTERS.AdapterLesson;
import com.mehdi.blankactivity.DATAS.CHILD;
import com.mehdi.blankactivity.DATAS.NOTE;
import com.mehdi.blankactivity.R;
import com.mehdi.blankactivity.databinding.LessonLayoutBinding;

import java.util.ArrayList;

public class FragmentClass extends Fragment implements AdapterKids.clickPrson {

    private int position;
    private String nameClass;
    public FragmentClass(int pos, String nameCl){
        position = pos;
        this.nameClass = nameCl;
    }

    private LessonLayoutBinding binding;

    private AdapterLesson adapterLesson;
    private ArrayList<NOTE> notes;

    private DatabaseReference reference;
    private ValueEventListener listener;

    private ArrayList<CHILD> children;
    private AdapterKids kids;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.lesson_layout, container, false);


        try {

            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            reference = database.getReference();

            SharedPreferences prf = PreferenceManager.getDefaultSharedPreferences(getContext());
            String uid = prf.getString("uid", "e");

            if (position == 0){

                reference = reference.child("SCHOOLS").child(uid)
                        .child("CLASSES").child(uid + "--" + nameClass).child("NOTE");

                adapterLesson = new AdapterLesson();
                notes = new ArrayList<>();
                binding.recLesson.setHasFixedSize(true);
                binding.recLesson.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.recLesson.setAdapter(adapterLesson);

                listener = new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        notes.clear();
                        adapterLesson.swapAdapter(notes);
                        binding.empty.setVisibility(View.GONE);
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            NOTE lesson = snapshot.getValue(NOTE.class);
                            if (lesson != null) notes.add(lesson);
                        }

                        adapterLesson.swapAdapter(notes);
                        if (notes.size() == 0){
                            binding.empty.setVisibility(View.VISIBLE);
                            binding.inf.setText("Class is empty !");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };

            }else if (position == 1){

                reference = reference.child("SCHOOLS").child(uid)
                        .child("CLASSES").child(uid + "--" + nameClass).child("STUDENTS");

                kids = new AdapterKids(this);
                GridLayoutManager manager = new GridLayoutManager(getContext(),2);
                binding.recLesson.setHasFixedSize(true);
                binding.recLesson.setLayoutManager(manager);
                binding.recLesson.setAdapter(kids);

                children = new ArrayList<>();

                listener = new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        children.clear();
                        kids.swapAdapter(children);
                        binding.empty.setVisibility(View.GONE);
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            CHILD child = snapshot.getValue(CHILD.class);
                            children.add(child);
                        }
                        kids.swapAdapter(children);
                        if (children.size() == 0){
                            binding.empty.setVisibility(View.VISIBLE);
                            binding.inf.setText("No one here !");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };

            }
        }catch (Exception ignored){ }


        return binding.getRoot();

    }


    @Override
    public void select(String nameKid, String parenId) {
       try {
           Intent i = new Intent(getContext(), DetailKids.class);
           i.putExtra("inSchool", true);
           i.putExtra("de", nameClass + "," + nameKid + "--" + parenId);
           startActivity(i);
       }catch (Exception ignored){

       }
    }

    @Override
    public void onPause() {
        super.onPause();
            try {
                reference.removeEventListener(listener);
            }catch (Exception ignored){

            }
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            reference.addValueEventListener(listener);
        }catch (Exception ignored){ }
    }
}

