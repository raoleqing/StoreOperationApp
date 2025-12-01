package com.tiandao.store.operation.common.dialog

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.tiandao.store.operation.bean.PlatformRechargePlan
import com.tiandao.store.operation.common.adapter.PayServerAdapter
import com.tiandao.store.operation.common.weight.SpaceItemDecoration
import com.tiandao.store.operation.databinding.PayServerDialogBinding
import com.tiandao.store.operation.utils.DisplayUtil
import com.tiandao.store.operation.utils.ToastUtils

class PayServerDialog (context: Activity, private val type: String, private val planList: List<PlatformRechargePlan>,private
val listener: OnFilterAppliedListener) : AppCompatDialog(context) {

    private var binding: PayServerDialogBinding? = null
    private var platformRechargePlan: PlatformRechargePlan? = null

    private val adapter = PayServerAdapter()

    interface OnFilterAppliedListener {
        fun determine(dialog: PayServerDialog,platformRechargePlan: PlatformRechargePlan?,payType: String,amount: Int)
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

            // 设置选择监听器
            it.spinnerPayType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if(position == 3 || position == 4){
                       it.recyclerView.visibility = View.GONE
                       if(type == "shop"){
                           it.llMonth.visibility = View.VISIBLE
                       }else{
                           it.llNum.visibility = View.VISIBLE
                       }
                    }else{
                        it.recyclerView.visibility = View.VISIBLE
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // 没有选择时的处理
                    ToastUtils.showShort("请选择一个选项")
                }
            }

            val months = arrayOf("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月")
            var monthAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, months)
            monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            it.spinnerMonth.adapter = monthAdapter

        }
        binding?.butSubmit?.setOnClickListener {

            val payType = when (binding?.spinnerPayType?.selectedItemPosition) {
                0 -> "wechat"
                1 -> "alipay"
                2 -> "cash"
                3 -> "give"
                4 -> "refund"
                else -> "wechat"
            }

            var amount: Int = 0;
            if(payType == "give" || payType == "refund"){
                if(type == "shop"){
                    amount = binding?.spinnerMonth?.selectedItemPosition?: 0
                    amount++
                }else{
                    var num = binding?.etNum?.text.toString()
                    if(num.isEmpty()){
                        ToastUtils.showShort("请输入数量")
                        return@setOnClickListener
                    }
                    amount = num.toInt()
                }
            }else{
                if(platformRechargePlan == null){
                    ToastUtils.showShort("请选择充值项")
                    return@setOnClickListener
                }
            }
            listener.determine(this,platformRechargePlan,payType,amount)
        }

        binding?.ivClose?.setOnClickListener {
            listener.close(this)
        }
    }

}