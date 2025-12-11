package com.tiandao.store.operation.common.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.tiandao.store.operation.R
import com.tiandao.store.operation.base.BaseAdapter
import com.tiandao.store.operation.bean.PlatformRechargePlan
import com.tiandao.store.operation.databinding.PlatformRechargePlanItemBinding

class PayServerAdapter : BaseAdapter<PlatformRechargePlan, PlatformRechargePlanItemBinding>(

    diffCallback = object : DiffUtil.ItemCallback<PlatformRechargePlan>() {
        override fun areItemsTheSame(oldItem: PlatformRechargePlan, newItem: PlatformRechargePlan): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: PlatformRechargePlan, newItem: PlatformRechargePlan): Boolean {
            return oldItem == newItem
        }
    }) {

    private var onPosition = -1
    fun setPosition(position: Int) {
        onPosition = position
    }

    override fun getViewBinding(parent: ViewGroup, viewType: Int): PlatformRechargePlanItemBinding {
        return PlatformRechargePlanItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bind(binding: PlatformRechargePlanItemBinding, item: PlatformRechargePlan, position: Int) {
        binding.tvName.text = item.name
        var unit = when(item.type){
            "shop" -> "月"
            "sms" -> "条"
            else -> ""}
        binding.tvCount.text = item.amount.toString().plus( unit)
        binding.tvPrice.text = item.price.toString().plus("元")
        if(onPosition == position){
            binding.ivSelect.setBackgroundResource(R.mipmap.radio_button_checked)
        }else{
            binding.ivSelect.setBackgroundResource(R.mipmap.radio_button_unchecked)
        }

    }

}