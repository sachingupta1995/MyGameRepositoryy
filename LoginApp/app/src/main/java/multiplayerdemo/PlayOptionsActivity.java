package multiplayerdemo;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.accolite.loginapp.ChallengeFriendActivity;
import com.accolite.loginapp.PendingChallengesActivity;
import com.accolite.loginapp.R;
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

    Button singlePlayer, randomGame, challengeFriend, pendingChallenges;
    private WarpClient theClient;
    private EditText nameEditText;
    private ProgressDialog progressDialog;
    private boolean isConnected = false;
    private int selectedMonster;
    private int randomGameClicked = 0;
    private int challengeFriendClicked = 0;
    private String randomRoomId;
    private int roomsAvailable;
    private boolean singleplayerClicked = false;
    private int multiplayerClicked = 0;
    private int pendingChallengesClicked = 0;
    ArrayList<RoomData> roomDataArrayList;
    private String myRandomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        roomDataArrayList = new ArrayList<RoomData>();
        roomsAvailable = 0;
        singlePlayer = (Button) findViewById(R.id.single_player);
        randomGame = (Button) findViewById(R.id.random_game);
        randomGameClicked = 0;
        multiplayerClicked = 0;
        challengeFriendClicked = 0;
        singleplayerClicked = false;
        challengeFriend = (Button) findViewById(R.id.challenge_friend);
        pendingChallenges = (Button) findViewById(R.id.pending_challenge);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        selectedMonster = -1;

        init();

        singlePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.userName = nameEditText.getText().toString();
                singleplayerClicked = true;
                theClient.connectWithUserName(nameEditText.getText().toString());

            }
        });

        randomGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                randomGameClicked = 1;
                Utils.userName = nameEditText.getText().toString();
                Random r = new Random();
                int n = r.nextInt(1000) + 1;
                myRandomName=""+n;
                theClient.connectWithUserName("" + n);

				/*if(roomsAvailable==0)
                    theClient.createRoom("" + System.currentTimeMillis(), "Sachin", 2, properties);
*/
            }
        });

        challengeFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //theClient.connectWithUserName("Sachin");
                challengeFriendClicked = 1;
                finish();
                startActivity(new Intent(PlayOptionsActivity.this, ChallengeFriendActivity.class));
            }
        });
        pendingChallenges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pendingChallengesClicked = 1;
                finish();
                startActivity(new Intent(PlayOptionsActivity.this, PendingChallengesActivity.class));
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
                if (event.getResult() == WarpResponseResultCode.SUCCESS) {// if room created successfully


                    if (randomGameClicked == 0 && challengeFriendClicked == 0 && pendingChallengesClicked == 0) {
                        String roomId = event.getData().getId();
                        joinRoom(roomId);
                        Log.d("onCreateRoomDone", event.getResult() + " " + roomId);
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

            theClient.createRoom("" + System.currentTimeMillis(), "Sachin", 2, properties);

        } else {
            int roomFound = 0;
            roomsAvailable = 1;
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

                    Log.d("RoomId==", roomDataList[i].getId());
                }
                Log.d("RoomId=", " " + roomDataList.length);
                if (roomFound == 1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


//                        Log.d("RoomId=", matchedRoomsEvent.getRoomsData()[0].getId());
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


    public void onPlayGameClicked(View view) {
        if (nameEditText.getText().length() == 0) {
            Utils.showToastAlert(this, getApplicationContext().getString(R.string.enterName));
            return;
        }
        multiplayerClicked = 1;
        String userName = nameEditText.getText().toString();
        Utils.userName = userName;
        Log.d("Name to Join", "" + userName);
        theClient.connectWithUserName(userName);
        progressDialog = ProgressDialog.show(this, "", "connecting to appwarp");

    }


    //Initialisation of WarpClient
    private void init() {
        WarpClient.initialize(Constants.apiKey, Constants.secretKey);
        try {
            theClient = WarpClient.getInstance();
        } catch (Exception ex) {
            Utils.showToastAlert(this, "Exception in Initilization");
        }
        theClient.addConnectionRequestListener(this);

        //This needs to be added to the code to have  connecction done and get the success in onConnectDone
        theClient.addZoneRequestListener(this);
    }

    //This is called after theClient.conectwithUsername is called
    @Override
    public void onConnectDone(final ConnectEvent event) {
        Log.d("onConnectDone", event.getResult() + "");
        if (event.getResult() == WarpResponseResultCode.SUCCESS) {
            // go to room  list
            isConnected = true;
            if (multiplayerClicked == 1) {
                Intent intent = new Intent(PlayOptionsActivity.this, RoomListActivity.class);
                startActivity(intent);
            } else if (singleplayerClicked) {
                HashMap<String, Object> properties = new HashMap<String, Object>();
                properties.put("topLeft", "");
                properties.put("topRight", "");
                properties.put("bottomLeft", "");
                properties.put("bottomRight", "");
                theClient.createRoom("" + System.currentTimeMillis(), "Saurav", 4, properties);

            }
            else if (randomGameClicked == 1) {
                HashMap<String, Object> properties = new HashMap<String, Object>();
                properties.put("game", "random");
                theClient.getRoomWithProperties(properties);
                theClient.getRoomInRange(1, 1);

            }
        }
        else
        {
            isConnected = false;
            Utils.showToastOnUIThread(PlayOptionsActivity.this, "connection failed");
        }
    }

    @Override
    public void onDisconnectDone(final ConnectEvent event) {
        Log.d("onDisconnectDone", event.getResult() + "");
        isConnected = false;
    }

    @Override
    public void onInitUDPDone(byte arg0) {

    }

    public void joinRoom(String roomId) {
        if (roomId != null && roomId.length() > 0) {
            goToGameScreen(roomId);
        } else {
            Log.d("joinRoom", "failed:" + roomId);
        }
    }

    public void joinRandomRoom(String roomId) {
        Log.d("JoinRandomRoom", roomId);
        if (roomId != null && roomId.length() > 0) {
            goToRandomGameScreen(roomId);
        } else {
            Log.d("joinRoom", "failed:" + roomId);
        }
    }

    private void goToGameScreen(String roomId) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("roomId", roomId);
        startActivity(intent);
    }

    private void goToRandomGameScreen(String roomId) {
        Intent intent = new Intent(this, GameActivityRandom.class);
        intent.putExtra("roomId", roomId);
        intent.putExtra("myName",myRandomName);
        finish();
        startActivity(intent);
    }
}
