package com.example.bodyboost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Register extends AppCompatActivity {

    Button btnRegister;
    RadioGroup radioGroup;
    EditText firstName, lastName, email, pass, confirmPass;
    TextView emailError, confirmPassError, radioError;
    DBHelper DB;
    //array to store error messages
    List<Pair<Integer, String>> arrayError = new ArrayList<>();
    String sEmailError = "";
    String sConfirmPassError = "";
    String sRadioError = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister = findViewById(R.id.registerBtnRegister);
        radioGroup = findViewById(R.id.radioGroup);


        firstName = findViewById(R.id.registerFNEdit);
        lastName = findViewById(R.id.registerLNEdit);
        email = findViewById(R.id.registerEmailEdit);
        pass = findViewById(R.id.registerPassEdit);
        confirmPass = findViewById(R.id.registerConfirmPassEdit);
        //
        emailError = findViewById(R.id.emailError);
        confirmPassError = findViewById(R.id.confirmPassError);
        radioError = findViewById(R.id.radioTxt);

        DB = new DBHelper(this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstname = firstName.getText().toString();
                String lastname = lastName.getText().toString();
                String email_ = email.getText().toString();
                String password = pass.getText().toString();
                String confirmPassword = confirmPass.getText().toString();
                String userGoal = "";
                Float weight = 0f;
                Float height = 0f;
                int age = 0;

                emailError.setText("");
                confirmPassError.setText("");
                radioError.setTextColor(Color.WHITE);
                //back to normal
                radioError.setBackgroundColor(Color.TRANSPARENT);
                arrayError.clear();

                if(firstname.isEmpty() || lastname.isEmpty() || email_.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
                    Toast.makeText(Register.this, "Please make sure to fill the form", Toast.LENGTH_LONG).show();
                    return;
                }

                if(!isEmailValid(email_) ){
                    sEmailError = "Please make sure to put a valid email";
                    arrayError.add(new Pair<>(R.id.emailError, sEmailError));
//                    emailError.setText("Please make sure to put a valid email");
                }
                if (!password.equals(confirmPassword)) {
                    sConfirmPassError = "Please make sure your passwords match";
                    arrayError.add(new Pair<>(R.id.confirmPassError, sConfirmPassError));
                    //Check if the passwords match
//                    confirmPassError.setText("Please make sure your passwords match");
                }

                if (checkRadioBtn() == null){
                    sRadioError = "Select an option (you can change later on).";
                    arrayError.add(new Pair<>(R.id.confirmPassError, sConfirmPassError));
                    radioError.setTextColor(Color.RED);
                    radioError.setBackgroundColor(Color.parseColor("#83FFFFFF")); // Set background color
                }else{
                    userGoal = checkRadioBtn();
                }

                if(!arrayError.isEmpty()){
                    for (Pair<Integer, String> error : arrayError) {
                        TextView textView = findViewById(error.first);
                        textView.setText(error.second);
                    }
                    return;
                }

                //constructor for new users
                CustomerModel customerModel;
                try{
                    Boolean checkEmail = DB.checkIfEmailExists(email_);
                    if(!checkEmail){
                        Toast.makeText(Register.this, "Account created!!", Toast.LENGTH_LONG).show();
                        customerModel = new CustomerModel(firstname, lastname, email_, password, userGoal, weight, height, age);
                        //creating new user in database
                        DB.addCustomer(customerModel);

                        //clear editTexts
                        firstName.setText("");
                        lastName.setText("");
                        email.setText("");
                        pass.setText("");
                        confirmPass.setText("");
                        Intent intent = new Intent(Register.this, Login.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(Register.this, "This email is already in use.", Toast.LENGTH_LONG).show();
                        emailError.setText("Email already in use.");
                    }

                }catch (Exception e){

                }
            }
        });
    }

    public void goToLogin(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    //checking radio buttons
    public String checkRadioBtn(){
        int radioId = radioGroup.getCheckedRadioButtonId();
        if (radioId != -1) {
            RadioButton selectedRadioButton = findViewById(radioId);
            if (selectedRadioButton != null) {
                return selectedRadioButton.getText().toString();
            }
        }
        return null; // No radio button selected or selected radio button not found
    }
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("EMAILERROR", sEmailError);
        outState.putString("PASSERROR", sConfirmPassError);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null){
            sEmailError = savedInstanceState.getString("EMAILERROR", "");
            sConfirmPassError = savedInstanceState.getString("PASSERROR", "");
            emailError.setText(sEmailError);
            confirmPassError.setText(sConfirmPassError);
        }
    }
}