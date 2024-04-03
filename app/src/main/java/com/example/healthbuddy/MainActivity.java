package com.example.healthbuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import com.example.healthbuddy.Fragment.Exit;
import com.example.healthbuddy.Fragment.Fitness;
import com.example.healthbuddy.Fragment.Home;
import com.example.healthbuddy.Fragment.Nutrition;
import com.example.healthbuddy.Fragment.Profile;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {

    ChipNavigationBar chipNavigationBar;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    // Variables to store data
    private String mName, mPhoneNumber, mEmail, mPassword;

    Intent intent;

    FirebaseUser user;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intent = getIntent();


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

//        mEmail = user.getEmail();


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct!=null) {
            mName = acct.getDisplayName();
            mEmail = acct.getEmail();

            System.out.println(mName);
            System.out.println(mEmail);
        }

        initWidget();

        getIntentData();
    }

    private void getIntentData() {

        mName = intent.getStringExtra("Name");
        mPhoneNumber = intent.getStringExtra("Phone Number");
        mEmail = intent.getStringExtra("Email");
        mPassword = intent.getStringExtra("Password");
    }

    private void initWidget() {
        chipNavigationBar = findViewById(R.id.menu);

        chipNavigationBar.setItemSelected(R.id.bottom_nav_home, true);


        bottomMenu();
    }

    private void bottomMenu() {

        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch(i) {
                    case R.id.bottom_nav_nutrition:
                        fragment = new Nutrition();
                        break;
                    case R.id.bottom_nav_fitness:
                        fragment = new Fitness();
                        break;
                    case R.id.bottom_nav_home:
                        fragment = new Home();
                        break;
                    case R.id.bottom_nav_profile:
                        fragment = new Profile();
                        break;
                }

                Bundle bundle = new Bundle();
                bundle.putString("Name", mName);
                bundle.putString("Phone Number", mPhoneNumber);
                bundle.putString("Email", mEmail);
                bundle.putString("Password", mPassword);

                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        });
    }


}