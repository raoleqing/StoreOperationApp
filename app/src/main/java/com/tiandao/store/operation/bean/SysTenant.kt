package com.tiandao.store.operation.bean

import java.io.Serializable

class SysTenant (
    var tenantId: Long,
    var tenantCode: String,
    var tenantName: String,
    var contactName: String,
    var contactPhone: String,
    var contactEmail: String,
    var ownerStaffId: Long,
    var ownerStaffName: String,
    var salesmanId: Long,
    var salesmanName: String,
    var logo: String,
    var level: Int,
    var maxShopCount: Int,
    var status: Int,
    var remark: String,
    var createTime: String,
    var shopList: List<Shop>? = null,
): Serializable