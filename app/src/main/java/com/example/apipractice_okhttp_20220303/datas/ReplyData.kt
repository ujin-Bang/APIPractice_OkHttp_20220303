package com.example.apipractice_okhttp_20220303.datas

import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class ReplyData(
    var id: Int,
    var content: String,
) {

    var writer = UserData() //모든 댓글에는 작성자가 있다. null 가능성이 없다.

    var selectedSide = SideData()//모든 댓글에는 선택한 진영이 있다. null 가능성이 없다.

//    작성일시를 담아둘 변수
//    일/시 데이터를 변경 => 내부의 숫자만 변경. 변수에 새 객체 대입X. =>val로 써도 됨.
    val createdAt = Calendar.getInstance()

//    보조생성자 추가 연습 : 파라미터 없는 형태
    constructor() : this( 0, "내용없음")


    companion object{

        fun getReplyDataFromJon( jsonObj : JSONObject ) : ReplyData {

            val replyData = ReplyData()

//                JSON 정보 > 멤버변수 채우기
            replyData.id = jsonObj.getInt("id")
            replyData.content = jsonObj.getString("content")
            replyData.writer = UserData.getUserDataFromJon(jsonObj.getJSONObject("user"))

            replyData.selectedSide = SideData.getSideDataFromJon( jsonObj.getJSONObject("selected_side") )

//            Calendar로 되어있는 작성일시의 시간을 서버가 알려주는 댓글 작성 일시로 맞춰줘야 함.

//            임시 2022-01-12 10:55:35로 변경 (한번에 모두 변경)
//            replyData.createdAt.set(2022,Calendar.JANUARY,12,10,55,35)

//            임시2) 연돋만 2021년으로 변경 (항목을 찍어서 변경)
//            replyData.createdAt.set( Calendar.YEAR, 2021)

//            실제) 서버가 주는 create_at에 담긴 String을 => parse해서 Calendar로 변경
//            createAt 변수의 일시 값으로 => parse 결과물 사용.

//            서버가 주는 양식을 보고 그대로 적자.
            val sdf = SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" )

//            created_at으로 내려오는 문구
            val createdAtStr = jsonObj.getString("created_at")

//            createdAtStr 변수를 =>Date로 변경(parse) => Calendar의 time에 대입.
            replyData.createdAt.time = sdf.parse(createdAtStr)

            return replyData


        }
    }
}