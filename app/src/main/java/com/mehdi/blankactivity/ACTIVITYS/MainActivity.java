package com.mehdi.blankactivity.ACTIVITYS;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mehdi.blankactivity.FRAGMENTS.FragmentLogin;
import com.mehdi.blankactivity.FRAGMENTS.FragmentSignUp;
import com.mehdi.blankactivity.R;
import com.mehdi.blankactivity.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements FragmentLogin.ClickInLogin{


    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }


    ActivityMainBinding binding;

    private FirebaseAuth auth;
    private ProgressDialog PD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null){
            startActivity(new Intent(this, HomeActivity.class));
        }

        PagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(),
                new Fragment[] {new FragmentLogin(), new FragmentSignUp()});
        binding.pager.setAdapter(pagerAdapter);

        binding.pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
            @Override
            public void onPageSelected(int position) {
                if (position == 0){
                    binding.in.setTextColor(getResources().getColor(R.color.darkBlue));
                    binding.up.setTextColor(getResources().getColor(R.color.greeMoyenne));
                }else if (position == 1){
                    binding.up.setTextColor(getResources().getColor(R.color.darkBlue));
                    binding.in.setTextColor(getResources().getColor(R.color.greeMoyenne));
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });


        binding.in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.pager.setCurrentItem(0);
            }
        });

        binding.up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.pager.setCurrentItem(1);
            }
        });


    }

    @Override
    public void signup() {
        binding.pager.setCurrentItem(1);
    }

    @Override
    public void login(String email, String pass, final String t) {

        try {

            if (pass.length() > 0 && email.length() > 0) {
                PD.show();
                auth.signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(
                                            MainActivity.this,
                                            /*task.getResult().toString()*/"Login Failed",
                                            Toast.LENGTH_LONG).show();
                                } else {

                                    SharedPreferences.Editor preference = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                                    preference.putString("uid", auth.getUid());
                                    preference.apply();

                                    SharedPreferences.Editor prf = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                                    if (t.equals("p")){
                                        prf.putString("typeAPP", "p");
                                        prf.apply();
                                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                    }else {
                                        prf.putString("typeAPP", "s");
                                        prf.apply();
                                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                    }


                                    finish();
                                }
                                PD.dismiss();
                            }
                        });
            } else {
                Toast.makeText(
                        this,
                        "Fill All Fields",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            return 2;
        }
    }


}
