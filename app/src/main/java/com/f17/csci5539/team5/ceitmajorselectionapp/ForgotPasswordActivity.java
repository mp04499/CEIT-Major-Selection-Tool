package com.f17.csci5539.team5.ceitmajorselectionapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Timer;
import java.util.TimerTask;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText email_et;
    String pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //INITIALIZE VIEWS
        TextView remember_password_tv = (TextView) findViewById(R.id.remember_password_text_view);
        TextView register_tv = (TextView) findViewById(R.id.fp_register_text_view);
        email_et = (EditText) findViewById(R.id.forgot_email_edit_text);
        Button send_pw_btn = (Button) findViewById(R.id.send_email_pw_btn);

        SharedPreferences sp1 = this.getSharedPreferences("Login", MODE_PRIVATE);
        pw = sp1.getString("Psw", null);

        //SEND EMAIL BUTTON LISTENER
        send_pw_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TO SEND THIS EMAIL WE IMPORTED A LIBRARY DEVELOPED BY yesidlazaro ON GITHUB
                //https://github.com/yesidlazaro/GmailBackground


                BackgroundMail.newBuilder(ForgotPasswordActivity.this)
                        .withUsername("amanjotjo@gmail.com")
                        .withPassword("Pepsi112")
                        .withMailto(email_et.getText().toString().trim())
                        .withType(BackgroundMail.TYPE_PLAIN)
                        .withSubject("Retrieve Password")
                        .withBody("Your password is: " + pw + "\nIt is highly recommended to reset your password upon logging in.")
                        .send();
            }
        });

        //REMEMBER PASSWORD TEXTVIEW LISTENER

        remember_password_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
            }
        });

        //REGISTER TEXTVIEW LISTENER

        register_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPasswordActivity.this, RegisterAccountActivity.class));

            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
        this.finish();
    }
}
