package com.quickrise.zkno.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.quickrise.zkno.ui.fragments.about.AboutFragment
import com.quickrise.zkno.ui.fragments.dinner.DinnerFragment
import com.quickrise.zkno.ui.fragments.marks.MarksFragment
import com.quickrise.zkno.ui.fragments.profile.ProfileFragment
import com.quickrise.zkno.ui.fragments.schedule.ScheduleFragment
import com.quickrise.zkno.ui.fragments.settings.SettingsFragment

class FragmentContainerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    val fragments = listOf(
        /* 0 */ ProfileFragment(),
        /* 1 */ MarksFragment(),
        /* 2 */ ScheduleFragment(),
        /* 3 */ DinnerFragment(),
        /* 4 */ SettingsFragment(),
        /* 5 */ AboutFragment(),
    )

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    override fun getItemCount(): Int {
        return fragments.size
    }
}