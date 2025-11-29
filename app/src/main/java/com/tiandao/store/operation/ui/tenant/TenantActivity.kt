package com.tiandao.store.operation.ui.tenant

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tiandao.store.operation.base.BaseActivity
import com.tiandao.store.operation.bean.Shop
import com.tiandao.store.operation.bean.SysTenant
import com.tiandao.store.operation.common.adapter.ShopListAdapter
import com.tiandao.store.operation.common.event.EventCode
import com.tiandao.store.operation.common.event.EventMessage
import com.tiandao.store.operation.common.weight.SpaceItemDecoration
import com.tiandao.store.operation.databinding.ActivityTenantBinding
import com.tiandao.store.operation.ui.shop.ShopActivity
import com.tiandao.store.operation.utils.DisplayUtil
import com.tiandao.store.operation.utils.ToastUtils
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class TenantActivity : BaseActivity<ActivityTenantBinding, TenantViewModel>()   {

    companion object {
        const val TENANT_ID = "tenant_id"
    }

    var list: MutableList<Shop> = mutableListOf()
    val adapter = ShopListAdapter()

    var tenantId: Long = 0
    var sysTenant: SysTenant? = null

    override fun showBackArrow(): Boolean {
        return true
    }

    override fun isRegisteredEventBus(): Boolean {
        return true
    }

    override fun getViewBinding(): ActivityTenantBinding {
        return ActivityTenantBinding.inflate(layoutInflater)
    }

    override fun provideViewModel(): TenantViewModel {
        return ViewModelProvider(this).get(TenantViewModel::class.java)
    }

    override fun initView() {
        setTitleText("租户详情")
        infoAdapter();
    }

    private fun infoAdapter() {
        // 使用
        binding.recyclerView.setLayoutManager(LinearLayoutManager(this))
        adapter.onItemClick = { item, position , view->
            // 处理点击事件
            Intent(this, ShopActivity::class.java).apply {
                putExtra(ShopActivity.SHOP_ID, item.id)
                startActivity(this)
            }
        }
        val spacing = DisplayUtil.dp2px(12);
        val itemDecoration = SpaceItemDecoration(spacing)
        binding.recyclerView.addItemDecoration(itemDecoration)  // 先添加装饰
        binding.recyclerView.setAdapter(adapter)
    }


    override fun initData() {
        tenantId = intent.getLongExtra(TENANT_ID, 0)
        viewModel.getTenant(tenantId);

    }

    override fun observeLiveData() {
        viewModel.loadingState.observe(this) {
            if (it) {
                showLoading()
            }else{
                hideLoading()
            }
        }

        viewModel.errorMessage.observe(this) {
            it?.let {
                ToastUtils.showShort(it)
            }
        }

        viewModel.sysTenant.observe(this){
           if(it != null){
               setContent(it);
           }
        }
    }

    private fun setContent(tenant: SysTenant) {
        sysTenant = tenant
        binding.tvTenantName.text = tenant.tenantName
        binding.tvContactName.text = tenant.contactName
        binding.tvContactPhone.text = tenant.contactPhone
        binding.tvOwnerStaffName.text = tenant.ownerStaffName
        binding.tvContactEmail.text = tenant.contactEmail
        binding.tvRemark.text = tenant.remark
        binding.shopNumber.text = tenant.shopList?.size.toString()

        //"基础版", "独立版", "专业版"
        val level = when (tenant.level) {
            0 -> "基础版"
            1 -> "独立版"
            2 -> "专业版"
            3 -> "定制版"
            else -> "基础版"
        }
        binding.tvLevel.text = level


        tenant.shopList?.let {
            list.clear()
            list.addAll(it)
            adapter.submitList(list)
        }
    }


    override fun onClick(v: View?) {

    }

    /**
     * 接收并处理分发的事件（主线程回调）
     * @param event 事件消息（可为 null，但建议发送方避免空事件）
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiveEvent(event: EventMessage<*>?) {
        event ?: run {
            println("收到空事件，忽略处理")
            return
        }

        when (event.code) {
            EventCode.CREATE_ORDER_SUCCESS -> {
                viewModel.getTenant(tenantId)
            }
            else -> println("未知事件类型: ${event.code}")
        }
    }

}