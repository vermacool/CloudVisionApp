package com.example.demoapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        var preferenceUtils = PreferenceUtils(this@SplashActivity)
        if(PreferenceUtils.getLaunchCount()!=null && PreferenceUtils.getLaunchCount()!!){
            PreferenceUtils.setLaunchPref(false)
            PlaceUpdateService.startActionFoo(this@SplashActivity, "", "")
        }
        Handler().postDelayed(object :Runnable{
            override fun run() {
                startActivity(Intent(this@SplashActivity,MainActivity::class.java))
            }
        },3000)
    }
}
