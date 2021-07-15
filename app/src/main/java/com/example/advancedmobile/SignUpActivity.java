package com.example.advancedmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.advancedmobile.Models.Users;
import com.example.advancedmobile.DataSource;
import com.google.android.material.snackbar.Snackbar;

public class SignUpActivity extends AppCompatActivity {
    private ArrayAdapter<Data> mdApter;
    private DataSource dataSource;
    private DBHelper dbHelper;
    private EditText text;
    EditText userName;
    EditText firstName;
    EditText password;
    private Users user;
    Boolean addedSuccesfully;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //opens up the view
        setContentView(R.layout.sign_up);
        //initalizing data source
        dataSource = new DataSource(this);
        //openning data source
        dataSource.open();
        userName = findViewById(R.id.userName);
        firstName = findViewById(R.id.firstName);
        password = findViewById(R.id.password);
    }


    // Will be called via the onClick attribute of the buttons in main.xml
    public void onClick(View view) {
        Data data;

        user = new Users();
        this.user.name = (firstName.getText().toString());
        this.user.userName = (userName.getText().toString());
        this.user.password = (password.getText().toString());
        //if data is not null add user
        if (this.user.name != null && this.user.userName != null && this.user.password != null) {
            addedSuccesfully = dataSource.createUser(this.user);
            if (addedSuccesfully == true) {
                Toast.makeText(SignUpActivity.this, "Welcome to Akushon", Toast.LENGTH_SHORT).show();

                userName.setText("");
                firstName.setText("");
                password.setText("");
            } else {
                Snackbar.make(view, "Error when updating", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        } else {
            //display toastr to say they cant sign in with null values

            Toast.makeText(SignUpActivity.this, "Please complete the form", Toast.LENGTH_SHORT).show();

        }

    }

    public void navigateToLogin(View view) {
        // go to login page
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
    }

}
