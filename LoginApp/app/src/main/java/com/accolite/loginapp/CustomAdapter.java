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

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Sachin Gupta on 9/20/2016.
 */
public class CustomAdapter extends BaseAdapter {
    ArrayList<ApiDatabase.ApiClass> result;
    Context context;
    private static LayoutInflater inflater=null;
    public CustomAdapter(PendingRequestsActivity pendingRequestsActivity, ArrayList<ApiDatabase.ApiClass> prgmNameList) {

        Log.d("getAdapter",""+ prgmNameList.get(0));
        result=prgmNameList;
        context=pendingRequestsActivity;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
//        return 0; Instead of returning 0 write result.size
        if(result.size()==0)
        {
            Toast.makeText(context,"No pending Requests Now",Toast.LENGTH_LONG).show();
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


    public class Holder
    {
        TextView userName;
        Button accept,delete;
    }
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View rowView = convertView;
        Holder holder = null;

        if (rowView == null) {
            Log.d("getView","RowView is null");
            rowView = inflater.inflate(R.layout.pending_users, null);
            holder = new Holder();
            holder.userName = (TextView) rowView.findViewById(R.id.user_info);
            holder.accept=(Button) rowView.findViewById(R.id.accept_friend);
            holder.delete=(Button) rowView.findViewById(R.id.delete_friend);
            rowView.setTag(holder);
        } else {
            holder = (Holder) rowView.getTag();
        }

        holder.userName.setText(result.get(position).firstName+" "+result.get(position).lastName);
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // /api/users/{id}/acceptFriendRequest
                // /api/users/{id}/ignoreFriendRequest

                String serverUrl = UtilClass.serverlUrl + "/" + UtilClass.userId + "/acceptFriendRequest";
                String userId = result.get(position).id;
                String jsonFormat = "{\"id\":" + "\"" + userId + "\"}";
                String res = null;
                Log.d("JsonIs", jsonFormat + " " + serverUrl);
                if (checkInternet()) {
                    try {
                        res = UtilClass.FriendRequest(serverUrl, jsonFormat);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (res.equals("true")) {
                        Integer index = (Integer) view.getTag();
                        result.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Friend Request Accepted", Toast.LENGTH_LONG).show();

                    } else
                        Toast.makeText(context, "BadRequest!!Try again later.", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(context,"Check Internet Connection!!", Toast.LENGTH_LONG).show();

                }

            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String serverUrl = UtilClass.serverlUrl + "/" + UtilClass.userId + "/acceptFriendRequest";
                String userId = result.get(position).id;
                String jsonFormat = "{\"id\":" + "\"" + userId + "\"}";
                String res = null;
                Log.d("JsonIs", jsonFormat + " " + serverUrl);
                if (checkInternet()) {
                    try {
                        res = UtilClass.FriendRequest(serverUrl, jsonFormat);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (res.equals("true")) {
                        Integer index = (Integer) view.getTag();
                        result.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Friend Request ignored", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(context, "BadRequest!!Try again later.", Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(context, "Check Internet Connection!!", Toast.LENGTH_LONG).show();

                }
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
