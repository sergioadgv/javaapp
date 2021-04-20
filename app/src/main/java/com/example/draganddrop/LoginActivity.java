package com.example.draganddrop;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.draganddrop.db.SharePrefManager;
import com.example.draganddrop.network.Api;
import com.example.draganddrop.network.NetworkClient;
import com.example.luckydraw.model.UserModel;
import com.example.luckydraw.model.UserModelItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.edt_user_name)
    EditText edt_userName;
    @BindView(R.id.edt_pass)
    EditText edt_pass;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_register)
    Button register;
    @BindView(R.id.progress_ber)
    ProgressBar progressBar;
    @BindView(R.id.btn_demo)
    Button demoBtn;
    @BindView(R.id.show_pass)
    ImageView showPassBtn;
    @BindView(R.id.ck_box_trams)
    CheckBox tramsAndConditionsCkBox;
    @BindView(R.id.txt_trams)
    TextView txtTrams;
    @BindView(R.id.txt_forgot_pass)
    TextView forgetPassBtn;
    SharePrefManager sharePrefManager;
    ProgressDialog progressDialog;

    boolean passIsShown = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        sharePrefManager = new SharePrefManager(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = edt_userName.getText().toString();
                String pass = edt_pass.getText().toString();
                if(userName.isEmpty() ){
                    toast("Enter your email..");

                }else if(pass.isEmpty()){
                    toast("Enter password..");

                }else {
                    if(tramsAndConditionsCkBox.isChecked()){
                        login(userName,pass);
                    }else {
                        toast("Please confirm our terms & conditions.");
                    }

                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        demoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login("demo","123456");
            }
        });

        showPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passIsShown){
                    edt_pass.setTransformationMethod(new PasswordTransformationMethod());
                    showPassBtn.setImageDrawable(getResources().getDrawable(R.drawable.hide));
                    passIsShown = false;
                }else {
                    edt_pass.setTransformationMethod(null);
                    showPassBtn.setImageDrawable(getResources().getDrawable(R.drawable.show));
                    passIsShown = true;
                }

            }
        });
        forgetPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ChangePassActivity.class));
            }
        });

    }

    void toast(String s){
        Toast.makeText(LoginActivity.this,s,Toast.LENGTH_SHORT).show();
    }

    void login(String userName,String pass){
        progressDialog.show();
        progressBar.setVisibility(View.VISIBLE);
        Api service = NetworkClient.getRetrofitClient().create(Api.class);
        Call<UserModel> call = service.userLogin(userName,pass);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                Log.e("response",response.body().toString());
                progressBar.setVisibility(View.GONE);
                if(response!=null){
                    UserModelItem userModelItem= response.body().get(0);
                    if(userModelItem.getStatus().equals("success")){
                        sharePrefManager.saveProfileData(response.body().get(0));

                        progressDialog.cancel();
                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                        // set the new task and clear flags
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.putExtra("login",true);
                        startActivity(i);
                        finish();


                    }else {
                        toast("UserName and Password did not match..");
                        progressDialog.cancel();

                    }
                }



            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.e("error",t.toString());
                progressBar.setVisibility(View.GONE);
                progressDialog.cancel();

            }
        });
    }
}