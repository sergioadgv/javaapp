package com.example.draganddrop

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.draganddrop.model.change_pass_model.ChangePassModel
import com.example.draganddrop.network.Api
import com.example.draganddrop.network.NetworkClient
import kotlinx.android.synthetic.main.activity_change_pass.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePassActivity : AppCompatActivity() {
    var vefification_code:String? = null
    var email_verify :String ? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_pass)
        layout_enter_email.visibility = View.VISIBLE
        layout_email_verify.visibility = View.GONE
        layout_new_pass.visibility = View.GONE
        hideProgress()

        btn_send_email.setOnClickListener {
            if (edt_email.text.isNullOrEmpty()){
                toat("Enter a email")
            }else{
                sendEmail(edt_email.text.toString())
            }
        }
        btn_verify.setOnClickListener {
            if(edt_code.text.isNullOrEmpty()){
                toat("Enter code")
            }else{
                if(vefification_code==edt_code.text.toString()){
                    layout_enter_email.visibility = View.GONE
                    layout_email_verify.visibility = View.GONE
                    layout_new_pass.visibility = View.VISIBLE
                }
            }
        }
        btn_login.setOnClickListener {
            if(edt_pass.text.isNullOrEmpty()){
                toat("Enter password")
            }else{
                if(edt_confirm_pass.text.toString() == edt_pass.text.toString()){
                    email_verify?.let { it1 -> changePass(it1,edt_pass.text.toString()) }
                }else{
                    toat("Password must be match")
                }
            }
        }




    }

    fun backPress(view: View){
        finish()
    }

    fun sendEmail(email:String){
        showProgress()
        var api: Api = NetworkClient.getRetrofitClient().create(Api::class.java)
        var call:Call<ChangePassModel> = api.changePass(email)
        call.enqueue(object :Callback<ChangePassModel>{
            override fun onFailure(call: Call<ChangePassModel>?, t: Throwable?) {
                hideProgress()

            }

            override fun onResponse(call: Call<ChangePassModel>?, response: Response<ChangePassModel>?) {
                if (response != null) {
                    email_verify = email;

                    vefification_code = response.body().get(0).code.toString()
                    Log.e("code",vefification_code)
                    layout_enter_email.visibility = View.GONE
                    layout_email_verify.visibility = View.VISIBLE
                    layout_new_pass.visibility = View.GONE
                }
                hideProgress()

            }

        })

    }

    fun changePass(email:String,pass:String){
        showProgress()

        var api: Api = NetworkClient.getRetrofitClient().create(Api::class.java)
        var call:Call<String> = api.changePassByEmail(email,pass)
        call.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>?, t: Throwable?) {
                hideProgress()

            }

            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                hideProgress()
                finish()
                toat("Password reset successfully")
            }

        })
    }

    fun showProgress(){
        progress_bar.visibility = View.VISIBLE
    }
    fun hideProgress(){
        progress_bar.visibility = View.GONE
    }
    fun toat(mgs:String){
        Toast.makeText(this,mgs,Toast.LENGTH_SHORT).show()
    }
}