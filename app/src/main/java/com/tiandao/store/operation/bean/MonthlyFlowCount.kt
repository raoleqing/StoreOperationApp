package com.tiandao.store.operation.bean

import java.math.BigDecimal
import java.io.Serializable

class MonthlyFlowCount (
    var amount: BigDecimal,
    var commissionAmount: BigDecimal,
    var performancePoints: Int,
): Serializable