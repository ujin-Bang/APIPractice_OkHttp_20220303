package com.example.apipractice_okhttp_20220303

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.example.apipractice_okhttp_20220303.adapters.TopicAdapter
import com.example.apipractice_okhttp_20220303.databinding.ActivityMainBinding
import com.example.apipractice_okhttp_20220303.datas.TopicData
import com.example.apipractice_okhttp_20220303.utils.ContextUtil
import com.example.apipractice_okhttp_20220303.utils.ServerUtil
import org.json.JSONObject

class MainActivity : BaseActivity() {

    lateinit var binding : ActivityMainBinding

//    실제로 서버가 내려주는 주제 목록을 담을 그릇
    val mTopicList = ArrayList<TopicData>()

    lateinit var mAdapter: TopicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setValues()
        setupEvents()
    }
    override fun setupEvents() {

        binding.topicListView.setOnItemClickListener { adapterView, view, poistion, l ->

            val clickedTopic = mTopicList[poistion]

            val myIntent = Intent(mContext, ViewTopicDetailActivity::class.java)
            myIntent.putExtra("topic", clickedTopic)
            startActivity(myIntent)

        }

        binding.btnLogout.setOnClickListener {

//            경고창 > 확인시 로그아웃

            val alert = AlertDialog.Builder(mContext)
                .setTitle("로그아웃")
                .setMessage("정말 로그아웃 하시겠습니까?")
                .setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->

//                    로그인 : 토큰값을 받아서 > 기기에 저장

//                    로그아웃 : 토큰값 (폰에서) 삭제 > 서버에서 받아올 필요 없다.

                    ContextUtil.setToken(mContext, "")
                    val myIntent = Intent(mContext, SplashActivity::class.java)
                    startActivity(myIntent)
                    finish()
                })
                .setNegativeButton("취소", null)
                .show()

        }

    }

    override fun setValues() {

//        메인 화면 정보 가져오기 => API 호출 / 응답 처리.
//        코드상으로는 먼저 실행시키지만 완료는 어댑터 연결보다 늦을 수도 있다.
//         => 목록에 토론 주제 추가 : 어댑터 연결 이후 추가
//           => 리스트뷰 어댑터의 내용물의 변경 : notifyDataSetChanged 실행해야 함.
        getTopicListFromServer()

        mAdapter = TopicAdapter(mContext, R.layout.topic_list_item, mTopicList)
        binding.topicListView.adapter = mAdapter
    }

    fun getTopicListFromServer() {

        ServerUtil.getRequestMainInfo( mContext, object : ServerUtil.JsonResponseHandler{
            override fun onResponse(jsonObj: JSONObject) {

//                서버가 주는 토론 주제 목록 파싱 => mTopicList에 추가하기.
                val dataObj = jsonObj.getJSONObject("data")
                val topicsArr = dataObj.getJSONArray("topics")

//                topicsArr내부를 하나씩 추출(JSONObject { }) => Topics() 로 변환

//                JSONArray는 for-each 문법을 지원하지 않는다.(차후에는 ArrayList의 for-each활용 예정)
//                JAVA : for(int i=0; i < 배열.lenth; i++)와 완전히 동일한 문법.
                for(i in 0 until topicsArr.length()){

//                    [ ] => {}, {}, {} ...순서에 맞는{}를 변수에 담자.
//                    JSON파싱의 {}를 추출한다는 것은 => (JSONArray에게서) JSONObject로 추출하겠다는 것이다.

                    val topicObj = topicsArr.getJSONObject( i )
                    Log.d("받아낸 주제", topicObj.toString())

//                    TopicData 변수 생성 =>멤버변수에 topicObj가 들고 있는 값들을 대입
                    val topicData =TopicData()
                    topicData.id = topicObj.getInt("id")
                    topicData.title = topicObj.getString("title")
                    topicData.imageURL = topicObj.getString("img_url")
                    topicData.replyCount = topicObj.getInt("reply_count")

//                    완성된 TopicData객체를 목록에 추가.
                    mTopicList.add(topicData)
                }

//                리스트뷰의 내용물 새로고침 => UI에 내용물 변경 행위
                runOnUiThread {
                    mAdapter.notifyDataSetChanged()

                }
            }

        })

    }


}