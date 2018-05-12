package com.f17.csci5539.team5.ceitmajorselectionapp;

import android.content.Intent;
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
import java.sql.ResultSet;
import java.sql.Statement;

public class RegisterAccountActivity extends AppCompatActivity {

    Connection con;
    Boolean isSuccess = false;
    EditText fname_edittext, lname_edittext, email_edittext, password_edittext, confirm_password_edittext,
            admin_code_edittext, hs_edittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        //ASSIGN EDITBOXES TO VIEW
        fname_edittext = (EditText) findViewById(R.id.reg_fname_edit_text);
        lname_edittext = (EditText) findViewById(R.id.reg_lname_edit_text);
        email_edittext = (EditText) findViewById(R.id.reg_email_edit_text);
        password_edittext = (EditText) findViewById(R.id.reg_password_text);
        confirm_password_edittext = (EditText) findViewById(R.id.reg_confirm_password_text);
        hs_edittext = (EditText) findViewById(R.id.reg_high_school_text);
        //admin_code_edittext = (EditText) findViewById(R.id.reg_admin_code_text);


        //ACCOUNT EXISTS - BACK TO LOGIN TEXTVIEW LISTENER
        TextView login_exists_tv = (TextView) findViewById(R.id.login_exists_tv);

        login_exists_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterAccountActivity.this, LoginActivity.class));
            }
        });

        //REGISTER BUTTON LISTENER
        Button register_account = (Button) findViewById(R.id.register_btn);

        register_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //CHECK FOR EMPTY FIELDS
                if (fname_edittext.getText().toString().equals("") || fname_edittext.getText().toString().equals("") || email_edittext.getText().toString().equals("")
                        || password_edittext.getText().toString().equals("") || confirm_password_edittext.getText().toString().equals("")) {

                    Toast.makeText(RegisterAccountActivity.this, "Please fill out all fields.\n(Admin field is optional. Leave blank)", Toast.LENGTH_LONG).show();

                } else if (!password_edittext.getText().toString().trim().equals(confirm_password_edittext.getText().toString().trim())) {

                    Toast.makeText(RegisterAccountActivity.this, "Passwords do not match", Toast.LENGTH_LONG).show();

                } else {

                    //CONNECT AND QUERY
                    RegisterAccountConnection registeraccountconnection = new RegisterAccountConnection();
                    registeraccountconnection.execute();
                    Toast.makeText(RegisterAccountActivity.this, "Account created! Please login.", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(RegisterAccountActivity.this, LoginActivity.class));

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegisterAccountActivity.this, LoginActivity.class));
    }

    public class RegisterAccountConnection extends AsyncTask<String, String, String> {
        String statusMessage = "";

        @Override
        protected void onPostExecute(String r) {
            if (isSuccess) {
                Toast.makeText(RegisterAccountActivity.this, "Account Created", Toast.LENGTH_LONG).show();
                //finish();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            //GET USER INFO FROM EDITTEXT BOXES AND STORE IN STRINGS
            String first_name = fname_edittext.getText().toString().trim();
            String last_name = lname_edittext.getText().toString().trim();
            String email = email_edittext.getText().toString().trim();
            String new_password = password_edittext.getText().toString().trim();
            String confirm_password = confirm_password_edittext.getText().toString().trim();
            //String admin_code = admin_code_edittext.getText().toString().trim();
            String hs = hs_edittext.getText().toString().trim();


            try {
                con = SQLConnection.connectionclass();

                if (con == null) {
                    statusMessage = "Account couldn't be created, check internet connection and try again.";

                } else {

                    String query = "Select max(uid) as uid from USERS ";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    if (rs.next()) {

                        int uid = rs.getInt("uid");
                        int next_uid = uid + 1;
                        //int admin = 1;
                        int non_admin = 0;

                        //CHECK FOR MATCHING PASSWORDS

                        //FILTER FOR ADMIN USER OR STUDENT USER
                        //if (admin_code.equals("")) {
                        String studentuserquery = "";

                        //CHECK IF SCHOOL IS PROVIDED
                        if (hs.equals("")) {
                            studentuserquery = "insert into users(uid, admin, email, password, fname,lname)\n" +
                                    "values(" + next_uid + ", " + non_admin + ", '" + email + "', '" + new_password + "', '" + first_name + "', '" + last_name + "')";
                        } else {
                            studentuserquery = "insert into users(uid, admin, email, password, fname,lname)\n" +
                                    "values(" + next_uid + ", " + non_admin + ", '" + email + "', '" + new_password + "', '" + first_name + "', '" + last_name + "')"
                                    + "insert into HIGHSCHOOL(code, hsname, advisorFname, advisorLname)\n" +
                                    "values(" + 100 + next_uid + ", '" + hs + "', null, null)" +
                                    "insert into students(studentid, hscode, attnum)" +
                                    "values(" + next_uid + ",'" + 100 + next_uid + "', null)";

                        }


                        ResultSet rs1 = stmt.executeQuery(studentuserquery);
                        Log.d("INSERT QUERY: ", studentuserquery);

//                        } else {
//                            String adminuserquery = "insert into users(uid, admin, email, password, fname,lname)\n" +
//                                    "values(" + next_uid + ", " + admin + ", '" + email + "', '" + new_password + "', '" + first_name + "', '" + last_name + "')";
//
//                            String adduniversityquery = "";
//
//                            ResultSet rs2 = stmt.executeQuery(adminuserquery);
//                            Log.d("INSERT QUERY: ", adminuserquery);
//
//                        }
                        new Thread() {
                            public void run() {
                                RegisterAccountActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {

                                        Toast.makeText(RegisterAccountActivity.this, "Account created!", Toast.LENGTH_LONG).show();

                                    }
                                });
                            }
                        }.start();
                        isSuccess = true;


                        con.close();

                    } else {
                        statusMessage = "Account couldn't be created, try again";
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
