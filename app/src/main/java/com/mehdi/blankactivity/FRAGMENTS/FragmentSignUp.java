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

import com.mehdi.blankactivity.R;
import com.mehdi.blankactivity.ACTIVITYS.SignUp;
import com.mehdi.blankactivity.databinding.FragmentSignupBinding;

public class FragmentSignUp extends Fragment {


    FragmentSignupBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup, container, false);

        binding.school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                defaul();
                school();
            }
        });
        binding.actionSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), SignUp.class);
                i.putExtra("type", 2);
                startActivity(i);
            }
        });


        binding.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                defaul();
                parent();
            }
        });
        binding.actionParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), SignUp.class);
                i.putExtra("type", 1);
                startActivity(i);
            }
        });

        return binding.getRoot();

    }

    public void parent(){
        binding.parent.setBackgroundResource(R.drawable.shadow_sign_type);
        binding.parent.setElevation(2);
        binding.actionParent.setVisibility(View.VISIBLE);
    }

    public void school(){
        binding.school.setBackgroundResource(R.drawable.shadow_sign_type);
        binding.school.setElevation(2);
        binding.actionSchool.setVisibility(View.VISIBLE);
    }

    public void defaul(){
        binding.parent.setBackgroundResource(0);
        binding.parent.setElevation(0);
        binding.actionParent.setVisibility(View.GONE);

        binding.school.setBackgroundResource(0);
        binding.school.setElevation(0);
        binding.actionSchool.setVisibility(View.GONE);
    }
}
