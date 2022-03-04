package com.example.apipractice_okhttp_20220303.utils

import okhttp3.FormBody

class ServerUtil {

//    서버에 Request를 날리는 역할
//    함수를 만들려고 하는데 어떤 객체가 실행해도 결과만 잘 나오면 그만인 함수를 만들자.
//    코틀린에서 static에 해당하는 개념을 어떻게 하느냐? companion object{ }에 만들면 스태틱과 비슷한 처리(

    companion object {

//        서버 컴퓨터 주소만 변수로 저장(관리 일원화) => 외부 노출하기 싶지 않다. private로 하자
        private val BASE_URL = "http://54.180.52.26"

//        로그인 기능 호출 함수

        fun postRequestLogin( id: String, pw: String ){

//            Request 제작 -> 실제 호출 -> 서버의 응답을 화면에 전달

//            제작1) 어느 주소(Url)로 접근할지? => 서버주소 + 기능주소
            val urlSting = "${BASE_URL}/user"

//            제작2) 파라미터 담아주기 => 어떤 이름표를 / 어느 공간에 담아야 하는가
            val formData = FormBody.Builder()
                .add("email", id)
                .add("password",pw)
                .build()

        }
    }

}