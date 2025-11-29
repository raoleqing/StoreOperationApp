package com.tiandao.store.operation.common.dialog

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.tiandao.store.operation.bean.PlatformRechargePlan
import com.tiandao.store.operation.common.adapter.PayServerAdapter
import com.tiandao.store.operation.common.weight.SpaceItemDecoration
import com.tiandao.store.operation.databinding.PayServerDialogBinding
import com.tiandao.store.operation.utils.DisplayUtil
import com.tiandao.store.operation.utils.ToastUtils

class PayServerDialog (context: Activity, private val planList: List<PlatformRechargePlan>,private
val listener: OnFilterAppliedListener) : AppCompatDialog(context) {

    private var binding: PayServerDialogBinding? = null
    private var platformRechargePlan: PlatformRechargePlan? = null

    private val adapter = PayServerAdapter()

    interface OnFilterAppliedListener {
        fun determine(dialog: PayServerDialog,platformRechargePlan: PlatformRechargePlan,payType: String)
        fun close(dialog: PayServerDialog)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 关键设置：透明背景 + 白色内容布局
        //requestWindowFeature(Window.FEATURE_NO_TITLE) // 隐藏标题栏
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 透明背景

        binding = PayServerDialogBinding.inflate(layoutInflater)
        binding?.let {
            setContentView(it.root)
            window?.let { item ->
                val params = item.attributes
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT
                params.gravity = Gravity.BOTTOM
                item.attributes = params
            }

            it.recyclerView.setLayoutManager(LinearLayoutManager(context))
            adapter.onItemClick = { item, position , view->
                // 处理点击事件
                platformRechargePlan = item
                adapter.setPosition( position)
                adapter.notifyDataSetChanged()
            }
            val spacing = DisplayUtil.dp2px(12);
            val itemDecoration = SpaceItemDecoration(spacing)
            it.recyclerView.addItemDecoration(itemDecoration)  // 先添加装饰
            it.recyclerView.setAdapter(adapter)
            adapter.submitList(planList)

            val levels = arrayOf("微信", "支付宝", "现金","赠送","返还")
            var levelAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, levels)
            levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            it.spinnerPayType.adapter = levelAdapter
        }
        binding?.butSubmit?.setOnClickListener {
            if(platformRechargePlan == null){
                ToastUtils.showShort("请选择充值项")
                return@setOnClickListener
            }
            val payType = when (binding?.spinnerPayType?.selectedItemPosition) {
                0 -> "wechat"
                1 -> "alipay"
                2 -> "cash"
                3 -> "give"
                4 -> "refund"
                else -> "wechat"
            }

            platformRechargePlan?.let {
                listener.determine(this,it,payType)
            }

        }

        binding?.ivClose?.setOnClickListener {
            listener.close(this)
        }
    }

}