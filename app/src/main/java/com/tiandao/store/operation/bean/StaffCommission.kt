package com.tiandao.store.operation.bean

import java.io.Serializable
import java.math.BigDecimal
import java.util.Date

class StaffCommission (
    var id: String,
    var flowNo: String,
    var staffId: Long,
    var staffName: String,
    var staffLevel: String,
    var customerId: Long,
    var customerName: String,
    var orderNo: String,
    var orderDate: String,
    var productId: Long,
    var productName: String,
    var productCategory: String,
    var quantity: Int,
    var unitPrice: BigDecimal,
    var amount: BigDecimal,
    var commissionRate: BigDecimal,
    var commissionAmount: BigDecimal,
    var monthYear: String,
    var commissionStatus: Integer,
    var flowType: Int,
    var performancePoints: Integer,
): Serializable