package com.example.apipractice_okhttp_20220303.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.apipractice_okhttp_20220303.R
import com.example.apipractice_okhttp_20220303.datas.ReplyData
import com.example.apipractice_okhttp_20220303.datas.TopicData

class ReplyAdapter(
    val mContext: Context,
    resId: Int,
    val mList: List<ReplyData>
) : ArrayAdapter<ReplyData>(mContext, resId, mList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var tempRow = convertView
        if(tempRow == null){
            tempRow = LayoutInflater.from(mContext).inflate(R.layout.reply_list_item, null)
        }

        val row = tempRow!!

        val data = mList[position]

        val txtSelectedSide = row.findViewById<TextView>(R.id.txtSelectedSide)
        val txtWrihtNickname = row.findViewById<TextView>(R.id.txtWrihtNickname)
        val txtReplyCount = row.findViewById<TextView>(R.id.txtReplyCount)

        txtReplyCount.text = data.content
        txtWrihtNickname.text = data.writer.nickname
        txtSelectedSide.text = "[${data.selectedSide.title}]"


        return row

    }

}