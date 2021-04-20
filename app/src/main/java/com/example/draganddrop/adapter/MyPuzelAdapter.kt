package com.example.draganddrop.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.draganddrop.PuzzleImageActivity
import com.example.draganddrop.R
import com.example.draganddrop.db.SharePrefManager
import com.example.draganddrop.dialog.VerifyDialog
import com.example.draganddrop.model.my_puzzle_model.MyPuzzleModel
import com.example.draganddrop.model.my_puzzle_model.MyPuzzleModelItem
import com.squareup.picasso.Picasso

class MyPuzelAdapter (context: Context,myPuzzleModel: MyPuzzleModel): RecyclerView.Adapter<MyPuzelAdapter.MyViewHolder>() {
    var context:Context
    var myPuzzleModel:MyPuzzleModel
    var sharePrefManager: SharePrefManager
    var eng_str = "eng"
    var dtc_str = "dtc"
    var spa_str = "spa"
    init {
        this.context = context
        this.myPuzzleModel = myPuzzleModel
        sharePrefManager = SharePrefManager(context)
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name_txt: TextView
        var p_txt: TextView
        var itemCard: CardView
        var pizzle_image: ImageView

        init {
            name_txt = itemView.findViewById(R.id.name_txt)
            p_txt = itemView.findViewById(R.id.p_txt)
            itemCard = itemView.findViewById(R.id.item_card)
            pizzle_image = itemView.findViewById(R.id.puzzle_img)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(context)
                .inflate(R.layout.item_puzzle, parent, false)
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var myPuzzleModelItem:MyPuzzleModelItem = myPuzzleModel.get(position)


        val lang = sharePrefManager.language

        if (lang == eng_str) {
            holder.name_txt.setText(myPuzzleModelItem.puzzle_name)


        } else if (lang == dtc_str) {
            holder.name_txt.setText(myPuzzleModelItem.puzzle_name)

        } else if (lang == spa_str) {
            holder.name_txt.setText(myPuzzleModelItem.puzzle_name)

        }

        holder.p_txt.visibility = View.GONE
        Picasso.get().load(myPuzzleModelItem.puzzle_image).into(holder.pizzle_image)
        holder.itemCard.setOnClickListener {

            context.startActivity(Intent(context,PuzzleImageActivity::class.java).putExtra("puzzle_id",myPuzzleModelItem.puzzle_id))

        }

    }

    override fun getItemCount(): Int {
       return myPuzzleModel.size
    }
}