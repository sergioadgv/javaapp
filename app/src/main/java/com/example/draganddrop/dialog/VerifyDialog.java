package com.example.draganddrop.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.draganddrop.QuensActivity;
import com.example.draganddrop.R;
import com.example.draganddrop.db.SharePrefManager;
import com.example.draganddrop.model.code_verify_model.VerifyModel;
import com.example.draganddrop.network.Api;
import com.example.draganddrop.network.NetworkClient;
import com.example.luckydraw.model.registerUserModel.RegisterUserModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyDialog extends Dialog {

    EditText edt_refer;
    Button apply,cancel;
    SharePrefManager prefManager;
    Context context;
    String type,type_code;
    ProgressDialog progressDialog;
    OnClickApply onClickApply;

    public VerifyDialog(@NonNull Context context,String type,String type_code, OnClickApply onClickApply) {
        super(context);
        this.context = context;
        this.type = type;
        this.type_code = type_code;
        prefManager = new SharePrefManager(context);
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Loading...");
        this.onClickApply = onClickApply;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refer_dialog);
        edt_refer = findViewById(R.id.dt_refer_link);
        apply = findViewById(R.id.btn_copy);
        cancel = findViewById(R.id.btn_cancel);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = edt_refer.getText().toString();
                if(code.isEmpty()){
                    Toast.makeText(context,"Type code first",Toast.LENGTH_SHORT).show();
                }else {
                    verify(code);
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    void verify(String code){
        progressDialog.show();
        Api api = NetworkClient.getRetrofitClient().create(Api.class);
        Call<VerifyModel> call = api.virifyCode(prefManager.getUserId(),code,type,type_code,prefManager.getNAME());
        Log.e("verify",String.valueOf(prefManager.getUserId()+","+code+","+type+","+type_code));
        call.enqueue(new Callback<VerifyModel>() {
            @Override
            public void onResponse(Call<VerifyModel> call, Response<VerifyModel> response) {
                Log.e("verify",String.valueOf(response.body()));
                progressDialog.cancel();
                if(response!= null){
                    if(response.body().get(0).getStatus().equals("success")){

                        onClickApply.applyClickListener(type_code);
                        dismiss();

                    }else {
                        Toast.makeText(context,"this is not a valid code",Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<VerifyModel> call, Throwable t) {
                progressDialog.cancel();
                Log.e("verify",t.toString());


            }
        });
    }


    public interface OnClickApply{
        void applyClickListener(String id);
    }


}
