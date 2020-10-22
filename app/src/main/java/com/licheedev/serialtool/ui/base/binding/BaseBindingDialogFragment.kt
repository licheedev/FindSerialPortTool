package com.licheedev.serialtool.ui.base.binding

import BaseDialogFragment
import android.app.Dialog
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding


abstract class BaseBindingDialogFragment<T : ViewDataBinding> : BaseDialogFragment() {

    private var _binding: T? = null
    protected val binding get() = _binding!!

    override fun onDialogCreated(dialog: Dialog, rootView: View) {
        _binding = DataBindingUtil.bind(rootView)
        binding.lifecycleOwner = this
    }
}