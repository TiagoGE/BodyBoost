package com.example.bodyboost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class SetGoalActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    SharedPreferences sharedPreferences;
    String userFirstName, userWeight, userHeight, userGoal, userEmail, userGoalEdited;
    String selectedItem;
    Spinner goalSpinner;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_goal);

        // Find views within the included toolbar layout directly
        ImageView backButton = findViewById(R.id.toolbar_backButton);
        ImageView settingsButton = findViewById(R.id.toolbar_settingsButton);

        // Set click listeners for toolbar buttons
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle back button click
                Intent intent = new Intent(SetGoalActivity.this, MainActivity.class);
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

        //Handle navigation items click
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    Intent i = new Intent(SetGoalActivity.this, MainActivity.class);
                    startActivity(i);
                } else if (id == R.id.nav_profile) {
                    // Handle profile click
                    Intent i = new Intent(SetGoalActivity.this, ProfileActivity.class);
                    startActivity(i);
                } else if (id == R.id.nav_setGoal) {
                    if (!(SetGoalActivity.this instanceof SetGoalActivity)) {
                        drawerLayout.closeDrawer(Gravity.RIGHT);
                        Intent i = new Intent(SetGoalActivity.this, ProfileActivity.class);
                        startActivity(i);
                    }
                } else if (id == R.id.nav_logout) {
                    // Handle logout click
                    sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();

                    Intent i = new Intent(SetGoalActivity.this, Login.class);
                    startActivity(i);
                    finish();
                    Toast.makeText(SetGoalActivity.this, "Logout", Toast.LENGTH_SHORT).show();
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

        //get user data
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        userFirstName = sharedPreferences.getString("FirstName", "FirstName");
        userWeight = sharedPreferences.getString("Weight", "Weight");
        userHeight = sharedPreferences.getString("Height", "Height");
        userGoal = sharedPreferences.getString("UserGoal", "UserGoal");
        userEmail = sharedPreferences.getString("Email", "Email");
        userGoalEdited = sharedPreferences.getString("UserGoalEdited", "UserGoalEdited");
        if(userGoalEdited.equals("")){
            userGoalEdited = userGoal;
        }

        //Setting TextView
        TextView userTitle = findViewById(R.id.userMetricsTitle);
        TextView weight = findViewById(R.id.weight);
        TextView height = findViewById(R.id.height);
        ImageView imgEditGoal = findViewById(R.id.imgEditGoal);
        goalSpinner = findViewById(R.id.goal_spinner);

        userTitle.setText(userFirstName + "'s current measurements:");
        weight.setText(userWeight);
        height.setText(userHeight);

        // Create a Typeface for bold text
        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
        // Disable the spinner by default
        goalSpinner.setEnabled(false);

        ArrayAdapter<CharSequence> adapter = new CustomSpinnerAdapter(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.goal_options), boldTypeface);
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        goalSpinner.setAdapter(adapter);

        // Set the selected item in the spinner based on userGoal
        if (userGoalEdited != null) {
            int position = adapter.getPosition(userGoalEdited);
            goalSpinner.setSelection(position);
        }

        //         Set click listener for the ImageView
        imgEditGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Enable the spinner when the ImageView is clicked
                goalSpinner.setEnabled(!(goalSpinner.isEnabled()));
            }
        });

        goalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected item from the spinner
                selectedItem = parentView.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
    }
    public void updateUserGoal(View view) {
//        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
//        userGoal = sharedPreferences.getString("UserGoal", "UserGoal");
        //if nothing was edited, return;
        if (selectedItem.equals(userGoalEdited)) {
            return;
        }

        db = new DBHelper(SetGoalActivity.this);
        Boolean apply = db.userGoalUpdate(userEmail, selectedItem);
        if (apply) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("UserGoalEdited", selectedItem);
            editor.apply();
            Toast.makeText(SetGoalActivity.this, "New Goal updated!", Toast.LENGTH_SHORT).show();

            goalSpinner.setEnabled(false);

        } else {
            Toast.makeText(SetGoalActivity.this, "Failed to update user goal", Toast.LENGTH_SHORT).show();
        }
    }
}