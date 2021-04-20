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
import com.example.draganddrop.QuensActivity
import com.example.draganddrop.R
import com.example.draganddrop.db.SharePrefManager
import com.example.draganddrop.dialog.VerifyDialog
import com.example.draganddrop.model.quiz_model.QuizModel
import com.example.draganddrop.model.quiz_model.QuizModelItem
import com.example.draganddrop.services.AddUserData
import com.squareup.picasso.Picasso

class QuizAdapter(context: Context, quizModel: QuizModel): RecyclerView.Adapter<QuizAdapter.MyViewHolder>() {
    var context: Context
    var quizModel:QuizModel
    var sharePrefManager:SharePrefManager
    var eng_str = "eng"
    var dtc_str = "dtc"
    var spa_str = "spa"
    init {
        this.context = context
        this.quizModel = quizModel
        sharePrefManager = SharePrefManager(context)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name_txt: TextView
        var p_txt: TextView
        var itemCard: CardView
        var quiz_img : ImageView

        init {
            name_txt = itemView.findViewById(R.id.name_txt)
            p_txt = itemView.findViewById(R.id.p_txt)
            itemCard = itemView.findViewById(R.id.item_card)
            quiz_img = itemView.findViewById(R.id.quiz_img)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(context)
                .inflate(R.layout.item_quiz, parent, false)
        return MyViewHolder(itemView)


    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var quizModelItem: QuizModelItem = quizModel.get(position)

        val lang = sharePrefManager.language

        if (lang == eng_str) {
            holder.name_txt.setText(quizModelItem.name)
            if(quizModelItem.is_p.equals("1")){
                holder.p_txt.setText("Participated, Your score is " + quizModelItem.r_ans + "/" + quizModelItem.total)
            }else{
                holder.p_txt.setText("Not Participated, Locked")
            }

        } else if (lang == dtc_str) {
            holder.name_txt.setText(quizModelItem.name_dutch)
            if(quizModelItem.is_p.equals("1")){
                holder.p_txt.setText("Deelgenomen, je score is  " + quizModelItem.r_ans + "/" + quizModelItem.total)
            }else{
                holder.p_txt.setText("Niet meegedaan, op slot")
            }
        } else if (lang == spa_str) {
            holder.name_txt.setText(quizModelItem.name_spanish)
            if(quizModelItem.is_p.equals("1")){
                holder.p_txt.setText("Participado, tu puntuación es  " + quizModelItem.r_ans + "/" + quizModelItem.total)
            }else{
                holder.p_txt.setText("No participó, bloqueado")
            }
        }

        //Log.e("image",quizModelItem.image);
        Picasso.get().load(quizModelItem.image).fit().into(holder.quiz_img)
        holder.itemCard.setOnClickListener {

            if(quizModelItem.is_p.equals("1")){
                context.startActivity(Intent(context, QuensActivity::class.java).putExtra("quiz_id", quizModelItem.id))
                var addUserData = AddUserData(context,"Quiz",quizModelItem.name)
                addUserData.adduserData()
            }else{
                if(sharePrefManager.name.equals("demo")){
                    Toast.makeText(context,"Sorry you are using demo account", Toast.LENGTH_SHORT).show()
                }else{
                    var verifyDialog: VerifyDialog = VerifyDialog(context, "quiz", quizModelItem.id, object : VerifyDialog.OnClickApply {
                        override fun applyClickListener(id: String?) {
                            context.startActivity(Intent(context, QuensActivity::class.java).putExtra("quiz_id", id))
                            var addUserData = AddUserData(context,"Quiz",quizModelItem.name)
                            addUserData.adduserData()
                        }

                    })
                    verifyDialog.show()
                    verifyDialog.setCancelable(false)
                }

            }

        }
    }

    override fun getItemCount(): Int {
        return quizModel.size
    }
}