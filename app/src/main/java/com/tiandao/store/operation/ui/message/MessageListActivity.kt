package com.tiandao.store.operation.ui.message

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tiandao.store.operation.R
import com.tiandao.store.operation.base.BaseActivity
import com.tiandao.store.operation.bean.Notice
import com.tiandao.store.operation.common.adapter.NoticeListAdapter
import com.tiandao.store.operation.databinding.ActivityMessageListBinding
import com.tiandao.store.operation.utils.ToastUtils

class MessageListActivity : BaseActivity<ActivityMessageListBinding, MessageListViewModel>(){

    var list: MutableList<Notice> = mutableListOf()
    val adapter = NoticeListAdapter()

    override fun showBackArrow(): Boolean {
        return true
    }

    override fun getViewBinding(): ActivityMessageListBinding {
        return ActivityMessageListBinding.inflate(layoutInflater)
    }

    override fun provideViewModel(): MessageListViewModel {
        return ViewModelProvider(this).get(MessageListViewModel::class.java)
    }

    override fun initView() {
        setTitleText("消息中心")
        infoAdapter();
        // 设置刷新监听
        binding.srlRefreshTask.setOnRefreshListener {
            // 下拉刷新回调
            viewModel.pageIndex = 1
            viewModel.getNoticeList()

        }

        binding.srlRefreshTask.setOnLoadMoreListener {
            // 上拉加载回调
            viewModel.pageIndex++
            viewModel.getNoticeList()
        }

    }

    override fun initData() {
        viewModel.getNoticeList()

    }

    private fun infoAdapter() {
        // 使用
        binding.rvRecord.setLayoutManager(LinearLayoutManager(this@MessageListActivity))
        adapter.onItemClick = { notice, position , view->
            // 处理点击事件
            Intent(this@MessageListActivity, MessageActivity::class.java).apply {
                putExtra("id", notice.noticeId)
                startActivity(this)
            }
        }
        adapter.submitList(list)
        binding.rvRecord.setAdapter(adapter)
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

        viewModel.notice.observe(this){
            if(viewModel.pageIndex == 1){
                list.clear()
            }
            list.addAll(it)
            adapter.notifyDataSetChanged()
            if(list.size > 0){
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

}