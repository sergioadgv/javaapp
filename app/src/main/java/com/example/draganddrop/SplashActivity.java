package com.example.draganddrop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.draganddrop.db.SharePrefManager;
import com.example.draganddrop.model.splash_details_model.SplashDetailsModel;
import com.example.draganddrop.network.Api;
import com.example.draganddrop.network.NetworkClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    SharePrefManager sharePrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharePrefManager = new SharePrefManager(this);
        sharePrefManager.saveSeasoncode(sharePrefManager.getUserId()+getCurrentDateForSaveBook());
        final Handler handler = new Handler();
        getSplashDetails();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                if(sharePrefManager.isLogin()){
                    sharePrefManager.saveFirstTime(true);
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    finish();

                }else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();

                }


            }
        }, 1000*2);
    }

    public String getCurrentDateForSaveBook(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("hhmmss");

        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }

    private void getSplashDetails(){
        Api api = NetworkClient.getRetrofitClient().create(Api.class);
        Call<SplashDetailsModel> call = api.getSplashDetails();
        call.enqueue(new Callback<SplashDetailsModel>() {
            @Override
            public void onResponse(Call<SplashDetailsModel> call, Response<SplashDetailsModel> response) {

            }

            @Override
            public void onFailure(Call<SplashDetailsModel> call, Throwable t) {

            }
        });
    }

}