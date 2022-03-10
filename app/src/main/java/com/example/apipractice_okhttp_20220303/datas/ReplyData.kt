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

//    각 댓글이 자신의 작성일시를 핸드폰 시간대에 맞게 보정 + 가공된 문구로 내보내기

    fun getFormattedCreatedAt() :String {

//        가공 양식 지정
        val sdf = SimpleDateFormat("M월 d일 a h시 m분")

//        시차 보정에 사용할 Calendar 변수 (원래 있는 createdAt은 놔두고, 별도로 추가)
//        내 폰시간대(지역local)에 맞게 보정 예정
        val localCal= Calendar.getInstance()

//        작성일시의 일시값을 그대로 복사(원래값 : 현재일시)
        localCal.time = this.createdAt.time

//        localCal의 값을 내 폰설정에 맞게 시간을 더하자
//        ex. 한국: +9를 더해야 함.
//        ex. LA: +8을더해야 함.

//        Calendar 객체의 기능으로, 시간대 정보 추출하는 기능이 있음
        val localTimeZone = localCal.timeZone

//        시간대가 GMT와의 시차가 얼마나 나는지? (rawOffset - 몇초 차이까지)
        val diffHour = localTimeZone.rawOffset / 60 / 60 / 1000//ms를 =>시간단위러 변경

//        구해진 시차를 기존 시간에서 시간값으로 더해주기
        localCal.add(Calendar.HOUR, diffHour)

        return sdf.format(localCal.time)
    }


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