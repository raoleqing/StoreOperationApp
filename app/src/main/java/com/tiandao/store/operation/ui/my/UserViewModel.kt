package com.tiandao.store.operation.ui.my

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tiandao.store.operation.base.BaseViewModel
import com.tiandao.store.operation.bean.SysUserBo
import com.tiandao.store.operation.network.ApiService
import com.tiandao.store.operation.network.RetrofitClient

class UserViewModel: BaseViewModel() {

    // region 状态管理
    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    //错误管理
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    //成功管理
    private val _userInfo = MutableLiveData<SysUserBo>()
    val userInfo: LiveData<SysUserBo> = _userInfo

    fun getInfo() {
        _loadingState.value = true
        this.http(api = {
            RetrofitClient.createService<ApiService>().getInfo()
        }, success = {
            _userInfo.value = it
            _loadingState.value = false
        }) {
            _loadingState.value = false
            _errorMessage.value = it.message
        }
    }
}