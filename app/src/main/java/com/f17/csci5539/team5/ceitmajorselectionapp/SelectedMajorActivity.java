package com.f17.csci5539.team5.ceitmajorselectionapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SelectedMajorActivity extends AppCompatActivity {

    TextView major_name, major_info, major_careers, major_colleges, major_ceit_link;
    ImageView major_pic;
    String link = "";
    String linkText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_major);

        //OBTAIN PASSED VALUE / MAJOR SELECTED BY USER
        String passed_major = "";
        Bundle extras = getIntent().getExtras();
        Bundle extras1 = getIntent().getExtras();
        if (extras != null) {
            passed_major = extras.getString("selection");
        }

        //RENAME VARIABLE IF COMING FROM RESULTS ACTIVITY
        if(passed_major.equals("Computer Science")){
            passed_major = "cs";
        }else if(passed_major.equals("Computer Engineering")){
            passed_major = "ce";
        }else if(passed_major.equals("Information Technology")){
            passed_major = "it";
        }else if(passed_major.equals("Information Systems")){
            passed_major = "is";
        }

        //Initialize TextViews
        major_name = (TextView) findViewById(R.id.major_name_tv);
        major_info = (TextView) findViewById(R.id.major_info_tv);
        major_careers = (TextView) findViewById(R.id.major_careers_tv);
        major_colleges = (TextView) findViewById(R.id.colleges_with_major_tv);
        major_ceit_link = (TextView) findViewById(R.id.link_tv);
        major_pic = (ImageView) findViewById(R.id.selected_major_image_view);


        //CS SELECTED
        if (passed_major.equals("cs")) {
            major_name.setText(getResources().getString(R.string.cs));
            major_info.setText(getResources().getString(R.string.cs_info));
            major_careers.setText(getResources().getString(R.string.cs_careers));
            major_colleges.setText(getResources().getString(R.string.cs_colleges));

            //LINK TO GSU CEIT MAJOR PAGE
            link = getResources().getString(R.string.cs_link);
            createLink(link);

            major_pic.setBackgroundResource(R.drawable.cs_image);
        }

        //CE SELECTED
        if (passed_major.equals("ce")) {
            major_name.setText(getResources().getString(R.string.ce));
            major_info.setText(getResources().getString(R.string.ce_info));
            major_careers.setText(getResources().getString(R.string.ce_careers));
            major_colleges.setText(getResources().getString(R.string.ce_colleges));

            //LINK TO GSU CEIT MAJOR PAGE
            link = getResources().getString(R.string.ce_link);
            createLink(link);

            major_pic.setBackgroundResource(R.drawable.se_image);
        }

        //IT SELECTED
        if (passed_major.equals("it")) {
            major_name.setText(getResources().getString(R.string.it));
            major_info.setText(getResources().getString(R.string.it_info));
            major_careers.setText(getResources().getString(R.string.it_careers));
            major_colleges.setText(getResources().getString(R.string.it_colleges));

            //LINK TO GSU CEIT MAJOR PAGE
            link = getResources().getString(R.string.it_link);
            createLink(link);

            major_pic.setBackgroundResource(R.drawable.it_image);
        }

        //IS SELECTED
        if (passed_major.equals("is")) {
            major_name.setText(getResources().getString(R.string.is));
            major_info.setText(getResources().getString(R.string.is_info));
            major_careers.setText(getResources().getString(R.string.is_careers));
            major_colleges.setText(getResources().getString(R.string.is_colleges));

            //LINK TO GSU CEIT MAJOR PAGE
            link = getResources().getString(R.string.is_link);
            createLink(link);

            major_pic.setBackgroundResource(R.drawable.is_image);
        }

        //BACK BUTTON LISTENER
        Button back_btn = (Button) findViewById(R.id.back_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectedMajorActivity.this, MajorsActivity.class));
            }
        });


    }

    public void createLink(String link) {
        linkText = "For more information, <a href='" + link + "'>click here</a>";
        major_ceit_link.setText(Html.fromHtml(linkText));
        major_ceit_link.setMovementMethod(LinkMovementMethod.getInstance());
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

//    @Override
//    public void onBackPressed() {
//        startActivity(new Intent(SelectedMajorActivity.this, MajorsActivity.class));
//    }
}
