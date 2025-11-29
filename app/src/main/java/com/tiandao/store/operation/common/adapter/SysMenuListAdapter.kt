package com.tiandao.store.operation.common.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.tiandao.store.operation.base.BaseAdapter
import com.tiandao.store.operation.bean.SysMenu
import com.tiandao.store.operation.databinding.SysMenuItemBinding


class SysMenuListAdapter:  BaseAdapter<SysMenu, SysMenuItemBinding>(

diffCallback = object : DiffUtil.ItemCallback<SysMenu>() {
    override fun areItemsTheSame(oldItem: SysMenu, newItem: SysMenu): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: SysMenu, newItem: SysMenu): Boolean {
        return oldItem == newItem
    }
}) {




    override fun getViewBinding(parent: ViewGroup, viewType: Int): SysMenuItemBinding {
        return SysMenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bind(binding: SysMenuItemBinding, item: SysMenu, position: Int) {
        binding.ivIcon.setBackgroundResource(item.icon)
        binding.tvName.text = item.menuName
    }

}