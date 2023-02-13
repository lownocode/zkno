package com.quickrise.zkno.ui.activities.splash_screen

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.quickrise.zkno.*
import com.quickrise.zkno.Utils
import com.quickrise.zkno.ui.activities.auth.AuthActivity
import com.quickrise.zkno.ui.activities.main.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash_screen)

        getDarkMode()
        initialize()
    }

    private fun getDarkMode() {
        val mode = when (Preferences(this).settings?.getString(Key.PREF_DARK_THEME_MODE, Key.MODE_ON)) {
            Key.MODE_ON -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_NO
        }

        AppCompatDelegate.setDefaultNightMode(mode)
    }

    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)

        finish()
        startActivity(intent)
    }

    private fun initialize() {
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

                            goToMain()
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

        goToMain()
    }
}