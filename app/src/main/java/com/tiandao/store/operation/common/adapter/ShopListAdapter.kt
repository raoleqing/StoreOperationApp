package com.tiandao.store.operation.common.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.tiandao.store.operation.base.BaseAdapter
import com.tiandao.store.operation.bean.Shop
import com.tiandao.store.operation.databinding.ShopItemBinding

class ShopListAdapter : BaseAdapter<Shop, ShopItemBinding>(

    diffCallback = object : DiffUtil.ItemCallback<Shop>() {
        override fun areItemsTheSame(oldItem: Shop, newItem: Shop): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Shop, newItem: Shop): Boolean {
            return oldItem == newItem
        }
    }) {

    override fun getViewBinding(parent: ViewGroup, viewType: Int): ShopItemBinding {
        return ShopItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bind(binding: ShopItemBinding, item: Shop, position: Int) {
        binding.tvShopName.text = item.shopName
        binding.tvCreateTime.text = item.createTime
        binding.tvExpireTime.text = item.validEndTime
        binding.tvSmsNum.text = item.smsCount.toString()

    }

}