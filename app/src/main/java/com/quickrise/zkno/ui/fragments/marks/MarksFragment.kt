package com.quickrise.zkno.ui.fragments.marks

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.quickrise.zkno.R
import com.quickrise.zkno.adapters.MarksAdapter
import com.quickrise.zkno.databinding.FragmentMarksBinding
import com.quickrise.zkno.foundation.base.log
import com.quickrise.zkno.foundation.base.viewBinding
import com.quickrise.zkno.foundation.base.viewModelFactory

class MarksFragment : Fragment(R.layout.fragment_marks) {
    private val binding by viewBinding(FragmentMarksBinding::bind)
    private val viewModel: MarksViewModel by viewModels { viewModelFactory() }
    private lateinit var marksAdapter: MarksAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        marksAdapter = MarksAdapter(requireActivity())

        setupViews()
        setupViewModeObservers()
    }

    private fun setupViews() = with (binding) {
        marksList.adapter = marksAdapter
        marksList.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupViewModeObservers() = with (viewModel) {
        marksList.observe(viewLifecycleOwner) {
            marksAdapter.list = it
        }
    }
}