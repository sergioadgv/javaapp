package com.example.draganddrop

import android.content.Intent
import android.graphics.LinearGradient
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.draganddrop.db.SharePrefManager
import com.example.draganddrop.dialog.OfferDialog
import com.example.draganddrop.model.offer_model.OfferModel
import com.example.draganddrop.network.Api
import com.example.draganddrop.network.NetworkClient
import com.example.draganddrop.services.AddUserData
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeActivity : BaseActivity(){

    var sharePrefManager:SharePrefManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        sharePrefManager = SharePrefManager(this)





        getOffer()
/*
        drawer_btn.setOnClickListener(View.OnClickListener {
            if (drawer.isDrawerOpen(Gravity.LEFT)) {
                drawer.closeDrawer(Gravity.LEFT)
            } else {
                drawer.openDrawer(Gravity.LEFT)
            }
        })


        text_title.setText(sharePrefManager!!.name.toString())

        logout_btn.setOnClickListener {
            sharePrefManager!!.setLogout()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        nav_shop_sticker.setOnClickListener {
            startActivity(Intent(this, WebViewActivity::class.java))

        }
        nav_sticker_collection.setOnClickListener {
            startActivity(Intent(this, BookLetActivity::class.java))
        }
        nav_puzle.setOnClickListener {
            startActivity(Intent(this, PuzzleActivity::class.java))
        }
        nav_quiz.setOnClickListener {
            startActivity(Intent(this, QuizActivity::class.java))
        }
        nav_my_profile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        nav_world.setOnClickListener {
            startActivity(Intent(this, Wasme::class.java))
        }
        about.setOnClickListener {
            startActivity(Intent(this, Wasme::class.java))
        }


        change_lan.setOnClickListener {
            var changeLanguageDialog = ChangeLanguageDialog(this, object : ChangeLanguageDialog.onClick {
                override fun chooseLanguage(lan: String?) {
                    if (lan.equals("English")) {
                        setNewLocale(this@HomeActivity, LocaleManager.ENGLISH)
                    } else if (lan.equals("Dutch")) {
                        setNewLocale(this@HomeActivity, LocaleManager.DUTCH)
                    } else if (lan.equals("Spanish")) {
                        setNewLocale(this@HomeActivity, LocaleManager.SPANISH)
                    }

                }

            })
            changeLanguageDialog.show()
        }

*/


//        Dexter.withContext(this)
//                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
//                .withListener(object : PermissionListener {
//                    override fun onPermissionGranted(response: PermissionGrantedResponse) { /* ... */
//                    }
//
//                    override fun onPermissionDenied(response: PermissionDeniedResponse) { /* ... */
//                    }
//
//                    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) { /* ... */
//                    }
//                }).check()



    }



    fun onClickQuiz(view: View){
        startActivity(Intent(this, QuizActivity::class.java))
    }

    fun onClickPuzzle(view: View){
        startActivity(Intent(this, PuzzleActivity::class.java))
    }
    fun onClickBookLet(view: View){
        startActivity(Intent(this, BookLetActivity::class.java))
    }

    fun onClickShop(view: View){
        startActivity(Intent(this, WebViewActivity::class.java))
    }
    fun onClickWasme(view: View){
        startActivity(Intent(this, Wasme::class.java))
    }

    fun onClickProfile(view: View) {
        startActivity(Intent(this, ProfileActivity::class.java))
    }

    fun onClickMenu(view: View){
         val view = layoutInflater.inflate(R.layout.popupmenu, null)
         val layout = findViewById<LinearLayout>(R.id.backgroundlayout)


        layout.addView(view)

    }

    fun onClickCloseMenu(view: View){
        setContentView(R.layout.activity_home)

    }

    fun onClickLogout(view: View) {
        sharePrefManager!!.setLogout()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }


    private fun setNewLocale(mContext: AppCompatActivity, @LocaleManager.LocaleDef language: String) {
        LocaleManager.setNewLocale(this, language)
        val intent = mContext.intent
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
    }








//    Banner for Popup Offers
    fun getOffer() {
        var api:Api = NetworkClient.getRetrofitClient().create(Api::class.java)
        var call:Call<OfferModel> = api.offer
        call.enqueue(object : Callback<OfferModel> {
            override fun onResponse(call: Call<OfferModel>?, response: Response<OfferModel>?) {
                var offer_id = sharePrefManager!!.offerId
                if (response != null) {

                    var newOfferId: Int = response.body().get(0).id.toInt()
                    if (newOfferId > offer_id) {
                        sharePrefManager!!.saveOfferId(newOfferId)
                        if (sharePrefManager!!.offerShown.equals("1")) {
                            var offerDialog: OfferDialog = OfferDialog(this@HomeActivity, response.body().get(0).image, response.body().get(0).link)
                            offerDialog.show()
                        }


                    }
                }
            }

            override fun onFailure(call: Call<OfferModel>?, t: Throwable?) {

            }

        })

    }

    override fun onResume() {
        super.onResume()
        var addUserData = AddUserData(this, "Main Menu", "0")
        addUserData.adduserData()
    }





}