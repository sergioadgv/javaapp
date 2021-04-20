package com.example.draganddrop.network

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.draganddrop.R
import kotlinx.android.synthetic.main.activity_my_sticker_collections.*

class MyStickerCollections : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_sticker_collections)

        back_btn.setOnClickListener {
            finish()
        }
    }
}