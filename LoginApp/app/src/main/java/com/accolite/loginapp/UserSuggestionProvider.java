package com.accolite.loginapp;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.List;

public class UserSuggestionProvider extends ContentProvider {

    List<String> cities;

    @Override
    public boolean onCreate() {
        return false;
    }
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        MatrixCursor cursor = new MatrixCursor(
                new String[] {
                        BaseColumns._ID,
                        SearchManager.SUGGEST_COLUMN_TEXT_1,
                        SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID
                }
        );


           /* cities=new ArrayList<String>();
            for(int i=0;i<100;i++)
                cities.add("FirstCity");
            cities.add("8053516559");
            Log.d("ContentProvider","Inside the content provider");
            String query = uri.getLastPathSegment().toUpperCase();
            int limit = Integer.parseInt(uri.getQueryParameter(SearchManager.SUGGEST_PARAMETER_LIMIT));
            Log.d("Valueis",query+" "+uri);
            int lenght = cities.size();
            for (int i = 0; i < lenght && cursor.getCount() < limit; i++) {
                String city = cities.get(i);

                if (city.toUpperCase().contains(query))
                    cursor.addRow(new Object[]{ i, city, i });

            }*/

        Log.d("AfterProvider","Inside the content provider");
        try {

            UtilClass.makeGetRequest("http://23.22.96.227/api/users");
            Log.d("UtilClass","Size is "+UtilClass.registeredUsers.size());
        } catch (IOException e) {

            e.printStackTrace();

        }
        int length = UtilClass.registeredUsers.size();
        String query = uri.getLastPathSegment().toUpperCase();
        int limit = Integer.parseInt(uri.getQueryParameter(SearchManager.SUGGEST_PARAMETER_LIMIT));

        for (int i = 0; i < length && cursor.getCount() < limit; i++)
        {
            ApiDatabase.ApiClass obj = UtilClass.registeredUsers.get(i);
            String userId=obj.id;
            String Name=obj.firstName+" "+obj.lastName;
            String emailId=obj.email;
            if (emailId.toUpperCase().equals(query)&&!(userId.equals(UtilClass.userId)))
                cursor.addRow(new Object[]{ i, emailId, i });
            else if(Name.toUpperCase().contains(query)&&!(userId.equals(UtilClass.userId)))
                cursor.addRow(new Object[]{ i, Name, i });
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }


}