package com.tiandao.store.operation.ui.commission

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tiandao.store.operation.base.BaseViewModel
import com.tiandao.store.operation.bean.CommissionCount
import com.tiandao.store.operation.bean.StaffCommissionVo
import com.tiandao.store.operation.network.ApiService
import com.tiandao.store.operation.network.RetrofitClient

class CommissionViewModel: BaseViewModel() {

    companion object {
        private const val PAGE_SIZE = 20
    }

    // region 状态管理
    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    //错误管理
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _staffCommissionList = MutableLiveData<StaffCommissionVo>()
    val staffCommissionList: LiveData<StaffCommissionVo> = _staffCommissionList

    private val _commissionCount = MutableLiveData<CommissionCount>()
    val commissionCount: LiveData<CommissionCount> = _commissionCount

    var pageIndex : Int = 1
    var staffId: Long = 0
    var month: String = ""
    fun getStaffCommissionListList() {
        _loadingState.value = true
        this.http(api = {
            RetrofitClient.createService<ApiService>().staffCommissionList(staffId,month,pageIndex, PAGE_SIZE)
        }, success = {
            _staffCommissionList.value = it
            _loadingState.value = false
        }) {
            _loadingState.value = false
        }
    }

    fun getStaffCountByMonth(month: String) {
        _loadingState.value = true
        this.http(api = {
            RetrofitClient.createService<ApiService>().getStaffCountByMonth(staffId,month)
        }, success = {
            _commissionCount.value = it
            _loadingState.value = false
        }) {
            _loadingState.value = false
        }
    }


}