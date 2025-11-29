package com.tiandao.store.operation.bean

import java.io.Serializable
import java.math.BigDecimal

class PlatformOrder (
    var id: Long,
    var tenantId: Long,
    var tenantName: String,
    var shopId: Long,
    var shopName: String,
    var orderNo: String,
    var planType: String,
    var planId: Long,
    var planName: String,
    var amount: Int,
    var paymentAmount: BigDecimal,
    var paymentChannel: String,
    var status: Int,
    var payTime: String,
    var staffId: Long,
    var staffName: String,
    var auditStatus: Int,
    var flowAuditRecord: FlowAuditRecord?,
    ): Serializable
