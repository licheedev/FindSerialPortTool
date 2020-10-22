package com.licheedev.serialtool.ui.base.binding

import BaseFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding


abstract class BaseBindingFragment<T : ViewDataBinding> : BaseFragment() {

    private var _binding: T? = null
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflate().invoke(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    /**
     * 返回 XxxBinding::inflate 函数引用
     */
    abstract fun bindingInflate(): (LayoutInflater, ViewGroup?, Boolean) -> T

}