package com.f17.csci5539.team5.ceitmajorselectionapp;

import android.content.Intent;
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
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;

public class ResultsActivity extends AppCompatActivity {

    Button majors_btn, save_btn, main_menu_btn, prim_major_btn, sec_major_btn;
    TextView results_tv, results_sec_tv;
    String[] done_arr;
    String primary;
    String secondary;
    Connection con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setIcon(R.drawable.gsulogosmall);

        //INITIALIZE VIEWS
        results_tv = (TextView) findViewById(R.id.results_tv);
        results_sec_tv = (TextView) findViewById(R.id.results_sec_tv);
        majors_btn = (Button) findViewById(R.id.major_info_btn);
        save_btn = (Button) findViewById(R.id.save_btn);
        main_menu_btn = (Button) findViewById(R.id.main_menu_btn);
        prim_major_btn = (Button) findViewById(R.id.prim_major_btn);
        sec_major_btn = (Button) findViewById(R.id.sec_major_btn);

        //GET EXTRAS FROM DISCOVERY ACTIVITY
        Intent intent = getIntent();
        int result_index = intent.getIntExtra("max_score", 0);
        int sec_result_index = intent.getIntExtra("sec_maj_index", 0);
        done_arr = intent.getStringArrayExtra("done_arr");

        //SET PRIMARY MAJOR
        if (result_index == 0) {
            results_tv.setText(getResources().getString(R.string.is));
        } else if (result_index == 1) {
            results_tv.setText(getResources().getString(R.string.it));
        } else if (result_index == 2) {
            results_tv.setText(getResources().getString(R.string.cs));
        } else if (result_index == 3) {
            results_tv.setText(getResources().getString(R.string.ce));
        }

        //SET SECONDARY MAJOR
        if (sec_result_index == 0) {
            results_sec_tv.setText(getResources().getString(R.string.is));
        } else if (sec_result_index == 1) {
            results_sec_tv.setText(getResources().getString(R.string.it));
        } else if (sec_result_index == 2) {
            results_sec_tv.setText(getResources().getString(R.string.cs));
        } else if (sec_result_index == 3) {
            results_sec_tv.setText(getResources().getString(R.string.ce));
        }

        //SET BUTTON NAMES
        prim_major_btn.setText(results_tv.getText());
        sec_major_btn.setText(results_sec_tv.getText());

        //PRIMARY BUTTON LISTENER
        prim_major_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String selection = results_tv.getText().toString();
                Intent i = new Intent(getApplicationContext(), SelectedMajorActivity.class);
                i.putExtra("selection", selection);
                startActivity(i);
            }
        });


        //SECONDARY BUTTON LISTENER
        sec_major_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String selection = results_sec_tv.getText().toString();
                Intent i = new Intent(getApplicationContext(), SelectedMajorActivity.class);
                i.putExtra("selection", selection);
                startActivity(i);

            }
        });


        //MAJOR BUTTON LISTENER
        majors_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResultsActivity.this, MajorsActivity.class));
            }
        });

        //SAVE BUTTON LISTENER
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                saveQuiz savequiz = new saveQuiz();
                savequiz.execute();


                Toast.makeText(ResultsActivity.this, "Results saved!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ResultsActivity.this, ViewResultsActivity.class));


            }
        });

        //BACK TO MAIN MENU BUTTON LISTENER
        main_menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResultsActivity.this, MainMenuActivity.class));
            }
        });
    }

    //CONNECT TO PULL QUIZ QUESTIONS
    public class saveQuiz extends AsyncTask<String, String, String> {
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

                    String query = "";
                    query = "Select * from attempts order by attnum desc";
                    Log.d("UPDATE QUESTION STRING", query);

                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    if (!rs.next()) {

                        String attemptQuery = "";
                        isSuccess = true;
                        for (int i = 0; i < done_arr.length; i++) {
                            attemptQuery = "insert into attempts (attNum, qid, qid2, qid3, qid4, qid5, qid6, [primary], [secondary])\n" +
                                    "values(1,'" + done_arr[0] + "', '" + done_arr[1] + "', '" + done_arr[2]
                                    + "', '" + done_arr[3] + "','" + done_arr[4] + "', '" + done_arr[5] + "','"
                                    + results_tv.getText().toString() + "','" + results_sec_tv.getText().toString() + "')";
                            Log.d("QUESTION ", attemptQuery);
                        }
                        ResultSet rs1 = stmt.executeQuery(attemptQuery);

                        con.close();
                    } else {

                        int attempt_num = rs.getInt("attNum");
                        int next_attempt_num = attempt_num + 1;

                        if (next_attempt_num == 5) {
                            next_attempt_num = 1;
                        }

                        String attemptQuery2 = "";
                        for (int i = 0; i < done_arr.length; i++) {
                            attemptQuery2 = "Delete from attempts where attNum = " + next_attempt_num + " " +
                                    "insert into attempts (attNum, qid, qid2, qid3, qid4, qid5, qid6, [primary], [secondary])\n" +
                                    "values(" + next_attempt_num + ",'" + done_arr[0] + "', '" + done_arr[1] + "', '" + done_arr[2]
                                    + "', '" + done_arr[3] + "','" + done_arr[4] + "', '" + done_arr[5] + "','"
                                    + results_tv.getText().toString() + "','" + results_sec_tv.getText().toString() + "')";
                        }

                        ResultSet rs1 = stmt.executeQuery(attemptQuery2);
                        con.close();

                    }
//                    else {
//                        statusMessage = "Something went wrong with the connection";
//                        isSuccess = false;
//                        con.close();
//                    }
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
        startActivity(new Intent(ResultsActivity.this, MainMenuActivity.class));

    }
}
