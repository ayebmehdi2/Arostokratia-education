package com.mehdi.blankactivity.ACTIVITYS;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.mehdi.blankactivity.FRAGMENTS.FragmentClasses;
import com.mehdi.blankactivity.FRAGMENTS.FragmentCommunity;
import com.mehdi.blankactivity.FRAGMENTS.FragmentNotifyParent;
import com.mehdi.blankactivity.FRAGMENTS.FragmentParentProfile;
import com.mehdi.blankactivity.FRAGMENTS.FragmentSchoolProfil;
import com.mehdi.blankactivity.R;
import com.mehdi.blankactivity.databinding.HomeScreenBinding;

public class HomeActivity extends AppCompatActivity {

    HomeScreenBinding binding;

    int firstImageOff;
    int firstImageOn;
    String fisrtName;
    Fragment firstFragment;
    Fragment profilFrag;

    private String HomeType;


    @Override
    public void onBackPressed() {
        if (binding.mainPager.getCurrentItem() == 0) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        } else {
            binding.mainPager.setCurrentItem(binding.mainPager.getCurrentItem() - 1);
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.home_screen);


        SharedPreferences prf = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
        HomeType = prf.getString("typeAPP", "e");

        if (HomeType.equals("p")){
            firstImageOff = R.drawable.megaphone_off;
            fisrtName = "Notifications";
            firstImageOn = R.drawable.ic_megaphone;
            firstFragment = new FragmentNotifyParent();
            profilFrag = new FragmentParentProfile();
        }else if(HomeType.equals("s")){
            firstImageOff = R.drawable.ic_presentation_off;
            fisrtName = "Classes";
            firstImageOn = R.drawable.ic_presentation;
            firstFragment =new FragmentClasses();
            profilFrag = new FragmentSchoolProfil();
        }else {
            SharedPreferences.Editor preference = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this).edit();
            preference.putString("uid", null);
            preference.putString("typeAPP", "e");
            preference.apply();
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(HomeActivity.this, "Account Not found", Toast.LENGTH_LONG).show();
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }

        binding.mainName.setText(fisrtName);
        defaul();
        binding.not.setImageDrawable(getResources().getDrawable(firstImageOn));

        PagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(),
                new Fragment[] { firstFragment, new FragmentCommunity(), profilFrag  });
        binding.mainPager.setAdapter(pagerAdapter);

        binding.mainPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
            @Override
            public void onPageSelected(int position) {
                if (position == 0){
                    defaul();
                    binding.not.setImageDrawable(getResources().getDrawable(firstImageOn));
                    binding.mainName.setText(fisrtName);
                }else if (position == 1){
                    defaul();
                    binding.msg.setImageDrawable(getResources().getDrawable(R.drawable.ic_message));
                    binding.mainName.setText("Community");
                }else if (position == 2){
                    defaul();
                    binding.prof.setImageDrawable(getResources().getDrawable(R.drawable.ic_user));
                    binding.mainName.setText("Profile");
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });


        binding.not.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.mainPager.setCurrentItem(0);
            }
        });

        binding.msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.mainPager.setCurrentItem(1);
            }
        });

        binding.prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.mainPager.setCurrentItem(2);
            }
        });


    }

    public void defaul(){
        binding.not.setImageDrawable(getResources().getDrawable(firstImageOff));
        binding.msg.setImageDrawable(getResources().getDrawable(R.drawable.message_off));
        binding.prof.setImageDrawable(getResources().getDrawable(R.drawable.ic_user_off));
    }



    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        private Fragment[] fragments;
        public ScreenSlidePagerAdapter(FragmentManager fm, Fragment[] fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return 3;
        }
    }



}
