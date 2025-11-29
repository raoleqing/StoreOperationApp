package com.tiandao.store.operation.common.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.tiandao.store.operation.base.BaseAdapter
import com.tiandao.store.operation.bean.StaffCommission
import com.tiandao.store.operation.databinding.StaffCommissionItemBinding
import com.tiandao.store.operation.utils.StringUtils

class StaffCommissionAdapter: BaseAdapter<StaffCommission, StaffCommissionItemBinding>(

diffCallback = object : DiffUtil.ItemCallback<StaffCommission>() {
    override fun areItemsTheSame(oldItem: StaffCommission, newItem: StaffCommission): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: StaffCommission, newItem: StaffCommission): Boolean {
        return oldItem == newItem
    }
}) {

    override fun getViewBinding(parent: ViewGroup, viewType: Int): StaffCommissionItemBinding {
        return StaffCommissionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bind(binding: StaffCommissionItemBinding, item: StaffCommission, position: Int) {
        binding.tvName.text = item.itemName
        binding.tvTime.text = item.createTime
        binding.tvOrderAmount.text = "￥".plus(item.orderAmount)
        binding.tvDeductWay.text = StringUtils.getDeductWay(item.deductWay)
        binding.tvStaff.text = item.staffName
        binding.tvAmount.text = "提成金额：￥".plus(item.commissionAmount)
    }

}