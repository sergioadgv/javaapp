package com.example.draganddrop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.draganddrop.services.AddUserData;

public class Wasme extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wasme);
        AddUserData addUserData =new AddUserData(this,"Wasme","0");
        addUserData.adduserData();
    }
}