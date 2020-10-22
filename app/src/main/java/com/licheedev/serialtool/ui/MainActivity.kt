package com.licheedev.serialtool.ui

import android.os.Bundle
import com.licheedev.serialtool.R
import com.licheedev.serialtool.ui.base.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}