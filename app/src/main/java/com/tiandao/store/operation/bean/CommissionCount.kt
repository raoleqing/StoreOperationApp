package com.tiandao.store.operation.bean

import java.io.Serializable
import java.math.BigDecimal

class CommissionCount(
    var staffId: String,
    var staffName: String,
    var month: String,
    var count: BigDecimal,
    var total: BigDecimal,
): Serializable