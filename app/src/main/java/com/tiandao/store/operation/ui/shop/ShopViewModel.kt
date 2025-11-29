package com.tiandao.store.operation.ui.shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tiandao.store.operation.base.BaseViewModel
import com.tiandao.store.operation.bean.PlatformOrder
import com.tiandao.store.operation.bean.PlatformRechargePlan
import com.tiandao.store.operation.bean.Shop
import com.tiandao.store.operation.bean.SysTenant
import com.tiandao.store.operation.network.ApiService
import com.tiandao.store.operation.network.RetrofitClient

class ShopViewModel: BaseViewModel() {

    // region 状态管理
    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    //错误管理
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage


    private val _sysTenant = MutableLiveData<SysTenant>()
    val sysTenant: LiveData<SysTenant> = _sysTenant

    private val _shop = MutableLiveData<Shop>()
    val shop: LiveData<Shop> = _shop

    private val _platformRechargePlanList = MutableLiveData<List<PlatformRechargePlan>>()
    val platformRechargePlanList: LiveData<List<PlatformRechargePlan>> = _platformRechargePlanList


    private val _createPlatformOrder = MutableLiveData<Boolean>()
    val createPlatformOrder: LiveData<Boolean> = _createPlatformOrder

    fun getTenant(tenantId: Long) {
        _loadingState.value = true
        this.http(api = {
            RetrofitClient.createService<ApiService>().getTenant(tenantId)
        }, success = {
            _sysTenant.value = it
            _loadingState.value = false
        }) {
            _loadingState.value = false
        }
    }


    fun getShop(shopId: Long) {
        _loadingState.value = true
        this.http(api = {
            RetrofitClient.createService<ApiService>().getShop(shopId)
        }, success = {
            _shop.value = it
            _loadingState.value = false
        }) {
            _loadingState.value = false
        }
    }

    fun getPlatformRechargePlanList(level: Int, type: String){
        _loadingState.value = true
        this.http(api = {
            RetrofitClient.createService<ApiService>().platformRechargePlanList(level, type)
        }, success = {
            _platformRechargePlanList.value = it
            _loadingState.value = false
        }) {
            _loadingState.value = false
        }
    }

    fun createPlatformOrder(platformOrder: PlatformOrder){
        _loadingState.value = true
        this.http(api = {
            RetrofitClient.createService<ApiService>().createPlatformOrder(platformOrder)
        }, success = {
            _createPlatformOrder.value = it
            _loadingState.value = false
        }) {
            _loadingState.value = false
        }
    }



}