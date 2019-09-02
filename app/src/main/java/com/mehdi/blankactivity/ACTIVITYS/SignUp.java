package com.mehdi.blankactivity.ACTIVITYS;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.mehdi.blankactivity.DATAS.CHILD;
import com.mehdi.blankactivity.DATAS.CHILDcode;
import com.mehdi.blankactivity.DATAS.PARENT;
import com.mehdi.blankactivity.R;
import com.mehdi.blankactivity.DATAS.SCHOOL;
import com.mehdi.blankactivity.databinding.SignupBinding;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

public class SignUp extends AppCompatActivity {

    private String uid;
    private ProgressDialog PD;
    private DatabaseReference reference;
    private SignupBinding binding;
    private String email, password, name;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.signup);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);

        final FirebaseAuth auth = FirebaseAuth.getInstance();

        final int typeSignUp = getIntent().getIntExtra("type", 1);

        if (typeSignUp == 1){
            binding.desc.setText("Sign up as parent");
            binding.name.setHint("Full name");
            binding.childs.setVisibility(View.VISIBLE);
        }else if (typeSignUp == 2){
            binding.desc.setText("Sign up as school");
            binding.name.setHint("School name");
            binding.childs.setVisibility(View.GONE);
        }

        binding.show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.show.setVisibility(View.GONE);
                binding.hide.setVisibility(View.VISIBLE);
                binding.pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }
        });

        binding.hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.hide.setVisibility(View.GONE);
                binding.show.setVisibility(View.VISIBLE);
                binding.pass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });


        binding.sinupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = binding.email.getText().toString();
                password = binding.pass.getText().toString();
                name = binding.name.getText().toString();


                try {

                    if (!(name.length() > 0 )){
                        Toast.makeText(SignUp.this, "Enter Name", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (!(password.length() > 0 )){
                        Toast.makeText(SignUp.this, "Enter Password", Toast.LENGTH_LONG).show();
                        return;
                    }


                    if (!(email.length() > 0 )){
                        Toast.makeText(SignUp.this, "Enter Email", Toast.LENGTH_LONG).show();
                        return;
                    }


                    PD.show();
                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(
                                    SignUp.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (!task.isSuccessful()) {
                                                Toast.makeText(
                                                        SignUp.this,
                                                        "Authentication Failed Check the email or password",
                                                        Toast.LENGTH_LONG).show();

                                            } else {

                                                uid = auth.getUid();


                                                    if (uid == null) return;
                                                    SharedPreferences.Editor preference = PreferenceManager.getDefaultSharedPreferences(SignUp.this).edit();
                                                    preference.putString("uid", uid);
                                                    preference.putString("name", name);
                                                    preference.apply();

                                                PD.dismiss();

                                                    if (typeSignUp == 2){
                                                        reference.child("SCHOOLS").child(uid).setValue(new SCHOOL(uid, name, password, email, ""));
                                                        SharedPreferences.Editor prf = PreferenceManager.getDefaultSharedPreferences(SignUp.this).edit();
                                                        prf.putString("typeAPP", "s");
                                                        prf.apply();
                                                        startActivity(new Intent(SignUp.this, HomeActivity.class));
                                                    }else if (typeSignUp == 1){
                                                        int numChild = binding.spinner.getSelectedItemPosition() + 1;
                                                        reference.child("PARENTS").child(uid).setValue(new PARENT(name, email, password, uid, String.valueOf(numChild), ""));
                                                        Intent i = new Intent(SignUp.this, AddKid.class);
                                                        i.putExtra("numChild", numChild);
                                                        startActivity(i);
                                                    }
                                            }

                                        }

                                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }


}
