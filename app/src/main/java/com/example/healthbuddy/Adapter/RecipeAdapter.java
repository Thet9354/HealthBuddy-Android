package com.example.healthbuddy.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthbuddy.Model.RecipeModel;
import com.example.healthbuddy.Model.WorkoutModel;
import com.example.healthbuddy.R;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.CardViewHolder>{

    private ArrayList<RecipeModel> recipeModelArrayList;
    private Context mContext;

    public RecipeAdapter(Context mContext, ArrayList<RecipeModel> recipeModelArrayList) {
        this.recipeModelArrayList = recipeModelArrayList;
        this.mContext = mContext;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private RecipeAdapter.OnItemClickListener mItemClickListener;

    public void setOnItemClickListener(RecipeAdapter.OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    @NonNull
    @Override
    public RecipeAdapter.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = LayoutInflater.from(context).
                inflate(R.layout.row_recipe, parent, false);

        return new RecipeAdapter.CardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.CardViewHolder holder, int position) {

        holder.txtView_recipeName.setText(recipeModelArrayList.get(position).getRecipeName());
        holder.txtView_recipeKcal.setText(recipeModelArrayList.get(position).getRecipeCalories());
        holder.txtView_recipeDetail.setText(recipeModelArrayList.get(position).getRecipeDetails());

    }

    @Override
    public int getItemCount() {
        return recipeModelArrayList.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {

        private CardView cv_recipe;
        private LinearLayout ll_recipeDetails;
        private TextView txtView_recipeName, txtView_recipeKcal, txtView_recipeDetail, txtView_viewMore;
        public CardViewHolder(@NonNull View itemView) {
            super(itemView);

            // CardView
            cv_recipe = itemView.findViewById(R.id.cv_recipe);

            // LinearLayout
            ll_recipeDetails = itemView.findViewById(R.id.ll_recipeDetails);

            // TextView
            txtView_recipeName = itemView.findViewById(R.id.txtView_recipeName);
            txtView_recipeKcal = itemView.findViewById(R.id.txtView_recipeKcal);
            txtView_recipeDetail = itemView.findViewById(R.id.txtView_recipeDetail);
            txtView_viewMore = itemView.findViewById(R.id.txtView_viewMore);

        }
    }
}
