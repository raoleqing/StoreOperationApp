package com.tiandao.store.operation.common.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.tiandao.store.operation.base.BaseAdapter
import com.tiandao.store.operation.bean.FlowAuditRecordItem
import com.tiandao.store.operation.databinding.FlowAuditItemBinding
import com.tiandao.store.operation.utils.StringUtils

class FlowAuditRecordItemAdapter : BaseAdapter<FlowAuditRecordItem, FlowAuditItemBinding>(
diffCallback = object : DiffUtil.ItemCallback<FlowAuditRecordItem>() {
    override fun areItemsTheSame(oldItem: FlowAuditRecordItem, newItem: FlowAuditRecordItem): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: FlowAuditRecordItem, newItem: FlowAuditRecordItem): Boolean {
        return oldItem == newItem
    }
}){

    override fun getViewBinding(parent: ViewGroup, viewType: Int): FlowAuditItemBinding {
        return FlowAuditItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bind(binding: FlowAuditItemBinding, item: FlowAuditRecordItem, position: Int) {
        if(item.auditOpName.isEmpty()){
            binding.tvName.text = item.auditItemName
        }else{
            binding.tvName.text = item.auditItemName.plus("(${item.auditOpName})")
        }

        binding.tvTime.text = "更新时间：".plus(item.updateTime)
        binding.tvStatus.text = StringUtils.getAuditStatusText(item.auditStatus)
        binding.tvContent.text = item.auditRemark
    }
}