package com.licheedev.serialtool.ui.base

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.licheedev.base.CommonActivity
import com.licheedev.myutils.ui.SystemUi


open class BaseActivity : CommonActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTranslucentStatus()
        super.onCreate(savedInstanceState)

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            SystemUi.hideSystemUI(this)
        }
    }

    override fun onResume() {
        super.onResume()
        SystemUi.hideSystemUI(this)
    }

    //<editor-fold desc="处理触摸软键盘外部，自动取消软键盘">
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        // 处理点击软键盘外部的情况
        if (ev.action == MotionEvent.ACTION_UP) {
            val v: View = currentFocus ?: return super.dispatchTouchEvent(ev)
            //如果不是落在EditText区域，则需要关闭输入法
            if (hideKeyboard(v, ev)) {
                val imm: InputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
    private fun hideKeyboard(view: View?, event: MotionEvent): Boolean {
        if (view is EditText) {
            val location = intArrayOf(0, 0)
            view.getLocationInWindow(location)

            //获取现在拥有焦点的控件view的位置，即EditText
            val left = location[0]
            val top = location[1]
            val bottom: Int = top + view.getHeight()
            val right: Int = left + view.getWidth()
            //判断我们手指点击的区域是否落在EditText上面，如果不是，则返回true，否则返回false
            val isInEt = event.x > left && event.x < right && event.y > top && event.y < bottom
            return !isInEt
        }
        return false
    }
    //</editor-fold>
}