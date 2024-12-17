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
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class ProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    //    TextView toolbarTitle;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    EditText fName, lName, email, weight, height, age;
    DBHelper db;
    SharedPreferences sharedPreferences;
    //user data
    String userFirstName, userLastName, userEmail, userWeight, userHeight, userAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

//        toolbar = findViewById(R.id.toolbar);
//        toolbarTitle = toolbar.findViewById(R.id.toolbar_title);

//        toolbarTitle.setText("Profile");

        // Find views within the included toolbar layout directly
        ImageView backButton = findViewById(R.id.toolbar_backButton);
        ImageView settingsButton = findViewById(R.id.toolbar_settingsButton);

        // Set click listeners for toolbar buttons
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle back button click
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
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
                    Intent i = new Intent(ProfileActivity.this, MainActivity.class);
                    startActivity(i);
                } else if (id == R.id.nav_profile) {
                    if (!(ProfileActivity.this instanceof ProfileActivity)) {
                        drawerLayout.closeDrawer(Gravity.RIGHT);
                        Intent i = new Intent(ProfileActivity.this, ProfileActivity.class);
                        startActivity(i);
                    }
                } else if (id == R.id.nav_setGoal) {
                    // Handle set goal click
                    Intent intent = new Intent(ProfileActivity.this, SetGoalActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_logout) {
                    // Handle logout click
                    sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();

                    Intent i = new Intent(ProfileActivity.this, Login.class);
                    startActivity(i);
                    finish();
                    Toast.makeText(ProfileActivity.this, "Logout", Toast.LENGTH_SHORT).show();
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

        //Get user data

        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        userFirstName = sharedPreferences.getString("FirstName", "FirstName");
        userLastName = sharedPreferences.getString("LastName", "LastName");
        userEmail = sharedPreferences.getString("Email", "Email");
        userWeight = sharedPreferences.getString("Weight", "Weight");
        userHeight = sharedPreferences.getString("Height", "Height");
        userAge = sharedPreferences.getString("Age", "Age");

        //set reference
        fName = findViewById(R.id.editFName);
        lName = findViewById(R.id.editLName);
        email = findViewById(R.id.editEmail);
        weight = findViewById(R.id.editWeight);
        height = findViewById(R.id.editHeight);
        age = findViewById(R.id.editAge);

        //set Edit text
        fName.setText(userFirstName);
        lName.setText(userLastName);
        email.setText(userEmail);
        weight.setText(userWeight);
        height.setText(userHeight);
        age.setText(userAge);

        //disable edit
        fName.setEnabled(false);
        lName.setEnabled(false);
        email.setEnabled(false);
        weight.setEnabled(false);
        height.setEnabled(false);
        age.setEnabled(false);

        ImageView editImg1 = findViewById(R.id.imgEdit1);
        ImageView editImg2 = findViewById(R.id.imgEdit2);
        ImageView editImg4 = findViewById(R.id.imgEdit4);
        ImageView editImg5 = findViewById(R.id.imgEdit5);
        ImageView editImg6 = findViewById(R.id.imgEdit6);

        //Image click to enable edit
        editImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Enable EditText for editing
                fName.setEnabled(!fName.isEnabled());

                // Set focus to the EditText
                fName.requestFocus();

                // Show soft keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(fName, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        editImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Enable EditText for editing
                lName.setEnabled(!lName.isEnabled());

                // Set focus to the EditText
                lName.requestFocus();

                // Show soft keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(lName, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        editImg4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Enable EditText for editing
                weight.setEnabled(!weight.isEnabled());

                // Set focus to the EditText
                weight.requestFocus();

                // Show soft keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(weight, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        editImg5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Enable EditText for editing
                height.setEnabled(!height.isEnabled());

                // Set focus to the EditText
                height.requestFocus();

                // Show soft keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(height, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        editImg6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Enable EditText for editing
                age.setEnabled(!age.isEnabled());

                // Set focus to the EditText
                age.requestFocus();

                // Show soft keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(age, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }

    public void applyUpdate(View view) {

        //Getting edited data
        String firstNameEdit = fName.getText().toString();
        String lastNameEdit = lName.getText().toString();
        String weightText = weight.getText().toString();
        String heightText = height.getText().toString();
        String ageText = age.getText().toString();

        if (checkEmpty()) {
            Toast.makeText(ProfileActivity.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse numerical values
        float weightEdit = Float.parseFloat(weightText);
        float heightEdit = Float.parseFloat(heightText);
        int ageEdit = Integer.parseInt(ageText);

        //if nothing was edited, return;
        if (firstNameEdit.equals(userFirstName) && lastNameEdit.equals(userLastName) &&
                weightEdit == Float.parseFloat(userWeight) && heightEdit == Float.parseFloat(userHeight) &&
                ageEdit == Integer.parseInt(userAge)) {
            return;
        }
        db = new DBHelper(this);
        Boolean apply = db.userUpdate(userEmail, firstNameEdit, lastNameEdit, weightEdit, heightEdit, ageEdit);
        if (apply) {

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("FirstName", firstNameEdit);
            editor.putString("LastName", lastNameEdit);
            editor.putString("Weight", String.valueOf(weightEdit));
            editor.putString("Height", String.valueOf(heightEdit));
            editor.putString("Age", String.valueOf(ageEdit));
            editor.apply();

            fName.setEnabled(false);
            lName.setEnabled(false);
            email.setEnabled(false);
            weight.setEnabled(false);
            height.setEnabled(false);
            age.setEnabled(false);
            Toast.makeText(ProfileActivity.this, "Profile updated!", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(ProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
        }

    }

    private Boolean checkEmpty() {
        if (fName.getText().toString().isEmpty() || lName.getText().toString().isEmpty() ||
                weight.getText().toString().isEmpty() || height.getText().toString().isEmpty() ||
                age.getText().toString().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

}