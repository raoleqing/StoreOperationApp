package com.tiandao.store.operation.base

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseAdapter<T, VB : ViewBinding>(
    private val diffCallback: DiffUtil.ItemCallback<T>,
    initialData: List<T> = emptyList()
) : RecyclerView.Adapter<BaseAdapter.BaseViewHolder<VB>>() {

    private val differ = AsyncListDiffer(this, diffCallback)

    init {
        submitList(initialData)
    }

    abstract fun getViewBinding(parent: ViewGroup, viewType: Int): VB
    abstract fun bind(binding: VB, item: T, position: Int)

    open fun getItemViewType(position: Int, item: T): Int = 0

    // 修改点：onItemClick 增加 View 参数
    var onItemClick: ((T, Int, View) -> Unit)? = null
    var onItemLongClick: ((T, Int, View) -> Boolean)? = null

    protected fun getItem(position: Int): T = differ.currentList[position]

    override fun getItemViewType(position: Int): Int {
        return getItemViewType(position, getItem(position))
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<VB> {
        val binding = getViewBinding(parent, viewType)
        return BaseViewHolder(binding).apply {
            binding.root.setOnClickListener { view ->
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick?.invoke(getItem(position), position, view) // 传递 View
                }
            }
            binding.root.setOnLongClickListener { view ->
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemLongClick?.invoke(getItem(position), position, view) ?: false
                } else false
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<VB>, position: Int) {
        bind(holder.binding, getItem(position), position)
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun submitList(newData: List<T>) {
        differ.submitList(newData)
    }

    class BaseViewHolder<VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)
}