package com.tiandao.store.operation.base

import android.app.Application
import android.content.Context
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.tiandao.store.operation.R
import com.tiandao.store.operation.utils.ActivityCollectorUtil
import com.tiandao.store.operation.utils.SPUtils

class StoreOperationApplication : Application() {

    companion object {
        lateinit var application: Application
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        //初始化
        init()
    }

    private fun init() {
        // 初始化 SharedPreferences
        SPUtils.init(this);
        // 设置默认的 Header 和 Footer
        initRefresh();
        // activity 管理
        ActivityCollectorUtil.init(this)

    }

    private fun initRefresh() {
        //设置全局默认配置（优先级最低，会被其他设置覆盖）

        SmartRefreshLayout.setDefaultRefreshInitializer { context: Context?, layout: RefreshLayout? ->
            //全局设置（优先级最低）
            layout!!.setEnableAutoLoadMore(true)
            layout!!.setEnableOverScrollBounce(false)
            layout!!.setEnableScrollContentWhenRefreshed(true)
            //layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);
            layout!!.setFooterMaxDragRate(4.0f)
            layout!!.setFooterHeight(45f)
            // 刷新尾部是否跟随内容偏移
            layout!!.setEnableFooterTranslationContent(true)
            // 加载更多是否跟随内容偏移
            layout!!.setEnableFooterFollowWhenNoMoreData(true)
            // 内容不满一页时是否可以上拉加载更多
            layout!!.setEnableLoadMoreWhenContentNotFull(false)
            // 仿苹果越界效果开关
            layout!!.setEnableOverScrollDrag(true)
        }
        // 设置全局的 Header 构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context: Context?, layout: RefreshLayout? ->
            //开始设置全局的基本参数（这里设置的属性只跟下面的MaterialHeader绑定，其他Header不会生效，能覆盖DefaultRefreshInitializer的属性和Xml设置的属性）
            layout!!.setEnableHeaderTranslationContent(true)
            ClassicsHeader(context).setAccentColor(getColor(R.color.colorPrimary))
        }
        // 设置全局的 Footer 构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context: Context?, layout: RefreshLayout? ->
            ClassicsFooter(
                context
            ).setAccentColor(getColor(R.color.colorPrimary))
        }
    }


}