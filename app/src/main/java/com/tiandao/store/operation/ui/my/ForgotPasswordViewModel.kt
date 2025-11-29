package com.tiandao.store.operation.ui.my

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tiandao.store.operation.base.BaseViewModel
import com.tiandao.store.operation.bean.UpdatePasswordFrom
import com.tiandao.store.operation.network.ApiService
import com.tiandao.store.operation.network.RetrofitClient

class ForgotPasswordViewModel: BaseViewModel() {

    // region 状态管理
    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    //错误管理
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    //成功管理
    private val _verificationCode = MutableLiveData<Boolean>()
    val verificationCode: LiveData<Boolean> = _verificationCode

    private val _updatePassword = MutableLiveData<Boolean>()
    val updatePassword: LiveData<Boolean> = _updatePassword

    fun updatePassword(phone: String,oldPassword: String,newPassword: String){
        _loadingState.value = true
        var userInfoFrom = UpdatePasswordFrom(phone,oldPassword,newPassword)
        this.http(api = {
            RetrofitClient.createService<ApiService>().updatePassword(userInfoFrom)
        }, success = {
            _updatePassword.value = it
            _loadingState.value = false
        }) {
            _loadingState.value = false
            _errorMessage.value = it.message
        }
    }
}