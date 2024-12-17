package com.example.bodyboost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class FoodActivity extends AppCompatActivity {

    Toolbar toolbar;
    //    TextView toolbarTitle;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    SharedPreferences sharedPreferences;
    String firstName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        ImageView backButton = findViewById(R.id.toolbar_backButton);
        ImageView settingsButton = findViewById(R.id.toolbar_settingsButton);

        // Set click listeners for toolbar buttons
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle back button click
                Intent intent = new Intent(FoodActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle settings button click
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });

        // Initialize drawerLayout and navigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        firstName = sharedPreferences.getString("FirstName", "Unknown");

        //Handle navigation items click
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    Intent i = new Intent(FoodActivity.this, MainActivity.class);
                    startActivity(i);
                } else if (id == R.id.nav_profile) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    Intent i = new Intent(FoodActivity.this, ProfileActivity.class);
                    startActivity(i);
                } else if (id == R.id.nav_setGoal) {
                    // Handle set goal click
                    Intent intent = new Intent(FoodActivity.this, SetGoalActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_logout) {
                    // Handle logout click
                    sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();

                    Intent i = new Intent(FoodActivity.this, Login.class);
                    startActivity(i);
                    finish();
                    Toast.makeText(FoodActivity.this, "Bye, " + firstName + "!", Toast.LENGTH_SHORT).show();
                }

                // Close the drawer
                drawerLayout.closeDrawer(Gravity.RIGHT);
                item.setChecked(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Unselect the selected item
                        item.setChecked(false);
                    }
                }, 100); // Delayed for 100 milliseconds

                return true;
            }
        });

        // Initialize ActionBarDrawerToggle
        navigationView.bringToFront();
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //SET REFERENCE
        ImageView image = findViewById(R.id.food_image);
        TextView title = findViewById(R.id.food_title);
        TextView calories = findViewById(R.id.food_calories);
        TextView protein = findViewById(R.id.food_proteins);
        TextView carbs = findViewById(R.id.food_carbs);
        TextView fat = findViewById(R.id.food_fats);

        Bundle bundle = getIntent().getExtras();

        //GET INTENT VALUES
        String foodImage = bundle.getString("image");
        String foodTitle = bundle.getString("label");
        String fooCalorie = bundle.getString("calories");
        Double foodProteinDouble = bundle.getDouble("protein");
        Double foodCarbsDouble = bundle.getDouble("carbs");
        Double foodFatDouble = bundle.getDouble("fat");

        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String foodProtein="", foodCarbs="", foodFat="";

        foodProtein = decimalFormat.format(foodProteinDouble);
        foodCarbs = decimalFormat.format(foodCarbsDouble);
        foodFat = decimalFormat.format(foodFatDouble);

        //SET VALUES
        Picasso.get().load(foodImage).into(image);
        calories.setText(fooCalorie);
        title.setText(foodTitle);
        protein.setText(foodProtein);
        carbs.setText(foodCarbs);
        fat.setText(foodFat);
    }

    public void consumeFood(View view) {
        Toast.makeText(this, "Food and Calories added to calories manager.", Toast.LENGTH_SHORT).show();
    }
}
