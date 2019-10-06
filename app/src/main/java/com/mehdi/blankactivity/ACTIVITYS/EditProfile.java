package com.mehdi.blankactivity.ACTIVITYS;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mehdi.blankactivity.DATAS.PARENT;
import com.mehdi.blankactivity.DATAS.SCHOOL;
import com.mehdi.blankactivity.R;
import com.mehdi.blankactivity.databinding.EditProfileBinding;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

public class EditProfile extends AppCompatActivity {
    EditProfileBinding binding;
    private DatabaseReference reference;
    private String uid;
    private String name;
    private ProgressDialog PD;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            binding = DataBindingUtil.setContentView(this, R.layout.edit_profile);

            PD = new ProgressDialog(this);
            PD.setMessage("Loading...");
            PD.setCancelable(true);
            PD.setCanceledOnTouchOutside(false);

            SharedPreferences prf = PreferenceManager.getDefaultSharedPreferences(this);
            String ty = prf.getString("typeAPP", "e");

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            reference = database.getReference();
            uid = prf.getString("uid", "e");


            ValueEventListener listener;
            if (ty.equals("p")){

                listener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        PARENT parent = dataSnapshot.getValue(PARENT.class);
                        if (parent == null) return;
                        if (parent.getImage().length() > 0){
                            try {
                                Glide.with(EditProfile.this).load(parent.getImage()).into(binding.img);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        name = parent.getName();
                        binding.kidName.setText(name);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                };
                reference.child("PARENTS").child(uid).addValueEventListener(listener);

                binding.addKid.setOnClickListener(view -> {
                    Intent i = new Intent(EditProfile.this, AddKid.class);
                    i.putExtra("numChild", 1);
                    startActivity(i);
                });

                binding.apply.setOnClickListener(view -> {
                    String na = binding.kidName.getText().toString();
                    if ((!(na.equals(name)) && na.length() > 0) && image == null){
                        reference.child("PARENTS").child(uid).child("name").setValue(na);
                        finish();
                    }else {
                        reference.child("PARENTS").child(uid).child("name").setValue(na);
                        if (image != null) uploadImage(image, "p");
                    }
                });

            }else if(ty.equals("s")){

                binding.addKid.setVisibility(View.GONE);

                listener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        SCHOOL parent = dataSnapshot.getValue(SCHOOL.class);
                        if (parent == null) return;
                        if (parent.getImage().length() > 0){
                            try {
                                Glide.with(EditProfile.this).load(parent.getImage()).into(binding.img);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        name = parent.getName();
                        binding.kidName.setText(name);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                };
                reference.child("SCHOOLS").child(uid).addValueEventListener(listener);


                binding.apply.setOnClickListener(view -> {
                    String na = binding.kidName.getText().toString();
                    if ((!(na.equals(name)) && na.length() > 0) && (image == null || !(image.length() > 0))){
                        reference.child("SCHOOLS").child(uid).child("name").setValue(na);
                        finish();
                    }else {
                        reference.child("SCHOOLS").child(uid).child("name").setValue(na);
                        if (image != null) uploadImage(image, "s");
                    }
                });

            }else {
                finish();
            }


            binding.img.setOnClickListener(view -> {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            });

            binding.out.setOnClickListener(view -> {
                SharedPreferences.Editor preference = PreferenceManager.getDefaultSharedPreferences(EditProfile.this).edit();
                preference.putString("uid", null);
                preference.putString("typeAPP", "e");
                preference.apply();

                FirebaseMessaging.getInstance().unsubscribeFromTopic(uid).addOnSuccessListener(aVoid -> Toast.makeText(getApplicationContext(),"Unsubscribe Success",Toast.LENGTH_LONG).show());

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(EditProfile.this, MainActivity.class));
            });

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    private String image = null;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
                if (data.getData() == null) return;
                image = data.getData().toString();
                binding.img.setImageURI(data.getData());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void uploadImage(final String filePath, final String ty) {

        if (filePath == null) return;
        PD.show();
        Bitmap bitmap = null;
        try {
            InputStream inputStream = getContentResolver().openInputStream(Uri.parse(filePath));
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            PD.dismiss();
        }
        if (bitmap == null){
            Toast.makeText(this, "Failed Try again", Toast.LENGTH_SHORT).show();
            PD.dismiss();
            return;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();

        final StorageReference ref = FirebaseStorage.getInstance().getReference()
                .child("images/"+ UUID.randomUUID().toString());
        UploadTask uploadTask = ref.putBytes(data);
        uploadTask.addOnFailureListener(exception -> {
            PD.dismiss();
            finish();
        }).addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
            if (ty.equals("p")){
                reference.child("PARENTS").child(uid).child("image").setValue(uri.toString());
            }else if (ty.equals("s")){
                reference.child("SCHOOLS").child(uid).child("image").setValue(uri.toString());
            }
            PD.dismiss();
            finish();
        }));


    }

}
