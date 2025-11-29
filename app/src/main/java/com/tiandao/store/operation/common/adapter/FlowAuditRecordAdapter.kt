package com.tiandao.store.operation.common.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.tiandao.store.operation.R
import com.tiandao.store.operation.base.BaseAdapter
import com.tiandao.store.operation.bean.FlowAuditRecord
import com.tiandao.store.operation.bean.FlowAuditRecordItem
import com.tiandao.store.operation.databinding.FlowAuditItemBinding
import com.tiandao.store.operation.databinding.FlowAuditRecordItemBinding
import com.tiandao.store.operation.utils.StringUtils

class FlowAuditRecordAdapter : BaseAdapter<FlowAuditRecord, FlowAuditRecordItemBinding>(
    diffCallback = object : DiffUtil.ItemCallback<FlowAuditRecord>() {
        override fun areItemsTheSame(oldItem: FlowAuditRecord, newItem: FlowAuditRecord): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: FlowAuditRecord, newItem: FlowAuditRecord): Boolean {
            return oldItem == newItem
        }
    }){

    override fun getViewBinding(parent: ViewGroup, viewType: Int): FlowAuditRecordItemBinding {
        return FlowAuditRecordItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bind(binding: FlowAuditRecordItemBinding, item: FlowAuditRecord, position: Int) {
        binding.tvType.text = StringUtils.getPayTypeText(item.flowType)
        binding.tvStatus.text = StringUtils.getAuditStatusText(item.auditStatus)
        binding.tvStatus.setBackgroundResource(getBackground(item.auditStatus))
        binding.tvAuditOpName.text = item.auditOpName
        binding.tvTime.text = item.createTime
        binding.tvAuditRemark.text = item.auditRemark
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