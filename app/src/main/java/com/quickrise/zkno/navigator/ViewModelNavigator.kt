package com.quickrise.zkno.navigator

import android.app.Application
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.viewpager2.widget.ViewPager2
import com.quickrise.zkno.*
import java.util.*

class ViewModelNavigator(
    application: Application
) : AndroidViewModel(application), INavigator {
    private var currentActiveTab = Tab.PROFILE
    private val tabStacks = HashMap<String, Stack<Int>>()

    init {
        tabStacks[Tab.PROFILE]  = Stack<Int>()
        tabStacks[Tab.MARKS]    = Stack<Int>()
        tabStacks[Tab.SCHEDULE] = Stack<Int>()
        tabStacks[Tab.DINNER]   = Stack<Int>()
    }

    override fun navigate(
        fragmentId: Int,
        shouldAddToStack: Boolean,
    ) = withActivity { activity ->
        if (
            shouldAddToStack &&
            tabStacks[currentActiveTab]?.find { fragmentId == it } == null
        ) {
            tabStacks[currentActiveTab]?.push(fragmentId)
        }

        val viewPager = activity.findViewById(R.id.viewPagerFragmentContainer) as ViewPager2

        viewPager.setCurrentItem(fragmentId, false)

        Utils.vibrate(activity, 10)
        getActualStatusBarColor()
    }

    override fun selectTab(tabId: String) {
        if (currentActiveTab == tabId && tabStacks[tabId]!!.isNotEmpty()) {
            navigate(getCurrentActiveTabId(), false)
            tabStacks[currentActiveTab]!!.clear()
            getActualStatusBarColor()

            return
        }

        currentActiveTab = tabId

        if (tabStacks[tabId]!!.isEmpty()) {
            when (tabId) {
                Tab.PROFILE  -> navigate(FragmentIndex.PROFILE, false)
                Tab.MARKS    -> navigate(FragmentIndex.MARKS, false)
                Tab.SCHEDULE -> navigate(FragmentIndex.SCHEDULE, false)
                Tab.DINNER   -> navigate(FragmentIndex.DINNER, false)
            }

            return
        }

        navigate(tabStacks[tabId]!!.last())
    }

    override fun goBack() = withActivity {
        if (tabStacks[currentActiveTab]!!.isEmpty()) {
            return@withActivity it.finish()
        }

        popFragment()
        getActualStatusBarColor()
    }

    override fun popFragment() = withActivity {
        if (tabStacks[currentActiveTab]!!.size == 1) {
            navigate(getCurrentActiveTabId(), false)
            tabStacks[currentActiveTab]!!.pop()

            return@withActivity
        }

        val viewPager: ViewPager2 = it.findViewById(R.id.viewPagerFragmentContainer)
        val fragmentId = tabStacks[currentActiveTab]!!.elementAt(
            tabStacks[currentActiveTab]!!.size - 2
        )

        viewPager.setCurrentItem(fragmentId, false)
        tabStacks[currentActiveTab]!!.pop()
    }

    override fun getCurrentActiveTabId() = when (currentActiveTab) {
        Tab.MARKS    -> FragmentIndex.MARKS
        Tab.SCHEDULE -> FragmentIndex.SCHEDULE
        Tab.DINNER   -> FragmentIndex.DINNER
        else -> FragmentIndex.PROFILE
    }

    override fun onCleared() {
        super.onCleared()
        withActivity.clear()
    }

    private fun getActualStatusBarColor() = withActivity { activity ->
        val color = if (tabStacks[currentActiveTab]!!.isEmpty()) {
            when(currentActiveTab) { // когда активны только фрагменты табов
                Tab.PROFILE -> R.color.background_content
                else -> R.color.background_bottom_nav
            }
        } else {
            when(tabStacks[currentActiveTab]!!.last()) { // когда активен любой из фрагментов
                FragmentIndex.PROFILE -> R.color.background_content
                else -> R.color.background_bottom_nav
            }
        }

        ContextCompat.getColor(activity, color).also {
            if (activity.window.statusBarColor != it) {
                activity.window.statusBarColor = it
            }
        }
    }
}