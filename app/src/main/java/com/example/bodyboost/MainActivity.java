package com.example.bodyboost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    SharedPreferences sharedPreferences;
    DBHelper DB;

    String firstName, userEmail, userGoal = "", userGoalEdited = "";
    TextView customTXT, premiumTXT;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    private RequestQueue requestQueue;
    private ArrayList<FoodModel> foodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Setting reference to exercise txt
        premiumTXT = findViewById(R.id.premiumTxt);

        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        foodList = new ArrayList<>();

        // Find views within the included toolbar layout directly
        ImageView settingsButton = findViewById(R.id.toolbar_settingsButton);

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
                    if (!(MainActivity.this instanceof MainActivity)) {
                        drawerLayout.closeDrawer(Gravity.RIGHT);
                        Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                        startActivity(i);
                    }
                } else if (id == R.id.nav_profile) {
                    // Handle profile click
                    Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(i);
                } else if (id == R.id.nav_setGoal) {
                    // Handle set goal click
                    Intent intent = new Intent(MainActivity.this, SetGoalActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_logout) {
                    // Handle logout click
                    sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();
                    cleanRecyclerView();

                    Intent i = new Intent(MainActivity.this, Login.class);
                    startActivity(i);
                    finish();
                    Toast.makeText(MainActivity.this, "Bye, " + firstName + "!", Toast.LENGTH_SHORT).show();
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
        DB = new DBHelper(this);

        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        firstName = sharedPreferences.getString("FirstName", "Unknown");
        userEmail = sharedPreferences.getString("Email", "Unknown");
        userGoal = sharedPreferences.getString("UserGoal", "Unknown");
        userGoalEdited = sharedPreferences.getString("UserGoalEdited", "Unknown");

        //In case user didnt change the userGoal;
        if(userGoalEdited.equals("")){
            userGoalEdited = userGoal;
        }else{
            Cursor newUserGoal = DB.getUserGoal(userEmail);
            userGoal = newUserGoal.toString();
        }

        customTXT = findViewById(R.id.textView_btns);
        customTXT.setText("Explore our delicious dishes and manage your calorie intake!");

        //if available
        loadSavedData();
        //Check if user edited the UserGoal
        if(!userGoal.equals(userGoalEdited)){
            fetchFood();
            userGoal = userGoalEdited;
        }else{
            if(foodList.isEmpty()){
                Log.d("FOODLIST IS EMPTY", "TRUE");
                fetchFood();
            }
            else{
                Log.d("FOODLIST IS EMPTY", "FALSE");
                setAdapter();
            }
        }
    }

    //Edamam API
    private void fetchFood() {
        foodList.clear();
        String apiKey = "b3ae30f943fdf5d653123b9c33b7c212";
        String appID = "6a6dddb6";
        String q = "";
//        int maxResults = 15;
        String caloriesMax = "nutrition-type=cooking&health=&calories=200-400&";
        String diet = "&diet=balanced";
        String mealType = "&mealType=Lunch";

        //reassign variables;
        if (userGoalEdited.equals("Gain mass")) {
//            caloriesMax = "";
            diet = "&diet=high-protein";
            q = "&q=chicken";
        }

        String url = "https://api.edamam.com/api/recipes/v2?type=public" + q + "&app_id=" + appID + "&app_key=" + apiKey + diet + mealType; //;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("hits");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            JSONObject recipeObject = jsonObject.getJSONObject("recipe");
                            String label = recipeObject.getString("label");
                            DecimalFormat decimalFormat = new DecimalFormat("0.00");
                            double caloriesDouble = recipeObject.optDouble("calories", 0.0);
                            String calories;
                            if (Double.isNaN(caloriesDouble)) {
                                // If the value is not a valid number, set calories to some default value
                                calories = "N/A";
                            } else {
                                // Format the valid number
                                calories = decimalFormat.format(caloriesDouble);
                            }
                            String imgUrl = recipeObject.getString("image");
//                            JSONArray ingredients = recipeObject.getJSONArray("ingredients");
                            JSONObject totalNutrients = recipeObject.getJSONObject("totalNutrients");

                            double protein = totalNutrients.getJSONObject("PROCNT").getDouble("quantity");
                            double fat = totalNutrients.getJSONObject("FAT").getDouble("quantity");
                            double carbs = totalNutrients.getJSONObject("CHOCDF").getDouble("quantity");
                            FoodModel food = new FoodModel(label, calories, imgUrl, protein, carbs, fat);
                            foodList.add(food);
                        }

                        Log.d("FoodList", "Size: " + foodList.size());

                        saveDataToLocal();
                        setAdapter();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }


    public void showFoods(View view) {
        customTXT.setText("Explore our delicious dishes and manage your calorie intake!");
        //set Adapter for foods
        adapter = new RecyclerAdapter(MainActivity.this, foodList);
        recyclerView.setAdapter(adapter);
        premiumTXT.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    public void showProgress(View view) {
        customTXT.setText("Track your weekly progress and stay motivated!");
        premiumTXT.setText("PROGRESS not available. Buy the premium version of BodyBoost app.");
        premiumTXT.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    public void showExercises(View view) {
        customTXT.setText("Start your workout and monitor your calorie burn with our exercises! \uD83D\uDCAA");
        premiumTXT.setText("EXERCISES not available. Buy the premium version of BodyBoost app.");
        premiumTXT.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.END)){
            drawerLayout.closeDrawer(GravityCompat.END);
        }
        else{
            super.onBackPressed();
        }
    }

    // Set the adapter for RecyclerView
    private void setAdapter() {
        adapter = new RecyclerAdapter(MainActivity.this, foodList);
        recyclerView.setAdapter(adapter);
    }

    private void saveDataToLocal() {
        // Implement saving data to SharedPreferences or a local database
        // Example using SharedPreferences:
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        JSONArray jsonArray = new JSONArray();
        for (FoodModel food : foodList) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("label", food.getFoodTitle());
                jsonObject.put("calories", food.getFoodCalories());
                jsonObject.put("imgUrl", food.getFoodImage());
                jsonObject.put("protein", food.getFoodProtein());
                jsonObject.put("carbohydrates", food.getFoodCarbohydrates());
                jsonObject.put("fat", food.getFoodFat());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        editor.putString("foodList", jsonArray.toString());
        editor.apply();
    }

    // Load saved data from local storage
    private void loadSavedData() {
        // Implement loading data from SharedPreferences or a local database
        // Example using SharedPreferences:
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String savedData = sharedPreferences.getString("foodList", null);
        if (savedData != null) {
            try {
                JSONArray jsonArray = new JSONArray(savedData);
                foodList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    // Parse JSON objects to FoodModel and add them to foodList
                    FoodModel food = new FoodModel(jsonObject.getString("label"), jsonObject.getString("calories"),
                            jsonObject.getString("imgUrl"), jsonObject.getDouble("protein"),
                            jsonObject.getDouble("carbohydrate"), jsonObject.getDouble("fat"));
                    foodList.add(food);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void cleanRecyclerView(){
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}