package com.example.apipractice_okhttp_20220303.datas

import org.json.JSONObject
import java.io.Serializable

class TopicData : Serializable {

    var id = 0 //id는 Int라고 명시.
    var title = "" //title은 String이라고 명시.
    var imageURL = "" //서버에서는 img_url로 주지만 앱에서는 imageURL로 받겠다. 변수명 다른 경우
    var replyCount = 0

//    하나의 토론주제 : 여러개의(목록) 선택진영
    val sideList = ArrayList<SideData>()


    companion object {

        //    주제 정보를 담고 있는 JSONObject가 들어오면 > TopicData형태로 변환해주는 함수. => static메쏘드로 만들자.

        fun getTopicDataFromJson( jsonObj: JSONObject) :TopicData {

//            기본 내용의 TopicData 생성
            val topicData = TopicData()

//            jsonObj에서 데이터 추출 > 멤버변수 대입
            topicData.id = jsonObj.getInt("id")
            topicData.title = jsonObj.getString("title")
            topicData.imageURL = jsonObj.getString("img_url")
            topicData.replyCount = jsonObj.getInt("reply_count")

//            side라는 JSONArray가 들어있음.
//            =>topicData의 하위 정보로, 선택진영 목록으로 저장.
//            => JOSONArray > ArrayList에 저장하자.

            val sideArr = jsonObj.getJSONArray("sides")

            for(i in 0 until sideArr.length()) {

//                선택진영 정보를들고 있는 JSONObject 추출
                val sidObj = sideArr.getJSONObject(i)

//                sideObj도 SideData로 (선택 진영) 변환.
                val sideData = SideData.getSideDataFromJon(sidObj)

//                topicData변수의 하위 목록으로 등록.
                topicData.sideList.add(sideData)

            }

//            완성된 TopicData 리턴

            return topicData
        }

    }

}