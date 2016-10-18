package com.accolite.loginapp;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    //SerializableClass obj;
    ArrayList<String> intentPass;
    String firstName, lastName, birthday, email;
    ImageView imageView;
    private static final int SIGN_IN_CODE = 0;
    ConnectionResult connection_result;
    GoogleApiClient googleApiClient;
    GoogleApiAvailability google_api_availability;
    CallbackManager callbackManager;
    EditText emailId, password;
    Button loginButton, registerButton;
    LoginButton facebookLogin;
    SignInButton googleLogin;
    SQLiteDatabase mydatabase;
    Cursor resultSet;
    private boolean is_intent_inprogress;
    private boolean is_signInBtn_clicked;
    private int request_code;
    int screenSize;
    ProgressDialog progress_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        facebookSDKInitialize();
        AppEventsLogger.activateApp(this);
        buidNewGoogleApiClient();
        setContentView(R.layout.front_page);

        //Done to resolve android.os.NetworkOnMainThreadException in case of postRequest
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        intentPass = new ArrayList<String>();
        imageView = (ImageView) findViewById(R.id.imageView1);
        emailId = (EditText) findViewById(R.id.editText2);
        password = (EditText) findViewById(R.id.editText);
        loginButton = (Button) findViewById(R.id.loginId);
        googleLogin = (SignInButton) findViewById(R.id.googleId);
        facebookLogin = (LoginButton) findViewById(R.id.fbId);
        registerButton = (Button) findViewById(R.id.registerId);
        List<String> permissions = new ArrayList<String>();
        permissions.add("email");
        facebookLogin.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends", "user_location"));
        getLoginDetails(facebookLogin);
        customizeGoogleSignBtn(googleLogin, "Signup with Google+");
        //printKeyHash();
        detectScreenSize();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }

        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("Register", "Inside the register Activity");
              startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
//                startActivity(new Intent(getApplicationContext(),SearchActivity.class));
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

    protected void detectScreenSize() {

        screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        int dimensionInDp;
        String toastMsg;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.d("Detect", "IT is portait");
            switch (screenSize) {
                case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                    dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 550, getResources().getDisplayMetrics());
                    imageView.getLayoutParams().height = dimensionInDp;
                    Log.d("ScreenSize is ", "XLarge");
                    dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, getResources().getDisplayMetrics());
                    facebookLogin.setPadding(facebookLogin.getPaddingLeft(), dimensionInDp, facebookLogin.getTotalPaddingRight(), dimensionInDp);
                    loginButton.setPadding(loginButton.getPaddingLeft(), dimensionInDp, loginButton.getPaddingRight(), dimensionInDp);
                    registerButton.setPadding(registerButton.getPaddingLeft(), dimensionInDp, registerButton.getPaddingRight(), dimensionInDp);
                    registerButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
                    facebookLogin.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
                    loginButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
                    toastMsg = "Large screen";
                    break;
                case Configuration.SCREENLAYOUT_SIZE_LARGE:
                    dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 500, getResources().getDisplayMetrics());
                    imageView.getLayoutParams().height = dimensionInDp;
                    Log.d("ScreenSize is ", "Large");
                    toastMsg = "Normal screen";
                    break;

                case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                    dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 190, getResources().getDisplayMetrics());
                    imageView.getLayoutParams().height = dimensionInDp;
                    Log.d("ScreenSize is ", "Normal");
                    toastMsg = "Normal screen";
                    break;
                case Configuration.SCREENLAYOUT_SIZE_SMALL:
                    dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
                    imageView.getLayoutParams().height = dimensionInDp;
                    Log.d("ScreenSize is ", "Small");
                    toastMsg = "Small screen";
                    break;
                default:
                    toastMsg = "Screen size is neither large, normal or small";
            }
        } else {
            Log.d("Detect", "IT is Landscape");
            switch (screenSize) {
                case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                    dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics());

                    facebookLogin.setPadding(facebookLogin.getPaddingLeft(), dimensionInDp, facebookLogin.getTotalPaddingRight(), dimensionInDp);
                    dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());
                    loginButton.setPadding(loginButton.getPaddingLeft(), dimensionInDp, loginButton.getPaddingRight(), dimensionInDp);

                    registerButton.setPadding(registerButton.getPaddingLeft(), dimensionInDp, registerButton.getPaddingRight(), dimensionInDp);
                    registerButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
                    facebookLogin.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
                    loginButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);

                    Log.d("ScreenSize is ", "XLarge");
                    toastMsg = "Large screen";
                    break;

                case Configuration.SCREENLAYOUT_SIZE_LARGE:
                    dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
                    imageView.getLayoutParams().height = dimensionInDp;
                    Log.d("ScreenSize is ", "Large");
                    toastMsg = "Normal screen";
                    break;
                /*
                xlarge (xhdpi): 640x960
                large (hdpi): 480x800
                medium (mdpi): 320x480
                small (ldpi): 240x320
                */
                case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                    Log.d("It is ", "Normal");
                    dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 140, getResources().getDisplayMetrics());
                    imageView.getLayoutParams().height = dimensionInDp;
                    Log.d("ScreenSize is ", "Normal");
                    toastMsg = "Normal screen";
                    break;
                case Configuration.SCREENLAYOUT_SIZE_SMALL:
                    dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
                    imageView.getLayoutParams().height = dimensionInDp;
                    Log.d("ScreenSize is ", "Small");
                    toastMsg = "Small screen";
                    break;
                default:
                    toastMsg = "Screen size is neither large, normal or small";
            }
        }
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
        // Callback registration
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult login_result) {
                Log.d("LoginSuccess", "Inside");
                System.out.println("User ID  : " + login_result.getAccessToken().getUserId());
                System.out.println("Authentication Token : " + login_result.getAccessToken().getToken());
                GraphRequest request = GraphRequest.newMeRequest(
                        login_result.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.d("onCompleted", response.toString());

                                Profile profile = Profile.getCurrentProfile();
                                if (profile != null) {
                                    String facebook_id = profile.getId();
                                    String f_name = profile.getFirstName();
                                    String l_name = profile.getLastName();
                                    Log.d("Info", "Using profile: " + facebook_id + " " + f_name + " " + l_name);
                                }
                                // Application code
                                try {


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
                                    try {
                                        intentPass = UtilClass.makePostRequest(getResources().getString(R.string.server_url1), jsonFormat);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Log.d("DataObtained", "Data is " + intentPass + " " + intentPass.size());
                                    if (intentPass.size() == 1) {
                                        Toast.makeText(getApplicationContext(), "Bad Request", Toast.LENGTH_LONG).show();
                                        LoginManager.getInstance().logOut();
                                    } else {
                                        Intent i = new Intent(getApplicationContext(), WelcomeActivity2.class);
                                        i.putExtra(getResources().getString(R.string.data_obtained), intentPass);
                                        finish();
                                        startActivity(i);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday,locale,first_name,last_name,link");//Add parameters here to get info
                //It is taken from link https://developers.facebook.com/docs/android/graph/
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // code for cancellation
                if (checkInternet() == false)
                    Toast.makeText(MainActivity.this, "Check the internet connection", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(MainActivity.this, "Login cancelled by user!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {


                Log.d("Throwing exception ", "And it is giving exception:" + exception);
                Toast.makeText(MainActivity.this, "Login unsuccessful! " + exception, Toast.LENGTH_LONG).show();
                //  code to handle error
            }
        });
    }


    /*
      Will receive the activity result and check which request we are responding to

     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        Log.d("OnActivityResult", "Yes inside the activity result " + is_signInBtn_clicked);
        if (resultCode != RESULT_OK)
            is_signInBtn_clicked = false;
        if (is_signInBtn_clicked) {
            Log.d("ButtonClicked", "Activity Result " + googleApiClient.isConnected());
            if (!googleApiClient.isConnecting()) {
                googleApiClient.connect();

            }

            Log.d("Calling next", "It is calling");

        } else
            callbackManager.onActivityResult(requestCode, resultCode, data);
        //Log.e(&quot;data&quot;,data.toString());
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
        System.out.print("I'm in start ");
        googleApiClient.connect();
        Log.d("OnStart ", "Yes it is ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
        Log.d("Onresume", "It is");
        if (googleApiClient.isConnected()) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("OnStop", "It is");
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
                Log.d("buttonclicked", "Button is clicked and google is connecting");
                googleApiClient.connect();
                progress_dialog = new ProgressDialog(this);
                progress_dialog.setMessage("Signing in....");
                resolveSignInError();
                //is_signInBtn_clicked = true;
                // progress_dialog.show();
                //resolveSignInError();
                //startActivity(new Intent(getApplicationContext(),WelcomeActivity1.class));
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

        //It is called when the user has signed in using google+ once so that it doesn;t have to login again
        Log.d("onConnected", "Inside onConnected " + UtilClass.connectionFlag);
        //progress_dialog.dismiss();
        if (googleApiClient.isConnected()) {
            boolean x = (UtilClass.connectionFlag == 0);

            Log.d("OnConnected", "Inside the is Connected ,Yes user is connected " + UtilClass.connectionFlag + " " + x);

            //Plus.AccountApi.clearDefaultAccount(googleApiClient);
            //This is done as this function is getting called in a loop once the user signs in
            //because the activity is getting finished every time getprofile is called.
            if (UtilClass.connectionFlag == 0) {
                UtilClass.connectionFlag++;
                Log.d("Values", " " + UtilClass.connectionFlag);
                getProfileInfo();

            }

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

        Log.d("OnConnectionSuspend ", "Hey this suspending connection");
        googleApiClient.connect();

    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {

        Log.d("OnConnectionFailed ", "Hey this is failing connection");
        if (!result.hasResolution()) {
            google_api_availability.getErrorDialog(this, result.getErrorCode(), request_code).show();
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
            Log.d("getProfileInfo", "Outside");

            //Here this was not getting resolvevd so I added implements ResultCallback<People.LoadPeopleResult>

            if (Plus.PeopleApi.getCurrentPerson(googleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(googleApiClient);
                Log.d("ProfileInside", "Person not null");
                setPersonalInfo(currentPerson);

            } else {
                Log.d("NoPersonal", "YEs no info");

            }

        } catch (Exception e) {
            Log.d("Exceptionnn", " " + e);
            e.printStackTrace();
        }

    }


    private void setPersonalInfo(Person currentPerson) throws IOException, JSONException {

        Log.d("PersonalInfo","SetPersonalInfo is shown");
        String personName = currentPerson.getDisplayName();
        String personPhotoUrl = currentPerson.getImage().getUrl();
        String fName = currentPerson.getName().getGivenName();
        String lName = currentPerson.getName().getFamilyName();
        String address = currentPerson.getCurrentLocation();
        String phone = "8888899999";
        String email = Plus.AccountApi.getAccountName(googleApiClient);
        String pass_word = "12345678";
        Log.d("PersonInformation", "email=" + email + " personName=" + fName + " " + lName + " " + " " + address + " " + phone + " " + email + " " + pass_word);
        String jsonFormat = UtilClass.createJson(fName, lName, address, phone, email, pass_word);

        try {
            intentPass = UtilClass.makePostRequest(getResources().getString(R.string.server_url1), jsonFormat);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("DataObtained", "Data is " + intentPass);
        if (intentPass.size() == 1) {
            Toast.makeText(getApplicationContext(), "Bad Request", Toast.LENGTH_LONG).show();
            Plus.AccountApi.clearDefaultAccount(googleApiClient);
            UtilClass.connectionFlag=0;
            googleApiClient.disconnect();

        } else {

            Intent i = new Intent(getApplicationContext(), WelcomeActivity.class);
            i.putExtra(getResources().getString(R.string.data_obtained), intentPass);
            finish();
            startActivity(i);
        }
    }

    //Method to resolve any signin errors
    private void resolveSignInError() {
        if (connection_result.hasResolution()) {
            try {
                is_intent_inprogress = true;
                connection_result.startResolutionForResult(this, SIGN_IN_CODE);
                Log.d("resolve error", "sign in error resolved");

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

        Log.d("Get Child count ", "Count is " + signInButton.getChildCount());

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
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        int dimensionDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, getResources().getDisplayMetrics());
                        tv.setPadding(5, dimensionDp, tv.getPaddingRight(), dimensionDp);
                    } else {
                        int dimensionDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics());
                        tv.setPadding(googleLogin.getPaddingLeft(), dimensionDp, googleLogin.getPaddingRight(), dimensionDp);


                    }
                } else
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                tv.setTypeface(null, Typeface.NORMAL);
                tv.setText(buttonText);


                tv.setPadding(5, tv.getPaddingTop(), tv.getPaddingRight(), tv.getPaddingBottom());
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


    // Code to print out the key hash for facebook app
    private void printKeyHash() {

        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.accolite.loginapp", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("KeyHash:", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("KeyHash:", e.toString());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //do whatever you need for the hardware 'back' button
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure you want to quit?");

            alertDialogBuilder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    //Toast.makeText(MainActivity.this,"You clicked yes button",Toast.LENGTH_LONG).show();

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
