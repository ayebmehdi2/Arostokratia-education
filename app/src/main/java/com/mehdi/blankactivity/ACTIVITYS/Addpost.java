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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mehdi.blankactivity.DATAS.POST;
import com.mehdi.blankactivity.R;
import com.mehdi.blankactivity.databinding.PostSomethingBinding;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Random;
import java.util.UUID;

public class Addpost extends AppCompatActivity {

    PostSomethingBinding binding;

    private String image = null;
    private String uid;

    private ProgressDialog PD;

    private DatabaseReference reference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            binding = DataBindingUtil.setContentView(this, R.layout.post_something);

            SharedPreferences prf = PreferenceManager.getDefaultSharedPreferences(this);
            uid = prf.getString("uid", "e");

            FirebaseStorage storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            reference = database.getReference();

            PD = new ProgressDialog(this);
            PD.setMessage("Loading...");
            PD.setCancelable(true);
            PD.setCanceledOnTouchOutside(false);

            binding.imgIn.setOnClickListener(view -> {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            });

            binding.post.setOnClickListener(view -> {
                if (image != null){
                    if (image.length() > 0){
                        uploadImage(image);
                    }
                }else {
                    uploadPost("");
                }
            });

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    public void uploadPost(String im){
        String sub = binding.sub.getText().toString();
        String des = binding.des.getText().toString();

        if (!(sub.length() > 0 )){
            Toast.makeText(Addpost.this, "Enter Subject", Toast.LENGTH_LONG).show();
            return;
        }

        if (!(des.length() > 0 )){
            Toast.makeText(Addpost.this, "Enter Description", Toast.LENGTH_LONG).show();
            return;
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Addpost.this);


        String[] colors = new String[] {"D50000", "C51162", "AA00FF", "304ffe", "2962FF", "0091EA","00BFA5" , "FFD600", "DD2C00", "245A64"};

        int color = Integer.parseInt(colors[new Random().nextInt(9)], 16)+0xFF000000;
        POST post = new POST(uid, sub, des, im, 0
                , (int) System.currentTimeMillis(), preferences.getString("typeAPP", "e"), color);

        reference.child("COMMUNITY").child(uid + "--" + sub).setValue(post);

        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
    }

    private void uploadImage(String filePath) {

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

        final StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
        UploadTask uploadTask = ref.putBytes(data);
        uploadTask.addOnFailureListener(exception -> {
            uploadPost("");
            PD.dismiss();
        }).addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
           uploadPost(uri.toString());
            PD.dismiss();

        }));




    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       try {
           if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
               if (data.getData() == null) return;
               image = data.getData().toString();
               binding.imgOut.setImageURI(data.getData());
           }
       }catch (Exception e){
           e.printStackTrace();
       }
    }
}
