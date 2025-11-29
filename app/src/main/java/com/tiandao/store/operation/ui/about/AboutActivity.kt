package com.tiandao.store.operation.ui.about

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.tiandao.store.operation.R
import com.tiandao.store.operation.base.BaseActivity
import com.tiandao.store.operation.databinding.ActivityAboutBinding
import com.tiandao.store.operation.network.RetrofitClient
import com.tiandao.store.operation.ui.webview.WebViewActivity

class AboutActivity  : BaseActivity<ActivityAboutBinding, AboutViewModel>() {

    override fun showBackArrow(): Boolean {
        return true
    }

    override fun getViewBinding(): ActivityAboutBinding {
        return ActivityAboutBinding.inflate(layoutInflater)
    }

    override fun provideViewModel(): AboutViewModel {
        return ViewModelProvider(this).get(AboutViewModel::class.java)
    }

    override fun initView() {
        setTitleText("关于我们")
        setOnClickListener(this, R.id.ll_version,R.id.tv_protocol, R.id.tv_privacy)
    }

    override fun initData() {
    }

    override fun observeLiveData() {
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_version -> {

            }
            R.id.tv_protocol -> {
                val url = RetrofitClient.TERMS_URL
                val intent = Intent(this@AboutActivity, WebViewActivity::class.java)
                intent.putExtra(WebViewActivity.EXTRA_URL, url)
                startActivity(intent)
            }
            R.id.tv_privacy -> {
                val url = RetrofitClient.PRIVACY_URL
                val intent = Intent(this@AboutActivity, WebViewActivity::class.java)
                intent.putExtra(WebViewActivity.EXTRA_URL, url)
                startActivity(intent)
            }
        }

    }
}