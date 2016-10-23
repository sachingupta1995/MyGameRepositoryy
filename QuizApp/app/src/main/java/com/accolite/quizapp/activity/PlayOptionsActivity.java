package com.accolite.quizapp.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.accolite.quizapp.R;
import com.accolite.quizapp.UtilClass;
import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.AllRoomsEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.AllUsersEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.ConnectEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LiveUserInfoEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.MatchedRoomsEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomData;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.ConnectionRequestListener;
import com.shephertz.app42.gaming.multiplayer.client.listener.ZoneRequestListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class PlayOptionsActivity extends Activity implements ConnectionRequestListener, ZoneRequestListener {

    private Thread thread;
    Button singlePlayer, randomGame, challengeFriend, pendingChallenges;
    private WarpClient theClient;
    private ProgressDialog progressDialog;
    private boolean isConnected = false;
    private int randomGameClicked = 0;
    private int challengeFriendClicked = 0;
    private String randomRoomId;
    private int singleplayerClicked;
    private int pendingChallengesClicked = 0;
    ArrayList<RoomData> roomDataArrayList;
    int roomFound = 0;
    String myRandomName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_options);
        roomDataArrayList = new ArrayList<RoomData>();
        singlePlayer = (Button) findViewById(R.id.single_player);
        randomGame = (Button) findViewById(R.id.random_game);
        randomGameClicked = 0;
        challengeFriendClicked = 0;
        singleplayerClicked = 0;
        challengeFriend = (Button) findViewById(R.id.challenge_friend);
        pendingChallenges = (Button) findViewById(R.id.pending_challenge);
        roomFound = 0;
        init();



        singlePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*To do
                Utils.userName = nameEditText.getText().toString();*/
                singleplayerClicked = 1;
                Random r = new Random();
                int n = r.nextInt(100000) + 1;
                theClient.connectWithUserName("" + n);
            }
        });

        randomGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                randomGameClicked = 1;
                /*To do
                Utils.userName = nameEditText.getText().toString();*/
                Random r = new Random();
                int n = r.nextInt(1000) + 1;
                myRandomName=""+n;
                theClient.connectWithUserName("" + n);

            }
        });

        challengeFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //theClient.connectWithUserName("Sachin");
                challengeFriendClicked = 1;
                finish();
                /*To do
                startActivity(new Intent(PlayOptionsActivity.this, ChallengeFriendActivity.class));*/
            }
        });
        pendingChallenges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pendingChallengesClicked = 1;
                finish();
                /*To do
                startActivity(new Intent(PlayOptionsActivity.this, PendingChallengesActivity.class));*/
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        theClient.removeConnectionRequestListener(this);
        if (theClient != null && isConnected) {
            theClient.disconnect();
        }
        /*To do
        finish();
        startActivity(new Intent(PlayOptionsActivity.this, SearchActivity.class));*/
    }

    @Override
    public void onDeleteRoomDone(RoomEvent roomEvent) {

    }

    @Override
    public void onGetAllRoomsDone(AllRoomsEvent allRoomsEvent) {

    }

    //It is called after createRoom is called by WarpClient
    @Override
    public void onCreateRoomDone(final RoomEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                if (event.getResult() == WarpResponseResultCode.SUCCESS) {
                    if (singleplayerClicked==1) {
                        String roomId = event.getData().getId();
                        joinRoom(roomId);
                    } else if (randomGameClicked == 1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                randomRoomId = event.getData().getId();
                                joinRandomRoom(randomRoomId);
                            }
                        });


                    }
                }
            }
        });
    }

    @Override
    public void onGetOnlineUsersDone(AllUsersEvent allUsersEvent) {

    }

    @Override
    public void onGetLiveUserInfoDone(LiveUserInfoEvent liveUserInfoEvent) {

    }

    @Override
    public void onSetCustomUserDataDone(LiveUserInfoEvent liveUserInfoEvent) {

    }

    @Override
    public void onGetMatchedRoomsDone(final MatchedRoomsEvent matchedRoomsEvent) {

        RoomData[] roomDataList = matchedRoomsEvent.getRoomsData();
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("game", "random");
        if (roomDataList == null || roomDataList.length == 0) {
            theClient.createRoom("" + System.currentTimeMillis(), getResources().getString(R.string.room_owner), 2, properties);
        } else {
            roomFound = 0;
            if (roomDataArrayList.size() > 0) {
                for (int i = 0; i < roomDataList.length; i++) {
                    for (int j = 0; j < roomDataArrayList.size(); j++) {
                        if (roomDataArrayList.get(j).getId().equals(roomDataList[i].getId())) {
                            roomFound = 1;
                            randomRoomId = roomDataArrayList.get(j).getId();
                            break;
                        }
                    }
                    if (roomFound == 1)
                        break;

                }
                if (roomFound == 1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (randomGameClicked == 1)
                                joinRandomRoom(randomRoomId);

                        }
                    });
                }
            } else {
                for (int i = 0; i < roomDataList.length; i++)
                    roomDataArrayList.add(roomDataList[i]);
            }
        }
    }

    @Override
    public void onGetRoomsCountDone(RoomEvent roomEvent) {

    }

    @Override
    public void onGetUsersCountDone(AllUsersEvent allUsersEvent) {

    }

    //Initialisation of WarpClient
    private void init() {
        WarpClient.initialize(UtilClass.apiKey, UtilClass.secretKey);
        try {
            theClient = WarpClient.getInstance();
        } catch (Exception ex) {
        }
        theClient.addConnectionRequestListener(this);
        theClient.addZoneRequestListener(this);
    }

    //This is called after theClient.conectwithUsername is called
    @Override
    public void onConnectDone(final ConnectEvent event) {

        if (event.getResult() == WarpResponseResultCode.SUCCESS) {
            isConnected = true;
            /*To do
            if (multiplayerClicked == 1) {

                Intent intent = new Intent(PlayOptionsActivity.this, RoomListActivity.class);
                startActivity(intent);
            }
            */
            if (singleplayerClicked == 1) {
                HashMap<String, Object> properties = new HashMap<String, Object>();
                properties.put("game", "singlePlayer");
                theClient.createRoom("" + System.currentTimeMillis(), getResources().getString(R.string.room_owner), 4, properties);

            } else if (randomGameClicked == 1) {
                HashMap<String, Object> properties = new HashMap<String, Object>();
                properties.put("game", "random");
                theClient.getRoomWithProperties(properties);
                theClient.getRoomInRange(1, 1);

            }
        } else {
            isConnected = false;
        }
    }

    @Override
    public void onDisconnectDone(final ConnectEvent event) {
        isConnected = false;
    }

    @Override
    public void onInitUDPDone(byte arg0) {
    }

    public void joinRoom(String roomId) {
        if (roomId != null && roomId.length() > 0) {
            goToGameScreen(roomId);
        } else {
        }
    }

    public void joinRandomRoom(String roomId) {
        if (roomId != null && roomId.length() > 0) {
            goToRandomGameScreen(roomId);
        } else {
        }
    }

    private void goToGameScreen(String roomId) {
        /*To do
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("roomId", roomId);
        startActivity(intent);*/
    }

    private void goToRandomGameScreen(String roomId) {

        Intent intent = new Intent(this, GameActivityRandom.class);
        if(roomFound==1)
            intent.putExtra("Created",0);
        else
            intent.putExtra("Created",1);
        intent.putExtra("roomId", roomId);
        intent.putExtra("myName",myRandomName);
        finish();
        startActivity(intent);
    }
}
