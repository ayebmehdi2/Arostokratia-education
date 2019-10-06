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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mehdi.blankactivity.ACTIVITYS.ActivityClass;
import com.mehdi.blankactivity.ADAPTERS.AdapterClasses;
import com.mehdi.blankactivity.DATAS.CLASS;
import com.mehdi.blankactivity.R;
import com.mehdi.blankactivity.databinding.ClassesFragmentLayoutBinding;

import java.util.ArrayList;

public class FragmentClasses extends Fragment implements AdapterClasses.CLICK {

    private ClassesFragmentLayoutBinding binding;
    private DatabaseReference refClasses;
    private String uid;
    private ArrayList<CLASS> arrayClasses;
    private AdapterClasses adapClasses;
    private ValueEventListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.classes_fragment_layout, container, false);


        try {

            FirebaseDatabase database = FirebaseDatabase.getInstance();

            SharedPreferences prf = PreferenceManager.getDefaultSharedPreferences(getContext());
            uid = prf.getString("uid", "e");

            refClasses = database.getReference().child("SCHOOLS").child(uid).child("CLASSES");


            adapClasses = new AdapterClasses(this);
            binding.recClasses.setLayoutManager(new GridLayoutManager(getContext(), 2));
            binding.recClasses.setHasFixedSize(true);
            binding.recClasses.setAdapter(adapClasses);

            arrayClasses = new ArrayList<>();


            listener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    arrayClasses.clear();
                    adapClasses.swapAdapter(arrayClasses);
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        CLASS c = snapshot.getValue(CLASS.class);
                        arrayClasses.add(c);
                    }
                    adapClasses.swapAdapter(arrayClasses);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };


            binding.newClassAction.setOnClickListener(view -> {
                binding.newClass.setVisibility(View.VISIBLE);
                refClasses.removeEventListener(listener);
            });

            binding.finish.setOnClickListener(view -> {
                String className = binding.className.getText().toString();
                if (className.length() > 0){
                    refClasses.child(uid + "--" + className)
                            .setValue(new CLASS(uid, className,(int) System.currentTimeMillis()));
                    binding.newClass.setVisibility(View.GONE);
                    refClasses.addValueEventListener(listener);
                    binding.className.setText("");
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
        return binding.getRoot();
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            refClasses.removeEventListener(listener);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            refClasses.addValueEventListener(listener);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void select(String nameClass) {
        try {
            if (nameClass == null) return;
            Intent i = new Intent(getActivity(), ActivityClass.class);
            i.putExtra("nameClass", nameClass);
            startActivity(i);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
