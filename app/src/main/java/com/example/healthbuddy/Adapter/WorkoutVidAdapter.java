package com.example.healthbuddy.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthbuddy.Model.WorkoutModel;
import com.example.healthbuddy.R;

import java.util.ArrayList;

public class WorkoutVidAdapter extends RecyclerView.Adapter<WorkoutVidAdapter.CardViewHolder>{

    private ArrayList<WorkoutModel> workoutModelArrayList;

    private Context mContext;

    private String ytLink;

    public WorkoutVidAdapter(Context mContext, ArrayList<WorkoutModel> workoutModelArrayList) {
        this.workoutModelArrayList = workoutModelArrayList;
        this.mContext = mContext;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private WorkoutVidAdapter.OnItemClickListener mItemClickListener;

    public void setOnItemClickListener(WorkoutVidAdapter.OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    @NonNull
    @Override
    public WorkoutVidAdapter.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = LayoutInflater.from(context).
                inflate(R.layout.row_workoutvid, parent, false);

        return new WorkoutVidAdapter.CardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutVidAdapter.CardViewHolder holder, int position) {

        holder.txtView_title.setText(workoutModelArrayList.get(position).getVideoTitle());

        holder.cv_workout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent to the actual youtube link when clicked on
                int pos = holder.getAdapterPosition();

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(workoutModelArrayList.get(pos).getVideoLink()));

                if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                    mContext.startActivity(intent);
                } else {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(workoutModelArrayList.get(pos).getVideoLink()));
                    mContext.startActivity(intent);
                }
            }
        });

        holder.imgView_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, workoutModelArrayList.get(pos).getVideoLink());

                mContext.startActivity(Intent.createChooser(intent, "Share using"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return workoutModelArrayList.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {

        private CardView cv_workout;
        private ImageView imgView_workoutVid, imgView_share;
        private TextView txtView_title;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);

            // CardView
            cv_workout = itemView.findViewById(R.id.cv_workout);

            // ImageView
            imgView_workoutVid = itemView.findViewById(R.id.imgView_workoutVid);
            imgView_share = itemView.findViewById(R.id.imgView_share);

            // TextView
            txtView_title = itemView.findViewById(R.id.txtView_title);
        }
    }
}
