package com.bizmiz.umidjonmarket111

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bizmiz.umidjonmarket111.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var prefs: SharedPreferences
    private  var isAuth = false
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = getSharedPreferences("MY_PREFS_NAME", Context.MODE_PRIVATE)
        isAuth = getPrefs()
//        if (!isAuth){
//            startActivity(Intent(this, ContainerActivity::class.java))
//            finish()
//        }
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