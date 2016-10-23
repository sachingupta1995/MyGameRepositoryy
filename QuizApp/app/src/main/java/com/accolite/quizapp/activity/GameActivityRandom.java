package com.accolite.quizapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.accolite.quizapp.R;
import com.accolite.quizapp.model.ResponseClass;
import com.accolite.quizapp.model.User;
import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.ChatEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LiveRoomInfoEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LobbyData;
import com.shephertz.app42.gaming.multiplayer.client.events.MoveEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomData;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.UpdateEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.NotifyListener;
import com.shephertz.app42.gaming.multiplayer.client.listener.RoomRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;


public class GameActivityRandom extends Activity implements RoomRequestListener, NotifyListener {

    int decrementStarted = 0;
    int insideHandler = 0;
    ProgressDialog prog;
    private int playerJoin = 0;
    RelativeLayout mainLayout;
    final static int GAME_DURATION = 15; // game duration, seconds.
    private static int secondsLeft;
    TextView opponent;
    Button moveToNextQuestion;
    private WarpClient theClient;
    private HashMap<String, User> userMap = new HashMap<String, User>();
    private String roomId = "";
    private static Integer mScore = 0;
    private static int questionId = 0;
    ResponseClass listQuestions;
    int mSecondsLeft = -1;
    private static String opponentName;
    private static Integer opponentScore = 0;
    private String myName;
    private int roomCreated;
    Button rd1, rd2, rd3, rd4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.game_activity);
        rd1 = (Button) findViewById(R.id.option1);
        rd2 = (Button) findViewById(R.id.option2);
        rd3 = (Button) findViewById(R.id.option3);
        rd4 = (Button) findViewById(R.id.option4);

        try {
            theClient = WarpClient.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        decrementStarted = 0;
        playerJoin = 0;
        opponent = (TextView) findViewById(R.id.opponent_score);
        moveToNextQuestion = (Button) findViewById(R.id.next_question);

        //Done to fix the error:How to fix android.os.NetworkOnMainThreadException?
        //Follow link:https://goo.gl/bg7Fji
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Intent intent = getIntent();
        roomId = intent.getStringExtra("roomId");
        myName = intent.getStringExtra("myName");
        roomCreated = intent.getIntExtra("Created", 0);
        init(roomId);


        if (roomCreated == 1) {
            try {
                String jsonFormat = buildJson();
                listQuestions = RetrievingData.getData(getResources().getString(R.string.server_url) + "quiz/initiateGame/level/1", jsonFormat);
                ;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        prog = ProgressDialog.show(this, "", "Please Wait");
        mainLayout = (RelativeLayout) this.findViewById(R.id.rel_layout);
        mainLayout.setVisibility(RelativeLayout.GONE);


        rd1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                rd1.setPressed(true);
                rd2.setPressed(false);
                rd3.setPressed(false);
                rd4.setPressed(false);
                rd1.setBackgroundColor(Color.BLUE);
                rd3.setBackgroundColor(Color.WHITE);
                rd2.setBackgroundColor(Color.WHITE);
                rd4.setBackgroundColor(Color.WHITE);

                return true;
            }
        });
        rd2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                rd2.setPressed(true);
                rd1.setPressed(false);
                rd3.setPressed(false);
                rd4.setPressed(false);
                rd2.setBackgroundColor(Color.BLUE);
                rd1.setBackgroundColor(Color.WHITE);
                rd3.setBackgroundColor(Color.WHITE);
                rd4.setBackgroundColor(Color.WHITE);

                return true;
            }
        });
        rd3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                rd3.setPressed(true);
                rd1.setPressed(false);
                rd2.setPressed(false);
                rd4.setPressed(false);
                rd3.setBackgroundColor(Color.BLUE);
                rd1.setBackgroundColor(Color.WHITE);
                rd2.setBackgroundColor(Color.WHITE);
                rd4.setBackgroundColor(Color.WHITE);

                return true;
            }
        });
        rd4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                rd4.setPressed(true);
                rd1.setPressed(false);
                rd2.setPressed(false);
                rd3.setPressed(false);
                rd4.setBackgroundColor(Color.BLUE);
                rd1.setBackgroundColor(Color.WHITE);
                rd2.setBackgroundColor(Color.WHITE);
                rd3.setBackgroundColor(Color.WHITE);

                return true;
            }
        });

        moveToNextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedId = 0;
                if (rd1.isPressed() == false && rd2.isPressed() == false && rd3.isPressed() == false && rd4.isPressed() == false)
                    selectedId = -1;
                if (selectedId == -1) {
                    Toast.makeText(getApplicationContext(), "Select one option!!", Toast.LENGTH_LONG).show();
                } else {
                    secondsLeft = mSecondsLeft;
                    if (questionId < listQuestions.questionList.size() - 1) {
//                    mSecondsLeft=GAME_DURATION;

                        mSecondsLeft = listQuestions.questionList.get(questionId + 1).getTime();
                        decrementStarted=0;
                        scoreOnePoint();
                    } else {

                        findViewById(R.id.next_question).setVisibility(View.GONE);
                        scoreOnePoint();
                        //broadcastScore(true);
                        sendUpdateEvent();
                    }
                }


            }
        });
    }


    String formatScore(Integer i) {
        if (i < 0)
            i = 0;
        String s = String.valueOf(i);
        return s.length() == 1 ? "00" + s : s.length() == 2 ? "0" + s : s;
    }


    void updateNextQuestion() {

        ((TextView) findViewById(R.id.question)).setText(listQuestions.questionList.get(questionId).getQuesText());

        rd1.setPressed(false);
        rd2.setPressed(false);
        rd3.setPressed(false);
        rd4.setPressed(false);
        rd1.setBackgroundColor(Color.WHITE);
        rd2.setBackgroundColor(Color.WHITE);
        rd3.setBackgroundColor(Color.WHITE);
        rd4.setBackgroundColor(Color.WHITE);

        rd1.setText(listQuestions.questionList.get(questionId).getOptions().get(0).getOptionText());
        rd2.setText(listQuestions.questionList.get(questionId).getOptions().get(1).getOptionText());
        rd3.setText(listQuestions.questionList.get(questionId).getOptions().get(2).getOptionText());
        rd4.setText(listQuestions.questionList.get(questionId).getOptions().get(3).getOptionText());

    }


    void startGame() {
        updateNextQuestion();
        findViewById(R.id.next_question).setVisibility(View.VISIBLE);

        // run the gameTick() method every second to update the game.
        final Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSecondsLeft <= 0)
                    return;
                if (insideHandler == 0)
                    gameTick();
                h.postDelayed(this, 1000);
            }
        }, 1000);
    }

    void gameTick() {
        if (mSecondsLeft > 0) {

            if (decrementStarted == 0) {
                insideHandler = 1;
                final Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        decrementStarted = 1;
                        insideHandler=0;
                    }
                }, 3000);
            } else
                --mSecondsLeft;
        }
        if (opponentName != null)
            ((TextView) findViewById(R.id.opponent_score)).setText(formatScore(opponentScore) + " -" + opponentName);

        // update countdown
        ((TextView) findViewById(R.id.countdown)).setText("0:" +
                (mSecondsLeft < 10 ? "0" : "") + String.valueOf(mSecondsLeft));

        if (mSecondsLeft <= 0) {
            // finish game
            if (questionId == listQuestions.questionList.size() - 1) {
                scoreOnePoint();
                findViewById(R.id.next_question).setVisibility(View.GONE);
                sendUpdateEvent();

            } else {
                if (questionId < listQuestions.questionList.size() - 1) {
                    mSecondsLeft = GAME_DURATION;
                    mSecondsLeft = listQuestions.questionList.get(questionId + 1).getTime();
                    decrementStarted=0;
                }
                scoreOnePoint();
            }
        }
    }


    void scoreOnePoint() {

        String selectedAnswer = null;
        int selectedId = 0;
        if (rd1.isPressed() == true) {
            selectedId = 1;
            selectedAnswer = rd1.getText().toString();
        } else if (rd2.isPressed() == true) {
            selectedId = 2;
            selectedAnswer = rd2.getText().toString();
        } else if (rd3.isPressed() == true) {
            selectedId = 3;
            selectedAnswer = rd3.getText().toString();
        } else if (rd4.isPressed() == true) {
            selectedId = 4;
            selectedAnswer = rd4.getText().toString();
        }

        if (selectedId != 0) {
            if (selectedAnswer.equals(listQuestions.correcOptions.get(questionId))) {
            /*
            To do
                if (selectedId == 1)
                    rd1.setBackgroundColor(Color.GREEN);
                else if (selectedId == 2)
                    rd2.setBackgroundColor(Color.GREEN);
                else if (selectedId == 3)
                    rd3.setBackgroundColor(Color.GREEN);
                else
                    rd4.setBackgroundColor(Color.GREEN);
                    */
                if (mSecondsLeft <= 0)
                    mScore++;
                else
                    mScore += secondsLeft;
            } else {

                /*To do
                if (selectedId == 1)
                    rd1.setBackgroundColor(Color.RED);
                else if (selectedId == 2)
                    rd2.setBackgroundColor(Color.RED);
                else if (selectedId == 3)
                    rd3.setBackgroundColor(Color.RED);
                else
                    rd4.setBackgroundColor(Color.RED);*/
            }
        }
        questionId++;
        if (questionId <= listQuestions.questionList.size() - 1)
            updateNextQuestion();

        sendUpdateEvent();

        //This means Questions are finished.
        if (questionId > listQuestions.questionList.size() - 1) {
            findViewById(R.id.next_question).setVisibility(View.GONE);
            mSecondsLeft = -1;
            ((TextView) findViewById(R.id.countdown)).setText("");
            ((TextView) findViewById(R.id.question)).setText("");
            rd1.setVisibility(View.INVISIBLE);
            rd2.setVisibility(View.INVISIBLE);
            rd3.setVisibility(View.INVISIBLE);
            rd4.setVisibility(View.INVISIBLE);


        }


        // broadcast our new score to our peers
        if (questionId <= listQuestions.questionList.size() - 1)
            sendUpdateEvent();

    }


    private void init(String roomId) {
        if (theClient != null) {
            theClient.addRoomRequestListener(this);
            theClient.addNotificationListener(this);
            theClient.joinRoom(roomId);
        }
    }

    //Adding Player in the room
    public void addMorePlayer(boolean isMine, String userName) {
        if (userMap.get(userName) != null) {// if already in room
            return;

        }
    }


    //Send update to other opponent
    private void sendUpdateEvent() {
        JSONObject object = new JSONObject();
        try {

            object.put("score", "" + mScore);
            object.put("senderName", myName);
            object.put("quizId", -1);
            Log.d("TheValue", object.toString());
            theClient.sendChat(object.toString());//Sending Chat to Opponent in Json Form
            ((TextView) findViewById(R.id.my_score)).setText(formatScore(mScore) + " - Me");
            if (opponentName != null)
                ((TextView) findViewById(R.id.opponent_score)).setText(formatScore(opponentScore) + " -" + opponentName);

        } catch (Exception e) {
            Log.d("sendUpdateEvent", " " + e);
        }
    }

    private void updateProperty(String position, String objectType) {
        HashMap<String, Object> table = new HashMap<String, Object>();
        table.put(position, objectType);
        theClient.updateRoomProperties(roomId, table, null);
    }


    public void handleLeave(String name) {

        if (name.length() > 0 && userMap.get(name) != null) {
            userMap.remove(name);
        }
    }


    @Override
    public void onBackPressed() {
        //When user Leaves the room
        if (theClient != null) {

            /*To do
            handleLeave(Utils.userName);*/
            theClient.leaveRoom(roomId);
            theClient.unsubscribeRoom(roomId);
            theClient.removeRoomRequestListener(this);
            theClient.removeNotificationListener(this);

        }
        super.onBackPressed();
    }


    private HashMap<String, Object> properties;


    @Override
    public void onChatReceived(ChatEvent event) {
        String sender = event.getSender();
        if (sender.equals(myName) == false) {// if not same user
            String message = event.getMessage();
            try {
                JSONObject object = new JSONObject(message);
                if (opponentName == null) {
                    int quizId = object.getInt("quizId");
                    listQuestions = RetrievingData.makeGetRequest(getResources().getString(R.string.server_url) + "quiz/" + quizId);
                    opponentName = sender;
                    mScore = 0;
                    questionId = 0;
                    opponentScore = 0;
                    mSecondsLeft = listQuestions.questionList.get(0).getTime();
                    decrementStarted=0;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            prog.dismiss();
                            mainLayout.setVisibility(View.VISIBLE);
                            startGame();
                            ((TextView) findViewById(R.id.my_score)).setText(formatScore(mScore) + "-Me");
                            ((TextView) findViewById(R.id.opponent_score)).setText(formatScore(opponentScore) + " -" + opponentName);
                        }
                    });

                }
                //This thread is run to resolve error:“Only the original thread that created a view hierarchy can touch its views.”
                //Follow this link: https://goo.gl/FdBzRK

                else {
                    opponentScore = Integer.parseInt(object.get("score") + "");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            ((TextView) findViewById(R.id.opponent_score)).setText(formatScore(opponentScore) + " -" + opponentName);
                        }
                    });
                }
//				((TextView) findViewById(R.id.opponent_score)).setText(formatScore(Integer.parseInt(object.get("score")+"")) +"-"+sender);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onPrivateChatReceived(String arg0, String arg1) {

    }

    @Override
    public void onRoomCreated(RoomData arg0) {

    }

    @Override
    public void onRoomDestroyed(RoomData arg0) {

    }

    @Override
    public void onUpdatePeersReceived(UpdateEvent arg0) {

    }

    @Override
    public void onUserChangeRoomProperty(RoomData roomData, String userName, HashMap<String, Object> tableProperties, HashMap<String, String> lockProperties) {
        /* To do
        if (userName.equals(Utils.userName)) {
            // just update the local property table.
            // no need to update UI as we have already done so.
            properties = tableProperties;
            return;
        }

        // notification is from a remote user. We need to update UI accordingly.

        for (Map.Entry<String, Object> entry : tableProperties.entrySet()) {
            if (entry.getValue().toString().length() > 0) {
                if (!this.properties.get(entry.getKey()).toString().equals(entry.getValue())) {
                    int fruitId = Integer.parseInt(entry.getValue().toString());
                    properties.put(entry.getKey(), entry.getValue());
                }
            }
        }*/
    }

    @Override
    public void onUserJoinedLobby(LobbyData arg0, String arg1) {


    }

    //When some opponent joins the room
    @Override
    public void onUserJoinedRoom(RoomData roomData, String name) {

        addMorePlayer(true, name);
        opponentName = name;
//        JSONObject object1 = new JSONObject();
        mScore = 0;
        questionId = 0;
        opponentScore = 0;
        mSecondsLeft = listQuestions.questionList.get(0).getTime();
        decrementStarted=0;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startGame();
                prog.dismiss();
                mainLayout.setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.my_score)).setText(formatScore(mScore) + "-Me");
                ((TextView) findViewById(R.id.opponent_score)).setText(formatScore(opponentScore) + " -" + opponentName);
                JSONObject object1 = new JSONObject();
                try {
                    object1.put("senderName", myName);
                    object1.put("score", "" + mScore);
                    object1.put("quizId", listQuestions.quiz.getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                theClient.sendChat(object1.toString());

            }
        });


        //setOpponentScore(name);
    }

    private void setOpponentScore(String userName) {
        ((TextView) findViewById(R.id.opponent_score)).setText("000-" + userName);
    }


    @Override
    public void onUserLeftLobby(LobbyData arg0, String arg1) {
    }

    //When some opponent leaves the room
    @Override
    public void onUserLeftRoom(RoomData roomData, String name) {
        /*To do
        handleLeave(name);
        */
    }

    //This is called when theClient.addRoomRequestListener is called
    //This is callback function
    @Override
    public void onGetLiveRoomInfoDone(LiveRoomInfoEvent event) {
        /* To do
        if (event.getResult() == WarpResponseResultCode.SUCCESS) {
            String[] joinedUser = event.getJoinedUsers();
            if (joinedUser != null) {
                for (int i = 0; i < joinedUser.length; i++) {
                    if (joinedUser[i].equals(Utils.userName)) {
                        addMorePlayer(true, joinedUser[i]);
                    } else {
                        addMorePlayer(false, joinedUser[i]);
                    }
                }
            }
            properties = event.getProperties();

        } else {

            *//*To do
            Utils.showToastOnUIThread(this, "onGetLiveRoomInfoDone: Failed " + event.getResult());*//*
        }
*/
    }

    //When user joins the room
    @Override
    public void onJoinRoomDone(RoomEvent event) {
        if (event.getResult() == WarpResponseResultCode.SUCCESS) {
            //It is called when user itself joins the room
            theClient.subscribeRoom(roomId);
        } else {
            /*  To do
            Utils.showToastOnUIThread(this, "onJoinRoomDone: Failed " + event.getResult());*/
        }
    }


    //When user leaves the room
    @Override
    public void onLeaveRoomDone(RoomEvent event) {

        Log.d("Left", "Room Left");
        //Here pop up needs to be shown
        finish();
        new AlertDialog.Builder(this)
                .setTitle("Victory!!")
                .setMessage("Your opponent left the game!!You win.")
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    @Override
    public void onSetCustomRoomDataDone(LiveRoomInfoEvent arg0) {

    }

    @Override
    public void onSubscribeRoomDone(RoomEvent event) {
        if (event.getResult() == WarpResponseResultCode.SUCCESS) {
            theClient.getLiveRoomInfo(roomId);
            Log.d("ListofUsers", "" + event.getData());
        } else {
            /*To
            Utils.showToastOnUIThread(this, "onSubscribeRoomDone: Failed " + event.getResult());
            */
        }
    }

    @Override
    public void onUnSubscribeRoomDone(RoomEvent arg0) {
    }

    @Override
    public void onUpdatePropertyDone(LiveRoomInfoEvent arg0) {

    }

    @Override
    public void onMoveCompleted(MoveEvent arg0) {

    }

    @Override
    public void onLockPropertiesDone(byte arg0) {

    }

    @Override
    public void onUnlockPropertiesDone(byte arg0) {

    }

    @Override
    public void onGameStarted(String arg0, String arg1, String arg2) {

    }

    @Override
    public void onGameStopped(String arg0, String arg1) {

    }

    @Override
    public void onUserPaused(String arg0, boolean arg1, String arg2) {

    }

    @Override
    public void onUserResumed(String arg0, boolean arg1, String arg2) {

    }

    @Override
    public void onNextTurnRequest(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPrivateUpdateReceived(String arg0, byte[] arg1, boolean arg2) {
        // TODO Auto-generated method stub

    }

    private String buildJson() {
        JSONObject j = new JSONObject();
        JSONObject j1 = new JSONObject();
        JSONObject j3 = new JSONObject();
        JSONObject j4 = new JSONObject();

        try {


            int userId = Integer.parseInt(myName);
            j1.put("id", userId);
            j.put("challenger", j1);
            j.put("opponent", null);
            j3.put("id", 2);//It is 2 for random type
            j.put("challengeType", j3);
            j.put("roomId", roomId);
            j4.put("id", 1);
            j.put("topic", j4);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return j.toString();

    }


}