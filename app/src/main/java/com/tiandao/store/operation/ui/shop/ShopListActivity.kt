package com.tiandao.store.operation.ui.shop

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tiandao.store.operation.R
import com.tiandao.store.operation.base.BaseActivity
import com.tiandao.store.operation.bean.Shop
import com.tiandao.store.operation.common.adapter.ShopListAdapter
import com.tiandao.store.operation.common.event.EventCode
import com.tiandao.store.operation.common.event.EventMessage
import com.tiandao.store.operation.common.weight.SpaceItemDecoration
import com.tiandao.store.operation.databinding.ActivityShopListBinding
import com.tiandao.store.operation.utils.DisplayUtil
import com.tiandao.store.operation.utils.ToastUtils
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ShopListActivity : BaseActivity<ActivityShopListBinding, ShopListViewModel>() {

    var list: MutableList<Shop> = mutableListOf()
    val adapter = ShopListAdapter()

    override fun showBackArrow(): Boolean {
        return true
    }

    override fun isRegisteredEventBus(): Boolean {
        return true
    }


    override fun getViewBinding(): ActivityShopListBinding {
        return ActivityShopListBinding.inflate(layoutInflater)
    }

    override fun provideViewModel(): ShopListViewModel {
        return ViewModelProvider(this).get(ShopListViewModel::class.java)
    }

    override fun initView() {
        setTitleText("门店列表")
        infoAdapter();
        // 设置刷新监听
        binding.srlRefreshTask.setOnRefreshListener {
            // 下拉刷新回调
            viewModel.pageIndex = 1
            viewModel.getShopList()

        }

        binding.srlRefreshTask.setOnLoadMoreListener {
            // 上拉加载回调
            viewModel.pageIndex++
            viewModel.getShopList()
        }

        binding.btnSearch.setOnClickListener(this);
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
        adapter.submitList( list)
        binding.recyclerView.addItemDecoration(itemDecoration)  // 先添加装饰
        binding.recyclerView.setAdapter(adapter)
    }

    override fun initData() {
        viewModel.pageIndex = 1
        viewModel.getShopList()
    }

    private fun performSearch(keyword: String) {
        viewModel.keyword = keyword
        viewModel.pageIndex = 1
        viewModel.getShopList()
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

        viewModel.shopList.observe(this){
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
            EventCode.CREATE_ORDER_SUCCESS -> {
                viewModel.pageIndex = 1
                viewModel.keyword = binding.searchView.query.toString()
                viewModel.getShopList()
            }
            else -> println("未知事件类型: ${event.code}")
        }
    }

}