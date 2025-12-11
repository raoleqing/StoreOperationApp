package com.tiandao.store.operation.ui.team

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tiandao.store.operation.base.BaseViewModel
import com.tiandao.store.operation.bean.StaffHierarchy
import com.tiandao.store.operation.network.ApiService
import com.tiandao.store.operation.network.RetrofitClient

class TeamViewModel: BaseViewModel() {

    companion object {
        private const val PAGE_SIZE = 20
    }

    // region 状态管理
    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    //错误管理
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage


    private val _staffHierarchyList = MutableLiveData<List<StaffHierarchy>>()
    val staffHierarchyList: LiveData<List<StaffHierarchy>> = _staffHierarchyList


    private val _staffHierarchy = MutableLiveData<StaffHierarchy>()
    val staffHierarchy: LiveData<StaffHierarchy> = _staffHierarchy

    var pageIndex : Int = 1
    var managerStaffId: Long = 0
    fun getStaffHierarchyList() {
        _loadingState.value = true
        this.http(api = {
            RetrofitClient.createService<ApiService>().getStaffHierarchyList(managerStaffId,pageIndex, PAGE_SIZE)
        }, success = {
            _staffHierarchyList.value = it
            _loadingState.value = false
        }) {
            _loadingState.value = false
        }
    }


    fun getStaffHierarchy(staffId: Long) {
        _loadingState.value = true
        this.http(api = {
            RetrofitClient.createService<ApiService>().getStaffHierarchy(staffId)
        }, success = {
            _staffHierarchy.value = it
            _loadingState.value = false
        }) {
            _loadingState.value = false
        }
    }

}