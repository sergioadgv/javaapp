package com.example.draganddrop.services;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.example.draganddrop.db.SharePrefManager;
import com.example.draganddrop.model.code_verify_model.VerifyModel;
import com.example.draganddrop.network.Api;
import com.example.draganddrop.network.NetworkClient;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddUserData {
    Context context;
    SharePrefManager sharePrefManager;
    String pageName;
    String type_name;

    public AddUserData(Context context,String pageName,String type_name) {
        this.context = context;
        this.pageName = pageName;
        this.type_name = type_name;

        sharePrefManager = new SharePrefManager(context);
    }
    public void adduserData(){
        GpsTracker gpsTracker=new GpsTracker(context);

        double lat=gpsTracker.getLat();
        double lng=gpsTracker.getLon();
        String location = getAddress(lat,lng);

        Api api = NetworkClient.getRetrofitClient().create(Api.class);
        Call<VerifyModel> call = api.addUserActivity(pageName,sharePrefManager.getUserId(),sharePrefManager.getNAME(),location,String.valueOf(lat),String.valueOf(lng),sharePrefManager.getSeasonCode(),getCurrentDateForSaveBook(),sharePrefManager.getCountry(),type_name);
        call.enqueue(new Callback<VerifyModel>() {
            @Override
            public void onResponse(Call<VerifyModel> call, Response<VerifyModel> response) {

            }

            @Override
            public void onFailure(Call<VerifyModel> call, Throwable t) {

            }
        });

    }



    public String getAddress(double lat,double lon){
        String address_txt="no address found";

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);
            // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            if(addresses.size()==0){

            }else {
                address_txt = addresses.get(0).getAddressLine(0);
            }
            // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

        } catch (IOException e) {
            e.printStackTrace();
        }
        return address_txt;

    }

    public String getCurrentDateForSaveBook(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }


}
