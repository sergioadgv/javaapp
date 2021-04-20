package com.example.draganddrop.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.draganddrop.R
import com.example.draganddrop.db.SharePrefManager
import com.example.draganddrop.model.bookletModel.BookLetModel
import com.example.draganddrop.model.bookletModel.BookLetModelItem
import com.squareup.picasso.Picasso

class BookLetAdapter(context: Context,bookLetModel: BookLetModel,listener:Listener) : RecyclerView.Adapter<BookLetAdapter.MyViewHolder>() {
    var context: Context
    var sharePrefManager: SharePrefManager
    var bookLetModel : BookLetModel
    var eng_str = "eng"
    var dtc_str = "dtc"
    var spa_str = "spa"
    var listener: Listener

    init {
        this.context = context
        this.bookLetModel = bookLetModel
        sharePrefManager = SharePrefManager(context)
        this.listener = listener

    }
    public class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var image: ImageView
        var name: TextView
        var itemBooklet: CardView

        init {
            image = itemView.findViewById(R.id.booklet_img)
            name = itemView.findViewById(R.id.booklet_name)
            itemBooklet = itemView.findViewById(R.id.item_booklet)



        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(context)
                .inflate(R.layout.item_boook_let, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
       return bookLetModel.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var bookLetModelItem = bookLetModel.get(position)
        var lanCode = sharePrefManager.lanCode
        if (lanCode.equals("1")){
            holder.name.setText(bookLetModelItem.booklet_name)
        }else if( lanCode.equals("2")){
            holder.name.setText(bookLetModelItem.booklet_name_dutch)
        }
        else if( lanCode.equals("3")){
            holder.name.setText(bookLetModelItem.booklet_name_spanish)
        }

        Picasso.get().load(bookLetModelItem.image).fit().into(holder.image)

        holder.itemBooklet.setOnClickListener {
            listener.onClikBooklet(bookLetModelItem)
        }


    }


    interface Listener{
        fun  onClikBooklet(bookLetModelItem: BookLetModelItem)
    }

}