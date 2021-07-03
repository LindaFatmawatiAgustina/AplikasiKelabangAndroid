package com.aer.kelabangapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import com.aer.kelabangapp.helper.SessionManager
import org.jetbrains.anko.startActivity

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val session = SessionManager(this)

        //4second splash time
        Handler().postDelayed({
            //start main activity
            if (session.isLogin()){
                startActivity(Intent(this, MainActivity::class.java))

            }else{
                startActivity(Intent(this, Login::class.java))
            }
            //finish this activity
            finish()
        },3000)

    }
    }
