package com.example.whatsappnotificationreceiver

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.whatsappnotificationreceiver.databinding.WhatsappItemViewBinding

class WhatsappNotificationAdapter(var msgList: ArrayList<ModelClass>, var context: Context) :
    RecyclerView.Adapter<WhatsappNotificationAdapter.MyViewHolder>() {
    class MyViewHolder(var binding: WhatsappItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(modelClass: ModelClass, context: Context) {
            val tvFrom = binding.tvFrom
            val tvMsg = binding.tvMsg

            tvFrom.text = modelClass.from
            if (modelClass.isImage) {
                Glide.with(context).load(modelClass.img).into(binding.ivPicture)
            } else {
                tvMsg.text = modelClass.msg
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            WhatsappItemViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(msgList[position], context)
    }

    override fun getItemCount() = msgList.size
}