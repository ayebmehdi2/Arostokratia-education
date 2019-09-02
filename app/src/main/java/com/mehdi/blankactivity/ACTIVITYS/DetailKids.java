package com.mehdi.blankactivity.ACTIVITYS;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mehdi.blankactivity.DATAS.CHILD;
import com.mehdi.blankactivity.R;
import com.mehdi.blankactivity.databinding.KidDetailBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DetailKids extends AppCompatActivity {

    private String name;
    KidDetailBinding binding;
    private String uid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.kid_detail);

        SharedPreferences prf = PreferenceManager.getDefaultSharedPreferences(this);
        uid = prf.getString("uid", "e");
        name = getIntent().getStringExtra("nameKid");

        FirebaseDatabase.getInstance().getReference().child("PARENTS").child(uid).child("CHILDREN").child(name)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        CHILD child = dataSnapshot.getValue(CHILD.class);
                        if (child == null) return;
                        binding.kidName.setText(child.getName());
                        try {
                            Glide.with(DetailKids.this).load(child.getPhoto()).into(binding.kidPhoto);
                            Glide.with(DetailKids.this).load(child.getQRcode()).into(binding.kidQrCode);
                        }catch (Exception e){ }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("PARENTS").child(uid).child("CHILDREN").child(name)
                        .removeValue();
                startActivity(new Intent(DetailKids.this, HomeActivity.class));
            }
        });

        binding.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitmapDrawable drawable = (BitmapDrawable) binding.kidQrCode.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                new AsyncDownImageShare().execute(bitmap);
            }
        });

        binding.down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitmapDrawable drawable = (BitmapDrawable) binding.kidQrCode.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                new AsyncDownImage().execute(bitmap);
            }
        });


    }

    @Override
    public void onBackPressed() {
        finish();
    }

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

    class AsyncDownImage extends AsyncTask<Bitmap, Void, Void>{

        @Override
        protected Void doInBackground(Bitmap... bitmaps) {
            saveImage(bitmaps[0]);
            return null;
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
