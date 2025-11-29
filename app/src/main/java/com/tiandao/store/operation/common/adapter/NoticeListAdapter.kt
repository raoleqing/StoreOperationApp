package com.tiandao.store.operation.common.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.tiandao.store.operation.base.BaseAdapter
import com.tiandao.store.operation.bean.Notice
import com.tiandao.store.operation.databinding.NoticeItemBinding

class NoticeListAdapter:  BaseAdapter<Notice, NoticeItemBinding>(
    diffCallback = object : DiffUtil.ItemCallback<Notice>() {
        override fun areItemsTheSame(oldItem: Notice, newItem: Notice): Boolean {
            return oldItem.noticeId == newItem.noticeId
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Notice, newItem: Notice): Boolean {
            return oldItem == newItem
        }
    }) {

    override fun getViewBinding(parent: ViewGroup, viewType: Int): NoticeItemBinding {
        return NoticeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bind(binding: NoticeItemBinding, item: Notice, position: Int) {
        if(item.tenantId == 1000L){
            binding.tvType.visibility = View.VISIBLE
        }else{
            binding.tvType.visibility = View.GONE
        }
        binding.tvMessageName.text = "标题：".plus(item.noticeTitle)
        binding.tvContent.text =  "内容：".plus(item.noticeContent)
        binding.tvTime.text = "创建时间：".plus(item.createTime)
    }
}