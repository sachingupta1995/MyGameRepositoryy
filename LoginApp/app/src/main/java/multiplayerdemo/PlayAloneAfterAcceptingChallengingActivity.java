package multiplayerdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.accolite.loginapp.R;
import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.ChatEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.ConnectEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LiveRoomInfoEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LobbyData;
import com.shephertz.app42.gaming.multiplayer.client.events.MoveEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomData;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.UpdateEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.ConnectionRequestListener;
import com.shephertz.app42.gaming.multiplayer.client.listener.NotifyListener;
import com.shephertz.app42.gaming.multiplayer.client.listener.RoomRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import model.ResponseClass;

public class PlayAloneAfterAcceptingChallengingActivity extends Activity implements ConnectionRequestListener,RoomRequestListener, NotifyListener {


    private String getJsonFormat;
    int selectedAnswerId;
    int correctAnswer=0;
    final static int GAME_DURATION = 15; // game duration, seconds.
    private static int secondsLeft;
    TextView opponent;
    private static int[] scoreArray = {0, 0, 0, 0, 0};
    Button moveToNextQuestion;
    private WarpClient theClient;
    private HashMap<String, User> userMap = new HashMap<String, User>();
    private String roomId = "";
    private int opponentId;
    private static Integer mScore = 0;
    private static int questionId = 0;
    ResponseClass listQuestions;
    int mSecondsLeft = -1;
    private static String opponentName;
    private static Integer opponentScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);
        selectedAnswerId=-1;
        correctAnswer=0;
        try {
            theClient = WarpClient.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        opponent = (TextView) findViewById(R.id.opponent_score);
        moveToNextQuestion = (Button) findViewById(R.id.next_question);

        //Done to fix the error:How to fix android.os.NetworkOnMainThreadException?
        //Follow link:https://goo.gl/bg7Fji
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Intent intent = getIntent();
        roomId = intent.getStringExtra("roomId");
        opponentId=intent.getIntExtra("opponentId",0);
        init(roomId);
        try {
            String jsonFormatt=buildJson1();
            Log.d("GettingData",jsonFormatt);
            listQuestions = RetrievingData.getData(getResources().getString(R.string.server_url) + "/initiateGame/topic/1/level/1",jsonFormatt);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mScore = 0;
        questionId = 0;
        opponentName = null;
        opponentScore = 0;
        ((TextView) findViewById(R.id.my_score)).setText(formatScore(mScore) + "-Me");
        startGame();
        mSecondsLeft = listQuestions.questionList.get(0).getTime();
//		sendUpdateEvent();

        moveToNextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RadioGroup rg = (RadioGroup) findViewById(R.id.option_group);
                int selectedId = rg.getCheckedRadioButtonId();

                if (selectedId == -1) {
                    Toast.makeText(getApplicationContext(), "Select one option!!", Toast.LENGTH_LONG).show();
                } else {
                    secondsLeft = mSecondsLeft;
                    if (questionId < listQuestions.questionList.size() - 1) {
//                    mSecondsLeft=GAME_DURATION;

                        mSecondsLeft = listQuestions.questionList.get(questionId + 1).getTime();
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

    private String buildJson1() {
        JSONObject j=new JSONObject();
        JSONObject j1=new JSONObject();
        JSONObject j2=new JSONObject();
        JSONObject j3=new JSONObject();
        JSONObject j4=new JSONObject();

        try {


            j1.put("id",1);
            j.put("challenger",j1);
            j2.put("id",opponentId);
            j.put("opponent",j2);
            j3.put("id",1);
            j.put("challengeType",j3);
            j.put("roomId",roomId);
            j4.put("id",1);
            j.put("topic",j4);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return j.toString();

    }


    String formatScore(Integer i) {
        if (i < 0)
            i = 0;
        String s = String.valueOf(i);
        return s.length() == 1 ? "00" + s : s.length() == 2 ? "0" + s : s;
    }


    void updateNextQuestion() {

        ((TextView) findViewById(R.id.question)).setText(listQuestions.questionList.get(questionId).getQuesText());
        RadioGroup rg = (RadioGroup) findViewById(R.id.option_group);
        int selectedId = rg.getCheckedRadioButtonId();
        rg.clearCheck();
        RadioButton rd1, rd2, rd3, rd4;
        rd1 = (RadioButton) findViewById(R.id.option1);
        rd1.setText(listQuestions.questionList.get(questionId).getOptions().get(0).getOptionText());

        rd2 = (RadioButton) findViewById(R.id.option2);
        rd2.setText(listQuestions.questionList.get(questionId).getOptions().get(1).getOptionText());

        rd3 = (RadioButton) findViewById(R.id.option3);
        rd3.setText(listQuestions.questionList.get(questionId).getOptions().get(2).getOptionText());

        rd4 = (RadioButton) findViewById(R.id.option4);
        rd4.setText(listQuestions.questionList.get(questionId).getOptions().get(3).getOptionText());

    }


    void startGame() {
        updateNextQuestion();
        //broadcastScore(false);

        findViewById(R.id.next_question).setVisibility(View.VISIBLE);

        // run the gameTick() method every second to update the game.
        final Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSecondsLeft <= 0)
                    return;
                gameTick();
                h.postDelayed(this, 1000);
            }
        }, 1000);
    }

    void gameTick() {
        if (mSecondsLeft > 0)
            --mSecondsLeft;
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
                //broadcastScore(true);
                sendUpdateEvent();

            } else {
                if (questionId < listQuestions.questionList.size() - 1) {
                    mSecondsLeft = GAME_DURATION;
                    mSecondsLeft = listQuestions.questionList.get(questionId + 1).getTime();
                }
                scoreOnePoint();
            }
        }
    }


    void scoreOnePoint() {

        RadioGroup rg = (RadioGroup) findViewById(R.id.option_group);
        int selectedId = rg.getCheckedRadioButtonId();

        if (selectedId != -1) {
            /*if (selectedAnswer.equals(Questions.answers[questionId]))
                ++mScore;*/
            RadioButton rb = (RadioButton) findViewById(selectedId);
            String selectedAnswer = rb.getText().toString();
            for(int i=0;i<4;i++)
            {
                if(listQuestions.questionList.get(questionId).getOptions().get(i).getOptionText().equals(selectedAnswer))
                {
                    selectedAnswerId=listQuestions.questionList.get(questionId).getOptions().get(i).getId();
                    Log.d("SelectedId=",listQuestions.questionList.get(questionId).getOptions().get(i).getOptionText());
                    break;
                }
            }


            if (selectedAnswer.equals(listQuestions.correcOptions.get(questionId))) {
                correctAnswer=1;
                if (mSecondsLeft <= 0)
                    mScore++;
                else
                    mScore += secondsLeft;
            }

        }
        else
            selectedAnswerId=-1;
        if (questionId == 0)
            scoreArray[questionId] = mScore;
        else
            scoreArray[questionId] = mScore;
        String jsonFormat=buildJson();
        String serverUrl=getResources().getString(R.string.server_url)+"/updateQuestionResponse";
        Log.d("jsonFormat",jsonFormat+" "+serverUrl);
        try {
            RetrievingData.updateScore(serverUrl,jsonFormat);
        } catch (IOException e) {
            e.printStackTrace();
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
            RadioGroup myRadioGroup = (RadioGroup) findViewById(R.id.option_group);
            myRadioGroup.setVisibility(View.INVISIBLE);

//            displayWinner();
//            switchToMainScreen();

        }


        // broadcast our new score to our peers
        if (questionId <= listQuestions.questionList.size() - 1)
            sendUpdateEvent();

    }

    private String buildJson() {

        JSONObject j1=new JSONObject();
        JSONObject j2=new JSONObject();

        JSONObject j3=new JSONObject();
        JSONObject j4=new JSONObject();
        JSONObject j=new JSONObject();
        try {
            j1.put("id",listQuestions.quiz.getId());
            j2.put("id",1);
            j3.put("id",listQuestions.questionList.get(questionId).getId());
            if(selectedAnswerId!=-1)
                j4.put("id",selectedAnswerId);
            else
                j4.put("id","null");
            j.put("quiz",j1);
            j.put("user",j2);
            j.put("question",j3);
            j.put("answer",j4);
            j.put("timeTakenToAnswer",listQuestions.questionList.get(questionId).getTime()-secondsLeft);
            if(correctAnswer!=0)
                j.put("score",secondsLeft);
            else
                j.put("score",0);
            correctAnswer=0;
            selectedAnswerId=-1;
            return j.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

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
        User user = new User();
        user.setName(userName);
        userMap.put(userName, user);
    }


    //Send update to other opponent
    private void sendUpdateEvent() {
        JSONObject object = new JSONObject();
        try {

            object.put("score", "" + mScore);
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

            handleLeave(Utils.userName);
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
        if (sender.equals(Utils.userName) == false) {// if not same user
            String message = event.getMessage();
            try {
                JSONObject object = new JSONObject(message);
                if (opponentName == null) {
                    opponentName = sender;
                }
//				((TextView) findViewById(R.id.opponent_score)).setText(formatScore(Integer.parseInt(object.get("score")+"")) +"-"+sender);
                opponentScore = Integer.parseInt(object.get("score") + "");


                //This thread is run to resolve error:“Only the original thread that created a view hierarchy can touch its views.”
                //Follow this link: https://goo.gl/FdBzRK
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView) findViewById(R.id.opponent_score)).setText(formatScore(opponentScore) + " -" + opponentName);
                    }
                });
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
        }
    }

    @Override
    public void onUserJoinedLobby(LobbyData arg0, String arg1) {


    }

    //When some opponent joins the room
    @Override
    public void onUserJoinedRoom(RoomData roomData, String name) {

        addMorePlayer(true, name);
        opponentName = name;
        JSONObject object1 = new JSONObject();
        try {
            object1.put("score", "" + mScore);
            theClient.sendChat(object1.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        handleLeave(name);
    }

    //This is called when theClient.addRoomRequestListener is called
    //This is callback function
    @Override
    public void onGetLiveRoomInfoDone(LiveRoomInfoEvent event) {
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
            Utils.showToastOnUIThread(this, "onGetLiveRoomInfoDone: Failed " + event.getResult());
        }

    }

    //When user joins the room
    @Override
    public void onJoinRoomDone(RoomEvent event) {
        if (event.getResult() == WarpResponseResultCode.SUCCESS) {
            //It is called when user itself joins the room
            theClient.subscribeRoom(roomId);
        }
        else
        {
            Utils.showToastOnUIThread(this, "onJoinRoomDone: Failed " + event.getResult());
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
            Log.d("ListofUsers",""+event.getData());
        } else {
            Utils.showToastOnUIThread(this, "onSubscribeRoomDone: Failed " + event.getResult());
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

    @Override
    public void onConnectDone(ConnectEvent connectEvent) {

    }

    @Override
    public void onDisconnectDone(ConnectEvent connectEvent) {

    }

    @Override
    public void onInitUDPDone(byte b) {

    }
}