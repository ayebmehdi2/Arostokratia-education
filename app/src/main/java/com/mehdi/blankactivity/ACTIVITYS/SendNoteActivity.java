package com.mehdi.blankactivity.ACTIVITYS;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mehdi.blankactivity.DATAS.CHILD;
import com.mehdi.blankactivity.DATAS.NOTE;
import com.mehdi.blankactivity.DATAS.NOTE_FOR_PARENT;
import com.mehdi.blankactivity.R;
import com.mehdi.blankactivity.databinding.NoteSendBinding;

import java.text.DateFormat;
import java.util.Date;
import java.util.Random;

import static com.mehdi.blankactivity.ACTIVITYS.ActivityScanCode.lessonDetail;

public class SendNoteActivity extends AppCompatActivity {


    NoteSendBinding binding;
    private String desk, nameClass;
    private int time, imageDesk;
    private String uid;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            binding = DataBindingUtil.setContentView(this, R.layout.note_send);

            SharedPreferences prf = PreferenceManager.getDefaultSharedPreferences(this);
            uid = prf.getString("uid", "e");

            nameClass = getIntent().getStringExtra("nameClass");
            desk = getIntent().getStringExtra("desk");
            imageDesk = getIntent().getIntExtra("deskImage", 1);
            time = (int) System.currentTimeMillis();

            binding.desc.setText(desk);

            String date = DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date()) + " - " +
                    DateFormat.getDateInstance().format(new Date());

            binding.date.setText(date);

            binding.send.setOnClickListener(view -> {
                String detail = binding.detail.getText().toString();
                if (detail.length() > 0){
                    String ChildNoteName = new Random().nextInt(1000) + "--" + detail;
                    final NOTE note = new NOTE(ChildNoteName, detail, desk, imageDesk, time);
                    FirebaseDatabase.getInstance().getReference().child("SCHOOLS").child(uid)
                            .child("CLASSES").child(uid + "--" + nameClass).child("NOTE").child(ChildNoteName)
                            .setValue(note);

                    if (imageDesk != 3){
                        Intent in =new Intent(SendNoteActivity.this, ActivityScanCode.class);
                        lessonDetail = note;
                        in.putExtra("nameClass", nameClass);
                        startActivity(in);
                    }else {

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(SendNoteActivity.this);
                        builder1.setMessage("Attention, you will send this to all parent !");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Yes",
                                (dialog, id) -> {
                                    if (nameClass == null) return;
                                    dialog.cancel();

                                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                                    reference.child("SCHOOLS").child(uid)
                                            .child("CLASSES").child(uid + "--" + nameClass).child("STUDENTS")
                                            .addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                                        CHILD child = snapshot.getValue(CHILD.class);

                                                        if (child == null) return;

                                                        String noteUid = note.getDetail() + "--" + child.getName();
                                                        reference.child("PARENTS").child(child.getUid()).child("NOTE").child(noteUid)
                                                                .setValue(new NOTE_FOR_PARENT(noteUid, child.getName(), child.getPhoto(), note.getDetail(),
                                                                        note.getDesk(), 3,
                                                                        (int) System.currentTimeMillis()));

                                                    }


                                                    Intent i = new Intent(SendNoteActivity.this, ActivityClass.class);
                                                    i.putExtra("nameClass", nameClass);
                                                    startActivity(i);
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                }
                                            });

                                });

                        builder1.setNegativeButton(
                                "No",
                                (dialog, id) -> dialog.cancel());

                        AlertDialog alert11 = builder1.create();
                        alert11.show();

                    }

                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(SendNoteActivity.this, ActivityClass.class);
        i.putExtra("nameClass", nameClass);
        startActivity(i);
    }
}
