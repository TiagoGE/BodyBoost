BodyBoost is a mobile app developed in Java that helps users track the nutritional content of their meals. The app provides detailed information on calories, protein, carbohydrates, and fat for each selected meal. Users can set goals to either lose weight or gain muscle mass, and the meal selection changes based on their goal.

HOW TO RUN
To run the app, you'll need the following tools:
- Java Development Kit (JDK)
- Android Studio
- Android SDK

STEPS TO RUN:
1. Download or Clone the Repository
2. Import the Project in Android Studio
  Open Android Studio, select the 3 vertical dots, select import project, and select the project folder.
3. Set Up an Emulator or Physical Device
  Open Android Device Manager and create a new device (recommendations: Pixel 5 or Pixel 3).
4. Run the App
Once the emulator or physical device is set up, click the Run button in Android Studio to launch the app.

-------------------------------------------------------------------------------------------------------------------------------------

HOW TO USE
1. User Registration
- If you're a new user, you can register by providing your information.
- After registration, you'll be redirected to the login screen.

2. Main Screen
- The main screen lists various meals with their calorie content.
- Click on a meal to see detailed nutritional information, including protein, carbohydrates, and fat.
- You can select meals to consume, although the tracking feature is not yet implemented.

Additional Tabs:
- Exercises: A placeholder for exercise tracking (not yet implemented).
- Progress: A placeholder for tracking progress (not yet implemented).
- Both tabs have a premium icon, indicating future premium features.

Navigation Menu
- Located in the top-right corner of the app, the menu provides access to:
- Home: Takes you to the main screen with all the meals.
- Profile: Allows you to edit and save your personal information.
- Set Goal: Enables you to set your weight, height, and goal (lose weight or gain muscle).
- Logout: Logs you out and returns you to the login screen.

-------------------------------------------------------------------------------------------------------------------------------------

How Data is Stored
The app uses SharedPreferences to store user information LOCALLY on the device. This includes basic data such as the user's username, password, weight, height, and selected goal (weight loss or muscle gain). SharedPreferences is a lightweight mechanism for storing key-value pairs, making it ideal for handling simple user data.

While SharedPreferences is not a full database, it provides an efficient way to store and retrieve user information on the device. For more complex data storage needs, such as handling relationships between multiple entities, the app may evolve to include solutions like SQLite or Room in the future.
