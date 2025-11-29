package com.tiandao.store.operation.bean

import java.io.Serializable
import java.math.BigDecimal

class StaffCommissionVo (
    var list: List<StaffCommission>? = null,
    var month: String,
    var monthTotal: BigDecimal,
): Serializable