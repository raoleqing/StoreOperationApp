package com.tiandao.store.operation.ui.tenant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tiandao.store.operation.base.BaseViewModel
import com.tiandao.store.operation.bean.SysTenant
import com.tiandao.store.operation.network.ApiService
import com.tiandao.store.operation.network.RetrofitClient

class TenantListViewModel: BaseViewModel() {

    companion object {
        private const val PAGE_SIZE = 20
    }

    // region 状态管理
    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    //错误管理
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _sysTenantList = MutableLiveData<List<SysTenant>>()
    val sysTenantList: LiveData<List<SysTenant>> = _sysTenantList

    var pageIndex : Int = 1
    var keyword: String = ""
    var salesmanId : Long = 0
    fun getTenantList() {
        if(_loadingState.value == true){
            return
        }
        _loadingState.value = true
        this.http(api = {
            RetrofitClient.createService<ApiService>().tenantList(keyword, salesmanId,pageIndex,PAGE_SIZE)
        }, success = {
            _sysTenantList.value = it
            _loadingState.value = false
        }) {
            _loadingState.value = false
        }
    }


}