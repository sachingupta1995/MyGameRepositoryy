package com.accolite.loginapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Sachin Gupta on 8/25/2016.
 */
public class LoginActivity extends AppCompatActivity {
    EditText emailId,password;
    SQLiteDatabase mydatabase;
    Button login_button;
    Cursor resultSet;
    int screenSize;
    String nameofDatabase,nameofTable;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_floating_labels);
        Log.d("LoginActivity","Inside it ");

        emailId=(EditText)findViewById(R.id.email_id);
        password=(EditText)findViewById(R.id.pass_word);
        login_button=(Button)findViewById(R.id.loginId);
        detectScreenSize();
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nameofDatabase=getResources().getString(R.string.database_name);
                nameofTable=getResources().getString(R.string.table_name);
                mydatabase = openOrCreateDatabase(nameofDatabase, MODE_PRIVATE, null);
                mydatabase.execSQL("CREATE TABLE IF NOT EXISTS "+nameofTable+"(Email VARCHAR,Password VARCHAR,First VARCHAR,Last VARCHAR,Gender VARCHAR,DOB VARCHAR,Profession VARCHAR,Education VARCHAR);");
                resultSet = mydatabase.rawQuery("Select * from "+nameofTable, null);
                int fl = 0;
                if (resultSet != null && resultSet.moveToFirst()) {
                    while (!resultSet.isAfterLast()) {
                        String email_id = resultSet.getString(0);
                        String pass_word = resultSet.getString(1);
                        String em = emailId.getText().toString();
                        String pass = password.getText().toString();
                        if (email_id.equals(em) && pass_word.equals(pass)) {
//                            startActivity(new Intent(getApplicationContext(), WelcomeActivity1.class));
                            startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                            fl = 1;
                            finish();
                            break;
                        }
                        resultSet.moveToNext();//Moving to next index

                    }
                }
                resultSet.close();
                if (fl == 0)
                    Toast.makeText(getApplicationContext(), "Invalid Login Credentials", Toast.LENGTH_SHORT).show();


            }
        });
    }

    protected void detectScreenSize() {

        screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        int dimensionInDp,textSize;
        String toastMsg;
            switch (screenSize) {
                case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                    dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
                    login_button.setPadding(login_button.getPaddingLeft(),dimensionInDp,login_button.getPaddingRight(),dimensionInDp);
                    textSize=getResources().getInteger(R.integer.text_size);
                    login_button.setTextSize(TypedValue.COMPLEX_UNIT_DIP,textSize);
                    emailId.setTextSize(TypedValue.COMPLEX_UNIT_DIP,textSize);
                    password.setTextSize(TypedValue.COMPLEX_UNIT_DIP,textSize);
                    break;

            }

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
}
