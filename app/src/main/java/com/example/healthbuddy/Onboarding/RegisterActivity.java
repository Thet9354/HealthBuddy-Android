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
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private TextView txtView_logIn;
    private EditText editTxt_phoneNumber, editTxt_email, editTxt_password, editTxt_passwordConfirmation;
    private Button btn_getStarted;

    // Variables to store input data
    private String mPhoneNumber, mEmail, mPassword, mConfirmPassword;

    FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        initWidget();

        pageDirectories();
    }

    private void pageDirectories() {



        btn_getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPhoneNumber = editTxt_phoneNumber.getText().toString();
                mEmail = editTxt_email.getText().toString();
                mPassword = editTxt_password.getText().toString();
                mConfirmPassword = editTxt_passwordConfirmation.getText().toString();

                validatePhoneNumber();
                validateEmail();
                validatePassword();
                validateConfirmPassword();
                validateInputs();
            }
        });
    }

    private boolean validatePhoneNumber() {
        return !mPhoneNumber.isEmpty();
    }

    private void validateInputs() {
        if (!validatePhoneNumber() | !validateEmail() | !validatePassword() | !validateConfirmPassword())
        {
            return;
        }
        else
        {
            // Transfer data to the next page
            Intent intent = new Intent(getApplicationContext(), AskInformationActivity.class);
            intent.putExtra("Phone Number", mPhoneNumber);
            intent.putExtra("Email", mEmail);
            intent.putExtra("Password", mPassword);
            startActivity(intent);

        }
    }

    private boolean validateConfirmPassword() {

        if (mConfirmPassword.isEmpty())
        {
            editTxt_passwordConfirmation.setError("Required");
            return false;
        }
        else if (!mConfirmPassword.equals(mPassword))
        {
            editTxt_passwordConfirmation.setError("Your passwords do not match");
            return false;
        }
        else
            return true;
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
            editTxt_password.setError("Your password's not strong enough");
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
            editTxt_email.setError("Invalid Email Address");
            return false;
        }
        else
            return true;
    }


    private void initWidget() {

        // TextView
        txtView_logIn = findViewById(R.id.txtView_logIn);

        // EditText
        editTxt_phoneNumber = findViewById(R.id.editTxt_phoneNumber);
        editTxt_email = findViewById(R.id.editTxt_email);
        editTxt_password = findViewById(R.id.editTxt_password);
        editTxt_passwordConfirmation = findViewById(R.id.editTxt_passwordConfirmation);

        // Button
        btn_getStarted = findViewById(R.id.btn_getStarted);


    }
}