package com.f17.csci5539.team5.ceitmajorselectionapp;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLConnection {

    //DATABASE CONNECTION METHOD
    public static Connection connectionclass() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String hostName = "ceit-major-selection.database.windows.net";
            String dbName = "CEIT_APP";
            String user = "ceit_admin";
            String password = "Softteam5";
            String url = String.format("jdbc:jtds:sqlserver://%s:1433/%s;user=%s;password=%s;", hostName, dbName, user, password);
            Log.d("URL: ", url);
            connection = DriverManager.getConnection(url);
        } catch (SQLException se) {
            Log.e("Error 1: ", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("Error 2: ", e.getMessage());
        } catch (Exception e) {
            Log.e("Error 3: ", e.getMessage());
        }
        return connection;
    }
}
