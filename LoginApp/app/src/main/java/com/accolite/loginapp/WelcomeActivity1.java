package com.accolite.loginapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class WelcomeActivity1 extends AppCompatActivity implements View.OnClickListener {

    Button logout;
    TextView info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);
        Bundle bundle = getIntent().getExtras();
        ArrayList<String> dataObtained=bundle.getStringArrayList(getResources().getString(R.string.data_obtained));
        Log.d("DataObtained"," "+dataObtained);
        info=(TextView)findViewById(R.id.info);
        info.setText("First Name:"+dataObtained.get(0)+"\nLastName:"+dataObtained.get(1)+"\nEmailId:"+dataObtained.get(2)+"\nId:"+dataObtained.get(3));
        logout = (Button) findViewById(R.id.logout);
        detectScreenSize();
        logout.setOnClickListener(this);

    }
    protected void detectScreenSize() {

        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        int dimensionInDp;
        String toastMsg;
        switch (screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
                logout.setPadding(logout.getPaddingLeft(),dimensionInDp,logout.getPaddingRight(),dimensionInDp);
                logout.setTextSize(TypedValue.COMPLEX_UNIT_DIP,30);
                break;

        }

    }
    @Override
    public void onClick(View view) {

        Intent intent = new Intent(WelcomeActivity1.this, MainActivity.class);
        this.finish();
        startActivity(intent);


    }
}
