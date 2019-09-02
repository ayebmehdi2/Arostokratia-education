package com.mehdi.blankactivity.ACTIVITYS;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.mehdi.blankactivity.FRAGMENTS.FragmentClass;
import com.mehdi.blankactivity.FRAGMENTS.FragmentCommunity;
import com.mehdi.blankactivity.R;
import com.mehdi.blankactivity.databinding.ClassLayoutBinding;

public class ActivityClass extends AppCompatActivity {


    ClassLayoutBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.class_layout);

        PagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        binding.pagerClass.setAdapter(pagerAdapter);

        binding.pagerClass.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
            @Override
            public void onPageSelected(int position) {
                if (position == 0){
                    binding.v1.setVisibility(View.VISIBLE);
                    binding.v2.setVisibility(View.GONE);
                    ActionInLesson();
                }else if (position == 1){
                    binding.v1.setVisibility(View.GONE);
                    binding.v2.setVisibility(View.VISIBLE);
                    binding.more.setVisibility(View.GONE);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });


        binding.les.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.pagerClass.setCurrentItem(0);
            }
        });



        binding.les.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.pagerClass.setCurrentItem(1);
            }
        });

    }


    public void ActionInLesson(){
        binding.more.setVisibility(View.VISIBLE);
        binding.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.action.setVisibility(View.VISIBLE);

                binding.newLesson.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // New Lesson
                        binding.more.setVisibility(View.GONE);

                    }
                });

                binding.dm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // DM office
                        binding.more.setVisibility(View.GONE);

                    }
                });


                binding.absent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // teacher absent
                        binding.more.setVisibility(View.GONE);

                    }
                });

                binding.more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        binding.action.setVisibility(View.GONE);
                    }
                });
            }
        });

    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
                return new FragmentClass(position);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }


}
