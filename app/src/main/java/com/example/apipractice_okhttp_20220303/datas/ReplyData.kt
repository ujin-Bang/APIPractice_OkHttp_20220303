package com.example.apipractice_okhttp_20220303.datas

import org.json.JSONObject

class ReplyData(
    var id: Int,
    var content: String,
) {

    var writer = UserData() //모든 댓글에는 작성자가 있다. null가능성이 없다.

//    보조생성자 추가 연습 : 파라미터 없는 형태
    constructor() : this( 0, "내용없음")


    companion object{

        fun getReplyDataFromJon( jsonObj : JSONObject ) : ReplyData {

            val replyData = ReplyData()

//                JSON 정보 > 멤버변수 채우기
            replyData.id = jsonObj.getInt("id")
            replyData.content = jsonObj.getString("content")
            replyData.writer = UserData.getUserDataFromJon(jsonObj.getJSONObject("user"))

            return replyData


        }
    }
}