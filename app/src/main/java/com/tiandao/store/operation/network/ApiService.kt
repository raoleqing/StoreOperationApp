package com.tiandao.store.operation.network


import com.tiandao.store.operation.base.ResultBean
import com.tiandao.store.operation.bean.CommissionCount
import com.tiandao.store.operation.bean.FlowAuditRecord
import com.tiandao.store.operation.bean.FlowAuditRecordItem
import com.tiandao.store.operation.bean.Notice
import com.tiandao.store.operation.bean.PlatformOrder
import com.tiandao.store.operation.bean.PlatformRechargePlan
import com.tiandao.store.operation.bean.Shop
import com.tiandao.store.operation.bean.StaffCommissionVo
import com.tiandao.store.operation.bean.StaffHierarchy
import com.tiandao.store.operation.bean.SysTenant
import com.tiandao.store.operation.bean.SysUserBo
import com.tiandao.store.operation.bean.UpdatePasswordFrom
import com.tiandao.store.operation.bean.User
import com.tiandao.store.operation.bean.UserLogFrom
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // 登录
    @POST("system/mobile/login")
    suspend fun login(@Body userInfo: UserLogFrom): ResultBean<User>

    // 修改密码
    @POST("system/mobile/updatePassword")
    suspend fun updatePassword(@Body from : UpdatePasswordFrom): ResultBean<Boolean>

    // 登出
    @POST("system/mobile/logout")
    suspend fun logout(): ResultBean<Any>


    // 获取用户信息
    @GET("system/mobile/getInfo")
    suspend fun getInfo(): ResultBean<SysUserBo>


    // 公告
    @GET("system/mobile/notice/list")
    suspend fun noticeList(
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int
    ): ResultBean<List<Notice>>

    // 公告详情
    @GET("system/mobile/notice/{id}")
    suspend fun noticeDetail(
        @Path("id") id: Long,
    ): ResultBean<Notice>


    @GET("merchant/mobile/tenant/list")
    suspend fun tenantList(
        @Query("keyword") keyword: String,
        @Query("salesmanId") salesmanId: Long,
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int
    ): ResultBean<List<SysTenant>>

    @POST("merchant/mobile/tenant")
    suspend fun createTenant(
        @Body sysTenant: SysTenant
    ): ResultBean<Boolean>

    @GET("merchant/mobile/tenant/{tenantId}")
    suspend fun getTenant(
        @Path("tenantId") tenantId: Long,
    ): ResultBean<SysTenant>


    @GET("merchant/mobile/shop/list")
    suspend fun shopList(
        @Query("keyword") keyword: String,
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int
    ): ResultBean<List<Shop>>


    @GET("merchant/mobile/shop/{shopId}")
    suspend fun getShop(
        @Path("shopId") shopId: Long,
    ): ResultBean<Shop>


    @GET("system/mobile/platformRechargePlan/list")
    suspend fun platformRechargePlanList(
        @Query("level") level: Int,
        @Query("type") type: String,
    ): ResultBean<List<PlatformRechargePlan>>


    @POST("system/mobile/platformOrder")
    suspend fun createPlatformOrder(
        @Body platformOrder: PlatformOrder
    ): ResultBean<Boolean>

    @GET("system/mobile/platformOrder/list")
    suspend fun platformOrderList(
        @Query("keyword") keyword: String,
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int
    ): ResultBean<List<PlatformOrder>>


    @GET("system/mobile/platformOrder/{id}")
    suspend fun getPlatformOrder(
        @Path("id") id: Long,
    ): ResultBean<PlatformOrder>


    @POST("system/mobile/flowAuditRecordItem/audit")
    suspend fun audit(
        @Body auditRecordItem: FlowAuditRecordItem
    ): ResultBean<Boolean>

    @GET("system/mobile/flowAuditRecord/list")
    suspend fun flowAuditRecordList(
        @Query("auditOpId") auditOpId: Long,
        @Query("type") type: Int,
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int
    ): ResultBean<List<FlowAuditRecord>>

    @GET("system/mobile/staffMonthlyFlow/list")
    suspend fun staffCommissionList(
        @Query("staffId") staffId: Long,
        @Query("monthYear") month: String,
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int
    ): ResultBean<StaffCommissionVo>

    @GET("system/mobile/staffCommission/getStaffCountByMonth")
    suspend fun getStaffCountByMonth(
        @Query("staffId") staffId: Long,
        @Query("month") month: String
    ): ResultBean<CommissionCount>



    @GET("system/mobile/staffHierarchy/list")
    suspend fun getStaffHierarchyList(
        @Query("managerStaffId") managerStaffId: Long,
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int
    ): ResultBean<List<StaffHierarchy>>

    @GET("system/mobile/staffHierarchy/{staffId}")
    suspend fun getStaffHierarchy(
        @Path("staffId") staffId: Long,
    ): ResultBean<StaffHierarchy>

}