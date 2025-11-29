package com.tiandao.store.operation.ui.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tiandao.store.operation.base.BaseViewModel
import com.tiandao.store.operation.bean.PlatformOrder
import com.tiandao.store.operation.bean.Shop
import com.tiandao.store.operation.network.ApiService
import com.tiandao.store.operation.network.RetrofitClient

class OrderListViewModel: BaseViewModel() {

    companion object {
        private const val PAGE_SIZE = 20
    }

    // region 状态管理
    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    //错误管理
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _orderList = MutableLiveData<List<PlatformOrder>>()
    val orderList: LiveData<List<PlatformOrder>> = _orderList

    var pageIndex : Int = 1
    var keyword: String = ""
    fun getOrderList() {
        if(_loadingState.value == true){
            return
        }
        _loadingState.value = true
        this.http(api = {
            RetrofitClient.createService<ApiService>().platformOrderList(keyword,pageIndex, PAGE_SIZE)
        }, success = {
            _orderList.value = it
            _loadingState.value = false
        }) {
            _loadingState.value = false
        }
    }
}