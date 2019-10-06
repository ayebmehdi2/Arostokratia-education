package com.mehdi.blankactivity.FRAGMENTS;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mehdi.blankactivity.ADAPTERS.AdapterNote;
import com.mehdi.blankactivity.DATAS.NOTE_FOR_PARENT;
import com.mehdi.blankactivity.R;
import com.mehdi.blankactivity.databinding.LessonLayoutBinding;

import java.util.ArrayList;

public class FragmentNotifyParent extends Fragment {

    private LessonLayoutBinding binding;
    private AdapterNote notesAdap;
    private DatabaseReference reference;
    private ValueEventListener listener;
    private ArrayList<NOTE_FOR_PARENT> notesArray;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.lesson_layout, container, false);


        try {


            SharedPreferences prf = PreferenceManager.getDefaultSharedPreferences(getContext());
            String uid = prf.getString("uid", "e");

            notesArray = new ArrayList<>();
            notesAdap = new AdapterNote();

            binding.recLesson.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.recLesson.setHasFixedSize(true);
            binding.recLesson.setAdapter(notesAdap);

            reference = FirebaseDatabase.getInstance().getReference().child("PARENTS").child(uid).child("NOTE");

            listener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    notesArray.clear();
                    notesAdap.swapAdapter(notesArray);
                    binding.empty.setVisibility(View.GONE);
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        NOTE_FOR_PARENT child = snapshot.getValue(NOTE_FOR_PARENT.class);
                        notesArray.add(child);
                    }
                    notesAdap.swapAdapter(notesArray);
                    if (notesArray.size() == 0){
                        binding.empty.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };


        }catch (Exception e){
            e.printStackTrace();
        }

        return binding.getRoot();
    }


    @Override
    public void onPause() {
        super.onPause();

        try {
            reference.removeEventListener(listener);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            reference.addValueEventListener(listener);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
