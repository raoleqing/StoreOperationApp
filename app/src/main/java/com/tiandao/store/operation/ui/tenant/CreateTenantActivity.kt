package com.tiandao.store.operation.ui.tenant

import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.tiandao.store.operation.R
import com.tiandao.store.operation.base.BaseActivity
import com.tiandao.store.operation.bean.SysTenant
import com.tiandao.store.operation.common.event.EventCode
import com.tiandao.store.operation.common.event.EventMessage
import com.tiandao.store.operation.databinding.ActivityCreateTenantBinding
import com.tiandao.store.operation.utils.DateUtils
import com.tiandao.store.operation.utils.EventBusUtils
import com.tiandao.store.operation.utils.ToastUtils
import com.tiandao.store.operation.utils.UserUtils

class CreateTenantActivity : BaseActivity<ActivityCreateTenantBinding, TenantViewModel>()  {

    override fun showBackArrow(): Boolean {
        return true
    }
    override fun getViewBinding(): ActivityCreateTenantBinding {
        return ActivityCreateTenantBinding.inflate(layoutInflater)
    }

    override fun provideViewModel(): TenantViewModel {
        return ViewModelProvider(this).get(TenantViewModel::class.java)
    }

    override fun initView() {
        setTitleText("注册租户")
        setupSpinner()
        binding.butRegister.setOnClickListener(this)
    }

    private fun setupSpinner() {
        val levels = arrayOf("基础版", "独立版", "专业版")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, levels)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerLevel.adapter = adapter
    }

    override fun initData() {

    }

    override fun observeLiveData() {

        viewModel.loadingState.observe(this) {
            if (it) {
                showLoading()
            }else{
                hideLoading()
            }
        }

        viewModel.errorMessage.observe(this) {
            it?.let {
                ToastUtils.showShort(it)
            }
        }

        viewModel.createSysTenant.observe(this){
           if(it){
               ToastUtils.showShort("注册成功")
               EventBusUtils.post(EventMessage(EventCode.TENANT_REGISTRATION_SUCCESS, "注册成功"))
               finish()
           }
        }

    }

    override fun onClick(v: View?) {
       when(v?.id){
           R.id.but_register -> {
              var sysTenant = getBean();
               if(sysTenant != null){
                  viewModel.createTenant(sysTenant);
               }
           }
       }
    }

    private fun getBean(): SysTenant? {
        var tenantName = binding.etTenantName.text.toString()
        if(tenantName.isEmpty()){
            ToastUtils.showLong("请输入租户名称")
            return null;
        }

        var contactName = binding.etContactName.text.toString()
        if(contactName.isEmpty()){
            ToastUtils.showLong("请输入联系人名称")
            return null;
        }

        var contactPhone = binding.etContactPhone.text.toString()
        if(contactPhone.isEmpty()){
            ToastUtils.showLong("请输入联系人手机")
            return null;
        }

        var contactEmail = binding.etContactEmail.text.toString()
        if(contactEmail.isEmpty()){
            ToastUtils.showLong("请输入联系人邮箱")
            return null;
        }

        val level = when (binding.spinnerLevel.selectedItemPosition) {
            0 -> 1
            1 -> 2
            2 -> 3
            else -> 1
        }

        var remark = binding.etRemark.text.toString()

        return SysTenant(
            tenantId = 0,
            tenantCode = "",
            tenantName = tenantName,
            contactName = contactName,
            contactPhone = contactPhone,
            contactEmail = contactEmail,
            ownerStaffId = 0,
            ownerStaffName = "",
            salesmanId =  UserUtils.getUserId( this),
            salesmanName = UserUtils.getUserName( this),
            logo = "",
            level = level,
            maxShopCount = 30,
            status = 0,
            createTime = DateUtils.getCurrentTime(DateUtils.FORMAT_YMD_HMS),
            remark = remark,
            shopList = null
        )

    }

}