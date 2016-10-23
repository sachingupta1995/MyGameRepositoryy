package com.accolite.quizapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.accolite.quizapp.activity.LoginActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    ArrayList<String> intentPass;
    ImageView imageView;
    private static final int SIGN_IN_CODE = 0;
    ConnectionResult connection_result;
    GoogleApiClient googleApiClient;
    GoogleApiAvailability google_api_availability;
    CallbackManager callbackManager;
    Button loginButton, registerButton;
    LoginButton facebookLogin;
    SignInButton googleLogin;
    private boolean is_intent_inprogress;
    private boolean is_signInBtn_clicked;
    int screenSize;
    ProgressDialog progress_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        facebookSDKInitialize();
        AppEventsLogger.activateApp(this);
        buidNewGoogleApiClient();
        setContentView(R.layout.activity_main);
         if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        intentPass = new ArrayList<String>();
        imageView = (ImageView) findViewById(R.id.imageView1);
        loginButton = (Button) findViewById(R.id.loginId);
        googleLogin = (SignInButton) findViewById(R.id.googleId);
        facebookLogin = (LoginButton) findViewById(R.id.fbId);
        registerButton = (Button) findViewById(R.id.registerId);
        List<String> permissions = new ArrayList<String>();
        permissions.add("email");
        facebookLogin.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends", "user_location"));
        getLoginDetails(facebookLogin);
        customizeGoogleSignBtn(googleLogin,getResources().getString(R.string.google_plus_text));

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

        googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkInternet() == false)
                    Toast.makeText(MainActivity.this, "Check the internet connection", Toast.LENGTH_LONG).show();
                else {
                    is_signInBtn_clicked = true;
                    gPlusSignIn();
                }
            }
        });
    }

    /*
 Initialize the facebook sdk.
 And then callback manager will handle the login responses.
*/
    protected void facebookSDKInitialize() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }

    /*
     Register a callback function with LoginButton to respond to the login result.
    */
    protected void getLoginDetails(final LoginButton login_button) {
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult login_result) {
                GraphRequest request = GraphRequest.newMeRequest(
                        login_result.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {


                                    Log.d("ObjectIs",object.toString());
                                    String birthday, email = null;
                                    String fName = object.getString("first_name");
                                    String lName = object.getString("last_name");
                                    String address = object.getString("locale");
                                    String phone = "9999988888";
                                    if (object.has("email"))
                                        email = object.getString("email");
                                    else
                                        email = "sachin.1996@gmail.com";
                                    String pass_word = "12345678";
                                    if (object.has("birthday"))
                                        birthday = object.getString("birthday");
                                    String gender_user = object.getString("gender");
                                    String jsonFormat = UtilClass.createJson(fName, lName, "Haryana", phone, email, pass_word);
//                                    try {
//                                        intentPass = UtilClass.makePostRequest(getResources().getString(R.string.server_url1), jsonFormat);
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
                                    if (intentPass.size() == 1) {
                                        Toast.makeText(getApplicationContext(), "Bad Request", Toast.LENGTH_LONG).show();
                                        LoginManager.getInstance().logOut();
                                    } else {
//                                        Intent i = new Intent(getApplicationContext(), WelcomeActivity2.class);
//                                        i.putExtra(getResources().getString(R.string.data_obtained), intentPass);
//                                        finish();
//                                        startActivity(i);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday,locale,first_name,last_name,link");//Add parameters here to get info
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                if (checkInternet() == false)
                    Toast.makeText(MainActivity.this, "Check the internet connection", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(MainActivity.this, "Login cancelled by user!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {


                Log.d("Throwing exception ", "And it is giving exception:" + exception);
                Toast.makeText(MainActivity.this, "Login unsuccessful! " + exception, Toast.LENGTH_LONG).show();
            }
        });
    }


    /*
      Will receive the activity result and check which request we are responding to

     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            is_signInBtn_clicked = false;
        if (is_signInBtn_clicked) {
            if (!googleApiClient.isConnecting()) {
                googleApiClient.connect();

            }
        } else
            callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
        if (googleApiClient.isConnected()) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }


    /*
      Sign-in into the Google + account
     */
    private void gPlusSignIn() {

        if (checkInternet() == false)
            Toast.makeText(MainActivity.this, "Check the internet connection", Toast.LENGTH_LONG).show();
        else {
            if (!googleApiClient.isConnecting()) {
                googleApiClient.connect();
                progress_dialog = new ProgressDialog(this);
                progress_dialog.setMessage("Signing in....");
                resolveSignInError();
            }
        }

    }


    /*
    create and  initialize GoogleApiClient object to use Google Plus Api.
    While initializing the GoogleApiClient object, request the Plus.SCOPE_PLUS_LOGIN scope.
    */
    private void buidNewGoogleApiClient() {

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .build();
//        googleApiClient.connect();
    }


    @Override
    public void onConnected(Bundle bundle) {

        if (googleApiClient.isConnected()) {
            boolean x = (UtilClass.connectionFlag == 0);

            //Plus.AccountApi.clearDefaultAccount(googleApiClient);
            //This is done as this function is getting called in a loop once the user signs in
            //because the activity is getting finished every time getprofile is called.
            if (UtilClass.connectionFlag == 0) {
                UtilClass.connectionFlag++;
                getProfileInfo();

            }

        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();

    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            return;
        }

        if (!is_intent_inprogress) {
            connection_result = result;
            if (is_signInBtn_clicked)
                resolveSignInError();
        }
    }



    private void getProfileInfo() {

        try {
            if (Plus.PeopleApi.getCurrentPerson(googleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(googleApiClient);
                setPersonalInfo(currentPerson);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void setPersonalInfo(Person currentPerson) throws IOException, JSONException {

        String personName = currentPerson.getDisplayName();
        String personPhotoUrl = currentPerson.getImage().getUrl();
        String fName = currentPerson.getName().getGivenName();
        String lName = currentPerson.getName().getFamilyName();
        String address = currentPerson.getCurrentLocation();
        String phone = "8888899999";
        String email = Plus.AccountApi.getAccountName(googleApiClient);
        String pass_word = "12345678";
        String jsonFormat = UtilClass.createJson(fName, lName, address, phone, email, pass_word);

        try {
            intentPass = UtilClass.makePostRequest(getResources().getString(R.string.server_url), jsonFormat);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (intentPass.size() == 1) {
            Toast.makeText(getApplicationContext(), "Bad Request", Toast.LENGTH_LONG).show();
            Plus.AccountApi.clearDefaultAccount(googleApiClient);
            UtilClass.connectionFlag=0;
            googleApiClient.disconnect();

        } else {

//            Intent i = new Intent(getApplicationContext(), WelcomeActivity.class);
//            i.putExtra(getResources().getString(R.string.data_obtained), intentPass);
//            finish();
//            startActivity(i);
        }
    }

    //Method to resolve any signin errors
    private void resolveSignInError() {
        if (connection_result.hasResolution()) {
            try {
                is_intent_inprogress = true;
                connection_result.startResolutionForResult(this, SIGN_IN_CODE);
            } catch (IntentSender.SendIntentException e) {
                is_intent_inprogress = false;
                googleApiClient.connect();
            }
        }
    }

    /*
      Customize sign-in button. The sign-in button can be displayed in
      multiple sizes and color schemes. It can also be contextually
      rendered based on the requested scopes. For example. a red button may
      be displayed when Google+ scopes are requested, but a white button
      may be displayed when only basic profile is requested. Try adding the
      Plus.SCOPE_PLUS_LOGIN scope to see the  difference.
    */
    private void customizeGoogleSignBtn(SignInButton signInButton, String buttonText) {

        googleLogin.setSize(SignInButton.SIZE_STANDARD);
        googleLogin.setScopes(new Scope[]{Plus.SCOPE_PLUS_LOGIN});

        int sc = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);
            if (v instanceof Button) {
                Button b = (Button) v;
                b.setBackgroundResource(R.drawable.oval_background1);
            }
            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                int screenDetect = getResources().getConfiguration().screenLayout &
                        Configuration.SCREENLAYOUT_SIZE_MASK;
                if (sc == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getInteger(R.integer.integer4));
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        int dimensionDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, getResources().getInteger(R.integer.integer5), getResources().getDisplayMetrics());
                        tv.setPadding(getResources().getInteger(R.integer.integer1), dimensionDp, tv.getPaddingRight(), dimensionDp);
                    } else {
                        int dimensionDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, getResources().getInteger(R.integer.integer3), getResources().getDisplayMetrics());
                        tv.setPadding(googleLogin.getPaddingLeft(), dimensionDp, googleLogin.getPaddingRight(), dimensionDp);
                    }
                } else
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getInteger(R.integer.integer2));
                tv.setTypeface(null, Typeface.NORMAL);
                tv.setText(buttonText);


                tv.setPadding(getResources().getInteger(R.integer.integer1), tv.getPaddingTop(), tv.getPaddingRight(), tv.getPaddingBottom());
                return;
            }
        }
    }

    //Check Internet Connection
    private boolean checkInternet() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (!(activeNetworkInfo != null && activeNetworkInfo.isConnected()))
            return false;
        return true;

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure you want to quit?");

            alertDialogBuilder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                }
            });

            alertDialogBuilder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startMain);
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
