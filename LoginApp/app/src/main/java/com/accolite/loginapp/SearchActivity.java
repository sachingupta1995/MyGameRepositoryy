package com.accolite.loginapp;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import multiplayerdemo.PlayOptionsActivity;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    Button pendingRequest,gameScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        pendingRequest = (Button) findViewById(R.id.pending_request);
        gameScreen=(Button)findViewById(R.id.play_game);
        pendingRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*ArrayList<ApiDatabase.ApiClass> response=new ArrayList<ApiDatabase.ApiClass>();
                String serverUrl=getResources().getString(R.string.server_url)+"/1/pendingRequests";
                try {
                   response= UtilClass.getPendingRequest(serverUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                startActivity(new Intent(SearchActivity.this, PendingRequestsActivity.class));
            }
        });

        gameScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(SearchActivity.this, PlayOptionsActivity.class));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchActivity.class)));
        searchView.setIconifiedByDefault(false);
        /*Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this, "Searching by: "+ query, Toast.LENGTH_SHORT).show();

        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            String uri = intent.getDataString();
            String index=uri.substring(uri.lastIndexOf('/')+1);
            Integer idx=Integer.parseInt(index);
            Log.d("idx","idx="+idx);
            Toast.makeText(this, "Suggestion: "+ UtilClass.registeredUsers.get(Integer.parseInt(index)).firstName, Toast.LENGTH_SHORT).show();

            Intent myIntent = new Intent(SearchActivity.this, InviteActivity.class);
            myIntent.putExtra("indexClicked", idx);
            startActivity(myIntent);

        }*/
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
//            Toast.makeText(this, "Searching by: "+ query, Toast.LENGTH_SHORT).show();

        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {

//            Toast.makeText(this, "Suggestion: "+ uri, Toast.LENGTH_SHORT).show();
            String uri = intent.getDataString();
            String index = uri.substring(uri.lastIndexOf('/') + 1);
            Integer idx = Integer.parseInt(index);
            Log.d("idxxx", "idx=" + idx);
//            Toast.makeText(this, "Suggestion: "+ UtilClass.registeredUsers.get(Integer.parseInt(index)).firstName, Toast.LENGTH_SHORT).show();

            Intent myIntent = new Intent(SearchActivity.this, InviteActivity.class);
            myIntent.putExtra("indexClicked", idx);
            finish();
            startActivity(myIntent);

        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // User pressed the search button
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // User changed the text
        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //do whatever you need for the hardware 'back' button
            finish();
            startActivity(new Intent(SearchActivity.this,SearchActivity.class));
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
