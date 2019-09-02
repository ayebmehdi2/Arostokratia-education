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
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.mehdi.blankactivity.R;
import com.mehdi.blankactivity.databinding.AddKidBinding;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

public class AddKid extends AppCompatActivity {


    private String image = null;
    AddKidBinding binding;
    private String uid;

    private ProgressDialog PD;

    private DatabaseReference reference;
    private StorageReference storageReference;

    private int childNumbers = 0;
    private int counterChild = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.add_kid);

        SharedPreferences prf = PreferenceManager.getDefaultSharedPreferences(this);
        uid = prf.getString("uid", "e");

        childNumbers = getIntent().getIntExtra("numChild" , 1);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);

        setupChild(counterChild);

    }

    public void setupChild(int number){
        binding.child.setVisibility(View.VISIBLE);

        binding.img.setImageDrawable(getResources().getDrawable(R.drawable.ic_add));
        binding.kidName.setText("");
        image = null;

        String d = "Kid number " + number;
        binding.descKid.setText(d);

        binding.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });

        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = binding.kidName.getText().toString();

                if (name.length() > 0){
                    CHILD child = new CHILD(uid, name, "", "");
                    if (image != null){
                        if (image.length() > 0){
                            uploadImage(image, child);
                        }
                    }else {
                        AsyncT asyncT = new AsyncT();
                        asyncT.execute(child);
                    }
                }
            }
        });


    }

    private void uploadImage(String filePath, final CHILD code) {

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
        };
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();

        final StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
        UploadTask uploadTask = ref.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                AsyncT asyncT = new AsyncT();
                asyncT.execute(code);
                PD.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        AsyncT asyncT = new AsyncT();
                        asyncT.execute(new CHILD(code.getUid(), code.getName(), uri.toString(), ""));
                        PD.dismiss();

                    }
                });

            }
        });


    }

    class AsyncT extends AsyncTask<CHILD, Void, CHILDcode> {


        @Override
        protected CHILDcode doInBackground(CHILD... children) {
            Bitmap bmp = null;
            CHILD child = children[0];
            if (child == null) return null;
            try {
                QRCodeWriter writer = new QRCodeWriter();
                BitMatrix bitMatrix = writer.encode(child.getName() + "--" + child.getUid() , BarcodeFormat.QR_CODE, 150, 150);
                int width = bitMatrix.getWidth();
                int height = bitMatrix.getHeight();
                bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                    }
                }

            } catch (WriterException e) {
                e.printStackTrace();
            }
            return new CHILDcode(child.getUid(), child.getName(), child.getPhoto(), bmp);
        }


        @Override
        protected void onPostExecute(CHILDcode chilDcode) {
            binding.child.setVisibility(View.GONE);
            if (uid != null) {
                uploadBitmap(chilDcode);
            }
        }

    }


    private void uploadBitmap(final CHILDcode chilDcode) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        chilDcode.getQRcode().compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] data = baos.toByteArray();

        final StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
        UploadTask uploadTask = ref.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                reference.child("PARENTS").child(Objects.requireNonNull(uid))
                        .child("CHILDREN").child(chilDcode.getName())
                        .setValue(new CHILD(uid, chilDcode.getName(), chilDcode.getPhoto(), ""));
                PD.dismiss();
                if (counterChild < childNumbers) {
                    counterChild += 1;
                    setupChild(counterChild);
                }else {
                    stratHome("p");
                }
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        reference.child("PARENTS").child(Objects.requireNonNull(uid))
                                .child("CHILDREN").child(chilDcode.getName())
                                .setValue(new CHILD(uid, chilDcode.getName(), chilDcode.getPhoto(), uri.toString()));
                        PD.dismiss();
                        if (counterChild < childNumbers) {
                            counterChild += 1;
                            setupChild(counterChild);
                        }else {
                            stratHome("p");
                        }

                    }
                });

            }
        });


    }

    public void stratHome(String t){
        SharedPreferences.Editor prf = PreferenceManager.getDefaultSharedPreferences(this).edit();
        prf.putString("typeAPP", t);
        prf.apply();
        startActivity(new Intent(this, HomeActivity.class));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            if (data.getData() == null) return;
            image = data.getData().toString();
            binding.img.setImageURI(data.getData());
        }
    }


}
