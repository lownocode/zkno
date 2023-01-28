package com.quickrise.zkno.foundation.base

typealias ActivityAction <T> = (T) -> Unit

class ActivityActions <T> {
    var activity: T? = null
        set(value) {
            field = value
            if (value != null) {
                actions.forEach { it(value) }
                actions.clear()
            }
        }

    private val actions = mutableListOf<ActivityAction<T>>()

    operator fun invoke(action: ActivityAction<T>) {
        if (activity == null) {
            actions += action

            return
        }

        action(activity!!)
    }

    fun clear() {
        actions.clear()
    }
}