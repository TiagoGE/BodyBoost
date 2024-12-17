package com.example.bodyboost;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    TextView loginError;
    EditText username, password;
    Button btnLogin, btnRegister;
    DBHelper db;
    String sLoginError = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DBHelper(this);
        db.addAdminAccount();

        username = findViewById(R.id.usernameEdit);
        password = findViewById(R.id.passwordEdit);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        loginError = findViewById(R.id.loginError);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();

//               Verify account
                if(!db.verifyAccount(user, pass)){
                    sLoginError = "Incorrect Username or Password";
                    Toast.makeText(Login.this, "Incorrect Username or Password.", Toast.LENGTH_SHORT).show();
                    loginError.setText(sLoginError);
//                    loginError.setVisibility(View.VISIBLE);
                }
//                Else, Login, and pass user data to shared preferences
                else{
                    String[] data = db.getUserData(user);
                    SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                    String[] columns = {"UserID", "FirstName", "LastName", "Email", "Password", "UserGoal", "Weight", "Height", "Age"};

                    for(int i = 0; i < data.length; i++){
                        editor.putString(columns[i], data[i]);
                        Log.d(columns[i], data[i]);
                    }
                    editor.putString("UserGoalEdited", "");
                    editor.apply();

                    sLoginError = "";
                    username.setText("");
                    password.setText("");
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                }

            }
        });
    }

    public void goToRegister(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("LOGINERROR", sLoginError);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null){
            sLoginError = savedInstanceState.getString("LOGINERROR", "");
            loginError.setText(sLoginError);
        }
    }
}