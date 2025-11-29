package com.tiandao.store.operation.ui.message

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tiandao.store.operation.base.BaseViewModel
import com.tiandao.store.operation.bean.Notice
import com.tiandao.store.operation.network.ApiService
import com.tiandao.store.operation.network.RetrofitClient

class MessageViewModel : BaseViewModel() {
    // region 状态管理
    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    //错误管理
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _notice = MutableLiveData<Notice>()
    val notice: LiveData<Notice> = _notice

    fun getNotice(id: Long) {
        if(_loadingState.value == true){
            return
        }
        _loadingState.value = true
        this.http(api = {
            RetrofitClient.createService<ApiService>().noticeDetail(id)
        }, success = {
            _notice.value = it
            _loadingState.value = false
        }) {
            _loadingState.value = false
        }
    }
}