package com.accolite.loginapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.AllRoomsEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.AllUsersEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.ConnectEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LiveUserInfoEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.MatchedRoomsEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.ConnectionRequestListener;
import com.shephertz.app42.gaming.multiplayer.client.listener.ZoneRequestListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import model.User;
import multiplayerdemo.Constants;
import multiplayerdemo.PlayAfterChallengingActivity;

/**
 * Created by Sachin Gupta on 9/20/2016.
 */
public class ChallengeFriendAdapter extends BaseAdapter  implements ConnectionRequestListener,ZoneRequestListener {
    ArrayList<User> result;
    Context context;
    int opponentId;
    String opponentName;
    private static LayoutInflater inflater=null;
    private WarpClient theClient;
    public ChallengeFriendAdapter(ChallengeFriendActivity challengeFriendActivity, ArrayList<User> prgmNameList) {

//        Log.d("getAdapterr",""+ prgmNameList.get(0));
        result=prgmNameList;
        context=challengeFriendActivity;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
//        return 0; Instead of returning 0 write result.size
        if(result.size()==0)
        {
            Toast.makeText(context,"You don't have any friends to challenge ",Toast.LENGTH_LONG).show();
            context.startActivity(new Intent(context,SearchActivity.class));
        }
        return result.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public void onConnectDone(ConnectEvent connectEvent) {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("challenge", "friend");
        theClient.createRoom("" + System.currentTimeMillis(), "Sachin", 2, properties);
    }

    @Override
    public void onDisconnectDone(ConnectEvent connectEvent) {

    }

    @Override
    public void onInitUDPDone(byte b) {

    }

    @Override
    public void onDeleteRoomDone(RoomEvent roomEvent) {

    }

    @Override
    public void onGetAllRoomsDone(AllRoomsEvent allRoomsEvent) {

    }

    @Override
    public void onCreateRoomDone(final RoomEvent event) {

        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(event.getResult()== WarpResponseResultCode.SUCCESS){// if room created successfully
                    String roomId = event.getData().getId();
                    joinRoom(roomId);
                    Log.d("onCreateRoomDone", event.getResult()+" "+roomId);
                }else{

                }
            }
        });




    }

    public void joinRoom(String roomId){
        if(roomId!=null && roomId.length()>0){
            goToGameScreen(roomId);
        }else{
            Log.d("joinRoom", "failed:"+roomId);
        }
    }

    private void goToGameScreen(String roomId){
        Intent intent = new Intent(context, PlayAfterChallengingActivity.class);
        intent.putExtra("roomId", roomId);
        intent.putExtra("opponentId",opponentId);
        intent.putExtra("opponentName",opponentName);
        context.startActivity(intent);
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
    public void onGetMatchedRoomsDone(MatchedRoomsEvent matchedRoomsEvent) {

    }

    @Override
    public void onGetRoomsCountDone(RoomEvent roomEvent) {

    }

    @Override
    public void onGetUsersCountDone(AllUsersEvent allUsersEvent) {

    }


    public class Holder
    {
        TextView userName;
        Button sendChallenge;
    }
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View rowView = convertView;
        Holder holder = null;

        if (rowView == null) {
            Log.d("getView","RowView is null");
            rowView = inflater.inflate(R.layout.challenge_friend, null);
            holder = new Holder();
            holder.userName = (TextView) rowView.findViewById(R.id.user_info);
            holder.sendChallenge=(Button) rowView.findViewById(R.id.challenge_send);
            rowView.setTag(holder);
        } else {
            holder = (Holder) rowView.getTag();
        }

        holder.userName.setText(result.get(position).getFirstName()+" "+result.get(position).getLastName());
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });

        holder.sendChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                opponentId=result.get(position).getId();
                opponentName=result.get(position).getFirstName()+" "+result.get(position).getLastName();
                init();
                Random rand = new Random();
                int  n = rand.nextInt(50) + 1;
                theClient.connectWithUserName("sGG"+n);
                HashMap<String, Object> properties = new HashMap<String, Object>();
                properties.put("challenge", "friend");


            }
        });

        return rowView;
    }

    private boolean checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            return true;

        }
        return false;
    }
    private void init(){
        WarpClient.initialize(Constants.apiKey, Constants.secretKey);
        try {
            theClient = WarpClient.getInstance();
        } catch (Exception ex) {
//            Utils.showToastAlert(context, "Exception in Initilization");
        }
        theClient.addConnectionRequestListener(this);
        //This needs to be added to the code to have  connecction done and get the success in onConnectDone
        theClient.addZoneRequestListener(this);
    }

}
