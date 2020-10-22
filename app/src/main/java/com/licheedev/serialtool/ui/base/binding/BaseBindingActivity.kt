package com.licheedev.serialtool.ui.base.binding

import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.ViewDataBinding
import com.licheedev.serialtool.ui.base.BaseActivity


abstract class BaseBindingActivity<T : ViewDataBinding> : BaseActivity() {


    private var _binding: T? = null
    protected val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = bindingInflate().invoke(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)
    }

    /**
     * 返回 XxxBinding::inflate 函数引用
     */
    abstract fun bindingInflate(): (LayoutInflater) -> T

}