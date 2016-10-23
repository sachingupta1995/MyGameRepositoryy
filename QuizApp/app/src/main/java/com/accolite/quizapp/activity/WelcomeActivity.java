package com.accolite.quizapp.activity;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.accolite.quizapp.R;

public class WelcomeActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    Button pendingRequestButton, gameScreenButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        pendingRequestButton = (Button) findViewById(R.id.pending_request_button);
        gameScreenButton = (Button) findViewById(R.id.play_game);

        pendingRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WelcomeActivity.this, WelcomeActivity.class));
            }
        });

        gameScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(WelcomeActivity.this, PlayOptionsActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.search_menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(
                new ComponentName(this, WelcomeActivity.class)));
        searchView.setIconifiedByDefault(false);
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {

        //TODO
        super.onNewIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {

            String uri = intent.getDataString();
            String index = uri.substring(uri.lastIndexOf('/') + 1);
            Integer idx = Integer.parseInt(index);

/*            Intent myIntent = new Intent(SearchActivity.this, InviteActivity.class);
            myIntent.putExtra("indexClicked", idx);
            finish();
            startActivity(myIntent);*/

        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        //TODO
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        //TODO
        return false;
    }

}
