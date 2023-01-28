package com.quickrise.zkno.ui.activities.main

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.quickrise.zkno.*
import com.quickrise.zkno.adapters.FragmentContainerAdapter
import com.quickrise.zkno.databinding.ActivityMainBinding
import com.quickrise.zkno.foundation.base.viewBinding
import com.quickrise.zkno.navigator.ViewModelNavigator

class MainActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityMainBinding::inflate)
    private val navigator by viewModels<ViewModelNavigator> { AndroidViewModelFactory(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        setupViews()
        setupNavigation()
        setupBackHandler()
        initialize()
    }

    override fun onResume() {
        super.onResume()

        withActivity.activity = this
    }

    override fun onPause() {
        super.onPause()

        withActivity.activity = null
    }


    private fun setupViews() = with (binding) {
        viewPagerFragmentContainer.adapter = FragmentContainerAdapter(this@MainActivity)
        viewPagerFragmentContainer.isUserInputEnabled = false
    }

    private fun setupNavigation() {
        navigator.selectTab(Tab.PROFILE)

        with (binding) {
            bottomNavigationView.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.profile  -> navigator.selectTab(Tab.PROFILE)
                    R.id.marks    -> navigator.selectTab(Tab.MARKS)
                    R.id.schedule -> navigator.selectTab(Tab.SCHEDULE)
                    R.id.dinner   -> navigator.selectTab(Tab.DINNER)
                }
                true
            }
        }
    }

    private fun setupBackHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigator.goBack()
            }
        })
    }

    private fun notificationPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissions(
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
    }

    private fun initialize() {
        notificationPermission()

        window.navigationBarColor = resources.getColor(R.color.background_bottom_nav, theme)

        val isCompactBottomNav = Preferences(this)
            .settings
            ?.getBoolean(Key.MENU_IS_COMPACT, false)

        if (isCompactBottomNav == true) {
            binding.bottomNavigationView.apply {
                layoutParams.height = resources.getDimension(R.dimen.bottomNavCompact).toInt()
                labelVisibilityMode = BottomNavigationView.LABEL_VISIBILITY_UNLABELED
            }
        } else {
            binding.bottomNavigationView.apply {
                layoutParams.height = resources.getDimension(R.dimen.fullSizeBottomNav).toInt()
                labelVisibilityMode = BottomNavigationView.LABEL_VISIBILITY_SELECTED
            }
        }
    }
}