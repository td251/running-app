package com.example.advancedmobile;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.advancedmobile.Models.Login;
import com.example.advancedmobile.DataSource;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class LoginActivity extends AppCompatActivity {
    EditText userName;
    EditText password;
    private Login login;
    private DataSource dataSource;
    Boolean loginResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //chooses the layout to opeen
         setContentView(R.layout.login);
        dataSource = new DataSource(this);
        dataSource.open();
        userName = findViewById(R.id.userName);
        password = findViewById(R.id.password);
    }

    public void login(View view) {
        // initalizing
        login = new Login();
        //retriving values
        this.login.userName = (userName.getText().toString());
        this.login.password = (password.getText().toString());
        if (this.login.userName != null && this.login.password != null) {
            loginResult = dataSource.checkUserExists(login);
            if (loginResult == true) {
                //if login sucessful
                Toast.makeText(LoginActivity.this, "Welcome to Akushon", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                //passing the user name to main activity
                intent.putExtra("CURRENT_USER", login.userName);
                startActivity(intent);
            } else {
                //otherwise wrong credentials
                Toast.makeText(LoginActivity.this, "Wrong credentials", Toast.LENGTH_SHORT).show();

            }
        }else{
            //user has not inputted username or password
            Toast.makeText(LoginActivity.this, "Please complete form", Toast.LENGTH_SHORT).show();

        }
        userName.setText("");
        password.setText("");
        }

    public void signUp(View view) {
        //going to the sign up class
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }
}