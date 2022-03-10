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
import java.util.*

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
        val txtCreateAt = row.findViewById<TextView>(R.id.txtCreatedAt)

        txtReplyCount.text = data.content
        txtWrihtNickname.text = data.writer.nickname
        txtSelectedSide.text = "[${data.selectedSide.title}]"
//        임시 - 작성일자만 "2022-03-10" 형태로 표현 => 연/ 월/ 일 데이터 가공
//        월은 1작게 나옴. +1로 보정해줘야 함
        txtCreateAt.text = "${data.createdAt.get(Calendar.YEAR)}-${data.createdAt.get(Calendar.MONTH) +1}-${data.createdAt.get(Calendar.DAY_OF_MONTH)}"


        return row

    }

}