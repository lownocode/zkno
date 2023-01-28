package com.quickrise.zkno.ui.fragments.profile

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.quickrise.zkno.*
import com.quickrise.zkno.App.Companion.user
import com.quickrise.zkno.adapters.ProfileCardsAdapter
import com.quickrise.zkno.databinding.FragmentProfileBinding
import com.quickrise.zkno.foundation.base.navigator
import com.quickrise.zkno.foundation.base.viewBinding
import com.quickrise.zkno.foundation.base.viewModelFactory
import com.quickrise.zkno.foundation.model.ProfileCardItem
import com.quickrise.zkno.foundation.model.UserModel
import com.quickrise.zkno.ui.fragments.bottomSheets.NewAppVersionBottomSheet

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val binding by viewBinding(FragmentProfileBinding::bind)
    private val viewModel by viewModels<ProfileViewModel> { viewModelFactory() }
    private val profileCardsAdapter = ProfileCardsAdapter()
    private var savedInstanceState: Bundle? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.savedInstanceState = savedInstanceState

        loadUserAvatar()
        setupViews()
        setupViewModelObservers()
    }

    private fun setupViews() = with (binding) {
        val avatarBackground = GradientDrawable().also {
            it.cornerRadius = resources.getDimension(R.dimen.circleBorderRadius)
        }

        avatar.background = avatarBackground
        avatar.clipToOutline = true
        cardsList.adapter = profileCardsAdapter
        cardsList.layoutManager = LinearLayoutManager(requireContext())

        btnSettings.setOnClickListener { goToSettings() }
        btnUpdate.setOnClickListener { goToUpdateModal() }
    }

    private fun setupViewModelObservers() = with (viewModel) {
        user.observe(viewLifecycleOwner) {
            updateUserData(it)
            profileCardsAdapter.user = it
        }
    }

    private fun checkUpdate() {
        val ignoreCode = Preferences(requireActivity()).app?.getInt("ignoreAppUpdateCode", 0)

        if (
            viewModel.user.value?.newAppVersion != null &&
            ignoreCode != BuildConfig.VERSION_CODE &&
            savedInstanceState == null
        ) {
            NewAppVersionBottomSheet().show(parentFragmentManager, "newVersion")
        }//testst
    }

    private fun updateUserData(user: UserModel) = with (binding) {
        if (viewModel.user.value?.newAppVersion != null) {
            btnUpdate.visibility = View.VISIBLE
        }

        checkUpdate()
        loadUserAvatar()

        name.text = user.name
        groupShort.text = user.group?.name
    }

    private fun loadUserAvatar() =
        Glide.with(this)
            .load(viewModel.user.value?.photo)
            .skipMemoryCache(false)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .placeholder(R.drawable.avatar_placeholder)
            .into(binding.avatar)

    private fun goToSettings() =
        navigator().navigate(fragmentId = FragmentIndex.SETTINGS)

    private fun goToUpdateModal() =
        NewAppVersionBottomSheet().show(parentFragmentManager, "update")
}