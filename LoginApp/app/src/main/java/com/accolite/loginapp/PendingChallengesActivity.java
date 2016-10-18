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

import multiplayerdemo.PendingChallengesData;
import multiplayerdemo.PlayOptionsActivity;
import multiplayerdemo.RetrievingData;

/**
 * Created by Sachin Gupta on 9/20/2016.
 */
public class PendingChallengesActivity extends AppCompatActivity {

    ListView lv;
    TextView noResults;
    public static ArrayList<ApiDatabase.ApiClass> userList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_users);
        noResults=(TextView)findViewById(R.id.no_results);
        Log.d("Inside","PendingChallengesActivity");
        lv=(ListView) findViewById(R.id.listView);

        ArrayList<PendingChallengesData> res = null;
        String userId=UtilClass.userId;
        userId="550";
        String serverUrl=getResources().getString(R.string.server_url)+"/pendingChallenges/user/"+userId;
        if(checkInternet()) {
            try {
                res = RetrievingData.getPendingChallenges(serverUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(res.size()==0)
            {
                Toast.makeText(getApplicationContext(),"No Pending Challenges",Toast.LENGTH_LONG);
            }
            else
            {
                    lv.setAdapter(new PendingChallengeAdapter(this, res));
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Check the internet connection!!",Toast.LENGTH_LONG);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //do whatever you need for the hardware 'back' button
            finish();
            startActivity(new Intent(PendingChallengesActivity.this, PlayOptionsActivity.class));
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
