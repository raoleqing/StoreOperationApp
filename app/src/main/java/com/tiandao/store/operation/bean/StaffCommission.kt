package com.tiandao.store.operation.bean

import java.io.Serializable
import java.math.BigDecimal

class StaffCommission (
    var id: Long,
    var itemType: String,
    var itemId: Long,
    var itemName: String,
    var commissionNo: String,
    var deductWay: Int,
    var orderAmount: BigDecimal,
    var commissionRate: BigDecimal,
    var staffId: Long,
    var staffName: String,
    var commissionAmount: BigDecimal,
    var orderId: Long,
    var createTime: String,
): Serializable