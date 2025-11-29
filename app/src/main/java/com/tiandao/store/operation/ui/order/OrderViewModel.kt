package com.tiandao.store.operation.ui.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tiandao.store.operation.base.BaseViewModel
import com.tiandao.store.operation.bean.FlowAuditRecordItem
import com.tiandao.store.operation.bean.PlatformOrder
import com.tiandao.store.operation.network.ApiService
import com.tiandao.store.operation.network.RetrofitClient

class OrderViewModel: BaseViewModel() {

    // region 状态管理
    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    //错误管理
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _platformOrder = MutableLiveData<PlatformOrder>()
    val platformOrder: LiveData<PlatformOrder> = _platformOrder

    private val _audit = MutableLiveData<Boolean>()
    val audit: LiveData<Boolean> = _audit

    fun getPlatformOrder(orderId: Long) {
        _loadingState.value = true
        this.http(api = {
            RetrofitClient.createService<ApiService>().getPlatformOrder(orderId)
        }, success = {
            _platformOrder.value = it
            _loadingState.value = false
        }) {
            _loadingState.value = false
        }
    }

    fun audit(flowAuditRecordItem: FlowAuditRecordItem){
        _loadingState.value = true
        this.http(api = {
            RetrofitClient.createService<ApiService>().audit(flowAuditRecordItem)
        }, success = {
            _audit.value = it
            _loadingState.value = false
        }) {
            _loadingState.value = false
        }
    }

}