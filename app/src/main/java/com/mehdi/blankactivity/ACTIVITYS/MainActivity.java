package com.mehdi.blankactivity.ACTIVITYS;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mehdi.blankactivity.FRAGMENTS.FragmentLogin;
import com.mehdi.blankactivity.FRAGMENTS.FragmentSignUp;
import com.mehdi.blankactivity.R;
import com.mehdi.blankactivity.databinding.ActivityMainBinding;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements FragmentLogin.ClickInLogin{


    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void setupConnection(){
        if (!isNetworkAvailable()){
            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
            builder1.setMessage("No internet connection !");
            builder1.setCancelable(true);
            builder1.setPositiveButton(
                    "Try",
                    (dialog, id) -> {
                        dialog.cancel();
                        setupConnection();
                    });

            builder1.setNegativeButton(
                    "Exit",
                    (dialog, id) -> {
                        dialog.cancel();
                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
                    });

            builder1.setCancelable(false);

            AlertDialog alert11 = builder1.create();
            alert11.show();

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            setupConnection();
        }catch (Exception ignored){ }
    }

    ActivityMainBinding binding;

    private FirebaseAuth auth;
    private ProgressDialog PD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {

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


            binding.in.setOnClickListener(view -> binding.pager.setCurrentItem(0));

            binding.up.setOnClickListener(view -> binding.pager.setCurrentItem(1));

        }catch (Exception ignored){

        }

    }

    @Override
    public void signup() {
        try {
            binding.pager.setCurrentItem(1);
        }catch (Exception ignored){

        }
    }

    @Override
    public void login(String email, String pass, final String t) {

        try {

            if (pass.length() > 0 && email.length() > 0) {
                PD.show();
                auth.signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(this, task -> {
                            if (!task.isSuccessful()) {
                                Toast.makeText(
                                        MainActivity.this,
                                        /*task.getResult().toString()*/"Login Failed",
                                        Toast.LENGTH_LONG).show();
                            } else {

                                SharedPreferences.Editor preference = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                                preference.putString("uid", auth.getUid());
                                preference.apply();

                                FirebaseMessaging.getInstance().subscribeToTopic(Objects.requireNonNull(auth.getUid())).addOnSuccessListener(aVoid -> Toast.makeText(getApplicationContext(),"Subscribe Success",Toast.LENGTH_LONG).show());

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
        ScreenSlidePagerAdapter(FragmentManager fm, Fragment[] fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @NonNull
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
