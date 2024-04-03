package com.example.healthbuddy.Onboarding;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthbuddy.MainActivity;
import com.example.healthbuddy.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OnboardingActivity extends AppCompatActivity {

    private EditText editTxt_email, editTxt_password, editTxt_phoneNumber;
    private TextView txtView_register, txtView_backdoor;
    private Button btn_login;

    // Variable to store input
    private String mEmail, mPassword, mPhoneNumber;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    FirebaseAuth mAuth;

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://healthbuddy-42e58-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference databaseReference  = database.getReference().child("users");

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        mAuth = FirebaseAuth.getInstance();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(getApplicationContext(), gso);

        initWidget();

        pageDirectories();

    }

    private void pageDirectories() {

        txtView_backdoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        txtView_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPhoneNumber = editTxt_phoneNumber.getText().toString();
                mEmail = editTxt_email.getText().toString();
                mPassword = editTxt_password.getText().toString();

                validatePhoneNumber();
                validateEmail();
                validatePassword();
                validateInput();
            }
        });
    }

    private boolean validatePhoneNumber() {
        return !mPhoneNumber.isEmpty();
    }

    private void googleSignIn() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            } catch (ApiException e) {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void validateInput() {

        if (!validatePhoneNumber() | !validateEmail() | !validatePassword())
            Toast.makeText(this, "Please check the details you have entered", Toast.LENGTH_SHORT).show();
        else
        {
            // Authenticating with real time firebase database
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(mPhoneNumber))
                    {
                        // Phone number exist in firebase database
                        // now getting password of user from firebase data and match if with user entered password and username

                        final String getName = snapshot.child(mPhoneNumber).child("User's Information").child("Full Name").getValue(String.class);
                        final String getPhoneNumber = snapshot.child(mPhoneNumber).child("User's Information").child("Phone Number").getValue(String.class);
                        final String getEmail = snapshot.child(mPhoneNumber).child("User's Information").child("Email").getValue(String.class);
                        final String getPassword = snapshot.child(mPhoneNumber).child("User's Information").child("Password").getValue(String.class);
                        final String getAge = snapshot.child(mPhoneNumber).child("User's Information").child("Age").getValue(String.class);
                        final String getGender = snapshot.child(mPhoneNumber).child("User's Information").child("Gender").getValue(String.class);

                        if (getPassword.equals(mPassword) && getPhoneNumber.equals(mPhoneNumber) && getEmail.equals(mEmail))
                        {
                            // Lead user to the Main Menu Page activity
                            Toast.makeText(OnboardingActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                            intent.putExtra("Name", getName);
                            intent.putExtra("Phone Number", mPhoneNumber);
                            intent.putExtra("Email", mEmail);
                            intent.putExtra("Password", mPassword);

                            startActivity(intent);

                            finish();
                        }
                        else
                            Toast.makeText(OnboardingActivity.this, "Log In unsuccessful, please check your password or username", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(OnboardingActivity.this, "Log In unsuccessful, please check your mobile number", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }
    }

    private boolean validatePassword() {
        //Regex pattern to require alphanumeric and special characters
        Pattern regexPassword = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$");
        Matcher matcher = regexPassword.matcher(mPassword);

        if (mPassword.isEmpty())
        {
            editTxt_password.setError("Required");
            return false;
        }
        else if (!matcher.matches())
        {
            editTxt_password.setError("Invalid password");
            return false;
        }
        else
            return true;
    }

    private boolean validateEmail() {
        if (mEmail.isEmpty())
        {
            editTxt_email.setError("Required");
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(mEmail).matches())
        {
            editTxt_email.setError("Invalid Email");
            return false;
        }
        else
            return true;
    }


    private void initWidget() {

        //TextView
        txtView_register = findViewById(R.id.txtView_register);
        txtView_backdoor = findViewById(R.id.txtView_backdoor);

        // EditText
        editTxt_phoneNumber = findViewById(R.id.editTxt_phoneNumber);
        editTxt_email = findViewById(R.id.editTxt_email);
        editTxt_password = findViewById(R.id.editTxt_password);

        // Button
        btn_login = findViewById(R.id.btn_login);
    }
}