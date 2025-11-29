package com.tiandao.store.operation.ui.shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tiandao.store.operation.base.BaseViewModel
import com.tiandao.store.operation.bean.Shop
import com.tiandao.store.operation.network.ApiService
import com.tiandao.store.operation.network.RetrofitClient

class ShopListViewModel: BaseViewModel() {

    companion object {
        private const val PAGE_SIZE = 20
    }

    // region 状态管理
    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    //错误管理
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _shopList = MutableLiveData<List<Shop>>()
    val shopList: LiveData<List<Shop>> = _shopList

    var pageIndex : Int = 1
    var keyword: String = ""
    fun getShopList() {
        if(_loadingState.value == true){
            return
        }
        _loadingState.value = true
        this.http(api = {
            RetrofitClient.createService<ApiService>().shopList(keyword,pageIndex, PAGE_SIZE)
        }, success = {
            _shopList.value = it
            _loadingState.value = false
        }) {
            _loadingState.value = false
        }
    }
}