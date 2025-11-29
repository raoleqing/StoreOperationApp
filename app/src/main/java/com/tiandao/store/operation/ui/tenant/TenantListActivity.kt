package com.tiandao.store.operation.ui.tenant

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tiandao.store.operation.R
import com.tiandao.store.operation.base.BaseActivity
import com.tiandao.store.operation.bean.SysTenant
import com.tiandao.store.operation.common.adapter.SysTenantListAdapter
import com.tiandao.store.operation.common.event.EventCode
import com.tiandao.store.operation.common.event.EventMessage
import com.tiandao.store.operation.common.weight.SpaceItemDecoration
import com.tiandao.store.operation.databinding.ActivityTenantListBinding
import com.tiandao.store.operation.utils.DisplayUtil
import com.tiandao.store.operation.utils.ToastUtils
import com.tiandao.store.operation.utils.UserUtils
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class TenantListActivity : BaseActivity<ActivityTenantListBinding, TenantListViewModel>() {

    var list: MutableList<SysTenant> = mutableListOf()
    val adapter = SysTenantListAdapter()

    override fun showBackArrow(): Boolean {
        return true
    }

    override fun isRegisteredEventBus(): Boolean {
        return true
    }

    override fun getViewBinding(): ActivityTenantListBinding {
        return ActivityTenantListBinding.inflate(layoutInflater)
    }

    override fun provideViewModel(): TenantListViewModel {
        return ViewModelProvider(this).get(TenantListViewModel::class.java)
    }

    override fun initView() {
        setTitleText("租户列表")
        appCompatImageButton?.visibility = View.VISIBLE
        appCompatImageButton?.setImageResource(R.mipmap.add_merber_icon)
        appCompatImageButton?.setOnClickListener {
            Intent(this@TenantListActivity, CreateTenantActivity::class.java).apply {
                startActivity(this)
            }
        }
        infoAdapter();
        binding.rgUserType.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_my -> {
                    // 处理第一个单选按钮被选中
                    viewModel.salesmanId = UserUtils.getUserId(this);
                    viewModel.pageIndex = 1
                    viewModel.keyword = binding.searchView.query.toString()
                    viewModel.getTenantList()
                }
                R.id.rb_all -> {
                    // 处理第二个单选按钮被选中
                    viewModel.salesmanId = 0
                    viewModel.pageIndex = 1
                    viewModel.keyword = binding.searchView.query.toString()
                    viewModel.getTenantList()
                }
            }
        }
        // 设置刷新监听
        binding.srlRefreshTask.setOnRefreshListener {
            // 下拉刷新回调
            viewModel.pageIndex = 1
            viewModel.getTenantList()

        }

        binding.srlRefreshTask.setOnLoadMoreListener {
            // 上拉加载回调
            viewModel.pageIndex++
            viewModel.getTenantList()
        }

        binding.btnSearch.setOnClickListener(this);

    }

    private fun infoAdapter() {
        // 使用
        binding.recyclerView.setLayoutManager(LinearLayoutManager(this))
        adapter.onItemClick = { item, position , view->
            // 处理点击事件
            Intent(this, TenantActivity::class.java).apply {
                putExtra(TenantActivity.TENANT_ID, item.tenantId)
                startActivity(this)
            }
        }
        adapter.submitList(list)
        val spacing = DisplayUtil.dp2px(12);
        val itemDecoration = SpaceItemDecoration(spacing)
        binding.recyclerView.addItemDecoration(itemDecoration)  // 先添加装饰
        binding.recyclerView.setAdapter(adapter)
    }

    override fun initData() {
        viewModel.salesmanId = UserUtils.getUserId(this);
        viewModel.pageIndex = 1
        viewModel.getTenantList()
    }

    private fun performSearch(keyword: String) {
        viewModel.keyword = keyword
        viewModel.pageIndex = 1
        viewModel.getTenantList()
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

        viewModel.sysTenantList.observe(this){
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
        when (v?.id) {
            R.id.btn_search -> {
                val keyword = binding.searchView.query.toString()
                performSearch(keyword)
            }
        }
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
            EventCode.TENANT_REGISTRATION_SUCCESS -> {
                viewModel.pageIndex = 1
                viewModel.keyword = binding.searchView.query.toString()
                viewModel.getTenantList()
            }
            else -> println("未知事件类型: ${event.code}")
        }
    }


}