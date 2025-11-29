package com.tiandao.store.operation.ui.message

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tiandao.store.operation.base.BaseViewModel
import com.tiandao.store.operation.bean.Notice
import com.tiandao.store.operation.network.ApiService
import com.tiandao.store.operation.network.RetrofitClient

class MessageListViewModel : BaseViewModel() {
    companion object {
        private const val PAGE_SIZE = 20
    }

    // region 状态管理
    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    //错误管理
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _notice = MutableLiveData<List<Notice>>()
    val notice: LiveData<List<Notice>> = _notice

    var pageIndex : Int = 1

    fun getNoticeList() {
        if(_loadingState.value == true){
            return
        }
        _loadingState.value = true
        this.http(api = {
            RetrofitClient.createService<ApiService>().noticeList(pageIndex,PAGE_SIZE)
        }, success = {
            _notice.value = it
            _loadingState.value = false
        }) {
            _loadingState.value = false
        }
    }
}