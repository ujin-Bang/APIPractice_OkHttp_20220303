package com.example.apipractice_okhttp_20220303.utils

import android.util.Log
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class ServerUtil {

//    서버에 Request를 날리는 역할
//    함수를 만들려고 하는데 어떤 객체가 실행해도 결과만 잘 나오면 그만인 함수를 만들자.
//    코틀린에서 static에 해당하는 개념을 어떻게 하느냐? companion object{ }에 만들면 스태틱과 비슷한 처리
//    (ex=> 메인에서 companion object내에 함수/변수 호출시 => binding사용 안하고 Serverutil.함수(변수)이름으로 바로 찾을 수 있음
//
    companion object {

//        서버 컴퓨터 주소만 변수로 저장(관리 일원화) => 외부 노출하기 싫다. private로 하자
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

//            제작3) 모든 Request 정보를 종합한 객체 생성 (어느 주소로 + 어느 메쏘드로 + 어떤 파라미터를)
            val request = Request.Builder()
                .url(urlSting)
                .post(formData)
                .build()

//            종합한 Request도 실제 호출을 해 줘야 API 호출이 실행됨(startActivity와 같은 동작이 필요함)
//            실제 호출 : 클라이언트로써 동작 > OkHttpClient 클래스

            val client = OkHttpClient()

//            OkHttpClient 객체를 이용 >서버에 로그인 기능 실제 호출
//             호출을 했으면, 서버가 수행한 결과를 받아서 처리
//            => 서버에 다녀와서 할 일을 등록 : enqueue(Callback)

            client.newCall(request).enqueue( object : Callback {
                override fun onFailure(call: Call, e: IOException) {

//                    실패 : 서버 연결자체를 실패한 상황. 응답이 오지 않았다라는 것.
//                    ex.인터넷 끊김, 서버접속 불가 등등 물리적 연결 실패를 말함
//                    ex. 비번이 틀려서 로그인 실패 : 서버연결 성공, 응답도 돌아온 경우이다. 그 내용만 실패( 물리적 실패가 아니라는 것)

                }

                override fun onResponse(call: Call, response: Response) {

//                    어떤 내용이던 응답 자체는 잘 돌아온 경우(그 내용은 성공 / 실패일 수 있다.)

//                    응답: response 변수에 들어있음 > 응답의 본문(body)만 보자.

                    val bodyString = response.body!!.string() // toString() 아님!, string() 기능은 1회용, 변수에 담아두고 이용

//                    응답의 본문을 String으로 변환하면 JSON Encoding으로 적용된 상태(한글 깨짐현상)
//                    JSONObject 객체로 응답본문 String을 변환해주면 한글이 복구됨
//                    =>UI에서도 JSONObject를 이용해서 데이터 추출 / 실제 활용

                    val jsonObj = JSONObject( bodyString )

                    Log.d("서버테스트",jsonObj.toString())

//                    연습 : 로그인 성공 / 실패에 따른 로그 출력
//                    "code" 이름표의 Int를 추출, 그 값을 if로 물어보자

                    val code = jsonObj.getInt("code")

                   if(code == 200) {
                       Log.d("로그인 시도", "성공")

                       val dataObj = jsonObj.getJSONObject("data")
                       val userObj = dataObj.getJSONObject("user")
                       val nickname = userObj.getString("nick_name")

                       Log.d("로그인한 사람", nickname)
                   }
                    else {
                        Log.d("로그인 시도","실패")

                       val message =jsonObj.getString("message")

                       Log.d("실패사유", message)
                   }
                }


            })


        }
    }

}