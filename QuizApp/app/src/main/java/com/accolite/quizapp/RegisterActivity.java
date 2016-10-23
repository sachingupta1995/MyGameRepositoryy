package com.accolite.quizapp;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.DateFormat;
import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Button registerButton;//, setDob;
    EditText email, password, confirm_password, profession, phoneNumber, dateOfBirth,first_name,last_name;
    String emailId, password1, password2, gen, prof, edu, dob,firstName,lastName;
    Spinner gender;
    int validDate;
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    DateFormat format;
    TextInputLayout ti1,ti2,ti3,ti4,ti5,ti6,ti7;
    int screenSize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.registration_page_floating_labels);

        //Done to resolve android.os.NetworkOnMainThreadException in case of postRequest
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
       /* String nameOfDatabase=getResources().getString(R.string.database_name);
        final String nameofTable=getResources().getString(R.string.table_name);
        final SQLiteDatabase mydatabase = openOrCreateDatabase(nameOfDatabase, MODE_PRIVATE, null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS "+nameofTable+"(Email VARCHAR,Password VARCHAR,First VARCHAR,Last VARCHAR,Gender VARCHAR,DOB VARCHAR,Profession VARCHAR,Education VARCHAR);");
        //mydatabase.execSQL("CREATE TABLE IF NOT EXISTS DemoTable(Email VARCHAR,Password VARCHAR,Status INT);");
        registerButton = (Button) findViewById(R.id.registrationButton);
        format = new SimpleDateFormat(getResources().getString(R.string.dobHint));
        email = (EditText) findViewById(R.id.email_id);
        password = (EditText) findViewById(R.id.pass_word);
        confirm_password = (EditText) findViewById(R.id.confirm_password);
        gender = (Spinner) findViewById(R.id.gender);
        profession = (EditText) findViewById(R.id.profession);
        dateOfBirth = (EditText) findViewById(R.id.dateOfBirth);
        phoneNumber = (EditText) findViewById(R.id.phone_number);
        first_name=(EditText)findViewById(R.id.first_name);
        last_name=(EditText)findViewById(R.id.last_name);
        Log.d("Inside","Regsiter");
        detectScreenSize();

        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {

                Log.d("ITgets1","Yes");
                screenSize = getResources().getConfiguration().screenLayout &
                        Configuration.SCREENLAYOUT_SIZE_MASK;
                int dimensionInDp;
                String toastMsg;
                switch (screenSize) {
                    case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                        ((TextView) parent.getChildAt(0)).setTextSize(TypedValue.COMPLEX_UNIT_DIP,22);

                        break;
                    case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                        Log.d("ITgets","Yes");
                        ((TextView) parent.getChildAt(0)).setTextSize(TypedValue.COMPLEX_UNIT_DIP,17);

                        break;
                    case Configuration.SCREENLAYOUT_SIZE_SMALL:
                        ((TextView) parent.getChildAt(0)).setTextSize(15);

                        break;
                    default:
                        toastMsg = "Screen size is neither large, normal or small";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                emailId = email.getText().toString();
                password1 = password.getText().toString();
                password2 = confirm_password.getText().toString();
                gen = String.valueOf(gender.getSelectedItem());
                prof = profession.getText().toString();
                edu = phoneNumber.getText().toString();
                dob = dateOfBirth.getText().toString();
                firstName = first_name.getText().toString();
                lastName = last_name.getText().toString();
                validDate = 0;
                try {
                    format.setLenient(false);
                    format.parse(dob);
                    validDate = 1;
                } catch (ParseException e) {
                    validDate = 0;
                }
                Log.d("Date is ", dob);

                if (emailId.matches(emailPattern)) {
                    if (validDate == 1) {
                        if(phoneNumber.length()==10) {
                            if (password1.length() >= 8) {
                                if (password1.equals(password2)) {

                                    //Making Post Request
                                    Random r = new Random();
                                    String phoneNum = "6666698991";

                                    ArrayList<String> dataTobePassed = new ArrayList<String>();
                                    String jsonFormat = UtilClass.createJson(firstName, lastName, prof, edu, emailId, password1);
                                    try {
                                        dataTobePassed = UtilClass.makePostRequest(getResources().getString(R.string.server_url1), jsonFormat);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Log.d("DataTobePassed", "Data is " + dataTobePassed);

                                    mydatabase.execSQL("INSERT INTO " + nameofTable + " VALUES('" + emailId + "','" + password1 + "','" + firstName + "','" + lastName + "','" + gen + "','" + dob + "','" + prof + "','" + edu + "');");
                                    if (dataTobePassed.size() == 1) {
                                        Toast.makeText(getApplicationContext(), "Server Not found", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                        finish();
                                        startActivity(i);

                                    } else {
                                        Utils.userName = firstName + lastName;
                                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
//                                    i.putExtra(getResources().getString(R.string.data_obtained), dataTobePassed);
                                        finish();
                                        startActivity(i);

                                    }

                                } else {
                                    confirm_password.setSelectAllOnFocus(true);
                                    Toast.makeText(getApplicationContext(), "Passwords don't match ", Toast.LENGTH_SHORT).show();
                                    confirm_password.setText("");
                                }
                            } else {
                                password.setSelectAllOnFocus(true);
                                Toast.makeText(getApplicationContext(), "Password should be of minimum 8 characters", Toast.LENGTH_SHORT).show();
                                password.setText("");
                                confirm_password.setText("");
                            }
                        }
                        else
                        {
                            phoneNumber.setSelectAllOnFocus(true);
                            Toast.makeText(getApplicationContext(), "Enter Valid PhoneNumber", Toast.LENGTH_SHORT).show();
                            phoneNumber.setText("");
                        }
                    } else {
                        dateOfBirth.setSelectAllOnFocus(true);
                        Toast.makeText(getApplicationContext(), "Enter Valid Date", Toast.LENGTH_SHORT).show();
                        dateOfBirth.setText("");
                    }
                } else {
                    email.setSelectAllOnFocus(true);
                    Toast.makeText(getApplicationContext(), "Enter valid Email Address", Toast.LENGTH_SHORT).show();
                    email.setText("");
                }

            }

        });


        dateOfBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dateOfBirth.setHint(getResources().getString(R.string.dobHint));
                    setDate(v);
                    Log.d("value of dateof Birth", "Outside");
                    if (dateOfBirth.getText().toString().equals("")) {
                        Log.d("value of dateof Birth", "Inside");
                        setDate(v);
                    }

                } else {
                    //Toast.makeText(getApplicationContext(), "lost the focus", Toast.LENGTH_LONG).show();
                    dateOfBirth.setHint("Date of Birth");
                }
            }
        });

    }

    protected void detectScreenSize() {

        screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        int dimensionInDp;
        String toastMsg;
        Log.d("Itisss","Large"+screenSize);
        switch (screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                Log.d("Itiss","Large");
                dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
                registerButton.setPadding(registerButton.getPaddingLeft(),dimensionInDp,registerButton.getPaddingRight(),dimensionInDp);
                registerButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP,30);
                email.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                phoneNumber.setTextSize(TypedValue.COMPLEX_UNIT_DIP,30);
                profession.setTextSize(TypedValue.COMPLEX_UNIT_DIP,30);
                confirm_password.setTextSize(TypedValue.COMPLEX_UNIT_DIP,30);
                first_name.setTextSize(TypedValue.COMPLEX_UNIT_DIP,30);
                password.setTextSize(TypedValue.COMPLEX_UNIT_DIP,30);
                last_name.setTextSize(TypedValue.COMPLEX_UNIT_DIP,30);

                break;

        }

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) { *//* do nothing *//* }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //do whatever you need for the hardware 'back' button
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            showDate(arg1, arg2 + 1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        StringBuilder buil = new StringBuilder();
        if (day >= 1 && day <= 9) {
            buil.append("0").append(day).append("/");
        } else
            buil.append(day).append("/");
        if (month >= 1 && month <= 9) {
            buil.append("0").append(month).append("/");
        } else
            buil.append(month).append("/");
        buil.append(year);
        dateOfBirth.setText(buil);

    }
*/

    }
}
