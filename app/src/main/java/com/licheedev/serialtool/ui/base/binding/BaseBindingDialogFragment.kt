package com.licheedev.serialtool.ui.base.binding

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.licheedev.serialtool.ui.base.BaseDialogFragment


abstract class BaseBindingDialogFragment<T : ViewDataBinding> : BaseDialogFragment() {

    private var _binding: T? = null
    protected val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = DataBindingUtil.bind(view)
        binding.lifecycleOwner = viewLifecycleOwner
    }


}