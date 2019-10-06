package com.mehdi.blankactivity.ACTIVITYS;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mehdi.blankactivity.ADAPTERS.AdapterComment;
import com.mehdi.blankactivity.DATAS.MESSAGE;
import com.mehdi.blankactivity.DATAS.PARENT;
import com.mehdi.blankactivity.DATAS.POST;
import com.mehdi.blankactivity.DATAS.SCHOOL;
import com.mehdi.blankactivity.R;
import com.mehdi.blankactivity.ShowImage;
import com.mehdi.blankactivity.databinding.LayoutPostBinding;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class
PostDetail extends AppCompatActivity {

    LayoutPostBinding binding;
    private DatabaseReference reference;
    private ValueEventListener listener, listener2;
    private String postId;
    private AdapterComment commentAdap;
    private int numComment;

    private String image, name = "";

    private ArrayList<MESSAGE> comments;

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            binding = DataBindingUtil.setContentView(this, R.layout.layout_post);

            postId = getIntent().getStringExtra("postId");
            if (postId == null || !(postId.length() > 0)) return;

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            reference = database.getReference();


            SharedPreferences prf = PreferenceManager.getDefaultSharedPreferences(this);
            String myId = prf.getString("uid", "e");
            String t = prf.getString("typeAPP", "e");

            if (t.equals("p")){
                reference.child("PARENTS").child(myId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        PARENT parent = dataSnapshot.getValue(PARENT.class);
                        if (parent == null) return;
                        name = parent.getName();
                        if (parent.getImage() == null) return;
                        if (parent.getImage().length() > 0){
                            image = parent.getImage();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }else if(t.equals("s")){
                reference.child("SCHOOLS").child(myId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        SCHOOL s = dataSnapshot.getValue(SCHOOL.class);
                        if (s == null) return;
                        name = s.getName();
                        if (s.getImage() == null) return;
                        if (s.getImage().length() > 0){
                            image = s.getImage();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }




            listener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final POST post = dataSnapshot.getValue(POST.class);
                    if (post == null) return;

                    if (post.getType().equals("p")){
                        reference.child("PARENTS").child(post.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final PARENT parent = dataSnapshot.getValue(PARENT.class);
                                if (parent == null) return;
                                binding.postUserName.setText(parent.getName());
                                if (parent.getImage() == null) return;
                                if (parent.getImage().length() > 0){
                                    try {
                                        Glide.with(PostDetail.this).load(parent.getImage()).into(binding.postUserTof);

                                        binding.postUserTof.setOnClickListener(view -> {
                                            Intent i = new Intent(PostDetail.this, ShowImage.class);
                                            i.putExtra("url", parent.getImage());
                                            startActivity(i);
                                        });
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if (post.getType().equals("s")){
                        reference.child("SCHOOLS").child(post.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final SCHOOL sch = dataSnapshot.getValue(SCHOOL.class);
                                if (sch == null) return;
                                binding.postUserName.setText(sch.getName());
                                if (sch.getImage() == null) return;
                                if (sch.getImage().length() > 0){
                                    try {
                                        Glide.with(PostDetail.this).load(sch.getImage()).into(binding.postUserTof);

                                        binding.postUserTof.setOnClickListener(view -> {
                                            Intent i = new Intent(PostDetail.this, ShowImage.class);
                                            i.putExtra("url", sch.getImage());
                                            startActivity(i);
                                        });

                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    numComment = post.getNumComment();

                    Date date = new Date(post.getTime());
                    if (System.currentTimeMillis() - post.getTime() > 86400000){
                        binding.postTime.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(date));
                    }else {
                        binding.postTime.setText(DateFormat.getDateInstance().format(date));
                    }

                    binding.postSub.setText(post.getSubject());
                    binding.postDesc.setText(post.getDesc());

                    if (post.getImage().length() > 0){
                        try {
                            Glide.with(PostDetail.this).load(post.getImage()).into(binding.postImg);

                            binding.postImg.setOnClickListener(view -> {
                                Intent i = new Intent(PostDetail.this, ShowImage.class);
                                i.putExtra("url", post.getImage());
                                startActivity(i);
                            });

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }else {
                        binding.postImg.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            binding.send.setOnClickListener(view -> {

                String msg = binding.box.getText().toString();

                if (!(msg.length() > 0) || (!(name.length() > 0) && !(image.length() > 0))) return;

                binding.box.setText("");
                numComment++;

                MESSAGE message = new MESSAGE(postId.split(",")[0], image, name, msg, (int) System.currentTimeMillis());

                reference.child("COMMUNITY").child(postId).child("COMMENTS").push().setValue(message);

                reference.child("COMMUNITY").child(postId).child("numComment").setValue(numComment);
            });

            comments = new ArrayList<>();

            commentAdap = new AdapterComment();
            binding.recCommit.setLayoutManager(new LinearLayoutManager(this));
            binding.recCommit.setHasFixedSize(true);
            binding.recCommit.setAdapter(commentAdap);

            listener2 = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    comments.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        MESSAGE child = snapshot.getValue(MESSAGE.class);
                        comments.add(child);
                    }
                    commentAdap.swapAdapter(comments);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            reference.child("COMMUNITY").child(postId).addValueEventListener(listener);
            reference.child("COMMUNITY").child(postId).child("COMMENTS").addValueEventListener(listener2);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            reference.child("COMMUNITY").child(postId).removeEventListener(listener);
            reference.child("COMMUNITY").child(postId).child("COMMENTS").removeEventListener(listener2);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
