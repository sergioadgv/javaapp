package com.example.draganddrop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.draganddrop.db.SharePrefManager;
import com.example.draganddrop.services.AddUserData;

public class ProfileActivity extends AppCompatActivity {

    TextView profileEmail,profileName,profileCountry,language;
    ImageView backImage;

    SharePrefManager sharePrefManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profileEmail = findViewById(R.id.profile_email);
        profileName = findViewById(R.id.prodile_user_name);
        backImage = findViewById(R.id.back);
        profileCountry = findViewById(R.id.profile_country);
        language = findViewById(R.id.tv_language_settings);
        sharePrefManager = new SharePrefManager(this);

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        profileEmail.setText(sharePrefManager.getEmail());
        profileName.setText(sharePrefManager.getNAME());
        profileCountry.setText(sharePrefManager.getCountry());
        language.setText(sharePrefManager.getLanguage());

        AddUserData addUserData =new AddUserData(this,"Profile","0");
        addUserData.adduserData();



    }
}