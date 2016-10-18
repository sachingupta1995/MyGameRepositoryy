package com.accolite.loginapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Sachin Gupta on 9/11/2016.
 */
public class UtilClass {
    public static int connectionFlag = 0;
    public static ArrayList<ApiDatabase.ApiClass> registeredUsers;
    public static String userId;
    public static String serverlUrl="http://23.22.96.227/api/users";
    public static String acceptChallengeUrl="http://23.22.96.227/api/quiz/acceptChallenge";
    /*
    public static HttpResponse postRequest(String userFirstName, String userLastName, String userAddress, String userPhone, String userEmail, String userPassword) {

        //String path="http://localhost:8080/api/users";
        //This is done to resolve org.apache.http.conn.HttpHostConnectException: Connection to http://localhost:8080 refused
        String path = "http://10.10.10.31:8080/api/users";

        DefaultHttpClient httpclient = new DefaultHttpClient();

        //url with the post data
        HttpPost httpost = new HttpPost(path);


        String jsonFormat = "{\n" +
                "    \"firstName\":" + "\"" + userFirstName + "\"" + ",\n" +
                "    \"lastName\":" + "\"" + userLastName + "\"" + ",\n" +
                "    \"address\":" + "\"" + userAddress + "\"" + ",\n" +
                "    \"phoneNumber\":" + "\"" + userPhone + "\"" + ",\n" +
                "    \"email\":" + "\"" + userEmail + "\"" + ",\n" +
                "    \"password\":" + "\"" + userPassword + "\"" + "\n" +
                "}";
        //passes the results to a string builder/entity
        StringEntity se = null;
        try {
            se = new StringEntity(jsonFormat);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //sets the post request as the resulting string
        httpost.setEntity(se);
        //sets a request header so the page receving the request
        //will know what to do with it
        //httpost.setHeader("Accept", "application/json");
        httpost.setHeader("Content-type", "application/json");
        httpost.setHeader("Authorization", "Basic ZGV4dGVyQG1vcmdhbi5jb206cGFzc3dvcmQ=");
        //Handles what is returned from the page
        //ResponseHandler responseHandler = new BasicResponseHandler();
        HttpResponse res = null;
        String json = null;
        try {
            res = httpclient.execute(httpost);

            Log.d("GettingResponse", "The response is " + res);
            BufferedReader reader = new BufferedReader(new InputStreamReader(res.getEntity().getContent(), "UTF-8"));
            json = reader.readLine();
            Log.d("GettingJson", "The json is " + json);


        } catch (IOException e) {
            Log.d("Exceptionn", " " + e);
            e.printStackTrace();
        }

        try {
            JSONObject json1 = new JSONObject(json);
            Log.d("Gettingjson1", "The json1 is " + json1);
            if (json1 != null) {
                String id = json1.getString("id");
                Log.d("GettingId", "The id is " + id);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;

    }

    public static void anotherPostRequest(String userFirstName, String userLastName, String userAddress, String userPhone, String userEmail, String userPassword) throws IOException {

        Log.d("Calling", "Inside another PostRequest");
        StringBuilder urlString = new StringBuilder("http://10.10.10.31:8080/api/users");
        Log.d("Calling1", "Inside another PostRequest");

        URL url = new URL(urlString.toString());
        Log.d("Calling2", "Inside another PostRequest");


        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        Log.d("Calling3", "Inside another PostRequest");
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Basic ZGV4dGVyQG1vcmdhbi5jb206cGFzc3dvcmQ=");
        connection.setRequestProperty("Content-type", "application/json");
        OutputStream os = connection.getOutputStream();
        *//*String userFirstName = "sachin";
        String userLastName = "gupta";
        String userAddress = "asd";
        String userPhone = "0987654329";
        String userEmail = "aaa@gmail.com";
        String userPassword = "22345678";*//*
        String input = "{\n" +
                "    \"firstName\":" + "\"" + userFirstName + "\"" + ",\n" +
                "    \"lastName\":" + "\"" + userLastName + "\"" + ",\n" +
                "    \"address\":" + "\"" + userAddress + "\"" + ",\n" +
                "    \"phoneNumber\":" + "\"" + userPhone + "\"" + ",\n" +
                "    \"email\":" + "\"" + userEmail + "\"" + ",\n" +
                "    \"password\":" + "\"" + userPassword + "\"" + "\n" +
                "}";
        os.write(input.getBytes());
        os.flush();
        int responseCode = connection.getResponseCode();
        Log.d("responseCode", "response is " + responseCode);
        Log.d("ResponseCodeIs", "responseCode is " + responseCode + " " + HttpURLConnection.HTTP_OK);
        if (!(responseCode == 201)) {
            //Toast.makeText(getApplicationContext(), "Server Not found", Toast.LENGTH_LONG).show();
            System.out.println("ServerNotfound");
        }
        else
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (connection.getInputStream())));

            String output = "", result = "";
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
                result = result + output;
            }
            try {
                JSONObject json = new JSONObject(result);
                String id = json.getString("id");
                Log.d("GettingId", "Id=" + id);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }*/
    public static ArrayList<String> makePostRequest(String serverUrl,String jsonFormat) throws IOException {

        ArrayList<String> arrayList=new ArrayList<String>();
        Log.d("Calling", "Inside another PostRequest");
        StringBuilder urlString = new StringBuilder(serverUrl);
        URL url = new URL(urlString.toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Basic cnlhbkBleGFtcGxlLmNvbTpwYXNzd29yZA==");
        connection.setRequestProperty("Content-type", "application/json");
        OutputStream os = connection.getOutputStream();


        os.write(jsonFormat.getBytes());
        os.flush();
        int responseCode = connection.getResponseCode();
        Log.d("responseCode", "response is " + responseCode);
        Log.d("ResponseCodeIs", "responseCode is " + responseCode + " " + HttpURLConnection.HTTP_OK);
        if (!(responseCode == 201)) {
            //Toast.makeText(getApplicationContext(), "Server Not found", Toast.LENGTH_LONG).show();
            System.out.println("ServerNotfound");
            arrayList.add("ServerNotfound");
        }
        else
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (connection.getInputStream())));

            String output = "", result = "";
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
                result = result + output;
            }
            Log.d("The result of post is ",""+result);
            try {
                JSONObject json1 = new JSONObject(result);
                JSONObject json=json1.getJSONObject("user");
                String firstName = json.getString(ApiDatabase.firstName);
                String lastName=json.getString(ApiDatabase.lastName);
                String emailId=json.getString(ApiDatabase.email_id);
                String id=json.getString(ApiDatabase.user_id);
                UtilClass.userId=id;
                arrayList.add(firstName);
                arrayList.add(lastName);
                arrayList.add(emailId);
                arrayList.add(id);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return arrayList;



    }

    public static String createJson(String userFirstName, String userLastName, String userAddress, String userPhone, String userEmail, String userPassword)
    {

        String jsonFormat = "{\n" +
                "    \"firstName\":" + "\"" + userFirstName + "\"" + ",\n" +
                "    \"lastName\":" + "\"" + userLastName + "\"" + ",\n" +
                "    \"address\":" + "\"" + userAddress + "\"" + ",\n" +
                "    \"phoneNumber\":" + "\"" + userPhone + "\"" + ",\n" +
                "    \"email\":" + "\"" + userEmail + "\"" + ",\n" +
                "    \"password\":" + "\"" + userPassword + "\"" + "\n" +
                "}";

        return jsonFormat;

    }
    public static void makeGetRequest(String serverUrl) throws IOException {

        UtilClass.registeredUsers =new ArrayList<ApiDatabase.ApiClass>();
        Log.d("Calling", "Inside another GetRequest");
        StringBuilder urlString = new StringBuilder(serverUrl);
        URL url = new URL(urlString.toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Basic cnlhbkBleGFtcGxlLmNvbTpwYXNzd29yZA==");
        connection.setRequestProperty("Content-type", "application/json");


        int responseCode = connection.getResponseCode();
        Log.d("responseCode", "response is " + responseCode);
        Log.d("ResponseCodeIs", "responseCode is " + responseCode + " " + HttpURLConnection.HTTP_OK);
        if (!(responseCode == 200)) {
            //Toast.makeText(getApplicationContext(), "Server Not found", Toast.LENGTH_LONG).show();
            System.out.println("ServerNotfound");

        }
        else {
            BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));

            String output = "", result = "";
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
                result = result + output;
            }
            Log.d("GetRequest", "Result is " + result);
            try {
                JSONArray json_array = new JSONArray(result);
                Log.d("jsonArrayValue", "Value is " + json_array);

                for (int i = 0; i < json_array.length(); i++) {
                    JSONObject json = json_array.getJSONObject(i);
                    Log.d("jsonArrayValue", "Value of json is " + json);

                    ApiDatabase obj1 = new ApiDatabase();
                    ApiDatabase.ApiClass obj = obj1.new ApiClass();

                    obj.firstName = json.getString(ApiDatabase.firstName);
                    obj.lastName = json.getString(ApiDatabase.lastName);
                    obj.email = json.getString(ApiDatabase.email_id);
                    obj.id = json.getString(ApiDatabase.user_id);
                    Log.d("ObjValueis", " " + obj);
                    UtilClass.registeredUsers.add(obj);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }





    }

    public static String FriendRequest(String serverUrl, String jsonFormat) throws IOException {

        Log.d("Calling", "Inside another PostRequest");
        StringBuilder urlString = new StringBuilder(serverUrl);
        URL url = new URL(urlString.toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Basic cnlhbkBleGFtcGxlLmNvbTpwYXNzd29yZA==");
        connection.setRequestProperty("Content-type", "application/json");
        OutputStream os = connection.getOutputStream();
        os.write(jsonFormat.getBytes());
        os.flush();
        int responseCode = connection.getResponseCode();
        Log.d("responseCode", "response is " + responseCode);
        Log.d("ResponseCodeIs", "responseCode is " + responseCode + " " + HttpURLConnection.HTTP_OK);
        if (!(responseCode == 200)) {
            //Toast.makeText(getApplicationContext(), "Server Not found", Toast.LENGTH_LONG).show();
            return "ServerNotfound";
        }
        else
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (connection.getInputStream())));

            String output = "", result = "";
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
                result = result + output;
            }
            Log.d("FriendRequest","Result is "+result);
            return "true";
        }
    }


    public static ArrayList<ApiDatabase.ApiClass> getPendingRequest(String serverUrl) throws IOException {

        ArrayList<ApiDatabase.ApiClass> arrayList=new ArrayList<ApiDatabase.ApiClass>();
        Log.d("Calling", "Inside another PostRequest");
        StringBuilder urlString = new StringBuilder(serverUrl);
        URL url = new URL(urlString.toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Basic cnlhbkBleGFtcGxlLmNvbTpwYXNzd29yZA==");
        connection.setRequestProperty("Content-type", "application/json");
        int responseCode = connection.getResponseCode();
        Log.d("responseCode", "response is " + responseCode);
        Log.d("ResponseCodeIs", "responseCode is " + responseCode + " " + HttpURLConnection.HTTP_OK);
        if (!(responseCode == 200)) {
            //Toast.makeText(getApplicationContext(), "Server Not found", Toast.LENGTH_LONG).show();
            System.out.println("ServerNotfound");
//            arrayList.add("ServerNotfound");

            ApiDatabase obj1 = new ApiDatabase();
            ApiDatabase.ApiClass obj = obj1.new ApiClass();
            obj.firstName = "BadRequest";
            obj.lastName ="BadRequest";
            obj.email ="BadRequest";
            obj.id = "BadRequest";
            arrayList.add(obj);
        }
        else
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (connection.getInputStream())));

            String output = "", result = "";
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
                result = result + output;
            }
            try {
                JSONArray json_array = new JSONArray(result);
                for (int i = 0; i < json_array.length(); i++) {
                    JSONObject json = json_array.getJSONObject(i);
                    Log.d("jsonArrayValue", "Value of json is " + json);

                    ApiDatabase obj1 = new ApiDatabase();
                    ApiDatabase.ApiClass obj = obj1.new ApiClass();

                    obj.firstName = json.getString(ApiDatabase.firstName);
                    obj.lastName = json.getString(ApiDatabase.lastName);
                    obj.email = json.getString(ApiDatabase.email_id);
                    obj.id = json.getString(ApiDatabase.user_id);
                    Log.d("ObjValueis", " " + obj);
                    arrayList.add(obj);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return arrayList;



    }

}
