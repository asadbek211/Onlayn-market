package com.bizmiz.umidjonmarket111

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bizmiz.umidjonmarket111.auth.container.ContainerActivity
import com.bizmiz.umidjonmarket111.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var prefs: SharedPreferences
    private  var isAuth = false
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        prefs = getSharedPreferences("MY_PREFS_NAME", Context.MODE_PRIVATE)
        isAuth = getPrefs()
        if (auth.currentUser==null){
            startActivity(Intent(this, ContainerActivity::class.java))
            finish()
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setPrefs(false)
        
    }
    private fun setPrefs(isAuth: Boolean) {
        prefs = getSharedPreferences("MY_PREFS_NAME", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("isAuth", isAuth).apply()
    }
    fun getPrefs(): Boolean {
        prefs = getSharedPreferences("MY_PREFS_NAME", Context.MODE_PRIVATE)
        return prefs.getBoolean("isAuth", false)
    }
}