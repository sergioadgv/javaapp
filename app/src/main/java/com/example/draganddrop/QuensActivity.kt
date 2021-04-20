package com.example.draganddrop

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.draganddrop.db.SharePrefManager
import com.example.draganddrop.model.code_verify_model.VerifyModel
import com.example.draganddrop.model.quens_model.QuensModel
import com.example.draganddrop.model.quens_model.QuensModelItem
import com.example.draganddrop.network.Api
import com.example.draganddrop.network.NetworkClient
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_quens.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuensActivity : AppCompatActivity() {

    var quiz_id: String = "0"
    var quensModel: QuensModel = QuensModel()
    var progressDialog:ProgressDialog? = null
    var ans = "0"
    var i = 1
    var sharePrefManager:SharePrefManager? = null

    var ans_count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quens)
        progressDialog = ProgressDialog(this)
        progressDialog!!.setTitle("Loading")
        progressDialog!!.setCancelable(false)
        sharePrefManager = SharePrefManager(this)
        txt_quens.visibility = View.GONE
        option_1.visibility = View.GONE
        option_2.visibility = View.GONE
        option_3.visibility = View.GONE
        option_4.visibility = View.GONE
        quens_image.visibility = View.GONE

        btn_ans.setOnClickListener {

            var id: Int = option_group.checkedRadioButtonId

            if (id!=-1){ // If any radio button checked from radio group
                // Get the instance of radio button using id
                val radio: RadioButton = findViewById(id)
                var user_ans = "0"

                if(id == R.id.option_1){
                    user_ans = "1"
                }else if (id == R.id.option_2){
                    user_ans = "2"
                }else if (id == R.id.option_3){
                    user_ans = "3"
                }else if (id == R.id.option_4){
                    user_ans = "4"
                }
                if(user_ans.equals(ans)){
                    ans_count++
                    show_dialog(2, "Right answer", "You answer is correct")
                    submitAns(radio.text.toString(),"1",quensModel.get(i-1),intent.getStringExtra("quiz_id"),sharePrefManager!!.userId,sharePrefManager!!.name,sharePrefManager!!.lanCode)


                }else{
                    show_dialog(0, "Wrong answer", "Your answer is wrong")
                    submitAns(radio.text.toString(),"0",quensModel.get(i-1),intent.getStringExtra("quiz_id"),sharePrefManager!!.userId,sharePrefManager!!.name,sharePrefManager!!.lanCode)
                }



            }else{
                // If no radio button checked in this radio group
                Toast.makeText(
                        applicationContext,
                        " nothing selected",
                        Toast.LENGTH_SHORT
                ).show()
            }


        }
        getQuens(intent.getStringExtra("quiz_id"))
        back_btn.setOnClickListener {
            finish()
        }

    }

    fun getQuens(quiz_id: String){
        progressDialog!!.show()
        var api:Api = NetworkClient.getRetrofitClient().create(Api::class.java)
        var call: Call<QuensModel> = api.getQuens(quiz_id,sharePrefManager!!.lanCode)
        call.enqueue(object : Callback<QuensModel>{
            override fun onResponse(call: Call<QuensModel>?, response: Response<QuensModel>?) {
                progressDialog!!.cancel()
                if(response!!.body()!=null){
                    quensModel.addAll(response.body())
                    if (quensModel.size>0){
                        setView(quensModel.get(0),0)
                        total_qus_num.setText("Total Questions :"+quensModel.size.toString())

                    }


                }

            }

            override fun onFailure(call: Call<QuensModel>?, t: Throwable?) {
                progressDialog!!.cancel()

            }

        })
    }

    fun submitAns(ans:String,is_winner:String,quensModelItem: QuensModelItem,quiz_id: String,user_id: String,user_name:String,lan_code:String){
        var api:Api = NetworkClient.getRetrofitClient().create(Api::class.java)

        var call:Call<VerifyModel> = api.submitAns(user_id,user_name,quiz_id,quensModelItem.id,ans,lan_code,is_winner)

        call.enqueue(object : Callback<VerifyModel>{
            override fun onResponse(call: Call<VerifyModel>?, response: Response<VerifyModel>?) {


            }

            override fun onFailure(call: Call<VerifyModel>?, t: Throwable?) {

            }

        })


    }


    fun setView(quensModelItem: QuensModelItem,position:Int){

        qus_num.setText("Question No: "+(position+1).toString())
        txt_quens.setText(quensModelItem.quens)
        option_1.setText(quensModelItem.op1)
        option_2.setText(quensModelItem.op2)
        option_3.setText(quensModelItem.op3)
        option_4.setText(quensModelItem.op4)
        Picasso.get()
                .load(quensModelItem.image)
                .into(quens_image)
        txt_quens.visibility = View.VISIBLE
        option_1.visibility = View.VISIBLE
        option_2.visibility = View.VISIBLE
        option_3.visibility = View.VISIBLE
        option_4.visibility = View.VISIBLE
        quens_image.visibility = View.VISIBLE
        ans = quensModelItem.ans

    }


    fun show_dialog(flag: Int, mgs: String?, title: String?) {
        val builder1 = AlertDialog.Builder(this@QuensActivity)
        builder1.setTitle(title)
        builder1.setMessage(mgs)
        builder1.setCancelable(true)
        builder1.setPositiveButton(
                "Ok"
        ) { dialog, id ->
            dialog.cancel()
           option_group.clearCheck()

            if (quensModel.size>i){
                setView(quensModel.get(i),i)
                i++
                //Log.e("i", i.toString())
            }else{
                show_dialog_score()

            }

        }

        val alert11 = builder1.create()
        alert11.show()
    }

    fun show_dialog_score( ) {
        var title = "Your Score"
        var mgs = "Your Score is "+ans_count.toString()+"/"+quensModel.size.toString()
        val builder1 = AlertDialog.Builder(this@QuensActivity)
        builder1.setTitle(title)
        builder1.setMessage(mgs)
        builder1.setCancelable(true)
        builder1.setPositiveButton(
                "Ok"
        ) { dialog, id ->
            dialog.cancel()

            finish()

        }

        val alert11 = builder1.create()
        alert11.show()
    }
}