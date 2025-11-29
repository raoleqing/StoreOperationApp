package com.tiandao.store.operation.ui.tenant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tiandao.store.operation.base.BaseViewModel
import com.tiandao.store.operation.bean.SysTenant
import com.tiandao.store.operation.network.ApiService
import com.tiandao.store.operation.network.RetrofitClient

class TenantViewModel: BaseViewModel() {

    // region 状态管理
    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    //错误管理
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _createSysTenant = MutableLiveData<Boolean>()
    val createSysTenant: LiveData<Boolean> = _createSysTenant

    private val _sysTenant = MutableLiveData<SysTenant>()
    val sysTenant: LiveData<SysTenant> = _sysTenant

    fun createTenant(sysTenant: SysTenant) {
        _loadingState.value = true
        this.http(api = {
            RetrofitClient.createService<ApiService>().createTenant(sysTenant)
        }, success = {
            _createSysTenant.value = it
            _loadingState.value = false
        }) {
            _loadingState.value = false
        }
    }

    fun getTenant(tenantId: Long) {
        _loadingState.value = true
        this.http(api = {
            RetrofitClient.createService<ApiService>().getTenant(tenantId)
        }, success = {
            _sysTenant.value = it
            _loadingState.value = false
        }) {
            _loadingState.value = false
        }
    }

}