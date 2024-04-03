package com.example.healthbuddy.Onboarding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.healthbuddy.MainActivity;
import com.example.healthbuddy.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AskInformationActivity extends AppCompatActivity {

    private EditText editTxt_firstName, editTxt_lastName ,editTxt_age;
    private Spinner sp_gender;
    private Button btn_save;
    private ImageView imgView_profilePic;

    Intent intent;

    // String to store input data
    private String mPhoneNumber, mEmail, mPassword;
    private String mName, mAge, mGender = "";

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://healthbuddy-42e58-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference databaseReference  = database.getReference().child("users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_information);

        intent = getIntent();

        initWidget();

        getTransferredData();

        pageDirectories();
    }

    private void getTransferredData() {

        mPhoneNumber = intent.getStringExtra("Phone Number");
        mEmail = intent.getStringExtra("Email");
        mPassword = intent.getStringExtra("Password");

    }

    private void pageDirectories() {

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mName = (editTxt_firstName.getText().toString()) + "" + (editTxt_lastName.getText().toString());
                mAge = editTxt_age.getText().toString();

                validateName();
                validateAge();
                validateGender();
                validateInput();
            }
        });

    }

    private void validateInput() {
        if (!validateName() | !validateAge() | !validateGender()) {
            return;
        }
        else {
            // ADD DATA TO FIREBASE
            addData();



            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("Name", mName);
            startActivity(intent);

        }
    }

    private void addData() {

        //Adding data into google realtime database
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Checking if phone number is not registered before
                if (snapshot.hasChild(mPhoneNumber))
                {
                    //--->Asking user if he/she wants ti log in to existing account
                    AlertDialog.Builder builder = new AlertDialog.Builder(AskInformationActivity.this);
                    builder.setTitle("HealthBuddy");
                    builder.setMessage("Hey there, it seems like there's an existing account with the same name.");
                    builder.setNegativeButton("Register a new account", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                        }
                    });
                    builder.setPositiveButton("Log in to existing account", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(getApplicationContext(), OnboardingActivity.class));
                        }
                    });
                    builder.create().show();
                }
                else
                {
                    // Addding user's personal info to firebase
                    databaseReference.child(mPhoneNumber).child("User's Information").child("Full Name").setValue(mName);
                    databaseReference.child(mPhoneNumber).child("User's Information").child("Phone Number").setValue(mPhoneNumber);
                    databaseReference.child(mPhoneNumber).child("User's Information").child("Email").setValue(mEmail);
                    databaseReference.child(mPhoneNumber).child("User's Information").child("Password").setValue(mPassword);
                    databaseReference.child(mPhoneNumber).child("User's Information").child("Age").setValue(mAge);
                    databaseReference.child(mPhoneNumber).child("User's Information").child("Gender").setValue(mGender);

                    Toast.makeText(AskInformationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("Name", mName);
                    intent.putExtra("Phone Number", mPhoneNumber);
                    intent.putExtra("Email", mEmail);
                    intent.putExtra("Password", mPassword);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean validateGender() {

        if (mGender.isEmpty()) {
            Toast.makeText(this, "Gender field is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
            return true;
    }

    private boolean validateAge() {

        if (mAge.isEmpty()) {
            editTxt_age.setError("Required");
            return false;
        }
        else
            return true;
    }

    private boolean validateName() {
        //Regex pattern to allow only alphabets
        Pattern regexName = Pattern.compile("^[a-zA-Z]+$");
        Matcher matcher = regexName.matcher(mName);

        if (mName.isEmpty())
        {
            Toast.makeText(this, "Name field cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!matcher.matches())
        {
            Toast.makeText(this, "Invalid Input", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
            return true;
    }


    private void initWidget() {

        // EditText
        editTxt_firstName = findViewById(R.id.editTxt_firstName);
        editTxt_lastName = findViewById(R.id.editTxt_lastName);
        editTxt_age = findViewById(R.id.editTxt_age);

        // Spinner
        sp_gender = findViewById(R.id.sp_gender);

        // Button
        btn_save = findViewById(R.id.btn_save);

        // ImageView
        imgView_profilePic = findViewById(R.id.imgView_profilePic);

        initSpinner();

    }

    private void initSpinner() {

        List<String> spinnerItems = new ArrayList<>();
        spinnerItems.add("Male");
        spinnerItems.add("Female");
        spinnerItems.add("Others");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerItems);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_gender.setAdapter(adapter);

        sp_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mGender = spinnerItems.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}