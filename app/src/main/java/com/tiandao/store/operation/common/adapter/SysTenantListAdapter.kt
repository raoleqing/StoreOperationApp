package com.tiandao.store.operation.common.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.tiandao.store.operation.base.BaseAdapter
import com.tiandao.store.operation.bean.SysTenant
import com.tiandao.store.operation.databinding.SysTenantItemBinding

class SysTenantListAdapter : BaseAdapter<SysTenant, SysTenantItemBinding>(

    diffCallback = object : DiffUtil.ItemCallback<SysTenant>() {
        override fun areItemsTheSame(oldItem: SysTenant, newItem: SysTenant): Boolean {
            return oldItem.tenantId == newItem.tenantId
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: SysTenant, newItem: SysTenant): Boolean {
            return oldItem == newItem
        }
    }) {




    override fun getViewBinding(parent: ViewGroup, viewType: Int): SysTenantItemBinding {
        return SysTenantItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bind(binding: SysTenantItemBinding, item: SysTenant, position: Int) {
        binding.tvTenantName.text = item.tenantName
        binding.tvCreateTime.text = item.createTime
        binding.tvOwnerStaffName.text = item.ownerStaffName
        binding.tvSalesmanName.text = item.salesmanName

    }

}