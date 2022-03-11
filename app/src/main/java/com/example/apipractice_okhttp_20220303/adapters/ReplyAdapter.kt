package com.example.apipractice_okhttp_20220303.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.apipractice_okhttp_20220303.R
import com.example.apipractice_okhttp_20220303.ViewTopicDetailActivity
import com.example.apipractice_okhttp_20220303.datas.ReplyData
import com.example.apipractice_okhttp_20220303.datas.TopicData
import com.example.apipractice_okhttp_20220303.utils.ServerUtil
import org.json.JSONObject
import java.text.SimpleDateFormat
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

        val txtReReplyCount = row.findViewById<TextView>(R.id.txtReReplyCount)
        val txtLikeCount = row.findViewById<TextView>(R.id.txtLikeCount)
        val txtDisLikeCount = row.findViewById<TextView>(R.id.txtDisLikeCount)

        txtReplyCount.text = data.content
        txtWrihtNickname.text = data.writer.nickname
        txtSelectedSide.text = "[${data.selectedSide.title}]"
//        임시 - 작성일자만 "2022-03-10" 형태로 표현 => 연/ 월/ 일 데이터 가공
//        월은 1작게 나옴. +1로 보정해줘야 함
//        txtCreateAt.text = "${data.createdAt.get(Calendar.YEAR)}-${data.createdAt.get(Calendar.MONTH) +1}-${data.createdAt.get(Calendar.DAY_OF_MONTH)}"

//        임시 2 - "22.03.10" 형태로 표현 => SimpleDateFormat 활용

//        연습
//        양식1) 2022년 3월 5일
//        양식2) 220305
//        양식3) 3월 5일 오전 2시 5분
//        양식4) 21년 3/5 (토) - 18:05

//        val sdf = SimpleDateFormat("yyyy년 M월 d일")
//        val sdf = SimpleDateFormat("yyMMdd")
//        val sdf = SimpleDateFormat("M월 d일 a h시 m분")
//        val sdf = SimpleDateFormat("yy년 M/d (E) - HH:mm")

//        sdf.format(Date객체) => 지정해둔 양식의 String으로 가공
//          createdAt : Calendar / format의 파라미터 :Date => Calendaq의 내용물인 time 변수가 Date
        txtCreateAt.text = data.getFormattedCreatedAt()

        txtReReplyCount.text = "답글 ${data.reReplyCount}"
        txtLikeCount.text = "좋아요 ${data.likeCount}"
        txtDisLikeCount.text = "싫어요 ${data.disLickeCount}"

        txtLikeCount.setOnClickListener {

//            서버에 이 댓글에 좋아요 알림
            ServerUtil.postRequestReplyLikeOrDisLike(
            mContext,
            data.id,
            true,
            object : ServerUtil.JsonResponseHandler{
                override fun onResponse(jsonObj: JSONObject) {

//                  무조건 댓글 목록 새로고침
//                   Adapter 코딩 => 액티비티 기능 실행

//                    어댑터 객체화시 mContext 변수에 어느 화면에서 사용하는지 대입
//                      mContext는 Context타입으로 되어 있음. 대입하는 객체는 ViewTopic액티비티 객체이다 =>다형성을 활용하고 있는것

//                    부모 형태의 변수에 담긴 자식 객체는 캐스팅을 통해서 원상복구 가능.
//                    자식에서 만든 별도의 함수들을 다시 사용 가능

                    (mContext as ViewTopicDetailActivity).getTopicDetailFromServer()
                }

            })

            }


//        좋아요가 눌렸는지 아닌지에 따라 글씨색상 변경./ 배경 drawable도 설정

        if(data.isMyLike) {
            txtLikeCount.setTextColor(ContextCompat.getColor(mContext, R.color.naver_red))
            txtLikeCount.setBackgroundResource(R.drawable.naver_red_border_box)

        }
        else{
            txtLikeCount.setTextColor(ContextCompat.getColor(mContext, R.color.deep_dark_gray))
            txtLikeCount.setBackgroundResource(R.drawable.dark_gray_border_box)
        }


//        싫어요가 눌려도 마찬가지 처리 => 싫어요 API호출(기존함수 활용) + 토론 상세화면 댓글 목록 새로고침

        txtDisLikeCount.setOnClickListener {

            ServerUtil.postRequestReplyLikeOrDisLike(
                mContext,
                data.id,
                false,
                object : ServerUtil.JsonResponseHandler{
                    override fun onResponse(jsonObj: JSONObject) {
                        (mContext as ViewTopicDetailActivity).getTopicDetailFromServer()
                    }

                }
            )

//            좋아요가 눌렸는지 안 눌렸는지에 대한 글자 색상/ 배경 drawable 변경하기
        if(data.isMyDisLike){
            txtDisLikeCount.setTextColor(ContextCompat.getColor(mContext, R.color.naver_red))
            txtDisLikeCount.setBackgroundResource(R.drawable.naver_red_border_box)

        }
        else{
            txtDisLikeCount.setTextColor(ContextCompat.getColor(mContext, R.color.deep_dark_gray))
            txtDisLikeCount.setBackgroundResource(R.drawable.naver_red_border_box)

        }


        }

        return row

    }

}