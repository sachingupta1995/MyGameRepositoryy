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

/**
 * Created by Sachin Gupta on 9/20/2016.
 */
public class PendingRequestsActivity extends AppCompatActivity {

    ListView lv;
    TextView noResults;
    public static ArrayList<ApiDatabase.ApiClass> userList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_users);
        noResults=(TextView)findViewById(R.id.no_results);
        Log.d("Inside","PendingRequest");
        lv=(ListView) findViewById(R.id.listView);

        ArrayList<ApiDatabase.ApiClass> res = null;
        String userId=UtilClass.userId;
        String serverUrl=getResources().getString(R.string.server_url1)+"/"+userId+"/pendingRequests";
        if(checkInternet()) {
            try {
                res = UtilClass.getPendingRequest(serverUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(res.size()==1&&res.get(0).firstName.equals("BadRequest"))
            {
                Toast.makeText(getApplicationContext(),"Bad Request!!Try again later.",Toast.LENGTH_LONG);
            }
            else
            {
                userList = new ArrayList<ApiDatabase.ApiClass>();
                for (int i = 0; i < res.size(); i++)
                    userList.add(res.get(i));
                if (userList.size() == 0)
                    noResults.setText("No Pending Requests");
                else
                    lv.setAdapter(new CustomAdapter(this, userList));
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
            startActivity(new Intent(PendingRequestsActivity.this,SearchActivity.class));
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
