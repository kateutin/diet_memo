package com.example.diet_memo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class SplashActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth //파이어베이스에서 해당 내용 검색 후 페이지 내용 복사

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        auth = Firebase.auth//파이어베이스에서 내용 복사사용
        try {
            Log.d("SPLASH",auth.currentUser!!.uid)//로그인 정보 확인
            Toast.makeText(this,"비회원 로그인이 되어 있는 사람입니다",Toast.LENGTH_LONG).show()//로그인 정보가 있을 때 나타는 문구
            Handler().postDelayed({
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            },3000) //3초 딜레이 후 메인 Act 넘김

        }catch (e :Exception){
            Log.d("SPLASH","회원가입해")
        } //try를 시도했을 때 실패하면 catch를 실행 시켜라

        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this,"비회원 로그인 성공",Toast.LENGTH_LONG).show()//회원 가입 후 비회원 로그인 성공 토스트트
                    Handler().postDelayed({
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    },3000) //3초 딜레이 후 메인Act

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this,"비회원 로그인 실패",Toast.LENGTH_LONG).show()
                }//값이 없으면 로그인 후 메인Act 넘기면 됨
            }

    }
}