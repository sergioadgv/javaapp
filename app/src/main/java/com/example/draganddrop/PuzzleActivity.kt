package com.example.draganddrop

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.draganddrop.adapter.AllPuzzleAdapter
import com.example.draganddrop.adapter.MyPuzelAdapter
import com.example.draganddrop.db.SharePrefManager
import com.example.draganddrop.dialog.AllPuzzlesDialog
import com.example.draganddrop.model.my_puzzle_model.MyPuzzleModel
import com.example.draganddrop.model.puzzle_model.PuzzleModel
import com.example.draganddrop.network.Api
import com.example.draganddrop.network.NetworkClient
import com.example.draganddrop.services.AddUserData
import kotlinx.android.synthetic.main.activity_puzzle.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PuzzleActivity : AppCompatActivity() {
    var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puzzle)

        progressDialog = ProgressDialog(this)

        add_fab.setOnClickListener {
            var allPuzzlesDialog: AllPuzzlesDialog = AllPuzzlesDialog(this@PuzzleActivity)
            allPuzzlesDialog.show()
        }
//        back_btn.setOnClickListener {
//            finish()
//        }
        getAllPuzzles()
    }



    fun onClickBack(view: View){
        finish()
    }


    fun getMyPuzzle(){
        progressDialog!!.show()
        var sharePrefManager: SharePrefManager = SharePrefManager(this)
        var api:Api = NetworkClient.getRetrofitClient().create(Api::class.java)
        var call:Call<MyPuzzleModel> = api.getMyPuzzles(sharePrefManager.userId)
        call.enqueue(object : Callback<MyPuzzleModel> {
            override fun onResponse(call: Call<MyPuzzleModel>?, response: Response<MyPuzzleModel>?) {
                progressDialog!!.cancel()

                var myPuzelAdapter: MyPuzelAdapter = MyPuzelAdapter(this@PuzzleActivity, response!!.body())
                puzle_recycler.layoutManager = GridLayoutManager(this@PuzzleActivity,2)
                puzle_recycler.adapter = myPuzelAdapter

            }

            override fun onFailure(call: Call<MyPuzzleModel>?, t: Throwable?) {
                progressDialog!!.cancel()

            }

        })
    }


    fun getAllPuzzles() {
        var sharePrefManager:SharePrefManager = SharePrefManager(this)
        progressDialog!!.show()
        val api = NetworkClient.getRetrofitClient().create(Api::class.java)
        val call = api.getAllPuzzle(sharePrefManager.userId)
        call.enqueue(object : Callback<PuzzleModel?> {
            override fun onResponse(call: Call<PuzzleModel?>, response: Response<PuzzleModel?>) {
                progressDialog!!.cancel()
                if(response!=null){

                    val allPuzzleAdapter = AllPuzzleAdapter(this@PuzzleActivity, response.body()!!)
                    puzle_recycler.layoutManager = GridLayoutManager(this@PuzzleActivity,2)
                    puzle_recycler.setAdapter(allPuzzleAdapter)
                }
            }

            override fun onFailure(call: Call<PuzzleModel?>, t: Throwable) {
                progressDialog!!.cancel()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        var addUserData = AddUserData(this,"Puzzle","menu")
        addUserData.adduserData()
    }
}