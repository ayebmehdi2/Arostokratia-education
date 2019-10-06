package com.mehdi.blankactivity.ACTIVITYS;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mehdi.blankactivity.DATAS.CHILD;
import com.mehdi.blankactivity.R;
import com.mehdi.blankactivity.databinding.KidDetailBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DetailKids extends AppCompatActivity {

    KidDetailBinding binding;
    private DatabaseReference reference;
    private String name = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            binding = DataBindingUtil.setContentView(this, R.layout.kid_detail);

            SharedPreferences prf = PreferenceManager.getDefaultSharedPreferences(this);
            String uid = prf.getString("uid", "e");


            reference = FirebaseDatabase.getInstance().getReference();


            boolean inSchool = getIntent().getBooleanExtra("inSchool", false);

            if (inSchool){
                String de = getIntent().getStringExtra("de");
                if (de == null) return;
                reference = reference.child("SCHOOLS").child(uid)
                        .child("CLASSES").child(uid + "--" + de.split(",")[0]).child("STUDENTS").child(de.split(",")[1]);
            }else {
                String name = getIntent().getStringExtra("nameKid");
                if (name == null) return;
                reference = reference.child("PARENTS").child(uid).child("CHILDREN").child(name);
            }

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    CHILD child = dataSnapshot.getValue(CHILD.class);
                    if (child == null) return;
                    name = child.getName();
                    binding.kidName.setText(child.getName());
                    try {
                        Glide.with(DetailKids.this).load(child.getPhoto()).into(binding.kidPhoto);
                        Glide.with(DetailKids.this).load(child.getQRcode()).into(binding.kidQrCode);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            binding.delete.setOnClickListener(view -> {
                reference.removeValue();
                startActivity(new Intent(DetailKids.this, HomeActivity.class));
            });

            binding.share.setOnClickListener(view -> {
                BitmapDrawable drawable = (BitmapDrawable) binding.kidQrCode.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                new AsyncDownImageShare().execute(bitmap);
                Toast.makeText(DetailKids.this, "Wait .. ", Toast.LENGTH_LONG).show();

            });

            binding.down.setOnClickListener(view -> {
                BitmapDrawable drawable = (BitmapDrawable) binding.kidQrCode.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                MediaStore.Images.Media.insertImage(
                        getContentResolver(),
                        bitmap,
                        name,
                        "QR code of " + name
                );

                Toast.makeText(DetailKids.this, "Image saved check gallery", Toast.LENGTH_LONG).show();

            });
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @SuppressLint("StaticFieldLeak")
    class AsyncDownImageShare extends AsyncTask<Bitmap, Void, Uri>{

        @Override
        protected Uri doInBackground(Bitmap... bitmaps) {
            return saveImage(bitmaps[0]);
        }

        @Override
        protected void onPostExecute(Uri uri) {
            shareImageUri(uri);
        }
    }

    private Uri saveImage(Bitmap image) {
        File imagesFolder = new File(getCacheDir(), "images");
        Uri uri = null;
        try {
            imagesFolder.mkdirs();
            File file = new File(imagesFolder, "shared_image.png");

            FileOutputStream stream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.flush();
            stream.close();
            uri = FileProvider.getUriForFile(this, "com.mehdi.blankactivity", file);

        } catch (IOException e) {
            Log.d("DETAILKIDS", "IOException while trying to write file for sharing: " + e.getMessage());
        }
        return uri;
    }

    private void shareImageUri(Uri uri){
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/png");
        startActivity(intent);
    }
}
