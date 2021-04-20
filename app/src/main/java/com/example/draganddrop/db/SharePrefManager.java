package com.example.draganddrop.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.draganddrop.model.PuzzleScorModel;
import com.example.luckydraw.model.UserModelItem;
import com.google.gson.Gson;

import java.util.ArrayList;

public class SharePrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String LOGINDATA = "login";
    String PROFILE_NAME = "profile_name";
    String PROFILE_ID = "profile_id";
    String PROFILE_POINT = "profile_point";
    String ISLOGIN = "is_login";
    String PROFILE_PASS = "pass";
    String PROFILE_EMAIL = "profile_email";
    String PROFILE_TELEGRAM = "profile_telegram";
    String PROFILE_SLOTS = "profile_slots";
    String PROFILE_REFER = "profile_refer";
    String PROFILE_IMAGE = "profile_image";
    String PROFILE_SILVER = "profile_silver";
    String PROFILE_GOLD = "profile_gold";
    String PROFILE_PTOKEN = "profile_ptoken";
    String PROFILE_RM = "profile_RM";
    String LANGUAGE = "language";
    String eng_str="eng",dtc_str="dtc",spa_str="spa";
    String DEMOCOUNT = "demo_count";
    String SEASONCODE = "season_code";
    String USERCOUNTRY = "user_country";
    String isOfferShown = "offer_shown";




    // shared pref mode
    int PRIVATE_MODE = 0;
    Context _context;

    // Shared preferences file name
    private static final String PREF_NAME = "androidhive-welcome";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public SharePrefManager(Context context) {
        this._context = context.getApplicationContext();
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void
    saveProfileData(UserModelItem response){
        editor.putBoolean(ISLOGIN,true);
        editor.putString(PROFILE_ID, String.valueOf(response.getId()));
        editor.putString(PROFILE_NAME,response.getUser_name());
        editor.putString(PROFILE_PASS,response.getPassword());
        editor.putString(PROFILE_EMAIL,response.getEmail());
        editor.putString(USERCOUNTRY,response.getCountry());

        editor.commit();
    }


    public void saveFirstTime(boolean b){
        editor.putBoolean(IS_FIRST_TIME_LAUNCH,b);
        editor.commit();
    }
    public boolean getFirstTime(){
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH,true);
    }


public String getCountry(){
        return  pref.getString(USERCOUNTRY,"0");
}

    public String getImage(){
        return pref.getString(PROFILE_IMAGE,"0");
    }

    public String getSlots(){
        return pref.getString(PROFILE_SLOTS,"0");
    }

    public String getPass(){
        return pref.getString(PROFILE_PASS,"0");
    }

    public String getPin(){
     return pref.getString("profile_pin","0");
    }
    public String getUserId(){
        return pref.getString(PROFILE_ID,"0");
    }
    public boolean isLogin(){
        return pref.getBoolean(ISLOGIN,false);
    }
    public String getPoint(){
        return pref.getString(PROFILE_POINT,"0");
    }
    public String getSilver(){
        return pref.getString(PROFILE_SILVER,"0");
    }
    public String getPToken(){
        return pref.getString(PROFILE_PTOKEN,"0");
    }
    public String getRm(){
        return pref.getString(PROFILE_RM,"0");
    }
    public String getGold(){
        return pref.getString(PROFILE_GOLD,"0");
    }


    public String getNAME(){
        return pref.getString(PROFILE_NAME,"0");
    }

    public String getEmail(){
        return pref.getString(PROFILE_EMAIL,"null");
    }
    public String getTelegram(){
        return pref.getString(PROFILE_TELEGRAM,"null");
    }
    public String getRefer(){
        return  pref.getString(PROFILE_REFER,"000001");
    }

    public void setPoint(String point){
        editor.putString(PROFILE_POINT,point);
        editor.commit();
    }

    public void saveGold(String gold){
        editor.putString(PROFILE_GOLD,gold);
        editor.commit();
    }
    public void saveSilver(String silver){
        editor.putString(PROFILE_SILVER,silver);
        editor.commit();
    }
    public void saveSlot(String slot){
        editor.putString(PROFILE_SLOTS,slot);
        editor.commit();
    }

    public void savePTokens(String pToken){
        editor.putString(PROFILE_PTOKEN,pToken);
        editor.commit();
    }
    public void saveRM(String rm){
        editor.putString(PROFILE_RM,rm);
        editor.commit();
    }

    public void setLogout(){
        editor.putBoolean(ISLOGIN,false);
        editor.apply();
    }

    public void savelanguage(String language){
        editor.putString(LANGUAGE,language);
        editor.commit();
    }
    public String getLanguage(){
        return pref.getString(LANGUAGE,"eng");
    }

    public void saveOfferId(int id){
        editor.putInt("offer_id",id);
        editor.commit();
    }

    public  int getOfferId(){
        return pref.getInt("offer_id",0);
    }
    public void  saveSeasoncode(String code){
        editor.putString(SEASONCODE,code);
        editor.commit();
    }
    public String getSeasonCode(){
        return pref.getString(SEASONCODE,"0");
    }

    public String getLanCode(){
        String lang = pref.getString(LANGUAGE,"eng");
        String lan_code = "1";

        if (lang == eng_str) {
            lan_code = "1";


        } else if (lang == dtc_str) {
            lan_code = "2";

        } else if (lang == spa_str) {
            lan_code = "3";
        }

        return  lan_code;
    }
    public void saveDemoCount(){
        int count = getDemoAccount()+1;
        editor.putInt(DEMOCOUNT,count);
        editor.commit();
    }

    public int getDemoAccount(){
         return pref.getInt(DEMOCOUNT,0);
    }

    public  boolean ifDemoPossible(){
        int count = getDemoAccount();
        if(count<=3){
            return  true;
        }else {
            return  false;
        }
    }

    public void savePuzzlescore(ArrayList<PuzzleScorModel> puzzleScorModels){
        Gson gson = new Gson();
        String json = gson.toJson(puzzleScorModels);
        editor.putString("score",json);
        editor.commit();
    }


    public void saveOfferShown(String s){
        editor.putString(isOfferShown,s);
        editor.commit();
    }
    public String getOfferShown(){
        return pref.getString(isOfferShown,"0");
    }






}
