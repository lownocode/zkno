package com.quickrise.zkno.ui.activities.splash_screen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.quickrise.zkno.*
import com.quickrise.zkno.App.Companion.user
import com.quickrise.zkno.api.ApiRepository
import com.quickrise.zkno.Utils
import com.quickrise.zkno.ui.activities.auth.AuthActivity
import com.quickrise.zkno.ui.activities.main.MainActivity
import com.quickrise.zkno.ui.activities.required_update.RequiredUpdateActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash_screen)
        initialize()
    }

    private fun getDarkMode() {
        val mode = when(Preferences(this).settings?.getString(Key.DARK_THEME_MODE, Key.MODE_AUTO)) {
            Key.MODE_ON -> AppCompatDelegate.MODE_NIGHT_YES
            Key.MODE_OFF -> AppCompatDelegate.MODE_NIGHT_NO
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM //auto
        }

        AppCompatDelegate.setDefaultNightMode(mode)
    }

    private fun checkUpdate() {
//        if (user?.newAppVersion?.isRequired == true) {
//            val intent = Intent(this, RequiredUpdateActivity::class.java)
//
//            finish()
//            startActivity(intent)
//
//            return
//        }

        val intent = Intent(this, MainActivity::class.java)

        finish()
        startActivity(intent)
    }

    private fun initialize() {
        getDarkMode()

        with (window) {
            ContextCompat.getColor(applicationContext, R.color.accent).also {
                this.statusBarColor = it
                this.navigationBarColor = it
            }
        }

        val userTokenIsEmpty = Preferences(this).user?.getString("token", null) == null

        if (userTokenIsEmpty) {
            val authIntent = Intent(applicationContext, AuthActivity::class.java)

            if (intent.data != null) {
                val deeplinkData = Utils.decodeDeeplink(intent.data.toString())

                when(deeplinkData.path) {
                    "signUp" -> {
                        authIntent.putExtra("code", deeplinkData.values[0])

                        startActivity(authIntent)
                        finish()
                    }
                    "signIn" -> when (deeplinkData.values[0]) {
                        "ACCOUNT_NOT_LINKED" -> {
                            Toast.makeText(
                                applicationContext,
                                "Ваш аккаунт не привязан к ZKNO",
                                Toast.LENGTH_SHORT
                            ).show()

                            startActivity(authIntent)
                            finish()
                        }
                        else -> {
                            Preferences(this)
                                .user
                                ?.edit()
                                ?.putString("token", deeplinkData.values[0])
                                ?.apply()

                            checkUpdate()
                        }
                    }
                    else -> {
                        startActivity(authIntent)
                        finish()
                    }
                }

                return
            }

            startActivity(authIntent)
            return finish()
        }

        checkUpdate()
    }
}