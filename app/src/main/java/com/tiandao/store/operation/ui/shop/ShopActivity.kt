package com.tiandao.store.operation.ui.shop

import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.tiandao.store.operation.R
import com.tiandao.store.operation.base.BaseActivity
import com.tiandao.store.operation.bean.PlatformOrder
import com.tiandao.store.operation.bean.PlatformRechargePlan
import com.tiandao.store.operation.bean.Shop
import com.tiandao.store.operation.bean.SysTenant
import com.tiandao.store.operation.common.dialog.PayServerDialog
import com.tiandao.store.operation.common.enum.EnumType
import com.tiandao.store.operation.common.event.EventCode
import com.tiandao.store.operation.common.event.EventMessage
import com.tiandao.store.operation.databinding.ActivityShopBinding
import com.tiandao.store.operation.utils.DateUtils
import com.tiandao.store.operation.utils.EventBusUtils
import com.tiandao.store.operation.utils.ToastUtils
import com.tiandao.store.operation.utils.UserUtils
import java.math.BigDecimal

class ShopActivity : BaseActivity<ActivityShopBinding, ShopViewModel>()   {

    companion object {
        const val SHOP_ID = "shop_id"
    }

    var shopId: Long = 0
    var shop: Shop? = null
    var sysTenant: SysTenant? = null

    var smsList: List<PlatformRechargePlan> = mutableListOf()
    var shopList: List<PlatformRechargePlan> = mutableListOf()
    var type: String = ""


    override fun showBackArrow(): Boolean {
        return true
    }
    override fun getViewBinding(): ActivityShopBinding {
        return ActivityShopBinding.inflate(layoutInflater)
    }

    override fun provideViewModel(): ShopViewModel {
        return ViewModelProvider(this).get(ShopViewModel::class.java)
    }

    override fun initView() {
        setTitleText("门店信息")
        binding.butPayShop.setOnClickListener(this)
        binding.butPaySms.setOnClickListener(this)
    }

    override fun initData() {
        shopId = intent.getLongExtra(SHOP_ID, 0)
        viewModel.getShop(shopId)
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

        viewModel.shop.observe(this){
            if(it != null){
                setContent(it);
            }
        }

        viewModel.sysTenant.observe(this){
            if(it != null){
                this.sysTenant = it
            }
        }

        viewModel.platformRechargePlanList.observe(this){
            if(it != null){
                when(type){
                    "shop" -> {
                        shopList = it
                        showPayServerDialog("shop",shopList);
                    }
                    "sms" -> {
                        smsList = it
                        showPayServerDialog("sms",smsList);
                    }
                }

            }
        }

        viewModel.createPlatformOrder.observe(this){
            if(it != null){
                if(it){
                    ToastUtils.showShort("支付成功")
                    EventBusUtils.post(EventMessage(EventCode.CREATE_ORDER_SUCCESS, "支付成功"))
                    finish()
                }else{
                    ToastUtils.showShort("支付失败")
                }
            }
        }
    }

    private fun setContent(shop: Shop) {
        this.shop = shop
        binding.tvShopName.text = shop.shopName
        binding.tvContactName.text = shop.contactName
        binding.tvContactPhone.text = shop.contactMobile
        binding.tvOwnerStaffName.text = shop.ownerStaffName
        binding.tvValidEndTime.text = shop.validEndTime
        binding.tvSmsNum.text = shop.smsCount.toString()
        binding.tvStatus.text = when(shop.status){
            0 -> "正常"
            1 -> "停用"
            2 -> "注销"
            else -> ""}
        viewModel.getTenant(shop.tenantId)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.but_pay_shop -> {
                if(shopList.isEmpty()){
                    type = "shop"
                    viewModel.getPlatformRechargePlanList(sysTenant?.level?:0, "shop");
                }else{
                    showPayServerDialog("shop",shopList)
                }
            }
            R.id.but_pay_sms -> {
                if(smsList.isEmpty()){
                    type = "sms"
                    viewModel.getPlatformRechargePlanList(sysTenant?.level?:0, "sms");
                }else{
                    showPayServerDialog("sms",smsList)
                }
            }
        }
    }

    private fun showPayServerDialog(type: String,planList: List<PlatformRechargePlan>) {

         var dialog = PayServerDialog(this,type,planList,object : PayServerDialog.OnFilterAppliedListener{
             override fun determine(dialog: PayServerDialog, platformRechargePlan: PlatformRechargePlan?, payType: String, amount: Int) {
                 dialog.dismiss()
                 createOrder(platformRechargePlan,payType,amount);
             }
             override fun close(dialog: PayServerDialog) {
                 dialog.dismiss()
             }

         })
        dialog.show()
    }


    private fun createOrder(plan: PlatformRechargePlan?, payType: String, amount: Int) {

        val planId: Long
        val planType: String
        val planName: String
        val payAmount: Int

        if(payType == EnumType.PayType.GIVE || payType == EnumType.PayType.REFUND){
            planId = 0
            planType = type
            planName = if(payType == EnumType.PayType.GIVE){
                "赠送"
            }else{
                "返还"
            }
            payAmount = amount
        }else{
            planId = plan?.id?:0
            planType =  plan?.type?:""
            planName = plan?.name?:""
            payAmount = plan?.amount?:0
        }

         var platformOrder = PlatformOrder(
             id = 0,
             tenantId = sysTenant?.tenantId?:0,
             tenantName = sysTenant?.tenantName?:"",
             shopId = shop?.id?:0,
             shopName = shop?.shopName?:"",
             orderNo = "",
             planType = planType,
             planId = planId,
             planName = planName,
             amount = payAmount,
             paymentAmount = plan?.price?:BigDecimal.ZERO,
             paymentChannel = payType,
             status = 0,
             payTime = DateUtils.getCurrentTime(DateUtils.FORMAT_YMD_HMS),
             staffId = UserUtils.getUserId( this),
             staffName = UserUtils.getUserName( this),
             auditStatus = 0,
             flowAuditRecord = null
         )
        viewModel.createPlatformOrder(platformOrder);
    }



}