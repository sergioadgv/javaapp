package com.example.draganddrop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.draganddrop.R;
import com.example.draganddrop.network.Api;
import com.example.draganddrop.network.NetworkClient;
import com.example.draganddrop.network.NewsClient;
import com.example.luckydraw.model.add_user_response.AddUserResponse;
import com.example.luckydraw.model.registerUserModel.RegisterUserModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.edt_user_name)
    EditText edtUsername;
    @BindView(R.id.edt_pass)
    EditText edtpass;
    @BindView(R.id.edt_re_pass)
    EditText edtRePass;
    @BindView(R.id.edt_email)
    EditText edtEmail;
    @BindView(R.id.btn_sign_up)
    Button btnSignUp;
    @BindView(R.id.edt_user_country)
    EditText edtCountry;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtUsername.getText().toString();
                String pass = edtpass.getText().toString();
                String email = edtEmail.getText().toString();
                String rePass = edtRePass.getText().toString();
                String country = edtCountry.getText().toString();
                if(pass.isEmpty() || rePass.isEmpty() || name.isEmpty() || email.isEmpty() ){
                    toast("Enter all fields");

                }else {
                    if(pass == rePass){
                        toast("Password not natch");
                        Log.e("pass",pass+","+rePass);

                    }else {
                        if(country.isEmpty()){
                            country = "0";
                        }
                        registerUser(name,email,pass,country);
                    }
                }



            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

    void registerUser(String name,String email,String pass,String country){
        Api api = NetworkClient.getRetrofitClient().create(Api.class);
        Call<AddUserResponse> call = api.addUser( name,email,pass,country);
        call.enqueue(new Callback<AddUserResponse>() {
            @Override
            public void onResponse(Call<AddUserResponse> call, Response<AddUserResponse> response) {
                if(response.body().get(0).getStatus().equals("success")){
                    toast(response.body().get(0).getMessage());
                    finish();
                }else {
                    toast(response.body().get(0).getMessage());
                }



            }

            @Override
            public void onFailure(Call<AddUserResponse> call, Throwable t) {

            }
        });

    }

    void toast(String s){
        Toast.makeText(RegisterActivity.this,s,Toast.LENGTH_SHORT).show();
    }


}