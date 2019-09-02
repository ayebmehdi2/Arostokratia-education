package com.mehdi.blankactivity.ACTIVITYS;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.database.FirebaseDatabase;
import com.mehdi.blankactivity.DATAS.NOTE;
import com.mehdi.blankactivity.R;
import com.mehdi.blankactivity.databinding.NoteSendBinding;

import java.text.DateFormat;
import java.util.Random;

public class SendNoteActivity extends AppCompatActivity {


    NoteSendBinding binding;
    private String desk, nameClass;
    private int time, imageDesk;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.note_send);


        nameClass = getIntent().getStringExtra("nameClass");
        desk = getIntent().getStringExtra("desk");
        imageDesk = getIntent().getIntExtra("deskImage", 1);
        time = (int) System.currentTimeMillis();

        binding.desc.setText(desk);


        String date = DateFormat.getTimeInstance(DateFormat.SHORT).format(time) + " - " +
                DateFormat.getDateInstance().format(time);

        binding.date.setText(date);

        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String detail = binding.detail.getText().toString();
                if (detail.length() > 0){
                    String ChildNoteName = new Random().nextInt(1000) + "--" + detail;
                    NOTE note = new NOTE(detail, desk, imageDesk, time);
                    FirebaseDatabase.getInstance().getReference().child("SCHOOLS").child("CLASSES").child(nameClass)
                    .child("NOTE").child(ChildNoteName).setValue(note);
                }
            }
        });

    }
}
