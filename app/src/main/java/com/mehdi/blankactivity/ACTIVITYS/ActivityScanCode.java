package com.mehdi.blankactivity.ACTIVITYS;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mehdi.blankactivity.DATAS.CHILD;
import com.mehdi.blankactivity.DATAS.NOTE;
import com.mehdi.blankactivity.DATAS.NOTE_FOR_PARENT;
import com.mehdi.blankactivity.MySingleton;
import com.mehdi.blankactivity.R;
import com.mehdi.blankactivity.databinding.LayoutScanBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ActivityScanCode extends AppCompatActivity {

    LayoutScanBinding binding;
    public static NOTE lessonDetail = null;

    private DatabaseReference reference;
    private String uid;
    private String nameClass;

    final private String serverKey = "key=" + "AAAAfyZSGhs:APA91bFkyGzIhur1gHuQIKq4MAvSwkd5AK_YqAqhLy8R8WZkZJilEistLVwnpNpClrbzv8X2mHcUmTuGrtdaSvhNAv4I_Ytwf6HmUbYwb3aayJEPzlGKcJ7iaHILcDlvxxWGFQ17m9kc";

    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            binding = DataBindingUtil.setContentView(this, R.layout.layout_scan);
            SharedPreferences prf = PreferenceManager.getDefaultSharedPreferences(this);
            uid = prf.getString("uid", "e");

            nameClass = getIntent().getStringExtra("nameClass");

            reference = FirebaseDatabase.getInstance().getReference();

            if (lessonDetail == null) return;

            layoutScan();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void layoutScan(){
        binding.layoutInfo.setVisibility(View.GONE);
        binding.layoutScan.setVisibility(View.VISIBLE);
        binding.scan.setOnClickListener(view -> goScanCode());

        binding.finish.setOnClickListener(view -> {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(ActivityScanCode.this);
            builder1.setMessage("Are you sure you setup all thing ?");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Yes",
                    (dialog, id) -> {
                        if (nameClass == null) return;
                        dialog.cancel();
                        Intent i = new Intent(ActivityScanCode.this, ActivityClass.class);
                        i.putExtra("nameClass", nameClass);
                        startActivity(i);
                    });

            builder1.setNegativeButton(
                    "No",
                    (dialog, id) -> dialog.cancel());

            AlertDialog alert11 = builder1.create();
            alert11.show();

        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ActivityScanCode.this, ActivityClass.class);
        i.putExtra("nameClass", nameClass);
        startActivity(i);
    }

    public void layoutInfo(final String contents){

        binding.layoutScan.setVisibility(View.GONE);
        binding.layoutInfo.setVisibility(View.VISIBLE);

        if (contents == null || !(contents.length() > 0)){
            layoutScan();
            return;
        }

        binding.noteDesk.setText(lessonDetail.getDesk());
        binding.noteDetail.setText(lessonDetail.getDetail());


        final String kidName = contents.split("--")[0];

        final String parentId = contents.split("--")[1];

        binding.studentName.setText(kidName);

        final CHILD[] chi = {null};

         final ValueEventListener lis = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CHILD child = dataSnapshot.getValue(CHILD.class);
                if (child == null) return;
                chi[0] = child;
                try {
                    Glide.with(ActivityScanCode.this).load(child.getPhoto()).into(binding.studentImg);
                }catch (Exception ignored){ }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        reference.child("PARENTS").child(parentId).child("CHILDREN").child(kidName).addValueEventListener(lis);


        binding.send.setOnClickListener(
                view -> {

                    if (chi[0] != null) {

                                                    String noteUid = lessonDetail.getDetail() + "--" + chi[0].getName();
                                                    reference.child("PARENTS").child(parentId).child("NOTE").child(noteUid)
                                                            .setValue(new NOTE_FOR_PARENT(noteUid, chi[0].getName(), chi[0].getPhoto(), lessonDetail.getDetail(),
                                                                    lessonDetail.getDesk(), lessonDetail.getImageDesk(),
                                                                    (int) System.currentTimeMillis()));

                                                    reference.child("SCHOOLS").child(uid)
                                                            .child("CLASSES").child(uid + "--" + nameClass).child("STUDENTS").child(contents)
                                                            .setValue(chi[0]);



                                                    TOPIC = "/topics/" + parentId; //topic must match with what the receiver subscribed to
                                                    NOTIFICATION_TITLE = chi[0].getName() + " - " + lessonDetail.getDetail();
                                                    NOTIFICATION_MESSAGE = DateFormat.getDateInstance().format(new Date()) + " - " + lessonDetail.getDesk();

                                                    JSONObject notification = new JSONObject();
                                                    JSONObject notifcationBody = new JSONObject();
                                                    try {
                                                        notifcationBody.put("title", NOTIFICATION_TITLE);
                                                        notifcationBody.put("message", NOTIFICATION_MESSAGE);

                                                        notification.put("to", TOPIC);
                                                        notification.put("data", notifcationBody);
                                                    } catch (JSONException e) {
                                                        Log.e(TAG, "onCreate: " + e.getMessage() );
                                                    }
                                                    sendNotification(notification);

                                                }

                    binding.studentName.setText("");
                    binding.studentImg.setImageResource(0);

                    reference.child("PARENTS").child(parentId).child("CHILDREN").child(kidName).removeEventListener(lis);

                    layoutScan();

                });


        binding.cancel.setOnClickListener(view -> {
            binding.studentName.setText("");
            binding.studentImg.setImageResource(0);

            reference.child("PARENTS").child(parentId).child("CHILDREN").child(kidName).removeEventListener(lis);

            layoutScan();
        });


            }

    private void sendNotification(JSONObject notification) {
        String FCM_API = "https://fcm.googleapis.com/fcm/send";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                response -> Log.i(TAG, "onResponse: " + response.toString()),
                error -> {
                    Toast.makeText(ActivityScanCode.this, "Request error", Toast.LENGTH_LONG).show();
                    Log.i(TAG, "onErrorResponse: Didn't work");
                }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    public void goScanCode(){

        IntentIntegrator integrator = new IntentIntegrator(ActivityScanCode.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if(result != null) {
                if(result.getContents() == null) {
                    Log.e("Scan*******", "Cancelled scan");

                } else {
                    Log.e("Scan", "Scanned");

                    layoutInfo(result.getContents());
                    Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                }
            } else {
                // This is important, otherwise the result will not be passed to the fragment
                super.onActivityResult(requestCode, resultCode, data);
            }
        }catch (Exception e){
            super.onActivityResult(requestCode, resultCode, data);
            e.printStackTrace();
        }


    }

}
