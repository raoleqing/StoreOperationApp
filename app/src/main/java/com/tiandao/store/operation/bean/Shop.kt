package com.tiandao.store.operation.bean

import java.io.Serializable
import java.math.BigDecimal
import java.util.Date

class Shop (
    var id: Long,
    var tenantId: Long,
    var shopName: String,
    var serviceTelephone: String,
    var contactName: String,
    var contactMobile: String,
    var ownerStaffId: Long,
    var ownerStaffName: String,
    var description: String,
    var logo: String,
    var imgUrl: String,
    var province: String,
    var city: String,
    var area: String,
    var address: String,
    var validEndTime: String,
    var longitude: BigDecimal,
    var latitude: BigDecimal,
    var status: Integer,
    var smsCount: Integer,
    var expireFlag: Integer,
    var createTime: String,
): Serializable