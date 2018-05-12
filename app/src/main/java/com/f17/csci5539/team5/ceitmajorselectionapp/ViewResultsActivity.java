package com.f17.csci5539.team5.ceitmajorselectionapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;

public class ViewResultsActivity extends AppCompatActivity {

    TextView attempt_1, attempt_2, attempt_3, attempt_4;
    Connection con;

    String attempt1 = "";
    String attempt2 = "";
    String attempt3 = "";
    String attempt4 = "";
    String QID = "";
    String QID2 = "";
    String QID3 = "";
    String QID4 = "";
    String QID5 = "";
    String QID6 = "";
    String prim = "";
    String sec = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_results);
        ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setIcon(R.drawable.gsulogosmall);


        Button back_btn = (Button) findViewById(R.id.back_btn);
        attempt_1 = (TextView) findViewById(R.id.attempt_1_tv);
        attempt_2 = (TextView) findViewById(R.id.attempt_2_tv);
        attempt_3 = (TextView) findViewById(R.id.attempt_3_tv);
        attempt_4 = (TextView) findViewById(R.id.attempt_4_tv);

        PullQuizResults pqr = new PullQuizResults();
        pqr.execute();


        while(attempt4 == "" ){
            Log.d("WAIT", "Waiting for load");
        }

        attempt_1.setText(attempt1);
        attempt_2.setText(attempt2);
        attempt_3.setText(attempt3);
        attempt_4.setText(attempt4);



        //EXIT BUTTON BACK TO MAIN MENU
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewResultsActivity.this.finish();

            }
        });


    }

    //CONNECT TO PULL QUIZ QUESTIONS
    public class PullQuizResults extends AsyncTask<String, String, String> {
        String statusMessage = "";
        Boolean isSuccess = false;

        @Override
        protected void onPostExecute(String r) {
            //Toast.makeText(LoginActivity.this, r, Toast.LENGTH_SHORT).show();
            if (isSuccess) {
                // Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                //finish();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                con = SQLConnection.connectionclass();

                if (con == null) {
                    statusMessage = "Check Your Internet Connection.";
                } else {

                    String query = "Select * from attempts";

                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    if (rs.next()) {
                        isSuccess = true;

                        String query1 = "Select * from attempts where attnum = 1";
                        ResultSet rs1 = stmt.executeQuery(query1);
                        if (rs1.next()) {
                            QID = rs1.getString("QID");
                            QID2 = rs1.getString("QID2");
                            QID3 = rs1.getString("QID3");
                            QID4 = rs1.getString("QID4");
                            QID5 = rs1.getString("QID5");
                            QID6 = rs1.getString("QID6");
                            prim = rs1.getString("primary");
                            sec = rs1.getString("secondary");
                            attempt1 = prim + "\n" + sec;
                        } else {
                            attempt1 = "Not Attempted Yet";
                        }


                        String query2 = "Select * from attempts where attnum = 2";
                        ResultSet rs2 = stmt.executeQuery(query2);
                        if (rs2.next()) {
                            QID = rs2.getString("QID");
                            QID2 = rs2.getString("QID2");
                            QID3 = rs2.getString("QID3");
                            QID4 = rs2.getString("QID4");
                            QID5 = rs2.getString("QID5");
                            QID6 = rs2.getString("QID6");
                            prim = rs2.getString("primary");
                            sec = rs2.getString("secondary");
                            attempt2 = prim + "\n" + sec;
                        } else {
                            attempt2 = "Not Attempted Yet";
                        }


                        String query3 = "Select * from attempts where attnum = 3";
                        ResultSet rs3 = stmt.executeQuery(query3);
                        if (rs3.next()) {
                            QID = rs3.getString("QID");
                            QID2 = rs3.getString("QID2");
                            QID3 = rs3.getString("QID3");
                            QID4 = rs3.getString("QID4");
                            QID5 = rs3.getString("QID5");
                            QID6 = rs3.getString("QID6");
                            prim = rs3.getString("primary");
                            sec = rs3.getString("secondary");
                            attempt3 = prim + "\n" + sec;
                        } else {
                            attempt3 = "Not Attempted Yet";
                        }


                        String query4 = "Select * from attempts where attnum = 4";
                        ResultSet rs4 = stmt.executeQuery(query4);
                        if (rs4.next()) {
                            QID = rs4.getString("QID");
                            QID2 = rs4.getString("QID2");
                            QID3 = rs4.getString("QID3");
                            QID4 = rs4.getString("QID4");
                            QID5 = rs4.getString("QID5");
                            QID6 = rs4.getString("QID6");
                            prim = rs4.getString("primary");
                            sec = rs4.getString("secondary");
                            attempt4 = prim + "\n" + sec;
                        } else {
                            attempt4 = "Not Attempted Yet";
                        }


                        con.close();
                    } else {
                        statusMessage = "Something went wrong with the connection";
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
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
