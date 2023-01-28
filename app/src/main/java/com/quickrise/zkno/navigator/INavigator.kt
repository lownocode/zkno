package com.quickrise.zkno.navigator

interface INavigator {
    fun navigate(fragmentId: Int, shouldAddToStack: Boolean = true)

    fun selectTab(tabId: String)

    fun goBack()

    fun popFragment()

    fun getCurrentActiveTabId(): Int
}