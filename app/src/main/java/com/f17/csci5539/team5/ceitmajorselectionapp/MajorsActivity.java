package com.f17.csci5539.team5.ceitmajorselectionapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MajorsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_majors);

        //CS BUTTON LISTENER
        Button CS_btn = (Button) findViewById(R.id.CS_btn);

        CS_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String selection = "cs";
                Intent i = new Intent(getApplicationContext(), SelectedMajorActivity.class);
                i.putExtra("selection", "cs");
                startActivity(i);

            }
        });

        //SE BUTTON LISTENER
        Button SE_btn = (Button) findViewById(R.id.CE_btn);

        SE_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String selection = "ce";
                Intent i = new Intent(getApplicationContext(), SelectedMajorActivity.class);
                i.putExtra("selection", "ce");
                startActivity(i);

            }
        });

        //IT BUTTON LISTENER
        Button IT_btn = (Button) findViewById(R.id.IT_btn);

        IT_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String selection = "it";
                Intent i = new Intent(getApplicationContext(), SelectedMajorActivity.class);
                i.putExtra("selection", "it");
                startActivity(i);
            }
        });

        //IS BUTTON LISTENER
        Button IS_btn = (Button) findViewById(R.id.IS_btn);

        IS_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String selection = "is";
                Intent i = new Intent(getApplicationContext(), SelectedMajorActivity.class);
                i.putExtra("selection", "is");
                startActivity(i);
            }
        });


        //BACK BUTTON LISTENER
        Button back_btn = (Button) findViewById(R.id.back_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MajorsActivity.this, MainMenuActivity.class));
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MajorsActivity.this, MainMenuActivity.class));

    }
}
