package com.example.apipractice_okhttp_20220303

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.apipractice_okhttp_20220303.databinding.ActivitySignUpBinding
import com.example.apipractice_okhttp_20220303.utils.ServerUtil
import org.json.JSONObject

class SignUpActivity : BaseActivity() {

    lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_up)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        binding.btnSignUp.setOnClickListener {

            val inputEmail = binding.edtEmail.text.toString()
            val inputPw = binding.edtPassword.text.toString()
            val inputNickname = binding.edtNickname.text.toString()

            ServerUtil.putRequestSignUp(
                inputEmail,
                inputPw,
                inputNickname,
                object :ServerUtil.JsonResponseHandler{
                    override fun onResponse(jsonObj: JSONObject) {

//                        회원가입 성공 / 실패 분기처리
                        val code = jsonObj.getInt("code")

                        if(code == 200){

//                            가입한 사람의 닉네임 추출 > ~~님 가입을 축하합니다 라는 문구로 토스트 띄우기
//                            회원가입화면 종료 > 로그인 화면으로 복귀
                            val dataObj = jsonObj.getJSONObject("data")
                            val userObj = dataObj.getJSONObject("user")

                            val nickname = userObj.getString("nick_name")

                            runOnUiThread {

                                Toast.makeText(mContext, "${nickname}님 가입을 축하합니다.", Toast.LENGTH_SHORT).show()

                            }
//                            화면 종료 : 객체 소멸 (UI동작 아님)
                            finish()

                        }
                        else {
                            val message = jsonObj.getString("message")

                            runOnUiThread {

                                Toast.makeText(mContext, "실패사유 :${message}", Toast.LENGTH_SHORT).show()

                            }
                        }

                    }

                }
            )

        }

    }

    override fun setValues() {

    }


}