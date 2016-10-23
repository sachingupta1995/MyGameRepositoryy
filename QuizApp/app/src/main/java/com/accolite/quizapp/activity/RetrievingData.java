package com.accolite.quizapp.activity;

import android.util.Log;

import com.accolite.quizapp.model.Question;
import com.accolite.quizapp.model.Quiz;
import com.accolite.quizapp.model.ResponseClass;
import com.accolite.quizapp.model.User;
import com.google.gson.Gson;

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
import java.util.List;

;

/**
 * Created by Sachin Gupta on 9/28/2016.
 */
public class RetrievingData {
    public static ArrayList<String> makePostRequest(String serverUrl, String jsonFormat) throws IOException {

        ArrayList<String> arrayList = new ArrayList<String>();
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
        } else {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (connection.getInputStream())));

            String output = "", result = "";
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
                result = result + output;
            }
            Log.d("The result of post is ", "" + result);
            try {
                JSONObject json1 = new JSONObject(result);
                JSONObject json = json1.getJSONObject("user");


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return arrayList;
    }

    public static ResponseClass makeGetRequest(String serverUrl) throws IOException {

        ResponseClass res = new ResponseClass();
        Gson gson = new Gson();
        Log.d("Calling", "Inside another GetRequest");
        StringBuilder urlString = new StringBuilder(serverUrl);
        URL url = new URL(urlString.toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Basic cnlhbkBleGFtcGxlLmNvbTpwYXNzd29yZA==");
        connection.setRequestProperty("Content-type", "application/json");

        Log.d("BeforeResponse", "Response Get");
        int responseCode = connection.getResponseCode();
        Log.d("responseCode", "response is " + responseCode);
        Log.d("ResponseCodeIs", "responseCode is " + responseCode + " " + HttpURLConnection.HTTP_OK);
        if (!(responseCode == 200)) {
            //Toast.makeText(getApplicationContext(), "Server Not found", Toast.LENGTH_LONG).show();
            System.out.println("ServerNotfound");

        } else {
            BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));

            String output = "", result = "";
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
                result = result + output;
            }
            List<Question> questionList = new ArrayList<Question>();
            List<String> correctOptions = new ArrayList<String>();
            Log.d("GetRequest", "Result is " + result);
            try {

                JSONObject jsonObject = new JSONObject(result);
                Quiz obj = gson.fromJson(String.valueOf(jsonObject), Quiz.class);
                res.quiz = obj;
                questionList = obj.getQuestions();
                for (int i = 0; i < questionList.size(); i++) {

                    for (int j = 0; j < 4; j++) {

                        if (questionList.get(i).getOptions().get(j).getIsCorrect() == 1)
                            correctOptions.add(questionList.get(i).getOptions().get(j).getOptionText());
                    }
                }

                res.questionList = questionList;
                res.correcOptions = correctOptions;

                /*JSONArray jsonArray = jsonObject.getJSONArray("question");
                for (int i = 0; i < jsonArray.length(); i++) {
                    {
                        ResponseClass obj=new ResponseClass();
                        Question q = gson.fromJson(jsonArray.getJSONObject(i).getString("question"), Question.class);
                        JSONArray jsonArray1=jsonArray.getJSONObject(i).getJSONArray("options");
                        ArrayList<Options> optionsArrayList=new ArrayList<Options>();
                        String correctOption = null;
                        for(int j=0;j<4;j++)
                        {
                            Options o=gson.fromJson(String.valueOf(jsonArray1.getJSONObject(j)),Options.class);

                            if(jsonArray1.getJSONObject(j).getInt("isCorrect")==1)
                                correctOption=jsonArray1.getJSONObject(j).getString("optionText");
                            optionsArrayList.add(o);
                        }
                        obj.questionObject=q;
                        obj.optionsArrayList=new ArrayList<Options>();
                        obj.optionsArrayList=optionsArrayList;
                        obj.correcOption=correctOption;
                        res.add(obj);
                    }

                }*/
            } catch (JSONException e) {
                Log.d("Resss", "Exception");

                e.printStackTrace();
            }
        }
        Log.d("SizeofResponse", " " + res.questionList + " " + res.correcOptions);
        return res;
    }

    public static ResponseClass getData(String serverUrl, String jsonFormat) throws IOException {

        ResponseClass res = new ResponseClass();
        Gson gson = new Gson();
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

        Log.d("BeforeResponse", "Response Get");
        int responseCode = connection.getResponseCode();
        Log.d("responseCode", "response is " + responseCode);
        Log.d("ResponseCodeIs", "responseCode is " + responseCode + " " + HttpURLConnection.HTTP_OK);
        if (!(responseCode == 200)) {
            //Toast.makeText(getApplicationContext(), "Server Not found", Toast.LENGTH_LONG).show();
            System.out.println("ServerNotfound");

        } else {
            BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));

            String output = "", result = "";
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
                result = result + output;
            }
            List<Question> questionList = new ArrayList<Question>();
            List<String> correctOptions = new ArrayList<String>();
            Log.d("GetRequest", "Result is " + result);
            try {

                JSONObject jsonObject = new JSONObject(result);
                Quiz obj = gson.fromJson(String.valueOf(jsonObject), Quiz.class);
                res.quiz = obj;
                questionList = obj.getQuestions();
                for (int i = 0; i < questionList.size(); i++) {

                    for (int j = 0; j < 4; j++) {

                        if (questionList.get(i).getOptions().get(j).getIsCorrect() == 1)
                            correctOptions.add(questionList.get(i).getOptions().get(j).getOptionText());
                    }
                }

                res.questionList = questionList;
                res.correcOptions = correctOptions;

                /*JSONArray jsonArray = jsonObject.getJSONArray("question");
                for (int i = 0; i < jsonArray.length(); i++) {
                    {
                        ResponseClass obj=new ResponseClass();
                        Question q = gson.fromJson(jsonArray.getJSONObject(i).getString("question"), Question.class);
                        JSONArray jsonArray1=jsonArray.getJSONObject(i).getJSONArray("options");
                        ArrayList<Options> optionsArrayList=new ArrayList<Options>();
                        String correctOption = null;
                        for(int j=0;j<4;j++)
                        {
                            Options o=gson.fromJson(String.valueOf(jsonArray1.getJSONObject(j)),Options.class);

                            if(jsonArray1.getJSONObject(j).getInt("isCorrect")==1)
                                correctOption=jsonArray1.getJSONObject(j).getString("optionText");
                            optionsArrayList.add(o);
                        }
                        obj.questionObject=q;
                        obj.optionsArrayList=new ArrayList<Options>();
                        obj.optionsArrayList=optionsArrayList;
                        obj.correcOption=correctOption;
                        res.add(obj);
                    }

                }*/
            } catch (JSONException e) {
                Log.d("Resss", "Exception");

                e.printStackTrace();
            }
        }
        Log.d("SizeofResponse", " " + res.questionList + " " + res.correcOptions);
        return res;
    }


    public static void updateScore(String serverUrl, String jsonFormat) throws IOException {

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
        Log.d("ScoreUpdateResponse", "response is " + responseCode);
        Log.d("ResponseCodeIs", "responseCode is " + responseCode + " " + HttpURLConnection.HTTP_OK);

    }


    public static ArrayList<User> getListofFriends(String serverUrl) throws IOException {

        ArrayList<User> userList = new ArrayList<User>();
        ResponseClass res = new ResponseClass();
        Gson gson = new Gson();
        Log.d("Calling", "Inside another GetRequest");
        StringBuilder urlString = new StringBuilder(serverUrl);
        URL url = new URL(urlString.toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Basic cnlhbkBleGFtcGxlLmNvbTpwYXNzd29yZA==");
        connection.setRequestProperty("Content-type", "application/json");

        Log.d("BeforeResponse", "Response Get");
        int responseCode = connection.getResponseCode();
        Log.d("responseCode", "response is " + responseCode);
        Log.d("ResponseCodeIs", "responseCode is " + responseCode + " " + HttpURLConnection.HTTP_OK);

        if (!(responseCode == 200)) {
            //Toast.makeText(getApplicationContext(), "Server Not found", Toast.LENGTH_LONG).show();
            System.out.println("ServerNotfound");

        } else {
            BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));

            String output = "", result = "";
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
                result = result + output;
            }

            Log.d("GetRequest", "Result is " + result);
            try {

                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    User obj = gson.fromJson(String.valueOf(jsonObject), User.class);
                    userList.add(obj);
                }

            } catch (JSONException e) {
                Log.d("Resss", "Exception");

                e.printStackTrace();
            }
        }
        Log.d("SizeofResponse", " " + res.questionList + " " + res.correcOptions);
        return userList;
    }




/*    To do

    public static ArrayList<PendingChallengesData> getPendingChallenges(String serverUrl) throws IOException {


        ArrayList<PendingChallengesData> pendingChallengesData=new ArrayList<PendingChallengesData>();
        Gson gson = new Gson();
        Log.d("Calling", "Inside another GetRequest");
        StringBuilder urlString = new StringBuilder(serverUrl);
        URL url = new URL(urlString.toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Basic cnlhbkBleGFtcGxlLmNvbTpwYXNzd29yZA==");
        connection.setRequestProperty("Content-type", "application/json");

        Log.d("BeforeResponse", "Response Get");
        int responseCode = connection.getResponseCode();
        Log.d("responseCode", "response is " + responseCode);
        Log.d("ResponseCodeIs", "responseCode is " + responseCode + " " + HttpURLConnection.HTTP_OK);
        if (!(responseCode == 200)) {
            //Toast.makeText(getApplicationContext(), "Server Not found", Toast.LENGTH_LONG).show();
            System.out.println("ServerNotfound");

        } else {
            BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));

            String output = "", result = "";
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
                result = result + output;
            }
            try {


                JSONArray jsonArray=new JSONArray(result);
                for(int i=0;i<jsonArray.length();i++)
                {
                    PendingChallengesData tmp=new PendingChallengesData();
                    JSONObject j1=jsonArray.getJSONObject(i);
                    JSONObject topicObject=j1.getJSONObject("topic");
                    Topic obj=gson.fromJson(String.valueOf(topicObject),Topic.class);
                    JSONObject userObject=j1.getJSONObject("user");
                    User obj1=gson.fromJson(String.valueOf(userObject),User.class);
                    JSONObject quizObject=j1.getJSONObject("quiz");
                    Quiz obj2=gson.fromJson(String.valueOf(quizObject),Quiz.class);
                    String roomIdObj=j1.getString("roomId");
                    tmp.topicObject=obj;
                    tmp.userObject=obj1;
                    tmp.quizObject=obj2;
                    tmp.roomIdObject=roomIdObj;
                    pendingChallengesData.add(tmp);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        return pendingChallengesData;
    }

    public static AcceptChallengeData acceptChallenge(String serverUrl, String jsonFormat) throws IOException {

        Gson gson=new Gson();
        AcceptChallengeData acceptChallengeData=new AcceptChallengeData();
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
        Log.d("ScoreUpdateResponse", "response is " + responseCode);
        Log.d("ResponseCodeIs", "responseCode is " + responseCode + " " + HttpURLConnection.HTTP_OK);
        BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));

        String output = "", result = "";
        System.out.println("Output from Server .... \n");
        while ((output = br.readLine()) != null) {
            System.out.println(output);
            result = result + output;
        }
        Log.d("result is ",result);
        try {
            ArrayList<GameDetail> gameDetails=new ArrayList<GameDetail>();
            JSONObject obj=new JSONObject(result);
            JSONObject o1=obj.getJSONObject("quiz");
            Quiz q=gson.fromJson(String.valueOf(o1),Quiz.class);
            int numberOfQuestions=q.getQuestions().size();
            JSONArray o2=obj.getJSONArray("gameDetails");
            for(int i=0;i<o2.length();i++)
            {

                JSONObject o3=o2.getJSONObject(i);
                GameDetail gD=gson.fromJson(String.valueOf(o3),GameDetail.class);
                gameDetails.add(gD);
            }
            if(numberOfQuestions==o2.length())
                acceptChallengeData.finishGame=1;
            else
                acceptChallengeData.finishGame=0;

            acceptChallengeData.quizObject=q;
            acceptChallengeData.gameDetailsObject=gameDetails;



        } catch (JSONException e) {
            e.printStackTrace();
        }

        return acceptChallengeData;
    }
*/



}
