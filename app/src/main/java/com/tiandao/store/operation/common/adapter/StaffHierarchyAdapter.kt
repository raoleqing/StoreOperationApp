package com.tiandao.store.operation.common.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.tiandao.store.operation.base.BaseAdapter
import com.tiandao.store.operation.bean.StaffHierarchy
import com.tiandao.store.operation.databinding.StaffHierarchyItemBinding

class StaffHierarchyAdapter : BaseAdapter<StaffHierarchy, StaffHierarchyItemBinding>(

    diffCallback = object : DiffUtil.ItemCallback<StaffHierarchy>() {
        override fun areItemsTheSame(oldItem: StaffHierarchy, newItem: StaffHierarchy): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: StaffHierarchy, newItem: StaffHierarchy): Boolean {
            return oldItem == newItem
        }
    }) {

    override fun getViewBinding(parent: ViewGroup, viewType: Int): StaffHierarchyItemBinding {
        return StaffHierarchyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bind(binding: StaffHierarchyItemBinding, item: StaffHierarchy, position: Int) {
        binding.tvName.text = item.staffName
        binding.tvTime.text = item.joinDate
        binding.tvLevel.text = item.level
        binding.tvActualSales.text = "￥：".plus(item.actualSales)
        binding.tvPerformanceScore.text = item.performanceScore.toString()
    }

}