package com.example.draganddrop.dialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.content.ContextCompat.startActivity
import com.example.draganddrop.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.offer_dialog.*


class OfferDialog(context: Context, url: String, link: String) : Dialog(context) {

    var url:String
    var link:String
    init {
        this.url = url
        this.link = link
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.offer_dialog)
        Picasso.get().load(url).fit().placeholder(R.drawable.progress_animation).into(offer_image)
        close.setOnClickListener {
            dismiss()
        }

        offer_image.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            context.startActivity(browserIntent)
        }

    }
}