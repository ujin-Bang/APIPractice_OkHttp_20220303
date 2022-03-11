package com.example.apipractice_okhttp_20220303.utils

import android.content.Context
import android.util.Log
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.json.JSONObject
import java.io.IOException

class ServerUtil {

//    서버유틸로 돌아온 응답을 => 액티비티에서 처리하도록, 일처리 넘기기.
//    나에게 생긴 일을 > 다른 클래스에게 처리 요청 : interface 활용하기

    interface JsonResponseHandler {
        fun onResponse(jsonObj: JSONObject)
    }

    //    서버에 Request를 날리는 역할
//    함수를 만들려고 하는데 어떤 객체가 실행해도 결과만 잘 나오면 그만인 함수를 만들자.
//    코틀린에서 static에 해당하는 개념을 어떻게 하느냐? companion object{ }에 만들면 스태틱과 비슷한 처리
//    (ex=> 메인에서 companion object내에 함수/변수 호출가능 =>  Serverutil.함수(변수)이름으로 사용가능.
//
    companion object {

        //        서버 컴퓨터 주소만 변수로 저장(관리 일원화) => 외부 노출하기 싫다. private로 하자
        private val BASE_URL = "http://54.180.52.26"

//        로그인 기능 호출 함수
//      handler: 이 함수를 쓰는 화면에서 JSON 분석을 어떻게 / UI에서 어떻게 활용할지 방안(인터페이스)
//      - 처리 방안을 임시로 비워두려면 null 대입 허용.

        fun postRequestLogin(id: String, pw: String, handler: JsonResponseHandler?) {

//            Request 제작 -> 실제 호출 -> 서버의 응답을 화면에 전달

//            제작1) 어느 주소(Url)로 접근할지? => 서버주소 + 기능주소
            val urlSting = "${BASE_URL}/user"

//            제작2) 파라미터 담아주기 => 어떤 이름표를 / 어느 공간에 담아야 하는가
            val formData = FormBody.Builder()
                .add("email", id)
                .add("password", pw)
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

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

//                    실패 : 서버 연결자체를 실패한 상황. 응답이 오지 않았다라는 것.
//                    ex.인터넷 끊김, 서버접속 불가 등등 물리적 연결 실패를 말함
//                    ex. 비번이 틀려서 로그인 실패 : 서버연결 성공, 응답도 돌아온 경우이다. 그 내용만 실패( 물리적 실패가 아니라는 것)

                }

                override fun onResponse(call: Call, response: Response) {

//                    어떤 내용이던 응답 자체는 잘 돌아온 경우(그 내용은 성공 / 실패일 수 있다.)

//                    응답: response 변수에 들어있음 > 응답의 본문(body)만 보자.

                    val bodyString =
                        response.body!!.string() // toString() 아님!, string() 기능은 1회용, 변수에 담아두고 이용

//                    응답의 본문을 String으로 변환하면 JSON Encoding으로 적용된 상태(한글 깨짐현상)
//                    JSONObject 객체로 응답본문 String을 변환해주면 한글이 복구됨
//                    =>UI에서도 JSONObject를 이용해서 데이터 추출 / 실제 활용

                    val jsonObj = JSONObject(bodyString)

                    Log.d("서버응답", jsonObj.toString())

//                   실제 : handler 변수에  jsonObj를 가지고 화면에서 어떻게 처리할지 계획이 들어와 있다
//                    (계획이 되어있을 때만) 해당 계획을 실행하자.

                    handler?.onResponse(jsonObj) //handler? =>핸들러가 null이 아니면
                }


            })


        }

        fun putRequestSignUp(
            email: String,
            pw: String,
            nickname: String,
            handler: JsonResponseHandler?
        ) {

            val urlString = "${BASE_URL}/user"

            val formData = FormBody.Builder()
                .add("email", email)
                .add("password", pw)
                .add("nick_name", nickname)
                .build()

            val request = Request.Builder()
                .url(urlString)
                .put(formData)
                .build()

            val client = OkHttpClient()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {

                    val bodyString = response.body!!.string()
                    val jsonObj = JSONObject(bodyString)
                    Log.d("서버응답", jsonObj.toString())
                    handler?.onResponse(jsonObj)

                }


            })
        }

//      이메일 or 닉네임 중복 검사 함수

        fun getRequestDuplicatedCheck(
            type: String,
            inputValue: String,
            handler: JsonResponseHandler?
        ) {

//            1)어느 주소로 가야하는가? + 어떤 파라미터를 첨부하는가? 도 주소에 같이 포함
//              =>라이브러리의 도움을 받자. HttpUrl 클래스(OkHttp 소속)의 도움받자.

            val urlBuilder = "${BASE_URL}/user_check".toHttpUrlOrNull()!!.newBuilder()
                .addEncodedQueryParameter("type", type)
                .addEncodedQueryParameter("value", inputValue)
                .build()

            val urlString = urlBuilder.toString()

//            2) 요청 정보 정리 > Request 생성

            val request = Request.Builder()
                .url(urlString)
                .get()
                .build()

//            3) Request 완성 > 서버에 호출하고 응답을 화면에 넘기자.

            val client = OkHttpClient()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {

                    val bodyString = response.body!!.string()

                    val jsonObj = JSONObject(bodyString)
                    Log.d("서버응답", jsonObj.toString())
                    handler?.onResponse(jsonObj)
                }

            })

        }

//        연습 : 내 정보 불러오기 (/user_info - GET)

//      토큰은 ContextUtil 클래스에서 getToken 함수로 꺼내올 수 있다.
//      토튼값 자체는 파라미터로 받아올 필요 없다. =>ContextUtil을 불러서 사용하면 된다.
//        메모장에 접근할 수 있게 Context 변수 하나를 미리 받아두자.

        fun getRequestMyInfo(context: Context, handler: JsonResponseHandler?) {

            val urlBuilder = "${BASE_URL}/user_info".toHttpUrlOrNull()!!.newBuilder()
                .build() // 쿼리파라미터를 담을게 없다. 바로Build로 마무리.

            val urlSting = urlBuilder.toString()

            val request = Request.Builder()
                .url(urlSting)
                .get()
                .header(
                    "X-Http-Token",
                    ContextUtil.getToken(context)
                ) //ContextUtil을 통해서 저장된 토큰을 받아서 첨부
                .build()

            val client = OkHttpClient()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {

                    val jsonObj = JSONObject(response.body!!.string())
                    Log.d("서버응답", jsonObj.toString())
                    handler?.onResponse(jsonObj)
                }

            })
        }

        fun getRequestMainInfo(context: Context, handler: JsonResponseHandler?) {

            val urlBuilder = "${BASE_URL}/v2/main_info".toHttpUrlOrNull()!!.newBuilder()
                .build() // 쿼리파라미터를 담을게 없다. 바로Build로 마무리.

            val urlSting = urlBuilder.toString()

            val request = Request.Builder()
                .url(urlSting)
                .get()
                .header(
                    "X-Http-Token",
                    ContextUtil.getToken(context)
                ) //ContextUtil을 통해서 저장된 토큰을 받아서 첨부
                .build()

            val client = OkHttpClient()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {

                    val jsonObj = JSONObject(response.body!!.string())
                    Log.d("서버응답", jsonObj.toString())
                    handler?.onResponse(jsonObj)
                }

            })
        }


        fun getRequestTopicDetail(context: Context, topicId: Int, handler: JsonResponseHandler?) {

            val urlBuilder = "${BASE_URL}/topic/${topicId}".toHttpUrlOrNull()!!.newBuilder()
                .addEncodedQueryParameter("order_type","NEW")//댓글도 최신수능로 내려오게 하는 파라미터 추가
                .build()

            val urlSting = urlBuilder.toString()

            val request = Request.Builder()
                .url(urlSting)
                .get()
                .header(
                    "X-Http-Token",
                    ContextUtil.getToken(context)
                ) //ContextUtil을 통해서 저장된 토큰을 받아서 첨부
                .build()

            val client = OkHttpClient()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {

                    val jsonObj = JSONObject(response.body!!.string())
                    Log.d("서버응답", jsonObj.toString())
                    handler?.onResponse(jsonObj)
                }

            })
        }

        fun postRequestVote(context: Context, sideId: Int, handler: JsonResponseHandler?) {

            val urlSting = "${BASE_URL}/topic_vote"

            val formData = FormBody.Builder()
                .add("side_id", sideId.toString())
                .build()

//            제작3) 모든 Request 정보를 종합한 객체 생성 (어느 주소로 + 어느 메쏘드로 + 어떤 파라미터를)
            val request = Request.Builder()
                .url(urlSting)
                .post(formData)
                .header("X-Http-Token", ContextUtil.getToken(context))
                .build()


            val client = OkHttpClient()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {


                }

                override fun onResponse(call: Call, response: Response) {


                    val bodyString = response.body!!.string()
                    val jsonObj = JSONObject(bodyString)

                    Log.d("서버응답", jsonObj.toString())

                    handler?.onResponse(jsonObj)
                }


            })


        }

        fun postRequestTopicReply(context: Context, topicId: Int, content: String, handler: JsonResponseHandler?) {

            val urlSting = "${BASE_URL}/topic_reply"

            val formData = FormBody.Builder()
                .add("topic_id", topicId.toString())
                .add("content", content)
                .build()

//            제작3) 모든 Request 정보를 종합한 객체 생성 (어느 주소로 + 어느 메쏘드로 + 어떤 파라미터를)
            val request = Request.Builder()
                .url(urlSting)
                .post(formData)
                .header("X-Http-Token", ContextUtil.getToken(context))
                .build()


            val client = OkHttpClient()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {


                }

                override fun onResponse(call: Call, response: Response) {


                    val bodyString = response.body!!.string()
                    val jsonObj = JSONObject(bodyString)

                    Log.d("서버응답", jsonObj.toString())

                    handler?.onResponse(jsonObj)
                }


            })


        }

    }

}