package com.quickrise.zkno.ui.activities.auth

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.quickrise.zkno.App.Companion.user
import com.quickrise.zkno.Preferences
import com.quickrise.zkno.R
import com.quickrise.zkno.api.ApiRepository
import com.quickrise.zkno.databinding.ActivityAuthBinding
import com.quickrise.zkno.foundation.model.SignInBody
import com.quickrise.zkno.foundation.model.SignUpBody
import com.quickrise.zkno.ui.fragments.dialogs.LoadingDialog
import com.quickrise.zkno.Utils
import com.quickrise.zkno.foundation.base.viewBinding
import com.quickrise.zkno.ui.activities.required_update.RequiredUpdateActivity
import com.quickrise.zkno.ui.activities.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AuthActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityAuthBinding::inflate)
    private val apiRepository get() = ApiRepository(this)
    private var authType = "signIn"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        with(window) {
            ContextCompat.getColor(applicationContext, R.color.background_content).also {
                this.statusBarColor = it
                this.navigationBarColor = it
            }
        }

        val inputBackground = GradientDrawable()

        with(inputBackground) {
            color = ContextCompat.getColorStateList(applicationContext, R.color.divider)
            cornerRadius = 25f
        }

        with(binding) {
            inputLogin.background = inputBackground
            inputPassword.background = inputBackground
            inputCode.background = inputBackground

            btnVK.setOnClickListener { authWithVK() }
            btnAuth.setOnClickListener { auth() }
            btnChangeAuthType.setOnClickListener { changeAuthTypeWithButton() }
        }

        if (intent.getStringExtra("code")?.isNotBlank() == true) {
            changeAuthType("signUp")
            binding.inputCode.setText(intent.getStringExtra("code").toString())
        }
    }

    private fun auth() {
        val loadingDialog = LoadingDialog().also {
            it.show(supportFragmentManager, "loading")
        }

        when(authType) {
            "signIn" -> signIn(loadingDialog)
            "signUp" -> signUp(loadingDialog)
        }
    }

    private fun signIn(loadingDialog: LoadingDialog) {
        val credentials = SignInBody(
            login = binding.inputLogin.text.toString(),
            password = binding.inputPassword.text.toString()
        )

        CoroutineScope(Dispatchers.IO).launch {
            val userData = apiRepository.signIn(credentials)

            loadingDialog.dismiss()

            if (userData.code() != 200) {
                val error = userData.errorBody()?.string()?.let { JSONObject(it) }

                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        applicationContext,
                        error?.get("message").toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                return@launch
            }

            user = userData.body()
            Preferences(this@AuthActivity)
                .user
                ?.edit()
                ?.putString("token", user?.token)
                ?.apply()

            checkUpdate()
        }
    }

    private fun signUp(loadingDialog: LoadingDialog) {
        val credentials = SignUpBody(
            login = binding.inputLogin.text.toString(),
            password = binding.inputPassword.text.toString(),
            code = binding.inputCode.text.toString(),
        )

        CoroutineScope(Dispatchers.IO).launch {
            val userData = apiRepository.signUp(credentials)

            loadingDialog.dismiss()

            if (userData.code() != 200) {
                val error = userData.errorBody()?.string()?.let { JSONObject(it) }

                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        applicationContext,
                        error?.get("message").toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                return@launch
            }

            user = userData.body()
            Preferences(this@AuthActivity)
                .user
                ?.edit()
                ?.putString("token", user?.token)
                ?.apply()

            checkUpdate()
        }
    }

    private fun changeAuthType(type: String) {
        when(type) {
            "signIn" -> {
                authType = "signIn"
                with(binding) {
                    authorization.text = getString(R.string.authorization)
                    inputCode.visibility = View.GONE
                    inputLogin.hint = getString(R.string.inputLogin)
                    inputPassword.hint = getString(R.string.inputPassword)

                    with(btnAuth) {
                        text = getString(R.string.signIn)
                        backgroundTintList = ContextCompat.getColorStateList(
                            applicationContext,
                            R.color.accent
                        )
                    }
                    with(btnChangeAuthType) {
                        text = getString(R.string.signUp)
                        backgroundTintList = ContextCompat.getColorStateList(
                            applicationContext,
                            R.color.mark_5
                        )
                    }
                }
            }
            "signUp" -> {
                authType = "signUp"
                with(binding) {
                    authorization.text = getString(R.string.registration)
                    inputCode.visibility = View.VISIBLE
                    inputLogin.hint = getString(R.string.thinkUpLogin)
                    inputPassword.hint = getString(R.string.thinkUpPassword)

                    with(btnAuth) {
                        text = getString(R.string.signUp)
                        backgroundTintList = ContextCompat.getColorStateList(
                            applicationContext,
                            R.color.mark_5
                        )
                    }
                    with(btnChangeAuthType) {
                        text = getString(R.string.signIn)
                        backgroundTintList = ContextCompat.getColorStateList(
                            applicationContext,
                            R.color.accent
                        )
                    }
                }
            }
        }
    }

    private fun checkUpdate() {
        if (user?.newAppVersion?.isRequired == true) {
            val intent = Intent(this, RequiredUpdateActivity::class.java)

            finish()
            startActivity(intent)

            return
        }

        val intent = Intent(this, MainActivity::class.java)

        Utils.hideKeyboard(this, View(this@AuthActivity))
        finish()
        startActivity(intent)
    }

    private fun changeAuthTypeWithButton() = when(authType) {
        "signIn" -> changeAuthType("signUp")
        else -> changeAuthType("signIn")
    }

    private fun authWithVK() {
        val url = "https://zkno.ru/auth/vk"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

        startActivity(intent)
        finish()
    }
}