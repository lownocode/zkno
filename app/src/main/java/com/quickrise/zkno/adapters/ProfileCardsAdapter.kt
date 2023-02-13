package com.quickrise.zkno.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.quickrise.zkno.*
import com.quickrise.zkno.databinding.ProfileDataItemBinding
import com.quickrise.zkno.foundation.base.viewBinding
import com.quickrise.zkno.foundation.model.UserModel
import com.quickrise.zkno.foundation.model.*

@SuppressLint("NotifyDataSetChanged")
class ProfileCardsAdapter : RecyclerView.Adapter<ProfileCardsAdapter.Holder>() {
    private var list: ArrayList<ProfileCardItem> = getCardsList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var user: UserModel = emptyUser
        set(value) {
            field = value
            list = getCardsList()
        }

    class Holder(val binding: ProfileDataItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(parent.viewBinding(ProfileDataItemBinding::inflate))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = list[position]

        with (holder.binding) {
            withActivity { activity ->
                container.background = GradientDrawable().also {
                    it.color = ContextCompat.getColorStateList(
                        activity,
                        R.color.background_bottom_nav
                    )
                    it.cornerRadii = getContainerCorners(position)
                }
                iconContainer.background = GradientDrawable().also {
                    it.color = ContextCompat.getColorStateList(
                        activity,
                        R.color.background_card
                    )
                    it.cornerRadius = 25f
                }
                icon.background = ContextCompat.getDrawable(
                    activity.applicationContext,
                    item.icon
                )
                icon.backgroundTintList = ContextCompat.getColorStateList(
                    activity,
                    R.color.text
                )

                if (item.actionIcon != null) {
                    with (actionIcon) {
                        visibility = View.VISIBLE
                        setOnClickListener { item.actionIconOnClick?.invoke() }
                        background = ContextCompat.getDrawable(
                            activity,
                            item.actionIcon
                        )
                    }
                }
            }

            title.text = item.title
            subtitle.text = item.subtitle
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    private fun getCardsList() = arrayListOf(
        ProfileCardItem(
            title = "Специальность",
            subtitle = user?.group?.specialty ?: "не null, честно",
            icon = R.drawable.ic_academic_cap
        ),
        ProfileCardItem(
            title = "Куратор",
            subtitle = user?.group?.curator ?: "не null, честно",
            icon = R.drawable.ic_teacher
        ),
        ProfileCardItem(
            title = "Корпус",
            subtitle = user?.group?.location?.address ?: "не null, честно",
            icon = R.drawable.ic_location_map,
            actionIcon = R.drawable.ic_location,
            actionIconOnClick = { openUserGroupLocation() }
        )
    )

    private fun openUserGroupLocation() = withActivity {
        val ll = user?.group?.location?.coordinates

        if (Utils.isAppInstalled("ru.yandex.yandexmaps")) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("yandexmaps://maps.yandex.ru/?ll=$ll&z=25"))

            return@withActivity it.startActivity(intent)
        }

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://yandex.ru/maps?ll=$ll&z=25"))

        it.startActivity(intent)
    }

    private fun getContainerCorners(position: Int): FloatArray {
        return when(position) {
            0 -> floatArrayOf(35f, 35f, 35f, 35f, 20f, 20f, 20f, 20f)
            list.size - 1 -> floatArrayOf(20f, 20f, 20f, 20f, 35f, 35f, 35f, 35f)
            else -> floatArrayOf(20f, 20f, 20f, 20f, 20f, 20f, 20f, 20f)
        }
    }
}