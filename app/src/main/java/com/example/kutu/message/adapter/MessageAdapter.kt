package com.example.kutu.message.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kutu.R
import com.example.kutu.app.App
import com.example.kutu.data.db.entity.MessageEntity
import com.example.kutu.util.AppUtil
import com.example.kutu.util.Constant

class MessageAdapter(
    private var context: Context,
    private val colorCode: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: MutableList<MessageEntity> = mutableListOf()
    var logTag = "@MessageAdapter"


    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var ivBackground: ImageView = view.findViewById(R.id.ivBackground)
        var tvMessage: TextView = view.findViewById(R.id.tvMessage)
        var tvTime: TextView = view.findViewById(R.id.tvTime)


        fun onBind(item : MessageEntity) {

            // text
            var answerList = AppUtil.getAnswerList(item.message)

            if (answerList[2] == 0){
                var message = AppUtil.getMessageText(item.message,answerList)
                tvMessage.text = message

            }else{
                tvMessage.text = item.message
            }



            var address: String = list[position].addressId

            if(item.type == 1){

                val colors = intArrayOf(Color.parseColor(AppUtil.colorList[colorCode]), Color.parseColor(AppUtil.colorList[colorCode]))

                val gradientDrawable = GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM, colors
                )
                gradientDrawable.shape = GradientDrawable.OVAL
                gradientDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
                ivBackground.setBackgroundDrawable(gradientDrawable);
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

       return  when(viewType){

           Constant.receiverViewType -> {

                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_received_message, parent, false)
                  ItemViewHolder(itemView)
            }
           else -> {
               val itemView = LayoutInflater.from(parent.context)
                   .inflate(R.layout.view_sent_message, parent, false)
               ItemViewHolder(itemView)
           }
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = list[position]
        if (holder is ItemViewHolder) {
            holder.onBind(item)
        }
    }

    fun submitList(newList: List<MessageEntity>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {

        var type: Int = list[position].type

        if(type ==1 ){
            return Constant.receiverViewType
        }
        return Constant.senderViewType
    }

}