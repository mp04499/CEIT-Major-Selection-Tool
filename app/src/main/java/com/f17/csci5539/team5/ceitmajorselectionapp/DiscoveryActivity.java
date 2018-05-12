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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DiscoveryActivity extends AppCompatActivity {

    final static String[] maj = {"00", "01", "10", "11"};        //array holds the strings to compare the QIDs to
    static int[] score = {0, 0, 0, 0};                           //array that holds the scores for each major
    static String[] done = new String[6];                        //array that holds the questions completed (the QIDs)
    static String[] pref = new String[3];                        //holds the majors they preferred
    static String[] notp = new String[3];                        //holds the majors the didn't prefer
    static List<String> list = new ArrayList<String>();
    String[] halves = new String[2];

    private Connection con;
    private TextView questions_tv, header_tv;
    private String question;
    private String qid;
    private String next_qid;
    private int i = 0;
    private int q_num = 1;
    private String which_button = "";
    private Quiz quiz;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery);
        ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setIcon(R.drawable.gsulogosmall);

        header_tv = (TextView) findViewById(R.id.header_tv);
        questions_tv = (TextView) findViewById(R.id.question_textview);
        final Button a_btn = (Button) findViewById(R.id.a_btn);
        final Button b_btn = (Button) findViewById(R.id.b_btn);
        final Button start_btn = (Button) findViewById(R.id.start_btn);
        Button exit_btn = (Button) findViewById(R.id.back_btn);
        quiz = new Quiz();
        quiz.execute();

        //START QUIZ BUTTON LISTENER -- SET UP QUIZ VIEW
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Set layout for quiz
                questions_tv.setVisibility(View.VISIBLE);
                a_btn.setVisibility(View.VISIBLE);
                b_btn.setVisibility(View.VISIBLE);
                start_btn.setVisibility(View.GONE);

                //SET TEXTVIEW TO QUESTION
                questions_tv.setText(question);

                header_tv.setText("What interests you more?\n\n1/6");

            }
        });

        //ANSWER A BUTTON LISTENER
        a_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                which_button = "a";

                //PROCESS USER SELECTION
                applyScore(2, 1);

                //METHOD TO FILTER FOR THE NEXT QUESTION
                questionFilters();

                //LOGCAT STRINGS FOR TESTING
                Log.d("My array", "done: " + Arrays.toString(done));
                Log.d("My array", "pref: " + Arrays.toString(pref));
                Log.d("My array", "notp: " + Arrays.toString(notp));
                Log.d("My array", "score: " + Arrays.toString(score));
                Log.d("i is equal to:", "" + Integer.toString(i));
                //INCREMENT i
                i++;
                if (q_num < 6) q_num++;
                header_tv.setText("What interests you more?\n\n" + q_num + "/6\n");

                if (i == 6) {
                    //METHOD - END QUIZ AFTER 6 QUESTIONS
                    finishQuiz();
                } else {

                    //search db for question with pref and without notp that hasn't been used
                    //FETCH NEXT QUESTION
                    Quiz updateQuiz = new Quiz();
                    updateQuiz.doInBackground();
                    questions_tv.setText(question);
                }

            }
        });

        //ANSWER B BUTTON LISTENER
        b_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                which_button = "b";
                //PROCESS USER SELECTION
                applyScore(1, 2);

                //METHOD TO FILTER FOR THE NEXT QUESTION
                questionFilters();

                //LOGCAT STRINGS FOR TESTING
                Log.d("My array", "done: " + Arrays.toString(done));
                Log.d("My array", "pref: " + Arrays.toString(pref));
                Log.d("My array", "notp: " + Arrays.toString(notp));
                Log.d("My array", "score: " + Arrays.toString(score));
                Log.d("i is equal to:", "" + Integer.toString(i));

                //INCREMENT i
                i++;
                if (q_num < 6) q_num++;

                header_tv.setText("What interests you more?\n\n" + q_num + "/6\n");
                if (i == 6) {
                    //METHOD - END QUIZ AFTER 6 QUESTIONS
                    finishQuiz();
                } else {

                    //search db for question with pref and without notp that hasn't been used
                    //FETCH NEXT QUESTION
                    Quiz updateQuiz = new Quiz();
                    updateQuiz.doInBackground();
                    questions_tv.setText(question);
                }

            }
        });

        //EXIT BUTTON BACK TO MAIN MENU
        exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clearArrays();
                startActivity(new Intent(DiscoveryActivity.this, MainMenuActivity.class));
                DiscoveryActivity.this.finish();
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
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    //PROCESS USER SELECTION
    //Split QIDs for sorting and apply score
    public void applyScore(int x, int y) {

        if (i == 6) {
            i = 5;
        }
        //STORE QID IN DONE ARRAY
        if (done[i] == null) {
            done[i] = qid;
        }

        //SPLIT QID INTO HALVES
        halves[0] = done[i].substring(0, 2);
        halves[1] = done[i].substring(2);            //the major IDs from the QIDs are now separated

        //TEST STRINGS
        Log.d("Array - Halves[0]", halves[0]);
        Log.d("Array - Halves[1]", halves[1]);

        //apply score
        int index = 0;
        int index2 = 0;

        for (int j = 0; j < maj.length; j++) {
            if (halves[0].equals(maj[j])) {
                index = j;
                break;
            }
        }

        for (int j = 0; j < maj.length; j++) {

            if (halves[1].equals(maj[j])) {
                index2 = j;
                break;
            }
        }

        //TEST STRINGS
        Log.d("Index", "" + Integer.toString(index));
        Log.d("Index2", "" + Integer.toString(index2));

        //search array maj for halves[1], get the index, add 2 to score[index]
        //search array maj for halves[0], get the index, add 1 to score[index]
        score[index] = score[index] + x;
        score[index2] = score[index2] + y;
    }

    //NULL ARRAYS TO RESET QUIZ
    public void clearArrays() {

        for (int i = 0; i < done.length; i++) {
            done[i] = null;
        }

        for (int i = 0; i < 3; i++) {
            pref[i] = null;
            notp[i] = null;

        }
    }

    //QUESTION FILTERS
    public void questionFilters() {

        int temp_i = i;

        for (int j = 0; j < 3; j++) {
            if (i == 3) {
                notp[j] = null;
                pref[j] = null;
            }

        }

        if (i > 2) {
            temp_i = i - 3;
        }

        if (which_button.equals("a")) {
            pref[temp_i] = halves[0];
            notp[temp_i] = halves[1];
        } else if (which_button.equals("b")) {
            pref[temp_i] = halves[1];
            notp[temp_i] = halves[0];
        }

        for (int k = 0; k < maj.length; k++) {

            if (!maj[k].equals(notp[temp_i]) & !maj[k].equals(pref[temp_i])) {

                for (int l = 0; l < done.length; l++) {

                    if ((pref[temp_i] + maj[k]).equals(done[l])) {


                        break;

                    } else {
                        Log.d("NEXT QID =====", pref[temp_i] + maj[k] + "");
                        Log.d("done[i] =====", done[l] + "");
                        next_qid = pref[temp_i] + maj[k];
                    }
                }
            }
        }
    }


    //EXECUTE ON FINISH QUIZ
    public void finishQuiz() {

        //apply halves[1] to pref[i]
        //apply halves[0] to notp[i]
        if (which_button.equals("a")) {
            applyScore(2, 1);
            pref[i - 4] = halves[0];
            notp[i - 4] = halves[1];
        } else if (which_button.equals("b")) {
            applyScore(1, 2);
            pref[i - 4] = halves[1];
            notp[i - 4] = halves[0];
        }

        //Logcat Strings for testing
        Log.d("My array", "done: " + Arrays.toString(done));
        Log.d("My array", "pref: " + Arrays.toString(pref));
        Log.d("My array", "notp: " + Arrays.toString(notp));
        Log.d("My array", "score: " + Arrays.toString(score));
        Log.d("i is equal to:", "" + Integer.toString(i));

        //GET INDEX OF HIGH SCORE
        int max = score[0];
        int maj_index = 0;
        int sec_maj_index = 0;

        for (int i = 0; i < score.length; i++) {

            if (max < score[i]) {
                sec_maj_index = max;
                max = score[i];
                maj_index = i;
            } else if (score[i] > max) {
                sec_maj_index = i;
            }
        }

        for (int i = 0; i < score.length; i++) {
            if (sec_maj_index == score[i]) {
                sec_maj_index = i;
            }
        }

        //STORE FOR
        SharedPreferences sp = getSharedPreferences("Results", MODE_PRIVATE);
        SharedPreferences.Editor Ed1 = sp.edit();
        Ed1.putInt("max_score", maj_index);
        Ed1.putInt("sec_maj_index", sec_maj_index);
        for (int i = 0; i < done.length; i++) {
            Ed1.putString("done_" + i, done[i]);
        }
        Ed1.apply();

        //PASS USER QUIZ DATA TO RESULTSACTIVITY TO VIEW AND/OR SAVE
        Intent intent = new Intent(getApplicationContext(), ResultsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("max_score", maj_index);
        intent.putExtra("sec_maj_index", sec_maj_index);
        intent.putExtra("done_arr", done);
        startActivity(intent);
        clearArrays();

        DiscoveryActivity.this.finish();

    }

    //CONNECT TO PULL QUIZ QUESTIONS
    public class Quiz extends AsyncTask<String, String, String> {
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

                    if (done[0] == null) {
                        //generate random int 1-12
                        //select that question for quiz

                        Random rand = new Random();
                        int q = rand.nextInt(12) + 1;
                        query = "With x as(\n" +
                                "SELECT ROW_NUMBER() OVER(ORDER BY qid Asc) AS rownum, * FROM questions)\n" +
                                "select * from x where rownum = " + q + ";\n";
                    } else {

                        query = "SELECT * FROM questions where qid = '" + next_qid + "'";
                        Log.d("UPDATE QUESTION STRING", query);

                    }

                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    if (rs.next()) {
                        isSuccess = true;

                        question = rs.getString("question");
                        qid = rs.getString("qid");
                        Log.d("QUESTION ", question);
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

    @Override
    public void onBackPressed() {

        clearArrays();

        startActivity(new Intent(DiscoveryActivity.this, MainMenuActivity.class));
        this.finish();
    }
}
