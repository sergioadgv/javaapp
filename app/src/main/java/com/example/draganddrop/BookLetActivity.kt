package com.example.draganddrop

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.draganddrop.adapter.BookLetAdapter
import com.example.draganddrop.db.SharePrefManager
import com.example.draganddrop.dialog.VerifyDialog
import com.example.draganddrop.model.bookletModel.BookLetModel
import com.example.draganddrop.model.bookletModel.BookLetModelItem
import com.example.draganddrop.model.code_verify_model.VerifyModel
import com.example.draganddrop.network.Api
import com.example.draganddrop.network.NetworkClient
import com.example.draganddrop.services.AddUserData
import kotlinx.android.synthetic.main.activity_book_let.*
import kotlinx.android.synthetic.main.activity_quiz.*
import kotlinx.android.synthetic.main.activity_quiz.back_btn
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookLetActivity : AppCompatActivity() {

    var progressDialog:ProgressDialog ? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_let)

        progressDialog = ProgressDialog(this)
        progressDialog!!.setTitle("Loading...")

//        back_btn.setOnClickListener {
//            finish()
//        }
        getBooklet()


    }

    fun onClickBack(view: View){
        finish()
    }


    fun onClickLayout(view: View){
        var sharePrefManager:SharePrefManager = SharePrefManager(this@BookLetActivity)
        ckBooklet("1",sharePrefManager.userId,"0")

    }


    fun goToPage(bookletId: String,bName:String){
        startActivity(Intent(this,PageActivity::class.java).putExtra("bid",bookletId).putExtra("b_name",bName))

    }


    fun addBooklet(bookletId:String,userid:String,bName:String){
        progressDialog!!.show()
        var api: Api = NetworkClient.getRetrofitClient().create(Api::class.java)
        var call:Call<VerifyModel> = api.addBooklet(userid,bookletId)
        call.enqueue(object: Callback<VerifyModel>{
            override fun onResponse(call: Call<VerifyModel>?, response: Response<VerifyModel>?) {
                progressDialog!!.cancel()
                if(response!!.body().get(0).status.equals("success")){
                    goToPage(bookletId,bName)
                }

            }

            override fun onFailure(call: Call<VerifyModel>?, t: Throwable?) {
                progressDialog!!.cancel()

            }

        })


    }

    fun ckBooklet(bookletId:String,userid:String,bName:String){
        progressDialog!!.show()
        var api: Api = NetworkClient.getRetrofitClient().create(Api::class.java)
        var call:Call<VerifyModel> = api.checkBooklet(userid,bookletId)
        call.enqueue(object: Callback<VerifyModel>{
            override fun onResponse(call: Call<VerifyModel>?, response: Response<VerifyModel>?) {
                progressDialog!!.cancel()
                if(response!!.body().get(0).status.equals("success")){
                    goToPage(bookletId,bName)
                }else{

                    var sharePrefManager = SharePrefManager(this@BookLetActivity)

                    if(sharePrefManager.name.equals("demo")){
                        Toast.makeText(this@BookLetActivity,"Sorry you are using demo account", Toast.LENGTH_SHORT).show()
                    }else{
                        var verrifyDialog: VerifyDialog = VerifyDialog(this@BookLetActivity,"booklet",bookletId,object:VerifyDialog.OnClickApply{
                            override fun applyClickListener(id: String?) {

                                var sharePrefManager:SharePrefManager = SharePrefManager(this@BookLetActivity)

                                addBooklet(bookletId,sharePrefManager.userId,bName)
                            }

                        })
                        verrifyDialog.show()
                    }

                }

            }

            override fun onFailure(call: Call<VerifyModel>?, t: Throwable?) {
                progressDialog!!.cancel()

            }

        })


    }

    override fun onResume() {
        super.onResume()
        var addUserData = AddUserData(this,"Booklet","menu")
        addUserData.adduserData()
    }

    fun  getBooklet(){
        var sharePrefManager:SharePrefManager = SharePrefManager(this@BookLetActivity)
        var api:Api = NetworkClient.getRetrofitClient().create(Api::class.java)
        var call: Call<BookLetModel> = api.getBooklet(sharePrefManager!!.userId)
        call.enqueue(object: Callback<BookLetModel>{
            override fun onFailure(call: Call<BookLetModel>?, t: Throwable?) {


            }

            override fun onResponse(call: Call<BookLetModel>?, response: Response<BookLetModel>?) {
                var bookLetAdapter = BookLetAdapter(this@BookLetActivity,response!!.body(),object: BookLetAdapter.Listener{
                    override fun onClikBooklet(bookLetModelItem: BookLetModelItem) {
                       ckBooklet(bookLetModelItem.id,sharePrefManager!!.userId,bookLetModelItem.booklet_name)
                    }

                })
                bookletRecycler.layoutManager = GridLayoutManager(this@BookLetActivity,3)
                bookletRecycler.adapter = bookLetAdapter


            }

        })
    }
}