package com.tiandao.store.operation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.tiandao.store.operation.R
import com.tiandao.store.operation.utils.EventBusUtils
import java.lang.reflect.ParameterizedType


abstract class BaseFragment<VB : ViewBinding, VM : ViewModel> : Fragment() ,View.OnClickListener{

    protected lateinit var binding: VB
    protected lateinit var viewModel: VM

    // Loading state views
    private lateinit var contentView: ViewGroup
    private lateinit var loadingView: View
    private lateinit var errorView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 初始化带有状态视图的根布局
        val rootView = inflater.inflate(R.layout.layout_loading_state_fragment, container, false)
        contentView = rootView.findViewById(R.id.contentView)
        loadingView = rootView.findViewById(R.id.loadingView)
        errorView = rootView.findViewById(R.id.errorView)

        // 将实际内容添加到contentView中
        binding = getViewBinding(inflater, contentView)
        contentView.addView(binding.root)

        // 重置
        errorView.findViewById<View>(R.id.btnRetry).setOnClickListener { onRetry() }


        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        viewModel = provideViewModel()

        // Call initialization methods
        initView()
        initData()
        observeLiveData()

        if (isRegisteredEventBus()) {
            EventBusUtils.register(this)
        }
    }

    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    abstract fun provideViewModel(): VM

    /**
     * 是否注册事件分发
     *
     * @return true 注册；false 不注册，默认不注册
     */
    protected open fun isRegisteredEventBus(): Boolean = false


    @Suppress("UNCHECKED_CAST")
    private fun getViewModelClass(): Class<VM> {
        val type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1]
        return type as Class<VM>
    }


    fun setOnClickListener(listener: View.OnClickListener?, @IdRes vararg ids: Int) {
        for (id in ids) {
            activity?.findViewById<View>(id)?.setOnClickListener(listener)
        }
    }

    // State management methods
    protected fun showLoading() {
        loadingView.visibility = View.VISIBLE
        //errorView.visibility = View.GONE
    }

    protected fun hideLoading() {
        loadingView.visibility = View.GONE
        //errorView.visibility = View.GONE
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

    protected open fun onRetry() {
       hideError()
    }

    abstract fun initView()
    abstract fun initData()
    abstract fun observeLiveData()

    override fun onDestroyView() {
        super.onDestroyView()
        // Clear binding reference to avoid memory leaks
        if (::binding.isInitialized) {
            (binding as? AutoCloseable)?.close()
        }

        if (isRegisteredEventBus()) {
            EventBusUtils.unregister(this)
        }
    }

}