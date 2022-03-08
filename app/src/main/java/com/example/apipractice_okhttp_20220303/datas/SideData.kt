package com.example.apipractice_okhttp_20220303.datas

import org.json.JSONObject
import java.io.Serializable

// 토론의 선택 진영(이름, id값 등등)을 표현하는 클래스

class SideData : Serializable{

    var id = 0
    var title = ""
    var voteCount = 0

    companion object {

        fun getSideDataFromJon(jsonObj : JSONObject) : SideData {

            val sideData = SideData()

            sideData.id = jsonObj.getInt("id")
            sideData.title = jsonObj.getString("title")
            sideData.voteCount = jsonObj.getInt("vote_count")

            return sideData
        }

    }

}