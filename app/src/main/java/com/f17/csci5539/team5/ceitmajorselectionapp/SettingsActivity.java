package com.f17.csci5539.team5.ceitmajorselectionapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SettingsActivity extends AppCompatActivity {

    Connection con;
    String userEmail, pass;
    String curr_fname = "", curr_lname, curr_email, curr_school, curr_password;
    EditText new_pw, confirm_new_pw, fname, lname, email, hs_uni;
    boolean deletable = false;
    boolean updatable = false;
    boolean pw_updatable = false;
    int uid;
    int is_admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setIcon(R.drawable.gsulogosmall);

        dataFetcher datafeetcher = new dataFetcher();
        datafeetcher.execute();
//        while(curr_fname == null){
//
//        }

        //Retrieve User info
        SharedPreferences sp1 = this.getSharedPreferences("Login", MODE_PRIVATE);

        userEmail = sp1.getString("email", null);
        pass = sp1.getString("Psw", null);


        while (curr_fname.equals("")) {
            Log.d("W", "Loading ");
        }

        //SETTINGS VIEW INITIALIZE
        fname = (EditText) findViewById(R.id.update_fname_text);
        lname = (EditText) findViewById(R.id.update_lname_text);
        email = (EditText) findViewById(R.id.update_email_text);
        hs_uni = (EditText) findViewById(R.id.update_hs_uni_text);

        final EditText old_pw_et = (EditText) findViewById(R.id.old_password_text);
        new_pw = (EditText) findViewById(R.id.new_password_text);
        confirm_new_pw = (EditText) findViewById(R.id.confirm_new_password_text);

        //Append ORIGINAL User Values to Edittext
        fname.setText(curr_fname);
        lname.setText(curr_lname);
        email.setText(curr_email);
        hs_uni.setText(curr_school);

//        if(is_admin == 0){
//            hs_uni.setHint("Highschool");
//            hs_uni.setText("");
//        }else{
//            hs_uni.setHint("University");
//            hs_uni.setText("");
//        }


        // SAVE/UPDATE SETTINGS BUTTON

        Button save_btn = (Button) findViewById(R.id.save_btn);

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatable = true;


                if (old_pw_et.getText().toString().trim().equals(pass)) {

                    if (!new_pw.getText().toString().trim().equals("") & !confirm_new_pw.getText().toString().trim().equals("")) {
                        pw_updatable = true;
                    }
                    SettingsHandler sh = new SettingsHandler();
                    sh.doInBackground();

                    Toast.makeText(SettingsActivity.this, "Account Updated", Toast.LENGTH_LONG).show();
                    updatable = false;

                } else if (old_pw_et.getText().toString().trim().equals("")) {
                    Toast.makeText(SettingsActivity.this, "Password field is blank", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SettingsActivity.this, "Incorrect Password", Toast.LENGTH_LONG).show();
                }
            }
        });

        //ATTEMPTS BUTTON

        Button att_btn = (Button) findViewById(R.id.attempt_btn);

        att_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, ViewResultsActivity.class));
            }
        });

        //DELETE BUTTON LISTENER

        Button delete_btn = (Button) findViewById(R.id.delete_btn);

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deletable = true;
                //ACTION TO DELETE ACCOUNT
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                SettingsHandler sa = new SettingsHandler();
                                sa.execute();
                                Toast.makeText(SettingsActivity.this, "Account Successfully Deleted", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                SettingsActivity.this.finish();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                deletable = false;
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setMessage("Are you sure you want to delete this account?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
    }

    //INFLATE MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    //MENU ITEM ACTIONS
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.Settings:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                break;
            case R.id.About:
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                break;
            case R.id.Logout:
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                SettingsActivity.this.finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    //CONNECT to DB in ASYNC
    public class SettingsHandler extends AsyncTask<String, String, String> {
        String statusMessage = "";
        Boolean isSuccess = false;

        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(SettingsActivity.this, r, Toast.LENGTH_SHORT).show();
            if (isSuccess) {
                Toast.makeText(SettingsActivity.this, "Loaded", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                con = SQLConnection.connectionclass();

                if (con == null) {
                    statusMessage = "Check Your Internet Connection.";

                } else if (deletable) { //DELETE ACCOUNT
                    PreparedStatement stmt = con.prepareStatement("delete from students where studentid = " + uid + "\n" +
                            "delete from HIGHSCHOOL where code = 100" + uid + "\n" +
                            "delete from users where uid = " + uid + "" +
                            "select * from users");
                    stmt.executeUpdate();
                    con.close();

                } else if (updatable) { //EXECUTE QUERY OPERATIONS

                    // USES PREPARED STATEMENT TO PREVENT SQL INJECTION
                    PreparedStatement stmt1 = con.prepareStatement("select * from users u join students s on u.uid = s.StudentID join HIGHSCHOOL h on s.HScode = h.code where email=? and password=?");
                    stmt1.setString(1, userEmail);
                    stmt1.setString(2, pass);
                    ResultSet rs = stmt1.executeQuery();

                    if (rs.next()) {
                        isSuccess = true;

                        PreparedStatement stmt_update = con.prepareStatement("update HIGHSCHOOL set hsname = '" + hs_uni.getText().toString().trim() + "' where code = 100" + uid + "\n" +
                                "update users set fname = '" + fname.getText().toString().trim() + "', lname = '" + lname.getText().toString().trim() + "', email = '" + email.getText().toString().trim() + "' where uid = " + uid + "");

                        Log.d("UPDATE USER STRING", "------" + stmt_update.toString() + "");
                        stmt_update.executeUpdate();

                        if (pw_updatable) {
                            if (new_pw.getText().toString().trim().equals(confirm_new_pw.getText().toString().trim())) {
                                PreparedStatement stmt_pw_update = con.prepareStatement("update users set password = '" + confirm_new_pw + "' where uid = " + uid + "");
                                stmt_pw_update.executeUpdate();
                                Log.d("UPDATE USER STRING", "------" + stmt_pw_update.toString() + "");
                            }
                        }

                        con.close();
                    } else {
                        statusMessage = "Failed to update data";
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

    //CONNECT to DB in ASYNC
    public class dataFetcher extends AsyncTask<String, String, String> {
        String statusMessage = "";
        Boolean isSuccess = false;

        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(SettingsActivity.this, r, Toast.LENGTH_SHORT).show();
            if (isSuccess) {
                //Toast.makeText(SettingsActivity.this, "Data Load successfull", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                con = SQLConnection.connectionclass();

                if (con == null) {
                    statusMessage = "Check Your Internet Connection.";

                } else { //EXECUTE QUERY OPERATIONS

                    // USES PREPARED STATEMENT TO PREVENT SQL INJECTION
                    PreparedStatement stmt1 = con.prepareStatement("select * from users u join students s on u.uid = s.StudentID join HIGHSCHOOL h on s.HScode = h.code where email=? and password=?");
                    stmt1.setString(1, userEmail);
                    stmt1.setString(2, pass);
                    ResultSet rs = stmt1.executeQuery();

                    if (rs.next()) {


                        curr_fname = rs.getString("fname");
                        curr_lname = rs.getString("lname");
                        curr_email = rs.getString("email");
                        curr_school = rs.getString("hsname");
                        curr_password = rs.getString("password");
                        uid = rs.getInt("uid");
                        isSuccess = true;
                        con.close();
                    } else {
                        statusMessage = "Failed to fetch data";
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


    @Override
    public void onBackPressed() {
        this.finish();
    }
}
