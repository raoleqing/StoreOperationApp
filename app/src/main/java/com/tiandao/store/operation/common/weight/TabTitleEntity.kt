package com.tiandao.store.operation.common.weight

import com.flyco.tablayout.listener.CustomTabEntity

class TabTitleEntity(var title: String) : CustomTabEntity {
    override fun getTabTitle(): String {
        return title
    }

    override fun getTabSelectedIcon(): Int {
        return 0
    }

    override fun getTabUnselectedIcon(): Int {
        return 0
    }
}