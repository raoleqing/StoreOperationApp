package com.tiandao.store.operation.bean

import java.io.Serializable
import java.math.BigDecimal

class StaffHierarchy(
    var id: Long,
    var staffId: Long,
    var staffName: String,
    var level: String,
    var managerStaffId: Long,
    var managerStaffName: String,
    var joinDate: String,
    var performanceScore: Int,
    var actualSales: BigDecimal,
    var commissioTotal: BigDecimal,
    var commissionRate: BigDecimal,
    var managerRate: BigDecimal,
    var status: Int,
    var memberTotal: Int,
): Serializable