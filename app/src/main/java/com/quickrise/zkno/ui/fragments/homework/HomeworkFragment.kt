package com.quickrise.zkno.ui.fragments.homework

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.quickrise.zkno.R
import com.quickrise.zkno.databinding.FragmentHomeworkBinding
import com.quickrise.zkno.foundation.base.viewBinding

class HomeworkFragment : Fragment(R.layout.fragment_homework) {
    private val binding by viewBinding(FragmentHomeworkBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}