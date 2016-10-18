package com.accolite.loginapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by Sachin Gupta on 9/19/2016.
 */


public class InviteActivity extends AppCompatActivity {

    TextView userInfo;
    Button addFriend;
    String userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adding_friend);
        addFriend = (Button) findViewById(R.id.add_friend);
        userInfo = (TextView) findViewById(R.id.user_info);

        Log.d("InviteActivity", "It is Called ");
        Intent mIntent = getIntent();
        final Integer index = mIntent.getIntExtra("indexClicked", 0);
        userId = UtilClass.registeredUsers.get(index).id;
        userInfo.setText(UtilClass.registeredUsers.get(index).firstName + " " + UtilClass.registeredUsers.get(index).lastName);
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String serverUrl = getResources().getString(R.string.server_url1) + "/" + UtilClass.userId + "/friends";
                String jsonFormat = "{\"id\":" + "\"" + userId + "\"}";
                String res = null;
                Log.d("JsonIs", jsonFormat + " " + serverUrl);
                if (checkInternet()) {
                    try {
                        res = UtilClass.FriendRequest(serverUrl, jsonFormat);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (res.equals("true")) {
                        startActivity(new Intent(InviteActivity.this, SearchActivity.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "BadRequest!!Try again later.", Toast.LENGTH_LONG);

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Check Internet Connection!!", Toast.LENGTH_LONG);

                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //do whatever you need for the hardware 'back' button
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean checkInternet() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (!(activeNetworkInfo != null && activeNetworkInfo.isConnected()))
            return false;
        return true;

    }
}
