package com.tiandao.store.operation.ui.message

import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.ViewModelProvider
import com.tiandao.store.operation.base.BaseActivity
import com.tiandao.store.operation.databinding.ActivityMessageBinding
import com.tiandao.store.operation.network.RetrofitClient
import com.tiandao.store.operation.utils.ToastUtils

class MessageActivity : BaseActivity<ActivityMessageBinding, MessageViewModel>(){

    var id: Long = 0

    override fun showBackArrow(): Boolean {
        return true
    }

    override fun getViewBinding(): ActivityMessageBinding {
        return ActivityMessageBinding.inflate(layoutInflater)
    }

    override fun provideViewModel(): MessageViewModel {
        return ViewModelProvider(this).get(MessageViewModel::class.java)
    }

    override fun initView() {
        setTitleText("消息详情")
        id = intent?.getLongExtra("id",0L) ?: 0L

        binding.webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            // 禁用缩放控制
            builtInZoomControls = false
            displayZoomControls = false
            // 禁止缩放手势
            setSupportZoom(false)
            // 禁用视口缩放（关键！）
            useWideViewPort = false
            loadWithOverviewMode = false
        }

        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                // 注入 JS 代码，限制所有图片的最大宽度
                view.loadUrl(
                    """
            javascript:(function() {
                var images = document.getElementsByTagName('img');
                for (var i = 0; i < images.length; i++) {
                    images[i].style.maxWidth = '100%';
                    images[i].style.height = 'auto';
                }
            })()
            """.trimIndent()
                )
            }
        }
    }

    override fun initData() {
        viewModel.getNotice(id)
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

        viewModel.notice.observe(this) {
            it?.let {
                setTitleText(it.noticeTitle)
                binding.tvCreateTime.text = it.createTime
                binding.webView.loadDataWithBaseURL(
                    RetrofitClient.IMG_URL, // 替换为你的基础URL，用于解析相对路径
                    it.noticeContent,
                    "text/html",
                    "UTF-8",
                    null
                )
            }
        }

    }

    override fun onClick(v: View?) {

    }

}