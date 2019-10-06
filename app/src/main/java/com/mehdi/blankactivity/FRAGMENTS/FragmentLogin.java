package com.mehdi.blankactivity.FRAGMENTS;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.mehdi.blankactivity.R;
import com.mehdi.blankactivity.databinding.FragmentLoginBinding;


public class FragmentLogin extends Fragment {


    public interface ClickInLogin{
        void signup();
        void login(String email, String pass, String t);
    }

    private ClickInLogin clickInLogin;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            clickInLogin = (ClickInLogin) context;
        }catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    private FragmentLoginBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        try {

            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);

            binding.parent.setChecked(true);
            binding.school.setChecked(false);

            binding.parent.setOnClickListener(view -> binding.school.setChecked(false));

            binding.school.setOnClickListener(view -> binding.parent.setChecked(false));

            binding.login.setOnClickListener(view -> {
                final String email = binding.email.getText().toString();
                final String password = binding.password.getText().toString();
                if (binding.parent.isChecked() && !(binding.school.isChecked())){
                    clickInLogin.login(email, password, "p");
                }else if (!(binding.parent.isChecked()) && binding.school.isChecked()){
                    clickInLogin.login(email, password, "s");
                }else {
                    Toast.makeText(getContext(), "Check type login plz !", Toast.LENGTH_LONG).show();
                }
            });


            binding.signUpButton.setOnClickListener(view -> clickInLogin.signup());

        }catch (Exception e){
            e.printStackTrace();
        }

        return binding.getRoot();



    }






}
