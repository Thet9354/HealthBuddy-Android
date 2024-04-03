package com.example.healthbuddy.Fragment;

import android.content.Context;
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
import android.widget.TextView;

import com.example.healthbuddy.Adapter.WorkoutVidAdapter;
import com.example.healthbuddy.Model.WorkoutModel;
import com.example.healthbuddy.R;
import com.example.healthbuddy.SpaceItemDecoration;

import java.util.ArrayList;

public class Fitness extends Fragment {

    private Context mContext;
    private RecyclerView rv_workoutVid;

    private WorkoutVidAdapter workoutVidAdapter;

    private final ArrayList<WorkoutModel> workoutModelArrayList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_fitness, container, false);

        mContext = getActivity();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
    }

    private void findViews(View v) {

        // RecyclerView
        rv_workoutVid = v.findViewById(R.id.rv_workoutVid);

        initRecView();
    }

    private void initRecView() {
        //for better performance of recyclerview.

        int spaceInPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());
        rv_workoutVid.addItemDecoration(new SpaceItemDecoration(spaceInPixels));

        rv_workoutVid.setHasFixedSize(true);

        workoutVidAdapter = new WorkoutVidAdapter(getContext(), workoutModelArrayList);
        rv_workoutVid.setAdapter(workoutVidAdapter);

        //layout to contain recyclerview
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setSmoothScrollbarEnabled(true);
        // orientation of linearlayoutmanager.
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        llm.setAutoMeasureEnabled(true);

        //set layoutmanager for recyclerview.
        rv_workoutVid.setLayoutManager(llm);

        new loadWorkout().execute();

    }

    WorkoutModel workoutModel;

    class loadWorkout extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {
            try {

                String[] videoTitle = getResources().getStringArray(R.array.workoutVidTitle);
                String[] videoLink = getResources().getStringArray(R.array.vidLink);


                for (int i = 0 ; i < videoTitle.length; i++)
                {
                    workoutModel = new WorkoutModel();

                    workoutModel.setVideoTitle(videoTitle[i]);
                    workoutModel.setVideoLink(videoLink[i]);

                    workoutModelArrayList.add(workoutModel);
                    workoutModel = null;
                }


            } catch (Exception e) {
                e.printStackTrace();

            }

            return null;
        }

        protected void onPostExecute(String file_url) {


            if (workoutModelArrayList != null && workoutModelArrayList.size() > 0) {
                workoutVidAdapter = new WorkoutVidAdapter(mContext, workoutModelArrayList);
                rv_workoutVid.setAdapter(workoutVidAdapter);
                workoutVidAdapter.notifyDataSetChanged();
            }
        }
    }

}