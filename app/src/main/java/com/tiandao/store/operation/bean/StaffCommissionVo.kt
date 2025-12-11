package com.tiandao.store.operation.bean

import java.io.Serializable

class StaffCommissionVo (
    var list: List<StaffCommission>? = null,
    var monthYear: String,
    var monthTotal: MonthlyFlowCount,
): Serializable