package com.mehdi.blankactivity.ACTIVITYS;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.mehdi.blankactivity.FRAGMENTS.FragmentClass;
import com.mehdi.blankactivity.R;
import com.mehdi.blankactivity.databinding.ClassLayoutBinding;

public class ActivityClass extends AppCompatActivity {

    ClassLayoutBinding binding;
    String nameClass;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            binding = DataBindingUtil.setContentView(this, R.layout.class_layout);

            ActionInLesson();

            PagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
            binding.pagerClass.setAdapter(pagerAdapter);

            nameClass = getIntent().getStringExtra("nameClass");

            if (nameClass == null) return;
            if (nameClass.length() > 20){
                String s = nameClass.substring(0, 20) + "...";
                binding.clsName.setText(s);
            }else {
                binding.clsName.setText(nameClass);
            }


            binding.pagerClass.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
                @Override
                public void onPageSelected(int position) {
                    if (position == 0){
                        binding.v1.setVisibility(View.VISIBLE);
                        binding.v2.setVisibility(View.INVISIBLE);
                        ActionInLesson();
                    }else if (position == 1){
                        binding.v1.setVisibility(View.INVISIBLE);
                        binding.v2.setVisibility(View.VISIBLE);
                        binding.more.setVisibility(View.GONE);
                        binding.action.setVisibility(View.GONE);
                    }
                }
                @Override
                public void onPageScrollStateChanged(int state) {}
            });


            binding.les.setOnClickListener(view -> binding.pagerClass.setCurrentItem(0));



            binding.stu.setOnClickListener(view -> binding.pagerClass.setCurrentItem(1));


        }catch (Exception e){
            e.printStackTrace();
        }

        }


        public void ActionInLesson(){
            binding.more.setVisibility(View.VISIBLE);
            binding.more.setOnClickListener(view -> {
                binding.action.setVisibility(View.VISIBLE);

                binding.newLesson.setOnClickListener(view1 -> {

                    Intent i = new Intent(ActivityClass.this, SendNoteActivity.class);
                    i.putExtra("nameClass", nameClass);
                    i.putExtra("desk", "New Lesson");
                    i.putExtra("deskImage", 1);
                    startActivity(i);

                    binding.more.setVisibility(View.GONE);

                });

                binding.dm.setOnClickListener(view12 -> {

                    // DM office
                    Intent i = new Intent(ActivityClass.this, SendNoteActivity.class);
                    i.putExtra("nameClass", nameClass);
                    i.putExtra("desk", "From DM Office");
                    i.putExtra("deskImage", 2);
                    startActivity(i);

                    binding.more.setVisibility(View.GONE);

                });


                binding.absent.setOnClickListener(view13 -> {

                    // teacher absent
                    Intent i = new Intent(ActivityClass.this, SendNoteActivity.class);
                    i.putExtra("nameClass", nameClass);
                    i.putExtra("desk", "Teacher abesnt");
                    i.putExtra("deskImage", 3);
                    startActivity(i);

                    binding.more.setVisibility(View.GONE);

                });

                binding.more.setOnClickListener(view14 -> {
                    binding.action.setVisibility(View.GONE);
                    ActionInLesson();
                });
            });


    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
                return new FragmentClass(position, nameClass);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }


    @Override
    public void onBackPressed() {
        Intent i =new Intent(this, HomeActivity.class);
        startActivity(i);
    }
}
