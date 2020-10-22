package com.licheedev.serialtool.ui

import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.licheedev.serialtool.databinding.FragmentLogBinding
import com.licheedev.serialtool.model.message.IMessage
import com.licheedev.serialtool.ui.adapter.LogAdapter
import com.licheedev.serialtool.ui.base.binding.BaseBindingFragment
import com.licheedev.widgets.recyclerview.RecyclerViewUtil
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class LogFragment : BaseBindingFragment<FragmentLogBinding>() {

    private lateinit var adapter: LogAdapter

    override fun bindingInflate(): (LayoutInflater, ViewGroup?, Boolean) -> FragmentLogBinding {
        return FragmentLogBinding::inflate
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.clickProxy = ClickProxy()
        adapter = LogAdapter()
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
        layoutManager.stackFromEnd = true;
        binding.rvLogs.layoutManager = layoutManager
        binding.rvLogs.adapter = adapter
        RecyclerViewUtil.addDefaultDivider(binding.rvLogs)
        registerEventBus()
    }

    override fun onDestroyView() {
        unregisterEventBus()
        super.onDestroyView()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(message: IMessage) {
        adapter.addMessage(message)
        scrollToEnd()
    }

    private fun scrollToEnd() {
        if (autoEnd) {
            if (adapter.itemCount > 0) {
                binding.rvLogs.scrollToPosition(adapter.itemCount-1);
            }
        }
    }

    private var autoEnd = true

    inner class ClickProxy {

        fun clearLog() {
            adapter.clear()
        }

        fun switchAutoEnd() {
            autoEnd = autoEnd.not()
            scrollToEnd()
        }
    }
}