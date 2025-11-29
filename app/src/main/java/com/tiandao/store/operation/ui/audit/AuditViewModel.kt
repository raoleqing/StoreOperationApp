package com.tiandao.store.operation.ui.audit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tiandao.store.operation.base.BaseViewModel
import com.tiandao.store.operation.bean.FlowAuditRecord
import com.tiandao.store.operation.network.ApiService
import com.tiandao.store.operation.network.RetrofitClient

class AuditViewModel: BaseViewModel() {

    companion object {
        private const val PAGE_SIZE = 20
    }

    // region 状态管理
    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    //错误管理
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _flowAuditRecordList = MutableLiveData<List<FlowAuditRecord>>()
    val flowAuditRecordList: LiveData<List<FlowAuditRecord>> = _flowAuditRecordList

    var pageIndex : Int = 1
    var auditOpId: Long = 0
    var type: Int = 0

    fun getFlowAuditRecordList() {
        if(_loadingState.value == true){
            return
        }
        _loadingState.value = true
        this.http(api = {
            RetrofitClient.createService<ApiService>().flowAuditRecordList(auditOpId,type,pageIndex, PAGE_SIZE)
        }, success = {
            _flowAuditRecordList.value = it
            _loadingState.value = false
        }) {
            _loadingState.value = false
        }
    }

}