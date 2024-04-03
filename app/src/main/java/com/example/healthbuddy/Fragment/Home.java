package com.example.healthbuddy.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthbuddy.BMI_Tracker;
import com.example.healthbuddy.R;
import com.example.healthbuddy.bmi_Activity;

public class Home extends Fragment {

    private TextView mcurrentheight, mcurrentweight, mcurrentage;
    private ImageView mincrementage,mdecrementage,mincrementweight,mdecrementweight, btn_back;
    private SeekBar mseekbarforheight;
    private Button mcalculatebmi;
    private RelativeLayout mmale, mfemale;

    private int intweight=55;
    private int intage=22;
    private int currentprogress;
    private String mintprogress="170";
    private String typerofuser="0";
    private String weight2="55";
    private String age2="22";

    private Context mContext;

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

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

        mcurrentage=v.findViewById(R.id.currentage);
        mcurrentweight=v.findViewById(R.id.currentweight);
        mcurrentheight=v.findViewById(R.id.currentheight);
        mincrementage=v.findViewById(R.id.incrementage);
        mdecrementage=v.findViewById(R.id.decrementage);
        mincrementweight=v.findViewById(R.id.incremetweight);
        mdecrementweight=v.findViewById(R.id.decrementweight);
        mcalculatebmi=v.findViewById(R.id.calculatebmi);
        mseekbarforheight=v.findViewById(R.id.seekbarforheight);
        mmale=v.findViewById(R.id.male);
        mfemale=v.findViewById(R.id.female);

        pageDirectories();

        seekbar();
    }

    private void seekbar() {
        mseekbarforheight.setMax(300);
        mseekbarforheight.setProgress(170);
        mseekbarforheight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                currentprogress=progress;
                mintprogress=String.valueOf(currentprogress);
                mcurrentheight.setText(mintprogress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void pageDirectories() {

        /** OnClickListener for mmale **/
        mmale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mmale.setBackground(ContextCompat.getDrawable(mContext,R.drawable.malefemalefocus));
                mfemale.setBackground(ContextCompat.getDrawable(mContext,R.drawable.malefemalenotfocus));
                typerofuser="Male";
            }
        });

        /** OnClickListener for mfemale **/
        mfemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mfemale.setBackground(ContextCompat.getDrawable(mContext,R.drawable.malefemalefocus));
                mmale.setBackground(ContextCompat.getDrawable(mContext,R.drawable.malefemalenotfocus));
                typerofuser="Female";
            }
        });

        seekbar();

        mincrementweight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intweight=intweight+1;
                weight2=String.valueOf(intweight);
                mcurrentweight.setText(weight2);
            }
        });

        mincrementage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intage=intage+1;
                age2=String.valueOf(intage);
                mcurrentage.setText(age2);
            }
        });

        mdecrementage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intage=intage-1;
                age2=String.valueOf(intage);
                mcurrentage.setText(age2);
            }
        });

        mdecrementweight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intweight=intweight-1;
                weight2=String.valueOf(intweight);
                mcurrentweight.setText(weight2);
            }
        });

        mcalculatebmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (typerofuser.equals("0"))
                {
                    Toast.makeText(mContext,"Select Your Gender First",Toast.LENGTH_SHORT).show();
                }
                else if (mintprogress.equals("0"))
                {
                    Toast.makeText(mContext,"Select Your Height First",Toast.LENGTH_SHORT).show();
                }
                else if(intage==0 || intage<0)
                {
                    Toast.makeText(mContext,"Age is Incorrect",Toast.LENGTH_SHORT).show();
                }

                else if(intweight==0|| intweight<0)
                {
                    Toast.makeText(mContext,"Weight Is Incorrect",Toast.LENGTH_SHORT).show();
                }
                else {
                    // Go back to the home fragment
                    Intent intent = new Intent(mContext, bmi_Activity.class);
                    intent.putExtra("gender", typerofuser);
                    intent.putExtra("height", mintprogress);
                    intent.putExtra("weight", weight2);
                    intent.putExtra("age", age2);
                    startActivity(intent);

                }
            }
        });

    }

    private void initUI() {

        // Retrieve and display the current BMI from firebase
    }
}