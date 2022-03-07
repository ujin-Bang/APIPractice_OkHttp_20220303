package com.example.apipractice_okhttp_20220303

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.apipractice_okhttp_20220303.databinding.ActivityLoginBinding
import com.example.apipractice_okhttp_20220303.utils.ContextUtil
import com.example.apipractice_okhttp_20220303.utils.ServerUtil
import org.json.JSONObject

class LoginActivity : BaseActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

//        체크박스의 체크여부가 변경되면(이벤트 처리) > ContextUtil 이용해서 체크값 저장

        binding.autoLoginCheckBox.setOnCheckedChangeListener { compoundButton, isChecked ->

            Log.d("체크값변경","${isChecked}로 변경됨")

        }

        binding.btnSignUp.setOnClickListener {

            val myIntent = Intent(mContext, SignUpActivity::class.java)
            startActivity(myIntent)
        }

        binding.btnLogin.setOnClickListener {

//            id / pw 추출
            val inputId = binding.edtId.text.toString()
            val inputPw = binding.edtPassword.text.toString()

//            API서버에 아이디 / 비번 보내서 실제로 회원인지 검사 => 로그인 시도

            ServerUtil.postRequestLogin(inputId, inputPw, object : ServerUtil.JsonResponseHandler {
                override fun onResponse(jsonObj: JSONObject) {

//                    화면의 입장에서 로그인 결과를 받아서 처리할 코드.
//                    서버에 다녀오고 실행 : 라이브러리가 자동으로 백그라운드에서 돌도록 만들어둔 코드.
                    val code = jsonObj.getInt("code")

                    if (code == 200) {

                        val dataObj = jsonObj.getJSONObject("data")
                        val userObj = dataObj.getJSONObject("user")
                        val nickname = userObj.getString("nick_name")

                        runOnUiThread {
                            Toast.makeText(mContext, "${nickname}님 환영합니다.", Toast.LENGTH_SHORT).show()
                        }

//                        서버가 내려준 토큰값을 변수에 담아보자.
                        val token = dataObj.getString("token")

                        ContextUtil.setToken(mContext, token)

//                        변수에 담긴 토큰값(String)을 SharedPreferences에 담아두자.
//                        로그인 성공시에는 담기만, 필요한 화면/클래스에서 깨내서 사용

//                        메인 화면으로 진입 => 클래스의 객체화(UI동작이 아니다. UI쓰레드안에 넣지 않아도 됨)

                        val myIntent = Intent(mContext, MainActivity::class.java)
                        startActivity(myIntent)

                    } else {

                        val message = jsonObj.getString("message")

//                        토스트는 UI를 조작하는 명령 => 백그라운드에서 UI를 건드리면 안드로이드는 위험한 동작으로 간주하고 앱을 강제로 종료시킨다.


                        runOnUiThread {

//                           토스트를 띄우는 코드만 UI전담 쓰레드에서 실행하도록
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()

                        }

                    }

                }

            })


        }


    }

    override fun setValues() {

//        이전에 설정한 자동로그인 여부를 미리 체크해두자.
//        껏다 켜도 계속 반영되고 싶다 => 반 영구적으로 저장 => SharedPreferences에 저장

//        저장되어있는 자동로그인 여부 값을 체크박스에 반영
        binding.autoLoginCheckBox.isChecked = ContextUtil.getAutoLogIn(mContext)


    }
}