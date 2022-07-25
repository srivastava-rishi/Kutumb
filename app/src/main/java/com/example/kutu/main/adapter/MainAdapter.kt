package com.example.kutu.main.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kutu.R
import com.example.kutu.data.db.entity.LastMessageEntity
import com.example.kutu.util.AppUtil

class MainAdapter(
    private var context: Context,
    private var listener: MainAdapterListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>()  {


    var list: MutableList<LastMessageEntity> = mutableListOf()
    var logTag = "@MainAdapter"



    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var ivBackground: ImageView = view.findViewById(R.id.ivBackground)
        var rlRoot: RelativeLayout = view.findViewById(R.id.rlRoot)
        var tvName: TextView = view.findViewById(R.id.tvName)
        var tvMessage: TextView = view.findViewById(R.id.tvMessage)


        fun onBind(item : LastMessageEntity) {

            tvName.text = item.addressId

            var answerList = AppUtil.getAnswerList(item.lastMessage)

            if (answerList[2] == 0){
                var message = AppUtil.getMessageText(item.lastMessage,answerList)
                tvMessage.text = message

            }else{
                tvMessage.text = item.lastMessage
            }


            val colors = intArrayOf(Color.parseColor(AppUtil.colorList[item.imageColor]), Color.parseColor(AppUtil.colorList[item.imageColor]))
            val gradientDrawable = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM, colors
            )
            gradientDrawable.shape = GradientDrawable.OVAL
            gradientDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
            ivBackground.setBackgroundDrawable(gradientDrawable);

            rlRoot.setOnClickListener {

                listener.onOpenAllMessages(item)

            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).
        inflate(R.layout.view_message_list, parent, false)
        return  ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = list[position]
        if (holder is ItemViewHolder) {
            holder.onBind(item)
        }
    }

    fun submitList(newList: List<LastMessageEntity>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return list.size
    }

    interface MainAdapterListener{
        fun onOpenAllMessages(lastMessageEntity: LastMessageEntity)

    }

}