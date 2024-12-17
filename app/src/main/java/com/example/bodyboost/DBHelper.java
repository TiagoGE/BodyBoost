package com.example.bodyboost;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    //Creating DB
    public static final String DBNAME = "BodyBoost.db";
    public static final int dbVersion = 13;

    //Creating Users table
    public static final String CUSTOMER = "CUSTOMER";
    public static final String COLUMN_USER_ID = "UserID";
    public static final String COLUMN_FIRST_NAME = "FirstName";
    public static final String COLUMN_LAST_NAME = "LastName";
    public static final String COLUMN_EMAIL = "Email";
    public static final String COLUMN_PASSWORD = "Password";
    public static final String COLUMN_WEIGHT = "Weight";
    public static final String COLUMN_HEIGHT = "Height";
    public static final String COLUMN_USER_GOAL = "UserGoal";
    public static final String COLUMN_AGE = "Age";

    public DBHelper(@Nullable Context context) {
        super(context, DBNAME, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableCustomer = "CREATE TABLE " + CUSTOMER +
                "(" + COLUMN_USER_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_FIRST_NAME + " TEXT, "
                + COLUMN_LAST_NAME + " TEXT, "
                + COLUMN_EMAIL + " TEXT, "
                + COLUMN_PASSWORD + " TEXT, "
                + COLUMN_USER_GOAL + " TEXT, "
                + COLUMN_WEIGHT + " FLOAT, "
                + COLUMN_HEIGHT + " FLOAT, "
                + COLUMN_AGE + " INTEGER)";
        //THE ORDER OF THE COLUMNS MATTER!!!!!!!!!!!!!!
        db.execSQL(createTableCustomer);

    }

    //called when database version changes.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + CUSTOMER);
        onCreate(db);
    }

    public Boolean addCustomer (CustomerModel customerModel){
        SQLiteDatabase db = this.getWritableDatabase();

        //linking data from getters to database fields
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_FIRST_NAME, customerModel.getFirstName());
        cv.put(COLUMN_LAST_NAME, customerModel.getLastName());
        cv.put(COLUMN_EMAIL, customerModel.getEmail());
        cv.put(COLUMN_PASSWORD, customerModel.getPassword());
        cv.put(COLUMN_USER_GOAL, customerModel.getUserGoal());
        cv.put(COLUMN_WEIGHT, customerModel.getWeight());
        cv.put(COLUMN_HEIGHT, customerModel.getHeight());
        cv.put(COLUMN_AGE, customerModel.getAge());
        //inserting data
        long insert = db.insert(CUSTOMER, null, cv);
        return insert != -1;
    }

    //query to get user data
    public String[] getUserData(String email){
        String[] userData = new String[9];
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM CUSTOMER WHERE Email= ?", new String[]{email});
        if(cursor != null){
            cursor.moveToFirst();
            for(int i = 0; i < userData.length; i++){
                userData[i] = cursor.getString(i);
            }
        }
        cursor.close();
        return userData;
    }
    public Cursor getUserGoal(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT UserGoal FROM CUSTOMER WHERE Email= ? ", new String[]{email});
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    //check if email already exists
    public boolean checkIfEmailExists(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM CUSTOMER WHERE Email= ?", new String[]{email});
        //if > 0 email exists returning true;
        int count = cursor.getCount(); // Get the count first
        cursor.close();
        return count > 0;
    }

    //Check if username and password matches;
    public boolean verifyAccount(String username, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM CUSTOMER WHERE Email= ? AND Password = ?", new String[]{username, password});
        int count = cursor.getCount(); // Get the count first
        cursor.close();
        return count > 0;
    }

    //query to update user data
    public boolean userUpdate(String email, String fName, String lName, Float weight, Float height, int age) {
        //writing data in the database
        SQLiteDatabase db = this.getWritableDatabase();
        //linking data from getters to database fields
        ContentValues cv = new ContentValues();
        cv.put("FirstName", fName);
        cv.put("LastName", lName);
        cv.put("Weight", weight);
        cv.put("Height", height);
        cv.put("Age", age);

        int rowsAffected = db.update("CUSTOMER", cv, "Email = ?", new String[]{email});
        return rowsAffected > 0;
    }

    public boolean userGoalUpdate(String email, String userGoal) {
        //writing data in the database
        SQLiteDatabase db = this.getWritableDatabase();
        //linking data from getters to database fields
        ContentValues cv = new ContentValues();
        cv.put("UserGoal", userGoal);

        int rowsAffected = db.update("CUSTOMER", cv, "Email = ?", new String[]{email});
        return rowsAffected > 0;
    }

    public void addAdminAccount() {
        CustomerModel customerModel = new CustomerModel("Admin", "admin lastName", "admin@", "123", "Gain mass", 70f, 178f, 21);
        if(!checkIfEmailExists(customerModel.getEmail())){
            addCustomer(customerModel);
        }
    }

}
