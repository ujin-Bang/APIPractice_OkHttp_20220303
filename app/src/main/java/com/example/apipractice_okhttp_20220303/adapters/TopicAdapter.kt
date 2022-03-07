package com.example.apipractice_okhttp_20220303.adapters

import android.content.Context
import android.widget.ArrayAdapter
import com.example.apipractice_okhttp_20220303.datas.TopicData

class TopicAdapter(
    val mContext: Context,
    resId: Int,
    val mList: List<TopicData>
) : ArrayAdapter<TopicData>(mContext, resId, mList) {
}