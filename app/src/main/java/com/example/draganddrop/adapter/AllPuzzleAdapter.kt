package com.example.draganddrop.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.draganddrop.PuzzleImageActivity
import com.example.draganddrop.QuensActivity
import com.example.draganddrop.R
import com.example.draganddrop.db.SharePrefManager
import com.example.draganddrop.dialog.VerifyDialog
import com.example.draganddrop.model.code_verify_model.VerifyModel
import com.example.draganddrop.model.puzzle_model.PuzzleModel
import com.example.draganddrop.model.puzzle_model.PuzzleModelItem
import com.example.draganddrop.network.Api
import com.example.draganddrop.network.NetworkClient
import com.example.draganddrop.services.AddUserData
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AllPuzzleAdapter (context: Context,puzzleModel: PuzzleModel): RecyclerView.Adapter<AllPuzzleAdapter.MyViewHolder>() {
    var context: Context
    var puzzleModel: PuzzleModel

    var sharePrefManager: SharePrefManager
    var eng_str = "eng"
    var dtc_str = "dtc"
    var spa_str = "spa"
    init {
        this.context = context
        this.puzzleModel = puzzleModel
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
        var puzzleModelItem : PuzzleModelItem = puzzleModel.get(position)


        val lang = sharePrefManager.language

        if (lang == eng_str) {
            holder.name_txt.setText(puzzleModelItem.name)
            if(puzzleModelItem.is_p.equals("1")){
                holder.p_txt.setText("Last best score ${puzzleModelItem.score}")
            }else{
                holder.p_txt.setText("Use code to unlock this")
            }


        } else if (lang == dtc_str) {
            holder.name_txt.setText(puzzleModelItem.name_dutch)
            if(puzzleModelItem.is_p.equals("1")){
                holder.p_txt.setText("Ontgrendeld")
            }else{
                holder.p_txt.setText("Gebruik code om dit te ontgrendelen")
            }

        } else if (lang == spa_str) {
            holder.name_txt.setText(puzzleModelItem.name_spanish)
            if(puzzleModelItem.is_p.equals("1")){
                holder.p_txt.setText("Desbloqueado")
            }else{
                holder.p_txt.setText("Use el código para desbloquear esto")
            }

        }


        Picasso.get().load(puzzleModelItem.image).fit().into(holder.pizzle_image)
        holder.itemCard.setOnClickListener {

            if(puzzleModelItem.is_p.equals("1")){
                context.startActivity(Intent(context, PuzzleImageActivity::class.java).putExtra("puzzle_id",puzzleModelItem.id).putExtra("score",puzzleModelItem.score))
                var addUserData = AddUserData(context,"Puzzle",puzzleModelItem.name)
                addUserData.adduserData()


            }else{

                if(sharePrefManager.name.equals("demo")){
                    Toast.makeText(context,"Sorry you are using demo account",Toast.LENGTH_SHORT).show()
                }else{
                    var verifyDialog:VerifyDialog = VerifyDialog(context,"puzzle",puzzleModelItem.id,object :VerifyDialog.OnClickApply{
                        override fun applyClickListener(id: String?) {

                            addtoMycollection(puzzleModelItem)

                        }

                    })
                    verifyDialog.show()
                    verifyDialog.setCancelable(false)
                }

            }

        }



    }

    override fun getItemCount(): Int {
       return puzzleModel.size

    }

    fun addtoMycollection(puzzleModelItem: PuzzleModelItem){
        var sharePrefManager:SharePrefManager = SharePrefManager(context)


        var api:Api = NetworkClient.getRetrofitClient().create(Api::class.java)
        var call:Call<VerifyModel> = api.addPuzzleMyCollection(puzzleModelItem.id,sharePrefManager.userId,puzzleModelItem.name,puzzleModelItem.image)
        call.enqueue(object :Callback<VerifyModel>{
            override fun onResponse(call: Call<VerifyModel>?, response: Response<VerifyModel>?) {

                if (response!!.body().get(0).status.equals("success")){
                    Toast.makeText(context,"You have successfully add this in your collection",Toast.LENGTH_SHORT).show()
                    context.startActivity(Intent(context, PuzzleImageActivity::class.java).putExtra("puzzle_id",puzzleModelItem.id).putExtra("score",puzzleModelItem.score))
                    var addUserData = AddUserData(context,"Puzzle",puzzleModelItem.name)
                    addUserData.adduserData()
                }else{
                    Toast.makeText(context,"You already add this in your collection",Toast.LENGTH_SHORT).show()
                }



            }

            override fun onFailure(call: Call<VerifyModel>?, t: Throwable?) {

            }

        })
    }

}