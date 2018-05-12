package com.f17.csci5539.team5.ceitmajorselectionapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {

    EditText email_EditText;
    EditText password_EditText;
    Connection con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_EditText = (EditText) findViewById(R.id.email_edit_text);
        password_EditText = (EditText) findViewById(R.id.password_edit_text);


        //LOGIN BUTTON LISTENER
        Button login_btn = (Button) findViewById(R.id.login_btn);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (email_EditText.getText().toString().equals("") || password_EditText.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();

                } else if (email_EditText.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "Email field is blank", Toast.LENGTH_SHORT).show();
                } else if (password_EditText.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "Password field is blank", Toast.LENGTH_SHORT).show();
                } else {
                    LoginTest logintest = new LoginTest();
                    logintest.execute();
                }

                //startActivity(new Intent(LoginActivity.this, MainMenuActivity.class));
            }
        });


        //FORGOT PASSWORD TEXTVIEW LISTENER
        TextView forgot_password_tv = (TextView) findViewById(R.id.forgot_password_text_view);

        forgot_password_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });


        //REGISTER TEXTVIEW LISTENER
        TextView register_tv = (TextView) findViewById(R.id.register_text_view);

        register_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterAccountActivity.class));

            }
        });
    }

    @Override
    public void onBackPressed() {

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finishAffinity();
        }

        getApplicationContext().getSharedPreferences("CREDENTIALS", 0).edit().clear().apply();
        this.finishAffinity();
    }

    //CONNECT AND LOGIN TEST
    public class LoginTest extends AsyncTask<String, String, String> {
        String statusMessage = "";
        Boolean isSuccess = false;

        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(LoginActivity.this, r, Toast.LENGTH_SHORT).show();
            if (isSuccess) {
                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String email = email_EditText.getText().toString().trim();
            String password = password_EditText.getText().toString().trim();

            try {
                con = SQLConnection.connectionclass();

                if (con == null) {
                    statusMessage = "Check Your Internet Connection.";
                } else {

                    // USES PREPARED STATEMENT TO PREVENT SQL INJECTION
                    PreparedStatement stmt = con.prepareStatement("select * from users u join students s on u.uid = s.StudentID join HIGHSCHOOL h on s.HScode = h.code where email=? and password=?");
                    stmt.setString(1, email);
                    stmt.setString(2, password);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        isSuccess = true;
                        String firstName = rs.getString("fname");
                        String lastname = rs.getString("lname");
                        String hs = rs.getString("hsname");
                        int is_admin = rs.getInt("admin");
                        int uid = rs.getInt("uid");


                        getApplicationContext().getSharedPreferences("CREDENTIALS", 0).edit().clear().apply();
                        //Store user info for settings
                        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
                        SharedPreferences.Editor Ed = sp.edit();
                        Ed.putString("email", email);
                        Ed.putString("Psw", password);
                        Ed.putString("fname", firstName);
                        Ed.putString("lname", lastname);
                        Ed.putInt("isAdmin", is_admin);
                        Ed.putInt("uid", uid);
                        Ed.putString("hs_uni", hs);
                        Ed.apply();

                        //Start Main Menu Activity
                        Intent i = new Intent(getApplicationContext(), MainMenuActivity.class);
                        startActivity(i);
                        con.close();
                    } else {
                        statusMessage = "Incorrect Email or Password";
                        isSuccess = false;
                        con.close();
                    }
                }
            } catch (Exception ex) {
                isSuccess = false;
                statusMessage = ex.getMessage();
            }

            return statusMessage;
        }
    }
}
