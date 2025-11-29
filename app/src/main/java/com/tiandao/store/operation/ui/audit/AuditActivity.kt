package com.tiandao.store.operation.ui.audit

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.tiandao.store.operation.base.BaseActivity
import com.tiandao.store.operation.bean.FlowAuditRecord
import com.tiandao.store.operation.common.adapter.FlowAuditRecordAdapter
import com.tiandao.store.operation.common.event.EventCode
import com.tiandao.store.operation.common.event.EventMessage
import com.tiandao.store.operation.common.weight.SpaceItemDecoration
import com.tiandao.store.operation.common.weight.TabTitleEntity
import com.tiandao.store.operation.databinding.ActivityAuditBinding
import com.tiandao.store.operation.ui.order.OrderActivity
import com.tiandao.store.operation.utils.DisplayUtil
import com.tiandao.store.operation.utils.ToastUtils
import com.tiandao.store.operation.utils.UserUtils
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class AuditActivity : BaseActivity<ActivityAuditBinding,AuditViewModel>()  {

    private val adapter = FlowAuditRecordAdapter()
    var list: MutableList<FlowAuditRecord> = mutableListOf()

    override fun showBackArrow(): Boolean {
        return true
    }
    override fun isRegisteredEventBus(): Boolean {
        return true
    }

    override fun getViewBinding(): ActivityAuditBinding {
        return ActivityAuditBinding.inflate(layoutInflater)
    }

    override fun provideViewModel(): AuditViewModel {
        return ViewModelProvider(this).get(AuditViewModel::class.java)
    }

    override fun initView() {
        setTitleText("审核中心")

        // 创建Tab实体
        val tabEntities = java.util.ArrayList<CustomTabEntity>()
        tabEntities.add(TabTitleEntity("待审核"))
        tabEntities.add(TabTitleEntity("已审核"))
        tabEntities.add(TabTitleEntity("已申请"))
        // 设置Tab数据
        binding.tlCardType.setTabData(tabEntities)
        // 联动绑定
        binding.tlCardType.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                // 处理Tab选中事件
                viewModel.type = position
                viewModel.pageIndex = 1
                viewModel.getFlowAuditRecordList()
            }
            override fun onTabReselect(position: Int) {}
        })


        infoAdapter();
        // 设置刷新监听
        binding.srlRefreshTask.setOnRefreshListener {
            // 下拉刷新回调
            viewModel.pageIndex = 1
            viewModel.getFlowAuditRecordList()

        }

        binding.srlRefreshTask.setOnLoadMoreListener {
            // 上拉加载回调
            viewModel.pageIndex++
            viewModel.getFlowAuditRecordList()
        }
    }

    private fun infoAdapter() {
        // 使用
        binding.recyclerView.setLayoutManager(LinearLayoutManager(this))
        adapter.onItemClick = { item, position , view->
            // 处理点击事件
            Intent(this, OrderActivity::class.java).apply {
                putExtra(OrderActivity.ORDER_ID, item.bizId)
                startActivity(this)
            }
        }
        val spacing = DisplayUtil.dp2px(12);
        val itemDecoration = SpaceItemDecoration(spacing)
        adapter.submitList( list)
        binding.recyclerView.addItemDecoration(itemDecoration)  // 先添加装饰
        binding.recyclerView.setAdapter(adapter)
    }

    override fun initData() {
        viewModel.pageIndex = 1
        viewModel.auditOpId = UserUtils.getUserId(this)
        viewModel.type = 0
        viewModel.getFlowAuditRecordList()
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

        viewModel.flowAuditRecordList.observe(this){
            if(viewModel.pageIndex == 1){
                list.clear()
            }
            list.addAll(it)
            adapter.notifyDataSetChanged()
            if(list.isEmpty()){
                binding.emptyView.visibility = View.VISIBLE
            }else{
                binding.emptyView.visibility = View.GONE
            }
            if(it.size < 20){
                binding.srlRefreshTask.finishLoadMoreWithNoMoreData()
            }
            binding.srlRefreshTask.finishRefresh()
            binding.srlRefreshTask.finishLoadMore()
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
            EventCode.AUDIT_SUCCESS -> {
                viewModel.pageIndex = 1
                viewModel.getFlowAuditRecordList()
            }
            else -> println("未知事件类型: ${event.code}")
        }
    }

}