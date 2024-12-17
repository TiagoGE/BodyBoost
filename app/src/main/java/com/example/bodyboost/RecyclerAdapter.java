package com.example.bodyboost;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<FoodModel> foodList;
    private Context context;

    public RecyclerAdapter(Context context, ArrayList<FoodModel> foods) {
        this.context = context;
        foodList = foods;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodModel food = foodList.get(position);
        holder.title.setText(food.getFoodTitle().toString());
        holder.calories.setText(food.getFoodCalories().toString()); // Set calories instead of source
        Picasso.get().load(food.getFoodImage()).into(holder.image);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FoodActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("label", food.getFoodTitle());
                bundle.putString("calories", food.getFoodCalories());
                bundle.putString("image", food.getFoodImage());
                bundle.putDouble("protein", food.getFoodProtein());
                bundle.putDouble("carbs", food.getFoodCarbohydrates());
                bundle.putDouble("fat", food.getFoodFat());

                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title;
        TextView calories;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.item_image);
            title = itemView.findViewById(R.id.item_title);
            calories = itemView.findViewById(R.id.item_detail);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }
}

