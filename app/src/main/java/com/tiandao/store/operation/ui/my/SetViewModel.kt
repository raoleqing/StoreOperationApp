package com.tiandao.store.operation.ui.my

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tiandao.store.operation.base.BaseViewModel
import com.tiandao.store.operation.network.ApiService
import com.tiandao.store.operation.network.RetrofitClient

class SetViewModel: BaseViewModel() {

    // region 状态管理
    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    //错误管理
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    //成功管理
    private val _logoutBo = MutableLiveData<Boolean>()
    val logoutBo: LiveData<Boolean> = _logoutBo


    fun logout() {
        _loadingState.value = true
        this.http(api = {
            RetrofitClient.createService<ApiService>().logout()
        }, success = {
            _logoutBo.value = true
            _loadingState.value = false
        }) {
            _loadingState.value = false
            _errorMessage.value = it.message
        }
    }

}