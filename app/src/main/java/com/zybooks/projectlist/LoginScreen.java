package com.zybooks.projectlist;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginScreen extends AppCompatActivity {

    /*
    VARIABLES: setup variables that will be using to call from XML to complete functionality.
    The login screen will serve the purpose of verifying the user first before giving access
    to the application.
     */
    DatabaseHelper db;

    private EditText mUserNameFirstTime;
    private EditText mPasswordFirstTime;
    private EditText mConfirmPasswordFirstTime;
    private Button mButtonRegister;

    private EditText userName, userPW;

    /*onCreate will first create the activity.  The contents it will display
    is 'activity_login_screen' which  will initialize variables being used for edit text, buttons, etc.
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        db = new DatabaseHelper(getApplicationContext());

        mUserNameFirstTime = findViewById(R.id.editUserNameFirstTime);
        mPasswordFirstTime = findViewById(R.id.editTextPasswordFirstTime);
        mConfirmPasswordFirstTime = findViewById(R.id.editTextPasswordReEnterFirstTime);
        mButtonRegister = findViewById(R.id.buttonRegister);

        userName = findViewById(R.id.editText_UserName);
        userPW = findViewById(R.id.editText_UserPassword);

        //delete later, bypasses access and adds admin
        if(!db.checkUserExist("loginInfo","admin")){
            addadminToDatabase();
        }
    }
    //DELETE LATER, TESTING PURPOSES ONLY FUNCTION
    public void addadminToDatabase(){
        db.addUser("admin","admin");
    }


/*The login function will be used to get what the user entered,
check the database if the username and password exist.  if they do, give access.
if they don't have access display a notification to the user so they know
* */

    public void logInButton(View view){
        String user_name = userName.getText().toString();
        String user_key = userPW.getText().toString();
        userPW.setText("");

        boolean user_exist = db.userLogin(user_name,user_key);

        if(user_exist) {

            login(user_name);
            firstTimeInvisible();
        }
        else{
            Toast.makeText(this, user_name +" does not exist, please register" , Toast.LENGTH_SHORT).show();
        }
    }

    /*if the user is trying to register, they will click on the button to run this function
    the function will set all set to visible so the user can register.
    * */
    public void firstTime(View view){
        //username, password, confirm password will become visible.
        mUserNameFirstTime.setVisibility(View.VISIBLE);
        mPasswordFirstTime.setVisibility(View.VISIBLE);
        mConfirmPasswordFirstTime.setVisibility(View.VISIBLE);
        mButtonRegister.setVisibility(View.VISIBLE);
    }
    public void firstTimeInvisible(){
        //username, password, confirm password will become invisible.
        mUserNameFirstTime.setVisibility(View.INVISIBLE);
        mPasswordFirstTime.setVisibility(View.INVISIBLE);
        mConfirmPasswordFirstTime.setVisibility(View.INVISIBLE);
        mButtonRegister.setVisibility(View.INVISIBLE);
    }
    /*to register, first we must verify the user has entered accurate credentials.
    This function will check if the username exist and if the password and
    the re-entered password match.

    if they match, it will allow them to register and provide a notification that
    the user has been registered.

    if they do not match, let the user know so that user can retry.
    * */
    public void Register(View view){
        if(!userNameExist("loginApproval",mUserNameFirstTime.getText().toString()) &&
                !userNameExist("loginInfo",mUserNameFirstTime.getText().toString()) &&
                confirmPWmatch(mPasswordFirstTime.getText().toString(),mConfirmPasswordFirstTime.getText().toString())){

            boolean user_registered = db.userPendingApproval(1,mUserNameFirstTime.getText().toString(),mPasswordFirstTime.getText().toString());


            if(user_registered){
                Toast.makeText(this, "Registered", Toast.LENGTH_SHORT).show();
                userName.setText("");
            }
            else{
                Toast.makeText(this, "Not Registered" , Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Error while registering." , Toast.LENGTH_SHORT).show();

        }
        mPasswordFirstTime.setText("");
        mConfirmPasswordFirstTime.setText("");
    }

    /*this function will check if the username already exist in the database
    or if the username entered is blank.
    * */
    public boolean userNameExist(String table_name, String userName){
        boolean checkUser = db.checkUserExist(table_name, userName);
        if(checkUser || userName.equals("")){
            Toast.makeText(this, "Username Exist" , Toast.LENGTH_SHORT).show();
            return true;
        }
        else{
            return false;
        }
    }
    /*this function will check if both the password and the confirm password match.
    if they match, allow the user to proceed.
    if they do not match, let the user know they do not match.
    * */
    public boolean confirmPWmatch(String pw, String cpw){
        boolean pw_match = false;
        if(!pw.equals("")) {
            if (pw.equals(cpw)) {
                pw_match = true;
            } else {
                pw_match = false;
                Toast.makeText(this, "Password Mismatch", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Password Mismatch", Toast.LENGTH_SHORT).show();
        }
        return pw_match;
    }
    /*When the user is authenticated, the function will open the main activity giving
    them access to the application.
    * */
    public void login(String user_name){
        Intent openInventory = new Intent(this, MainActivity.class);
        openInventory.putExtra("user_Name", user_name);
        startActivity(openInventory);
    }
}
