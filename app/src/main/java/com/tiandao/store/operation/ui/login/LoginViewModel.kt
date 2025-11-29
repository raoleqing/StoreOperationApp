package com.tiandao.store.operation.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tiandao.store.operation.base.BaseViewModel
import com.tiandao.store.operation.bean.User
import com.tiandao.store.operation.bean.UserLogFrom
import com.tiandao.store.operation.network.ApiService
import com.tiandao.store.operation.network.RetrofitClient

class LoginViewModel : BaseViewModel(){

    // region 状态管理
    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    //错误管理
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    //成功管理
    private val _userInfo = MutableLiveData<User>()
    val userInfo: LiveData<User> = _userInfo

    fun login(username: String,password: String) {
        _loadingState.value = true
        var userInfoFrom = UserLogFrom(username,password,"")
        this.http(api = {
            RetrofitClient.createService<ApiService>().login(userInfoFrom)
        }, success = {
            _userInfo.value = it
            _loadingState.value = false
        }) {
            _loadingState.value = false
            _errorMessage.value = it.message
        }
    }
}