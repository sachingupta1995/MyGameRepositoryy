package com.accolite.loginapp;

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

import java.util.ArrayList;

import multiplayerdemo.PendingChallengesData;
import multiplayerdemo.PlayAfterAcceptingChallengingActivity;
import multiplayerdemo.PlayOptionsActivity;

/**
 * Created by Sachin Gupta on 9/20/2016.
 */
public class PendingChallengeAdapter extends BaseAdapter {
    ArrayList<PendingChallengesData> result;
    Context context;
    private int challengerId;
    private String userName;
    private String roomId;
    private static LayoutInflater inflater = null;

    public PendingChallengeAdapter(PendingChallengesActivity pendingChallengeActivity, ArrayList<PendingChallengesData> prgmNameList) {

        Log.d("getAdapter", "" + prgmNameList.get(0));
        result = prgmNameList;
        context = pendingChallengeActivity;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
//        return 0; Instead of returning 0 write result.size
        if (result.size() == 0) {
            Toast.makeText(context, "No pending Challenges Now", Toast.LENGTH_LONG).show();
            context.startActivity(new Intent(context, PlayOptionsActivity.class));
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


    public class Holder {
        TextView userName;
        Button accept, delete;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View rowView = convertView;
        Holder holder = null;

        if (rowView == null) {
            Log.d("getView", "RowView is null");
            rowView = inflater.inflate(R.layout.pending_challenges, null);
            holder = new Holder();
            holder.userName = (TextView) rowView.findViewById(R.id.user_info);
            holder.accept = (Button) rowView.findViewById(R.id.challenge_accept);
            holder.delete = (Button) rowView.findViewById(R.id.ignore_challenge);
            rowView.setTag(holder);
        } else {
            holder = (Holder) rowView.getTag();
        }

        holder.userName.setText(result.get(position).userObject.getFirstName() + " " + result.get(position).userObject.getLastName());
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String serverUrl = UtilClass.acceptChallengeUrl;
                roomId = result.get(position).roomIdObject;
                challengerId = result.get(position).userObject.getId();
                userName = result.get(position).userObject.getFirstName() + " " + result.get(position).userObject.getLastName();
                Intent i = new Intent(context, PlayAfterAcceptingChallengingActivity.class);
                i.putExtra("roomId", roomId);
                i.putExtra("challengerName", userName);
                i.putExtra("challengerId", challengerId);
                context.startActivity(i);




/*
                Log.d("JsonIs", jsonFormat + " " + serverUrl);
                if (checkInternet())
                {
                    try {
                        RetrievingData.acceptChallenge(serverUrl, jsonFormat);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                        Integer index = (Integer) view.getTag();
                        result.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Challenge Accepted", Toast.LENGTH_LONG).show();

                }
                else {
                    Toast.makeText(context,"Check Internet Connection!!", Toast.LENGTH_LONG).show();

                }
*/

            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer index = (Integer) view.getTag();
                result.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "Challenge Ignored", Toast.LENGTH_LONG).show();

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


}
