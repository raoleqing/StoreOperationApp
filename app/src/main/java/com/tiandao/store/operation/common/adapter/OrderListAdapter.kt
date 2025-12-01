package com.tiandao.store.operation.common.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.tiandao.store.operation.R
import com.tiandao.store.operation.base.BaseAdapter
import com.tiandao.store.operation.bean.PlatformOrder
import com.tiandao.store.operation.databinding.OrderListItemBinding
import com.tiandao.store.operation.utils.StringUtils

class OrderListAdapter : BaseAdapter<PlatformOrder, OrderListItemBinding>(

diffCallback = object : DiffUtil.ItemCallback<PlatformOrder>() {
    override fun areItemsTheSame(oldItem: PlatformOrder, newItem: PlatformOrder): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: PlatformOrder, newItem: PlatformOrder): Boolean {
        return oldItem == newItem
    }
}) {

    private var onPosition = -1
    fun setPosition(position: Int) {
        onPosition = position
    }

    override fun getViewBinding(parent: ViewGroup, viewType: Int): OrderListItemBinding {
        return OrderListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bind(binding: OrderListItemBinding, item: PlatformOrder, position: Int) {
        val planType = StringUtils.getPlanTypeText(item.planType)
        binding.tvName.text = planType.plus("  ￥").plus(item.paymentAmount)
        var status = StringUtils.getAuditStatusText(item.auditStatus)
        binding.tvStatus.text = status
        binding.tvStatus.setBackgroundResource(getBackground(item.auditStatus))
        binding.tvTenantName.text = item.tenantName
        binding.tvShopName.text = item.shopName
        binding.payTime.text = "支付时间: ".plus(item.payTime)
        binding.tvStaff.text = "业务员：".plus(item.staffName)

    }


    private fun getBackground(status: Int): Int {
        return when(status){
            0 -> R.drawable.shape_but_bg5
            1 -> R.drawable.shape_but_bg7
            2 -> R.drawable.shape_but_bg1
            3 -> R.drawable.shape_but_bg6
            4 -> R.drawable.shape_but_bg4
            else -> R.drawable.shape_but_bg1
        }
    }

}