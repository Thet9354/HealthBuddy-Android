package com.example.healthbuddy.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.healthbuddy.Adapter.RecipeAdapter;
import com.example.healthbuddy.Adapter.WorkoutVidAdapter;
import com.example.healthbuddy.AddRecipe_Activity;
import com.example.healthbuddy.Model.RecipeModel;
import com.example.healthbuddy.Model.WorkoutModel;
import com.example.healthbuddy.R;
import com.example.healthbuddy.SpaceItemDecoration;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Nutrition extends Fragment {

    private Context mContext;

    private RecyclerView rv_yourRecipe;

    private Button btn_addRecipe;

    private RecipeAdapter recipeAdapter;

    private String mName = "Thet Pine", mPhoneNumber = "93542856", mEmail = "thetpine254@gmail.com", mPassword = "Phoon@050204";

    private final ArrayList<RecipeModel> recipeModelArrayList = new ArrayList<>();

    int[] recipePic = {R.drawable.spicy_chicken, R.drawable.peanut_butter_stuffed};

    FirebaseDatabase database = FirebaseDatabase.getInstance("Add your firebase url here");
    DatabaseReference databaseReference  = database.getReference().child("users");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_nutrition, container, false);

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

        // RecyclerView
        rv_yourRecipe = v.findViewById(R.id.rv_yourRecipe);

        // Button
        btn_addRecipe = v.findViewById(R.id.btn_addRecipe);

        getIntentData();

        initRecView();

        pageDirectories();
    }

    private void getIntentData() {

        try {
            mName = getArguments().getString("Name");
            mPhoneNumber = getArguments().getString("Phone Number");
            mEmail = getArguments().getString("Email");
            mPassword = getArguments().getString("Password");
        } catch (Exception e) {

        }



        if (mPhoneNumber == null)
        {
            mPhoneNumber = "93542856";
        }
        else
            return;
    }

    private void pageDirectories() {

        btn_addRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, AddRecipe_Activity.class);
                intent.putExtra("Name", mName);
                intent.putExtra("Phone Number", mPhoneNumber);
                intent.putExtra("Email", mEmail);
                intent.putExtra("Password", mPassword);

                System.out.println("Intent data from nutrition to add recipe");
                System.out.println(mName);
                System.out.println(mPhoneNumber);
                System.out.println(mEmail);
                System.out.println(mPassword);

                startActivity(intent);
            }
        });
    }

    private void initRecView() {
        //for better performance of recyclerview.

        int spaceInPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());
        rv_yourRecipe.addItemDecoration(new SpaceItemDecoration(spaceInPixels));

        rv_yourRecipe.setHasFixedSize(true);

        recipeAdapter = new RecipeAdapter(getContext(), recipeModelArrayList);

        //layout to contain recyclerview
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setSmoothScrollbarEnabled(true);
        // orientation of linearlayoutmanager.
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        llm.setAutoMeasureEnabled(true);

        //set layoutmanager for recyclerview.
        rv_yourRecipe.setLayoutManager(llm);

        rv_yourRecipe.setAdapter(recipeAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.child(mPhoneNumber).child("User's Recipe").getChildren())
                {
                    recipeModel = dataSnapshot.getValue(RecipeModel.class);
                    recipeModelArrayList.add(recipeModel);
                }
                recipeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    RecipeModel recipeModel;
    class loadRecipe extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {
            try {

                String[] recipeName = getResources().getStringArray(R.array.recipeName);
                String[] recipeCalories = getResources().getStringArray(R.array.recipeCalories);
                String[] recipeDetails = getResources().getStringArray(R.array.recipeDetails);


                for (int i = 0 ; i < recipeName.length; i++)
                {
                    recipeModel = new RecipeModel();

                    recipeModel.setRecipeName(recipeName[i]);
                    recipeModel.setRecipeCalories(recipeCalories[i]);
                    recipeModel.setRecipeDetails(recipeDetails[i]);

                    recipeModelArrayList.add(recipeModel);
                    recipeModel = null;
                }

            } catch (Exception e) {
                e.printStackTrace();

            }

            return null;
        }

        protected void onPostExecute(String file_url) {


            if (recipeModelArrayList != null && recipeModelArrayList.size() > 0) {
                recipeAdapter = new RecipeAdapter(mContext, recipeModelArrayList);
                rv_yourRecipe.setAdapter(recipeAdapter);
                recipeAdapter.notifyDataSetChanged();
            }
        }
    }

}