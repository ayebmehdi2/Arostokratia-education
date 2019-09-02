package com.mehdi.blankactivity.FRAGMENTS;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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
        clickInLogin = (ClickInLogin) context;
    }

    FragmentLoginBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);


        binding.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.school.setChecked(false);
            }
        });

        binding.school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.parent.setChecked(false);
            }
        });

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = binding.email.getText().toString();
                final String password = binding.password.getText().toString();
                if (binding.parent.isChecked() && !(binding.school.isChecked())){
                    clickInLogin.login(email, password, "p");
                }else if (!(binding.parent.isChecked()) && binding.school.isChecked()){
                    clickInLogin.login(email, password, "s");
                }else {
                    Toast.makeText(getContext(), "Check type login plz !", Toast.LENGTH_LONG).show();
                }
            }
        });


        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickInLogin.signup();
            }
        });

        return binding.getRoot();



    }






}
