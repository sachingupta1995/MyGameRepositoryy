package com.accolite.loginapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import model.User;
import multiplayerdemo.PlayOptionsActivity;
import multiplayerdemo.RetrievingData;

/**
 * Created by Sachin Gupta on 9/20/2016.
 */
public class ChallengeFriendActivity extends AppCompatActivity {

    ListView lv;
    TextView noResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_users);
        noResults = (TextView) findViewById(R.id.no_results);
        Log.d("InsideChallengeFriend", "Friends");
        lv = (ListView) findViewById(R.id.listView);

        String userId = UtilClass.userId;
        userId = "3";
        String serverUrl = getResources().getString(R.string.server_url1) + "/" + userId + "/friends";
        ArrayList<User> userArrayList = new ArrayList<User>();
        if (checkInternet()) {
            try {
                userArrayList = RetrievingData.getListofFriends(serverUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }


            lv.setAdapter(new ChallengeFriendAdapter(this, userArrayList));

        } else {
            Toast.makeText(getApplicationContext(), "Check the internet connection!!", Toast.LENGTH_LONG);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //do whatever you need for the hardware 'back' button
            finish();
            startActivity(new Intent(ChallengeFriendActivity.this, PlayOptionsActivity.class));
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
