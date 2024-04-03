package com.example.healthbuddy.Onboarding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.example.healthbuddy.MainActivity;
import com.example.healthbuddy.R;
import com.example.healthbuddy.Splash.SplashActivity;

public class OnboardingWelcomeActivity extends AppCompatActivity {

    private TextView txtView_name;

    Intent intent;

    private String mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_welcome);

        intent = getIntent();

        initWidget();

        getTransferredData();

        // Splash Page for 2 Seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(OnboardingWelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },1500);
    }

    private void getTransferredData() {

        mName = intent.getStringExtra("Name");

        initUI();
    }

    private void initUI() {

        txtView_name.setText(mName);
    }

    private void initWidget() {

        // TextView
        txtView_name = findViewById(R.id.txtView_name);
    }
}