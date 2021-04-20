package com.example.draganddrop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.draganddrop.adapter.QuizAdapter
import com.example.draganddrop.db.SharePrefManager
import com.example.draganddrop.model.quiz_model.QuizModel
import com.example.draganddrop.network.Api
import com.example.draganddrop.network.NetworkClient
import com.example.draganddrop.services.AddUserData
import kotlinx.android.synthetic.main.activity_quens.*
import kotlinx.android.synthetic.main.activity_quiz.*
import kotlinx.android.synthetic.main.activity_quiz.back_btn
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuizActivity : AppCompatActivity() {
    var sharePrefManager: SharePrefManager ?= null
    var eng_str = "eng"
    var dtc_str = "dtc"
    var spa_str = "spa"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        sharePrefManager = SharePrefManager(this)

//        BACK BUTTON
        back_btn.setOnClickListener {
            finish()
        }

        //getQuiz(sharePrefManager!!.userId)
    }



//    Populate Recycler
    fun getQuiz(userId:String,lan:String){
        progress_ber.visibility = View.VISIBLE
        var api:Api = NetworkClient.getRetrofitClient().create(Api::class.java)
        var call:Call<QuizModel> = api.getQuiz(userId,lan)
        call.enqueue(object :Callback<QuizModel>{
            override fun onResponse(call: Call<QuizModel>?, response: Response<QuizModel>?) {
                progress_ber.visibility = View.GONE
                Log.e("response",response!!.body().toString())
                if (response!=null){
                   var quizModel: QuizModel = response.body()

                    if (quizModel.size == 0){
                        quiz_recycler.visibility = View.GONE
                        emp_txt.visibility = View.VISIBLE
                    }else{
                        var quizAdapter:QuizAdapter = QuizAdapter(this@QuizActivity,quizModel)
                        quiz_recycler.layoutManager = GridLayoutManager(this@QuizActivity,2)
                        quiz_recycler.adapter = quizAdapter


                    }
                }else{
                    quiz_recycler.visibility = View.GONE
                    emp_txt.visibility = View.VISIBLE

                }

            }

            override fun onFailure(call: Call<QuizModel>?, t: Throwable?) {
                progress_ber.visibility = View.GONE


            }

        })
    }


//    MULTI LANGUAGE FUNCTIONALITY
    override fun onResume() {
        super.onResume()
        val lang = sharePrefManager!!.language
        var lan_code = "1"

        if (lang == eng_str) {
            lan_code = "1"


        } else if (lang == dtc_str) {
            lan_code = "2"

        } else if (lang == spa_str) {
            lan_code = "3"
        }
        getQuiz(sharePrefManager!!.userId,lan_code)



        var addUserData = AddUserData(this,"Quiz","menu")
        addUserData.adduserData()

    }


}