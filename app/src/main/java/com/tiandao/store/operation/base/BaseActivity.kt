package com.tiandao.store.operation.base

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.tiandao.store.operation.R
import com.tiandao.store.operation.common.event.EventMessage
import com.tiandao.store.operation.utils.EventBusUtils
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<VB : ViewBinding, VM : ViewModel> : AppCompatActivity() , View.OnClickListener{

    protected lateinit var binding: VB
    protected lateinit var viewModel: VM
    var toolbar: Toolbar? = null
    var appCompatImageButton: AppCompatImageButton? = null

    // 状态视图
    private lateinit var loadingView: View
    private lateinit var errorView: View
    private lateinit var contentView: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 初始化带有状态视图的根布局
        val rootView = layoutInflater.inflate(R.layout.layout_loading_state, null)
        contentView = rootView.findViewById(R.id.contentView)
        loadingView = rootView.findViewById(R.id.loadingView)
        errorView = rootView.findViewById(R.id.errorView)
        toolbar = rootView.findViewById(R.id.toolbar)
        appCompatImageButton = rootView.findViewById(R.id.appCompatImageButton)

        // 将实际内容添加到contentView中
        binding = getViewBinding()
        contentView.addView(binding.root)
        setContentView(rootView)
        // 设置重试按钮点击事件
        errorView.findViewById<View>(R.id.btnRetry).setOnClickListener { onRetry() }
        // 初始化ViewModel
        viewModel = provideViewModel()
        initView()
        initData()
        observeLiveData()

        toolbar?.let {
            setSupportActionBar(it)
            // 设置默认导航图标和点击事件
            if (showBackArrow()) {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                it.setNavigationOnClickListener { onBackPressed() }
            }
            // 设置标题
            if (getTitleText() != null) {
                setTitleText(getTitleText())
            }
        }

        if (isRegisteredEventBus()) {
            EventBusUtils.register(this)
        }

    }

    override fun onDestroy() {
        if (isRegisteredEventBus()) {
            EventBusUtils.unregister(this)
        }
        super.onDestroy()
    }

    /**
     * 设置标题文本
     */
    protected fun setTitleText(text: String?) {
        if (text == null) return
        this.toolbar?.let {
            it.setTitle(text)
        } ?: run {
            supportActionBar?.title = text
        }
    }

    /**
     * 获取ViewBinding
     */
    abstract fun getViewBinding(): VB

    abstract fun provideViewModel(): VM

    /**
     * 获取ViewModel类
     */
    @Suppress("UNCHECKED_CAST")
    private fun getViewModelClass(): Class<VM> {
        val type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1]
        return type as Class<VM>
    }

    /**
     * 是否显示返回箭头
     */
    protected open fun showBackArrow(): Boolean = false


    /**
     * 是否注册事件分发
     *
     * @return true 注册；false 不注册，默认不注册
     */
    protected open fun isRegisteredEventBus(): Boolean = false

    /**
     * 获取标题文本
     */
    protected open fun getTitleText(): String? = null

    abstract fun initView()

    abstract fun initData()

    abstract fun observeLiveData()

    fun setOnClickListener(listener: View.OnClickListener?, @IdRes vararg ids: Int) {
        for (id in ids) {
            findViewById<View>(id).setOnClickListener(listener)
        }
    }

    protected open fun onRetry() {
        // 默认实现 - 显示加载状态并重新初始化数据
        hideError()
    }

    protected fun setToolbarVisibility(visibility: Int) {
        toolbar?.let {
            it.visibility = visibility
        }
    }

    // State management methods
    protected fun showLoading() {
        loadingView.visibility = View.VISIBLE
        errorView.visibility = View.GONE
    }

    protected fun hideLoading() {
        loadingView.visibility = View.GONE
        errorView.visibility = View.GONE
    }

    protected fun showError(errorMsg: String? = null) {
        loadingView.visibility = View.GONE
        errorView.visibility = View.VISIBLE

        errorMsg?.let {
            errorView.findViewById<TextView>(R.id.tvErrorMsg).text = it
        }
    }

    protected fun hideError(){
        errorView.visibility = View.GONE
    }



    /**
     * 接受到分发的粘性事件
     *
     * @param event 粘性事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onReceiveStickyEvent(event: EventMessage<*>?) {
    }


}