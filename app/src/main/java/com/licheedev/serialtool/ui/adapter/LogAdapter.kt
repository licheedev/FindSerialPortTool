package com.licheedev.serialtool.ui.adapter

import android.view.ViewGroup
import com.licheedev.serialtool.databinding.ItemLogBinding
import com.licheedev.serialtool.model.message.IMessage
import com.licheedev.serialtool.model.message.OpenMessage
import com.licheedev.someext.recycler.BindingRecyclerAdapter
import com.licheedev.someext.recycler.BindingViewHolder
import com.licheedev.someext.recycler.inflateBindingHolder


class LogAdapter : BindingRecyclerAdapter<IMessage, ItemLogBinding>() {

    override fun createViewHolderInstance(
        parent: ViewGroup,
        viewType: Int
    ): BindingViewHolder<ItemLogBinding> {
        return parent.inflateBindingHolder(ItemLogBinding::inflate)
    }

    override fun onBindViewHolder(holder: BindingViewHolder<ItemLogBinding>, position: Int) {
        val item = getItem(position)
        holder.binding.message = item
        holder.binding.position = (position + 1).toString()
    }

    fun clear() {
        mData.clear()
        notifyDataSetChanged()
    }

    fun addMessage(message: IMessage) {
        mData.add(message)
        notifyItemRangeInserted(mData.size - 1, 1)
    }


}