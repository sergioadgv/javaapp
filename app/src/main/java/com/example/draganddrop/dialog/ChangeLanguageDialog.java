package com.example.draganddrop.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.draganddrop.LocaleManager;
import com.example.draganddrop.R;
import com.example.draganddrop.db.SharePrefManager;

public class ChangeLanguageDialog extends Dialog {

    Button button;
    RadioButton genderradioButton;
    RadioGroup radioGroup;
    Context context;
    SharePrefManager sharePrefManager;
    RadioButton eng_radio,dtc_radio,spa_radio;
    String eng_str="eng",dtc_str="dtc",spa_str="spa";
    onClick listener;

    public ChangeLanguageDialog(@NonNull Context context,onClick listener) {
        super(context);
        this.context = context;
        sharePrefManager = new SharePrefManager(context);
        this.listener = listener;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_language_dialog);
        radioGroup=(RadioGroup)findViewById(R.id.radio_group);
        eng_radio = findViewById(R.id.radio_english);
        dtc_radio = findViewById(R.id.radio_dutch);
        spa_radio = findViewById(R.id.radio_spanish);
        button = findViewById(R.id.btn_choose_language);
        String lang = sharePrefManager.getLanguage();
        Log.e("language",lang);
        if(lang.equals(eng_str)){
            eng_radio.setChecked(true);
        }else if(lang.equals(dtc_str)){
            dtc_radio.setChecked(true);
        }else if(lang.equals(spa_str)){
            spa_radio.setChecked(true);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickbuttonMethod();
            }
        });
    }

    public void onclickbuttonMethod(){
        int selectedId = radioGroup.getCheckedRadioButtonId();
        genderradioButton = (RadioButton) findViewById(selectedId);
        if(selectedId==-1){

        }
        else{
            String lang = genderradioButton.getText().toString();
            Log.e("language",lang);
            if(lang.equals("English")){
                sharePrefManager.savelanguage(eng_str);

            }else if(lang.equals("Dutch")){
                sharePrefManager.savelanguage(dtc_str);
            }else if(lang.equals("Spanish")){
                sharePrefManager.savelanguage(spa_str);
            }
            listener.chooseLanguage(lang);


        }

        dismiss();

    }

    public interface onClick{
        public void chooseLanguage(String lan);
    }


}
