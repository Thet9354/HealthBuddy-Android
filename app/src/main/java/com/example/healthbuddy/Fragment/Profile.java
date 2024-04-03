package com.example.healthbuddy.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.healthbuddy.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Profile extends Fragment {

    private TextView txtView_name, txtView_age, txtView_gender;
    private Button btn_save, btn_exit;

    private Context mContext;

    private String mName = "Thet Pine", mPhoneNumber = "93542856", mEmail = "thetpine254@gmail.com", mPassword = "Phoon@050204", mAge, mGender;
    private String getName, getPhoneNumber, getEmail, getPassword, getAge, getGender;

    FirebaseDatabase database = FirebaseDatabase.getInstance("Add your firebase url here");
    DatabaseReference databaseReference  = database.getReference().child("users");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        mContext = getActivity();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
    }

    private void findViews(View v) {
        //TextView
        txtView_name = v.findViewById(R.id.txtView_name);
        txtView_age = v.findViewById(R.id.txtView_age);
        txtView_gender = v.findViewById(R.id.txtView_gender);

        //Button
        btn_exit = v.findViewById(R.id.btn_exit);

        getIntentData();

        initUI();

        pageDirectories();
    }

    private void getIntentData() {

        try {
            mName = getArguments().getString("Name");
            mPhoneNumber = getArguments().getString("Phone Number");
            mEmail = getArguments().getString("Email");
            mPassword = getArguments().getString("Password");

            System.out.println(mName);
            System.out.println(mPhoneNumber);
            System.out.println(mEmail);
            System.out.println(mPassword);
        } catch (Exception e) {
            mName = "Thet Pine";
            mPhoneNumber = "93542856";
            mEmail = "thetpine254@gmail.com";
            mPassword = "Phoon@050204";
        }

        if (mPhoneNumber == null)
        {
            mPhoneNumber = "93542856";
        }
        else
            return;
    }

    private void initUI() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(mPhoneNumber))
                {
                    getName = snapshot.child(mPhoneNumber).child("User's Information").child("Full Name").getValue(String.class);
                    getPhoneNumber = snapshot.child(mPhoneNumber).child("User's Information").child("Phone Number").getValue(String.class);
                    getEmail = snapshot.child(mPhoneNumber).child("User's Information").child("Email").getValue(String.class);
                    getAge = snapshot.child(mPhoneNumber).child("User's Information").child("Age").getValue(String.class);
                    getGender = snapshot.child(mPhoneNumber).child("User's Information").child("Gender").getValue(String.class);

                    System.out.println(getName);
                    System.out.println(getAge);
                    System.out.println(getGender);

                    txtView_name.setText(getName);
                    txtView_age.setText(getAge);
                    txtView_gender.setText(getGender);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void pageDirectories() {

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Exit the app entirely
                System.exit(0);
            }
        });
    }


}